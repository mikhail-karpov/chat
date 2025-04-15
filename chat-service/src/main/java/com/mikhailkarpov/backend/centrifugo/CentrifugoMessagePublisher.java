package com.mikhailkarpov.backend.centrifugo;

import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoPublishRequest;
import com.mikhailkarpov.backend.messages.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CentrifugoMessagePublisher {

  private final CentrifugoClient client;

  public CentrifugoMessagePublisher(CentrifugoClient client) {
    this.client = client;
  }

  @TransactionalEventListener(Message.class)
  public void publish(Message message) {
    CentrifugoPublishRequest<Message> request = new CentrifugoPublishRequest<>("chat", message);
    client.publish(request);
  }

}
