# Unnamed

Game launcher (WIP)

# Development

## Prerequisites
- **[Android CLI](https://developer.android.com/tools/agents/android-cli)**
- **[Maestro (e2e tests)](https://docs.maestro.dev/get-started/quickstart)**
- **[Mise](https://github.com/jdx/mise)**

## Testing guidelines and architecture

Mise tasks are configured to run:
 - E2E tests. Emulator+snapshot based. The mise task will run all the tests.
 - Unit tests. We use roboelectric+roborazzi for UI unit tests. The mise task will run all the tests.
 - Manual tests. You can add the target snapshot as parameter to the mise task.

For the emulators, we have this setup:

| Emulator Name | Type / Arch | Snapshot Name | Snapshot Description |
| :--- | :--- | :--- | :--- |
| `medium_phone` | ARM64 / Phone | `clean_state` | Bare OS with no third-party apps. |
| | | `populated_state` | Contains test fixture games (2048, Wesnoth, etc.). |


# Architecture & Design
## Principles
- **Single Activity Architecture:** `MainActivity` acts as the entry point.
- **Feature-based Packaging:** Code organized by vertical slices (e.g., `features/launcher/`).
- **Unidirectional Data Flow:** ViewModels manage state; Composables are split into Stateful (Screen) and Stateless (Content) for better testability.
- **Repository Pattern:** Abstracted data sources (`GameRepository`) to allow seamless swapping between real Android system data and mocked test data.
- **Adaptive UI:** Responsive layout supporting both Portrait (LazyColumn) and Landscape (LazyRow).
