package com.mikhailkarpov.backend.centrifugo;

import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoPublishRequest;
import com.mikhailkarpov.backend.messages.Message;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CentrifugoMessagePublisherTest {

  @Mock
  private CentrifugoClient centrifugoClient;

  @InjectMocks
  private CentrifugoMessagePublisher centrifugoMessagePublisher;


  @Test
  void publish(@Captor ArgumentCaptor<CentrifugoPublishRequest<Message>> requestCaptor) {

    Message message = Message.builder()
        .conversationId(UUID.randomUUID())
        .userId("user-id")
        .text("test message")
        .build();

    centrifugoMessagePublisher.publish(message);

    Mockito.verify(centrifugoClient)
        .publish(requestCaptor.capture());

    Assertions.assertThat(requestCaptor.getValue()).satisfies(it -> {
      Assertions.assertThat(it.channel()).isEqualTo("chat");
      Assertions.assertThat(it.data()).isEqualTo(message);
      Assertions.assertThat(it.idempotencyKey()).isNotBlank();
    });
  }

}