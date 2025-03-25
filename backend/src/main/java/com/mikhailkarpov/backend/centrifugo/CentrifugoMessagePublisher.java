package com.mikhailkarpov.backend.centrifugo;

import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoPublishRequest;
import com.mikhailkarpov.backend.messages.Message;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CentrifugoMessagePublisher {

  private final CentrifugoClient client;

  public CentrifugoMessagePublisher(CentrifugoClient client) {
    this.client = client;
  }

  @EventListener(Message.class)
  public void publish(Message message) {
    CentrifugoPublishRequest<Message> request = new CentrifugoPublishRequest<>("chat", message);
    client.publish(request);
  }

}
