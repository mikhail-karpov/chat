package com.mikhailkarpov.backend.conversation;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository {

  Conversation addConversation(Conversation conversation);

  ConversationParticipant addParticipant(ConversationParticipant participant);

  ConversationParticipant findParticipant(UUID conversationId, String userId);

  List<ConversationParticipant> findParticipants(UUID conversationId);

  void blockParticipant(UUID conversationId, String userId);

  void unblockParticipant(UUID conversationId, String userId);
}
