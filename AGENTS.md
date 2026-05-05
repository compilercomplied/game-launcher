# AI Agent Guidelines

This project uses **Mise** to orchestrate the entire development and testing lifecycle. AI agents working on this codebase are expected to utilize these tasks rather than running raw Gradle, ADB, or Maestro commands directly.

## Development workflow

The source of truth for all operational tasks is the `mise.toml` file. Load this into your context to better understand the expected development flow.

### Core Testing Standards
All new features or significant changes MUST include:
1.  **End-to-End (E2E) Tests:** Use Maestro flows (under `e2e/`) to cover the basic happy paths and core user flows.
2.  **Unit Tests with Screenshots:** Use Robolectric and Roborazzi (under `app/src/test/`) to cover complex UI states, edge cases (e.g., error states, empty states), and different configurations (landscape vs. portrait).

#### Stateful Test Tags (Maestro)
To avoid flakiness in E2E tools caused by unpredictable mapping between Compose Semantics and the Android Accessibility Tree, we use a **State-Encoded ID** pattern.

- **Rule:** Encode component state (selection, loading, index) directly into the `testTag`.
- **Utility:** Use `Modifier.e2eTag(id, properties)` from `com.example.unnamedproject.core`.
- **Merging:** Always use `mergeDescendants = true` (handled by the utility) to consolidate the accessibility node into a single stable target.

**Example:**
- Kotlin: `Modifier.e2eTag("item", "index" to 0, "selected" to true)`
- Maestro: `assertVisible: "item__index_0__selected_true"`

Always add new tests or adapt the existing tests to the new changes.

### Key Principles for Agents
1. **Prefer Mise:** Always check `mise.toml` before executing a command. If a task exists, use it. Prefer simple tasks and divert to shell scripts the moment it takes more than 5 lines.
2. **Localization First:** NEVER hardcode user-facing strings in the codebase. All strings must be extracted to `app/src/main/res/values/strings.xml` and referenced using `stringResource(R.string...)` in Compose or `context.getString(R.string...)` elsewhere.
3. **Snapshot Awareness:** Be aware that `test-e2e` uses snapshots to isolate different testing scenarios.
4. **Environment Consistency:** Scripts are designed to be idempotent. If the environment seems broken, `mise run project-setup` is the recommended recovery path.
5. **Easy bootstrap**: Any change to the expected environment on the host machine should be added to the project-setup script.

## Mandatory Validation Checklist
**DO NOT mark a task as complete without performing these steps:**
1. **Unit Tests:** Run `mise run test-unit`. All tests must pass, and screenshot baselines must be maintained.
2. **E2E Tests:** Run `mise run test-e2e`. All tests MUST pass in the emulator environment.
3. **Compilation:** Confirm `mise run build` succeeds.

## Planning and product refinement workflow

- **File structure**: Feature specifications will go under `docs/features/`. Product specification will go under `docs/` in a single markdown file.

## Technical Debt & Active Hacks
- **Filesystem-based Cache Busting (2026-05-05)**: In `GameItem.kt`, we use `File.lastModified()` as a `remember` key.
    - **Why**: Fixes E2E tests where `loaded_true` wasn't detected because the file path remained the same after metadata sync.
    - **Note**: This involves minor blocking I/O on the UI thread.
    - **Maintenance**: **Once this hack is refactored (e.g., by moving to a library like Coil or adding a version field to the Game model), this block MUST be removed from AGENTS.md.**

