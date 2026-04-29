# Dependency Injection Framework

* Status: accepted
* Decider: Gemini CLI & User
* Date: 2026-04-27

## Context and Problem Statement

The project requires a robust and scalable way to manage dependencies (e.g., repositories, ViewModels). We need to choose between the two most popular standards in the Android ecosystem: Hilt and Koin.

## Decision Drivers

* **Type Safety:** The framework should ideally catch errors at compile-time rather than runtime.
* **Ecosystem Alignment:** Adherence to "Modern Android Development" (MAD) standards.
* **API Stability:** Simple and stable API that integrates well with Jetpack Compose.
* **Developer Experience (DevEx):** Build times and ease of setup.

## Considered Options

* **Manual Injection:** Simple but doesn't scale well and requires boilerplate.
* **Koin:** Kotlin-native, simple DSL, fast build times (no code-gen), but runtime-resolved (Service Locator).
* **Hilt:** Official Google recommendation, built on Dagger, compile-time safe, but requires KSP/KAPT code generation.

## Decision Outcome

Chosen option: **Hilt**, because it provides strict compile-time safety and is the official industry standard for Android. While Koin offers faster build times for small projects, the benefit of catching dependency errors at compile-time aligns with the project's goal of high technical integrity and strict typing.

### Consequences

* **Good:** Compile-time validation of the dependency graph.
* **Good:** Seamless integration with `hiltViewModel()` in Compose.
* **Good:** Automatic lifecycle management for Activity/ViewModel scopes.
* **Bad:** Increased build time due to KSP code generation (mitigated by using KSP over KAPT).
* **Bad:** Slightly more boilerplate (Application class, annotations).

## Pros and Cons of the Options

### Koin

* Good: Extremely fast setup and build times.
* Good: No code generation or hidden magic.
* Bad: Runtime resolution can lead to crashes if a dependency is missing.

### Hilt

* Good: Industry standard with maximum longevity.
* Good: Compile-time safety (if it builds, the graph is valid).
* Good: Extensive documentation and community support.
* Bad: Requires a few more plugins and annotations to set up.
