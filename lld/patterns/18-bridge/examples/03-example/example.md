# Example 03: Remote Controls and Electronic Devices

## 1. Problem Scenario
A consumer electronics company develops universal remote controls (`BasicRemote`, `AdvancedRemote`) designed to operate a variety of smart home hardware appliances (`TvDevice`, `RadioDevice`, etc.).

Without the Bridge pattern:
- The controller software creates rigid hybrid subclasses for each appliance: `BasicTvRemote`, `AdvancedTvRemote`, `BasicRadioRemote`, `AdvancedRadioRemote`.
- Adding a new remote capability (e.g., `VoiceRemote`) requires implementing classes for all supported devices.
- A remote instance is hardcoded to a specific hardware device and cannot be dynamically paired with a different device at runtime.

## 2. Bad Design (Subclass Rigidity)
- `BadBasicTvRemote` and `BadAdvancedRadioRemote` hardcode concrete device logic directly into remote control methods.
- The remote cannot be reused with any other electronic appliance.
- Testing remote control logic requires physical or mock hardware devices wired into the same class.

## 3. Good Design (Bridge Pattern)
- Decouples the **Abstraction** (`RemoteControl`, `BasicRemote`, `AdvancedRemote`) from the **Implementor** (`Device`, `TvDevice`, `RadioDevice`).
- The base `RemoteControl` holds a `#Device device` bridge reference.
- `BasicRemote`: Handles standard power toggling, volume step increments (+-5%), and channel tuning (+-1).
- `AdvancedRemote`: Adds higher-level operations such as `mute()` (caches prior volume and toggles zero/restores) and `setDirectChannel(int channel)`.
- The controlled device can be swapped on the fly using `remote.setDevice(newDevice)`.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/18-bridge/examples/03-example/diagram.mmd).

## 5. Execution Flow
1. Client initializes an appliance (e.g., `TvDevice`).
2. Client initializes an `AdvancedRemote`, injecting `tvDevice` across the bridge.
3. Client toggles power, changes volume, and tunes channels.
4. Client calls `advancedRemote.mute()`. The remote reads the current volume, saves it, and sets volume to 0 on the TV. Calling `mute()` again restores the previous volume.
5. Client switches the remote to control a `RadioDevice` using `remote.setDevice(radioDevice)`. The same remote now controls the radio without creating new objects.
