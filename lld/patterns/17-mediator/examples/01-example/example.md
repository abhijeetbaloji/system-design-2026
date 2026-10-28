# Example 01: Chat Room Mediator

## 1. Problem Scenario
In a collaborative team communication platform, users need to exchange group broadcast messages and private direct messages.
Without a mediator:
- Every `BadUser` instance must maintain references to every other user on the server.
- Whenever a user enters or leaves the chat, every existing user must be updated with the change ($O(N^2)$ updates).
- Centralized requirements such as message audit logging, profanity filtering, delivery acknowledgments, and rate limiting must be duplicated across every individual user instance or manually passed around.

## 2. Bad Design (Direct Peer Mesh)
- `BadUser` stores `List<BadUser> peers`.
- To broadcast, `BadUser` loops over its peer list and calls `peer.receiveMessage()`.
- To send a direct message, `BadUser` searches its local peer list and directly calls the target user.
- If 100 users join, each user holds 99 direct references, resulting in 9,900 peer links. Disconnecting or mutating any user risks stale references or concurrent modification errors across all peer lists.

## 3. Good Design (Mediator Pattern)
- `ChatMediator`: Central interface specifying user registration, message broadcasting, and direct routing.
- `ChatRoomMediator`: Central hub maintaining a map of active users and an append-only audit trail.
  - When user A broadcasts, `ChatRoomMediator` iterates over all registered users *except* the sender.
  - When user A sends a direct message to user B, `ChatRoomMediator` locates user B by ID and routes the message safely.
  - Centralized features (audit logging, presence checking, recipient validation) are implemented once in the mediator.
- `User`: Abstract colleague holding only `ChatMediator`. Knows nothing about peer user implementations or addresses.
- `StandardUser` and `AdminUser`: Concrete colleagues focusing on user-level preferences and inbox message handling.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/17-mediator/examples/01-example/diagram.mmd).

## 5. Execution Flow
1. `ChatRoomMediator` is instantiated.
2. `StandardUser` and `AdminUser` instances are created with a reference to the mediator and registered with `mediator.registerUser(user)`.
3. User A calls `userA.sendBroadcast("Hello everyone!")`, which delegates to `mediator.broadcastMessage(...)`.
4. The mediator records the message in its audit log and routes it to users B and C. User A is excluded from receiving their own broadcast.
5. User B calls `userB.sendDirect("Secret note", "user-C")`, which delegates to `mediator.sendDirectMessage(...)`.
6. The mediator validates user C's presence and routes the direct message exclusively to user C.
