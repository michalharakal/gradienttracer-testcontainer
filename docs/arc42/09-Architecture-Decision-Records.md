# 9. Architecture Decision Records (ADRs)

## ADR-001: Build image on-the-fly in tests
- Context: Need reproducibility without publishing images initially.
- Decision: Use Testcontainers `ImageFromDockerfile` in tests to build image.
- Status: Accepted
- Consequences: Slower first test run due to build; no registry dependency.

## ADR-002: Keep container idle with sleep
- Context: Tests need a running container to exec commands.
- Decision: Default CMD runs a long sleep via Python.
- Status: Accepted
- Consequences: Simple lifecycle; tests control execution via `execInContainer`.
