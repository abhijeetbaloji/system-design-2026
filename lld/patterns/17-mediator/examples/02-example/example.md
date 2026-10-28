# Example 02: Air Traffic Coordination Simulation

## 1. Problem Scenario
Airports must safely coordinate landings and takeoffs between commercial and cargo aircraft across multiple intersecting runways.
Without an Air Traffic Control (ATC) mediator:
- Pilots must communicate directly with other pilots and runways to negotiate who lands first.
- Direct peer negotiation easily leads to runway collisions, race conditions, and lack of a centralized holding pattern.
- If an emergency or runway maintenance occurs, every aircraft in the terminal airspace must be notified individually.

## 2. Bad Design (Direct Aircraft-to-Runway Negotiation)
- In the bad design, `BadAirplane` directly queries and sets `BadRunway.setOccupied(true)`.
- If two aircraft query the runway simultaneously, both observe `isOccupied == false` and initiate landing, creating catastrophic collisions.
- Aircraft hold direct references to runways and peer planes, creating high coupling and uncoordinated holding patterns.

## 3. Good Design (Mediator Pattern)
- `AirTrafficControlMediator`: Central interface specifying flight registration, runway registration, landing clearance, takeoff clearance, and runway vacation notifications.
- `AtcTower`: Concrete mediator maintaining runway availability, holding queues for landing and takeoff, and flight status records.
  - When an aircraft requests landing clearance, `AtcTower` checks if any runway is free.
  - If a runway is available, it is assigned and marked occupied immediately.
  - If all runways are occupied, the flight is placed into a FIFO holding pattern queue.
  - When a landed flight finishes taxiing and notifies the tower that the runway is vacated, the tower automatically promotes and clears the next flight in the holding pattern.
- `Flight`: Abstract colleague extending domain capabilities without referencing other flights or concrete runways directly.
- `Runway`: Managed component controlled by the tower.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/17-mediator/examples/02-example/diagram.mmd).

## 5. Execution Flow
1. `AtcTower` is initialized with runways (e.g., `09L`, `09R`).
2. Flights (e.g., `UA101`, `BA202`, `LH303`) register with the tower.
3. `UA101` requests landing -> Tower clears `UA101` for Runway `09L`.
4. `BA202` requests landing -> Tower clears `BA202` for Runway `09R`.
5. `LH303` requests landing -> Both runways are full; Tower instructs `LH303` to enter the holding pattern.
6. `UA101` completes landing and vacates `09L` -> Tower detects runway availability and immediately grants clearance to `LH303`.
