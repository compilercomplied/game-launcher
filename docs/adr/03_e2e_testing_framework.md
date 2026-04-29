# End-to-End (E2E) Testing Framework

* Status: accepted
* Decider: Gemini CLI & User
* Date: 2026-04-28

## Context and Problem Statement

We need a reliable way to validate complete user flows exactly as a user would experience them. While we have unit tests and Roborazzi for visual regression, these do not guarantee that the app functions correctly in a real environment (Android VM) from a purely black-box perspective. How can we validate user flows in a VM without mocks or relying in manual testing?

## Decision Drivers

* **Complete Black Box:** Tests must interact with the compiled application on the VM without knowledge of the internal code or requiring injected fakes.
* **Robust API & Auto-Waiting:** Minimizing flakiness and the time spent managing test infrastructure (e.g., explicit waits, synchronization issues) is crucial.
* **Easy VM Integration:** It must easily plug into our local development VMs and any future CI setups without excessive environment constraints.
* **Modern & Maintainable:** The chosen tool should be straightforward to read and maintain, avoiding legacy patterns that complicate E2E testing.

## Considered Options

* **Maestro**
* **AndroidX UI Automator**
* **Appium**
* **Espresso**

## Decision Outcome

Chosen option: **Maestro**, because its declarative YAML-based approach natively handles auto-waiting and intrinsically operates as a pure black-box testing framework against the installed APK via ADB. It provides high resilience with minimal configuration overhead compared to legacy frameworks.

### Consequences

* **Good:** High confidence in user flows with minimal test maintenance overhead.
* **Good:** Strong incentive to maintain a high-quality Compose Semantics Tree, improving accessibility for visually impaired users.
* **Good:** Fast test authoring using highly readable declarative steps.
* **Bad:** Adds a secondary non-Gradle tool dependency (`maestro` CLI) required for local development and CI execution.
* **Bad:** Tests are written in YAML instead of Kotlin.

## Pros and Cons of the Options

### Maestro

* Good: Declarative YAML flows are readable by non-developers.
* Good: Built-in resilience with native auto-waiting for UI states.
* Good: Zero code changes required in the Android app structure.
* Bad: Cannot easily utilize complex programmable logic or access internal app state (if needed as an exception).

### AndroidX UI Automator

* Good: Native to the Android ecosystem.
* Good: Written in Kotlin and runs via standard Gradle tasks.
* Bad: Noticeably slower execution compared to other modern options.
* Bad: More verbose API for flow-based assertions.

### Appium

* Good: Industry standard for cross-platform (iOS/Android) automation.
* Good: Highly flexible language choices (Python, JS, Java).
* Bad: Heavy infrastructure setup and maintenance overhead.
* Bad: Prone to flakiness without careful manual wait management.

### Espresso (Black-Box Mode)

* Good: Extremely fast execution within the VM.
* Good: Deep integration with Compose UI.
* Bad: Better suited for white-box component testing; struggles with complex cross-app flows or black-box environment decoupling.
