# Command-Line Tools for SDK and AVD Management

* Status: accepted
* Decider: Gemini CLI & User
* Date: 2026-04-29

## Context and Problem Statement

For provisioning our Android development and E2E testing environment (SDK components, emulators, etc.), we need a reliable set of command-line tools. In early 2026, Google introduced a new "Android CLI" optimized specifically for AI agents, alongside the traditional Java-based `cmdline-tools` package (containing `sdkmanager` and `avdmanager`). Which toolset should we use to manage our project setup and emulator lifecycle?

## Decision Drivers

* **Reliability:** The tools must reliably install SDK components and manage emulator lifecycle without cryptic dependency errors.
* **Agentic Usability:** The tools must be highly scriptable and manageable by AI agents or standard shell scripts, with concise syntax.
* **Modernity:** Favoring the latest, purpose-built tools that align with modern CI/CD and agentic workflows.

## Considered Options

* **Traditional `cmdline-tools` (`sdkmanager` & `avdmanager`)**
* **New 2026 Android CLI (`android sdk install`, `android emulator`)**

## Decision Outcome

Chosen option: **New 2026 Android CLI (`android sdk install`, `android emulator`)**, because the traditional `cmdline-tools` (specifically `sdkmanager`) proved unreliable in the current environment, failing with cryptic "Dependant package with key emulator not found" errors. The new Android CLI provides a more robust, streamlined, and agent-friendly interface for environment provisioning.

### Consequences

* **Good:** Streamlined, modern command syntax (`android sdk install`, `android emulator`) that is easier for agents to handle.
* **Good:** Improved reliability during project setup by avoiding legacy Java-based dependency resolution issues.
* **Bad:** Requires a one-time manual installation of the Android CLI via a curl script.

## Pros and Cons of the Options

### Traditional `cmdline-tools` (`sdkmanager` & `avdmanager`)

* Good: Extensive legacy community knowledge.
* Bad: Verbose, string-heavy API.
* Bad: Slower execution and fragile dependency resolution in some environments.

### New 2026 Android CLI

* Good: Rebuilt for "machine-readable" first, vastly reducing LLM token usage and execution time.
* Good: Clean, modern command syntax.
* Good: Features like `android docs` bridge knowledge gaps for AI agents.
* Good: Avoids "key emulator not found" issues by consolidating management into a single binary.
