package com.mikhailkarpov.backend.centrifugo;

import com.mikhailkarpov.backend.centrifugo.client.CentrifugoBroadcastRequest;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import com.mikhailkarpov.backend.conversation.ConversationParticipant;
import com.mikhailkarpov.backend.conversation.ConversationService;
import com.mikhailkarpov.backend.messages.Message;
import java.util.List;
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

  @Mock
  private ConversationService conversationService;

  @InjectMocks
  private CentrifugoMessagePublisher centrifugoMessagePublisher;


  @Test
  void publish(@Captor ArgumentCaptor<CentrifugoBroadcastRequest<Message>> captor) {

    var conversationId = UUID.randomUUID();
    var message = Message.builder()
        .conversationId(conversationId)
        .userId("user-id")
        .text("test message")
        .build();

    Mockito.when(conversationService.findParticipants(conversationId)).thenReturn(List.of(
        new ConversationParticipant(conversationId, "user-1"),
        new ConversationParticipant(conversationId, "user-2", true),
        new ConversationParticipant(conversationId, "user-3")
    ));

    centrifugoMessagePublisher.publish(message);

    Mockito.verify(centrifugoClient).broadcast(captor.capture());

    Assertions.assertThat(captor.getValue()).satisfies(it -> {
      Assertions.assertThat(it.channels()).contains("personal:#user-1", "personal:#user-3");
      Assertions.assertThat(it.data()).isEqualTo(message);
      Assertions.assertThat(it.idempotencyKey()).isNotBlank();
    });
  }

  @Test
  void publishParticipantsEmpty() {

    var conversationId = UUID.randomUUID();
    var message = Message.builder()
        .conversationId(conversationId)
        .userId("user-id")
        .text("test message")
        .build();

    Mockito.when(conversationService.findParticipants(conversationId)).thenReturn(List.of(
        new ConversationParticipant(conversationId, "user-1", true)
    ));

    centrifugoMessagePublisher.publish(message);

    Mockito.verifyNoInteractions(centrifugoClient);
  }

}