package com.mikhailkarpov.backend.centrifugo.proxy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.config.WithMockChatUser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CentrifugoProxyController.class)
@Import(SecurityTestConfig.class)
@WithMockChatUser
class CentrifugoProxyControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @Nested
  class ProxyConnectTests {

    @Test
    void connectAuthorized() throws Exception {

      mockMvc.perform(post("/api/v1/centrifugo/proxy/connect")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                  {
                    "client": "9336a229-2400-4ebc-8c50-0a643d22e8a0",
                    "transport": "websocket",
                    "protocol": "json",
                    "encoding": "json"
                  }
                  """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("result.user").value("test-user-id"))
          .andExpect(jsonPath("result.expire_at").isNumber());
    }

    @Test
    void connectBadRequest() throws Exception {

      mockMvc.perform(post("/api/v1/centrifugo/proxy/connect"))
          .andExpect(status().isBadRequest());
    }
  }


  @Nested
  class ProxyRefreshTests {

    @Test
    void refreshAuthorized() throws Exception {

      mockMvc.perform(post("/api/v1/centrifugo/proxy/refresh")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                  {
                    "client": "9336a229-2400-4ebc-8c50-0a643d22e8a0",
                    "transport": "websocket",
                    "protocol": "json",
                    "encoding": "json",
                    "user": "test-user-id"
                  }
                  """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("result.expire_at").isNumber());
    }

    @Test
    void connectBadRequest() throws Exception {

      mockMvc.perform(post("/api/v1/centrifugo/proxy/refresh"))
          .andExpect(status().isBadRequest());
    }
  }


  @Nested
  class ProxySubscribeTests {

    @Test
    void subscribeAuthorized() throws Exception {

      mockMvc.perform(post("/api/v1/centrifugo/proxy/subscribe")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                  {
                    "client": "9336a229-2400-4ebc-8c50-0a643d22e8a0",
                    "transport":"websocket",
                    "protocol": "json",
                    "encoding": "json",
                    "user": "test-user-id",
                    "channel":  "test-channel"
                  }
                  """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("result").isEmpty());
    }

    @Test
    void subscribeBadRequest() throws Exception {

      mockMvc.perform(post("/api/v1/centrifugo/proxy/subscribe"))
          .andExpect(status().isBadRequest());
    }
  }
}