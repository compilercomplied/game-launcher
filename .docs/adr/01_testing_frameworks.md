# Testing Frameworks for Visual Validation

* Status: accepted
* Decider: Gemini CLI & User
* Date: 2026-04-27

## Context and Problem Statement

Android UI testing traditionally requires an emulator or physical device, which introduces significant latency, flakiness, and infrastructure overhead. We need a way to validate our launcher's UI (especially its adaptive layouts) with high velocity and 100% reproducibility.

## Decision Drivers

* **Velocity:** Tests must run fast enough to be part of a local "watch" loop.
* **Consistency:** Screenshots must be identical regardless of where they are run (CI vs. local).
* **Ease of Use:** Must support Jetpack Compose natively.
* **No Infrastructure Overhead:** Avoid the need for managed emulators or "device farms" for basic UI validation.

## Considered Options

* **Espresso / UI Automator (Instrumented Tests):** The standard way, but requires an emulator/device. Slow and prone to environmental flakiness.
* **Compose UI Tooling Previews:** Good for development, but not easily automated for regression testing or pixel-perfect diffing.
* **Robolectric + Roborazzi:** Robolectric provides a "shadow" Android environment on the JVM. Roborazzi captures screenshots from this environment.

## Decision Outcome

Chosen option: **Robolectric + Roborazzi**, because it allows us to run "Native Graphics" tests directly on the JVM. This setup provides the best balance of speed (running in seconds, not minutes) and visual accuracy, enabling a "record-and-verify" workflow that fits into a high-velocity development cycle.

### Consequences

* **Good:** No emulator required for UI validation.
* **Good:** Extremely fast feedback loop (compatible with `test-watch`).
* **Good:** Visual diffs are generated automatically on failure, making regressions obvious.
* **Bad:** Since it runs on the JVM, it does not catch low-level OEM-specific rendering bugs.
* **Bad:** Requires careful management of "Golden Images" in source control.

## Pros and Cons of the Options

### Espresso (Instrumented)

* Good: Tests run on the real Android OS.
* Bad: Slow startup times; requires complex emulator management.
* Bad: Hardware acceleration and screen density can cause inconsistent screenshots across machines.

### Robolectric + Roborazzi

* Good: 100% deterministic; runs anywhere a JVM runs.
* Good: Fast enough to run on every code change.
* Good: Native Graphics mode (via `GraphicsMode.Mode.NATIVE`) provides high-fidelity rendering.
* Bad: Not a replacement for final E2E testing on real hardware.
