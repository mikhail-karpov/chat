package com.mikhailkarpov.backend.conversation;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationService {

  private final ConversationRepository conversationRepository;

  public ConversationService(ConversationRepository conversationRepository) {
    this.conversationRepository = conversationRepository;
  }

  @Transactional
  public Conversation createConversation(String... userIds) {

    var conversation = conversationRepository.addConversation(new Conversation());
    for (String userId : userIds) {
      conversationRepository.addParticipant(new ConversationParticipant(conversation.getId(), userId));
    }
    return conversation;
  }

  public ConversationParticipant findParticipant(UUID conversationId, String userId) {
    return conversationRepository.findParticipant(conversationId, userId);
  }

  public List<ConversationParticipant> findParticipants(UUID conversationId) {
    return conversationRepository.findParticipants(conversationId);
  }

  public void blockParticipant(UUID conversationId, String userId) {
    conversationRepository.blockParticipant(conversationId, userId);
  }

  public void unblockParticipant(UUID conversationId, String userId) {
    conversationRepository.unblockParticipant(conversationId, userId);
  }

}
