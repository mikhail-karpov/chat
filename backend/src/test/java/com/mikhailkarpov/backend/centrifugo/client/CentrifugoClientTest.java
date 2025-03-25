package com.mikhailkarpov.backend.centrifugo.client;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import com.mikhailkarpov.backend.config.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@IntegrationTest
@EnableWireMock({
    @ConfigureWireMock(
        port = 8000
    )
})
class CentrifugoClientTest {

  @Autowired
  private CentrifugoClient centrifugoClient;

  private final CentrifugoPublishRequest<TestData> testRequest =
      new CentrifugoPublishRequest<>("test-channel", new TestData("test-data"));

  @Test
  void publishOkResult() {

    stubFor(post("/api/publish")
        .withHeader("X-API-KEY", equalTo("7bd5cf56-53bb-4710-8238-7186472819cb"))
        .withRequestBody(matchingJsonPath("channel", equalTo("test-channel")))
        .withRequestBody(matchingJsonPath("data.value", equalTo("test-data")))
        .willReturn(okJson("""
            {
                "result": {}
            }
            """)));

    centrifugoClient.publish(testRequest);
  }

  @Test
  void publishErrorResponse() {

    stubFor(post("/api/publish")
        .willReturn(okJson("""
            {
                "error": {"code": 102, "message":  "namespace not found"}
            }
            """)));

    Assertions.assertThatThrownBy(() -> centrifugoClient.publish(testRequest))
        .isInstanceOf(CentrifugoException.class);
  }

  record TestData(String value) {}
}