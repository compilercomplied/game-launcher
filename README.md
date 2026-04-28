# Unnamed

Game launcher (WIP)

## Architecture & Design
- **Single Activity Architecture:** `MainActivity` acts as the entry point.
- **Feature-based Packaging:** Code organized by vertical slices (e.g., `features/launcher/`).
- **Unidirectional Data Flow:** ViewModels manage state; Composables are split into Stateful (Screen) and Stateless (Content) for better testability.
- **Repository Pattern:** Abstracted data sources (`GameRepository`) to allow seamless swapping between real Android system data and mocked test data.
- **Adaptive UI:** Responsive layout supporting both Portrait (LazyColumn) and Landscape (LazyRow).

## Key Design Decisions (ADRs)
- **Hilt over Koin:** Chosen for strict compile-time safety and ecosystem alignment, prioritizing integrity over build-time savings.
- **JVM-prefered Testing:** All visual validation runs on the JVM to eliminate emulator overhead and maximize velocity.
