# Example 02: Game Character Cloning (Warriors, Mages, and Inventories)

## 1. Problem Scenario
In an Action-RPG game engine, the wave-spawning subsystem must rapidly generate hundreds of NPC enemies (such as `GruntWarrior` or `ArchMage`) in real time.
Initializing a character from scratch requires:
- Loading 3D mesh assets, textures, and animation state trees.
- Reading weapon and armor stats from config files.
- Calculating baseline attributes (health, mana, skill trees, inventory items).

Calling `new ConcreteCharacter(...)` repeatedly during gameplay causes frame rate stutter and latency spikes.

## 2. Bad Design (Reference Aliasing Hazard)
- The bad design attempts to clone characters by copying references: `clone.equipment = prototype.equipment`.
- Because the `equipment` list and its contained `BadItem` objects are shared across all spawned units:
  - When Player 1 hits Warrior #1 and damages its shield durability, every other warrior's shield in the entire level is instantly damaged!
  - When Mage #1 casts a potion and removes it from inventory, the potion disappears from all other mages!

## 3. Good Design (Prototype Pattern)
- `GamePrototype<T>`: Generic type-safe cloning contract.
- `Item`: Equipment component implementing `GamePrototype<Item>` with copy constructor.
- `GameCharacter`: Abstract base class implementing deep cloning:
  - Deep-copies the `List<Item> equipment` by cloning each individual item.
  - Deep-copies the `List<String> abilities` list.
- `Warrior` and `Mage`: Concrete prototypes overriding `clone()` to invoke their respective copy constructors.
- `CharacterSpawnerRegistry`: Holds pre-warmed archetype templates (`"dungeon-orc-warrior"`, `"high-elf-mage"`).
  - Invoking `registry.spawn("dungeon-orc-warrior")` produces an isolated, fully armed warrior clone in microseconds.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/19-prototype/examples/02-example/diagram.mmd).

## 5. Execution Flow
1. Baseline archetypes are configured with weapons, armor, and abilities during game startup.
2. Archetypes are registered in `CharacterSpawnerRegistry`.
3. Level designer spawns a wave of 10 warriors using `spawner.spawn("dungeon-orc-warrior")`.
4. Each spawned warrior operates with completely independent health, equipment durability, and inventory.
5. Combat modifications to one warrior have zero side effects on sibling clones or the golden archetype.
