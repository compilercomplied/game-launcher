# Unnamed

Game launcher (WIP)

## Architecture & Design
- **Single Activity Architecture:** `MainActivity` acts as the entry point.
- **Feature-based Packaging:** Code organized by vertical slices (e.g., `features/launcher/`).
- **Unidirectional Data Flow:** ViewModels manage state; Composables are split into Stateful (Screen) and Stateless (Content) for better testability.
- **Repository Pattern:** Abstracted data sources (`GameRepository`) to allow seamless swapping between real Android system data and mocked test data.
- **Adaptive UI:** Responsive layout supporting both Portrait (LazyColumn) and Landscape (LazyRow).

## Setup Requirements
### Prerequisites
- **[Android CLI](https://developer.android.com/tools/agents/android-cli)**
- **[Maestro (e2e tests)](https://docs.maestro.dev/get-started/quickstart)**
- **[Mise](https://github.com/jdx/mise)**
