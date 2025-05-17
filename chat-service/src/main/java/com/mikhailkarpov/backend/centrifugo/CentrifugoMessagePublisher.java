package com.mikhailkarpov.backend.centrifugo;

import com.mikhailkarpov.backend.centrifugo.client.CentrifugoBroadcastRequest;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import com.mikhailkarpov.backend.conversation.ConversationParticipant;
import com.mikhailkarpov.backend.conversation.ConversationService;
import com.mikhailkarpov.backend.messages.Message;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class CentrifugoMessagePublisher {

  private final CentrifugoClient client;
  private final ConversationService conversationService;

  public CentrifugoMessagePublisher(
      CentrifugoClient client, ConversationService conversationService) {

    this.client = client;
    this.conversationService = conversationService;
  }

  @TransactionalEventListener(value = Message.class, phase = TransactionPhase.AFTER_COMMIT)
  public void publish(Message message) {

    var channels = conversationService.findParticipants(message.getConversationId())
        .stream()
        .filter(Predicate.not(ConversationParticipant::isBlocked))
        .map(ConversationParticipant::getUserId)
        .map("personal:#%s"::formatted)
        .toArray(String[]::new);

    if (channels.length == 0) {
      log.warn("No channels found for message: {}", message);
      return;
    }

    var request = new CentrifugoBroadcastRequest<>(channels, message);
    client.broadcast(request);
  }

}
