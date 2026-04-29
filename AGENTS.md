# AI Agent Guidelines

This project uses **Mise** to orchestrate the entire development and testing lifecycle. AI agents working on this codebase are expected to utilize these tasks rather than running raw Gradle, ADB, or Maestro commands directly.

## Development Workflow

The source of truth for all operational tasks is the `mise.toml` file. Load this into your context to better understand the expected development flow.

## Key Principles for Agents
1. **Prefer Mise:** Always check `mise.toml` before executing a command. If a task exists, use it. Prefer simple tasks and divert to shell scripts the moment it takes more than 5 lines.
3. **Snapshot Awareness:** Be aware that `test-e2e`.
4. **Environment Consistency:** Scripts are designed to be idempotent. If the environment seems broken, `mise run project-setup` is the recommended recovery path.
5. **Easy bootstrap**: Any change to the expected environment on the host machine should be added to the project-setup script.
