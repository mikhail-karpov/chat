import type { Message } from '@/types/chat'

export const MOCK_USER_ID = 'me'

function msg(id: string, conversationId: string, userId: string, text: string): Message {
  return { id, conversationId, userId, text, createdAt: new Date() }
}

export const messages: Record<string, Message[]> = {
  maria: [
    msg('1', 'maria', 'them', 'hey! are we still on for coffee tomorrow?'),
    msg('2', 'maria', 'them', 'i found that place we talked about'),
    msg('3', 'maria', MOCK_USER_ID, 'yes! 10am good?'),
    msg('4', 'maria', 'them', "perfect. i'll send the address"),
    msg('5', 'maria', MOCK_USER_ID, 'sounds good — tmrw works'),
  ],
  jordan: [
    msg('1', 'jordan', 'them', 'check this out 📷'),
    msg('2', 'jordan', MOCK_USER_ID, 'haha what even is that'),
    msg('3', 'jordan', 'them', 'right?? found it on a walk'),
  ],
  alex: [
    msg('1', 'alex', 'them', 'did you get my message from yesterday?'),
    msg('2', 'alex', MOCK_USER_ID, "sorry just saw it — what's up?"),
    msg('3', 'alex', 'them', 'we need to reschedule the sync'),
    msg('4', 'alex', 'them', 'thursday instead of wednesday?'),
    msg('5', 'alex', MOCK_USER_ID, 'works for me'),
  ],
  sam: [
    msg('1', 'sam', 'them', "let's grab coffee next week — i have some ideas"),
    msg('2', 'sam', MOCK_USER_ID, 'sounds great, what day works?'),
  ],
  taylor: [
    msg('1', 'taylor', 'them', 'that meeting dragged on forever'),
    msg('2', 'taylor', 'them', 'haha same'),
    msg('3', 'taylor', MOCK_USER_ID, 'at least we got a decision'),
  ],
  lee: [
    msg('1', 'lee', 'them', 'thx for the link — will read tonight'),
    msg('2', 'lee', MOCK_USER_ID, 'let me know what you think'),
  ],
  riley: [
    msg('1', 'riley', 'them', 'see you there 👋'),
    msg('2', 'riley', MOCK_USER_ID, "can't wait!"),
  ],
  devon: [
    msg('1', 'devon', 'them', 'ok np, moved it to friday'),
    msg('2', 'devon', MOCK_USER_ID, 'thanks for the heads up'),
  ],
}
