# Tasks for GradientTracer Testcontainers Module

Generated on: 2025-09-02 22:47

Legend:
- [ ] = todo
- [x] = done (implemented or present in repo)

Note: Items are grouped by logical area and numbered. Completion marks reflect the current repository state and conservative inference.

## 1. Assumptions Verification
1.1 [ ] Verify GradientTracer install method (pip/pyproject) and dependencies; identify CLI entrypoint.
1.2 [ ] Confirm CPU-only operation is sufficient for basic tracing; note GPU variant plan.
1.3 [ ] Confirm supported Python versions (target 3.11) and Linux x86_64 compatibility.
1.4 [ ] Determine runtime artifacts: environment variables, module/CLI usage, outputs to capture.
1.5 [ ] Produce a minimal set of commands to run GradientTracer for a demo.

## 2. Container Image
2.1 [x] Create Dockerfile using slim Python base image.
2.2 [x] Install required system dependencies (build-essential, git, curl, ca-certificates).
2.3 [x] Pin pip/setuptools/wheel versions.
2.4 [x] Install GradientTracer from a specific tag/commit via build-arg GT_REF.
2.5 [x] Create non-root user and working directory.
2.6 [ ] Add optional entrypoint wrapper to execute arbitrary python module/commands and/or start a lightweight HTTP server if needed.
2.7 [ ] Expose any required ports (if applicable).
2.8 [ ] Add a container healthcheck script and wire it in Dockerfile.

## 3. Image Distribution Strategy
3.1 [x] Support building image locally in tests using Testcontainers ImageFromDockerfile.
3.2 [ ] Evaluate/publish image to a registry for faster CI (optional, later).

## 4. Java Testcontainers Module
4.1 [x] Initialize Maven/Gradle module (Maven present) with Testcontainers dependency scaffold.
4.2 [x] Implement GradientTracerContainer class extending GenericContainer with startup timeout.
4.3 [ ] Add helper: execPython(String... args) -> execInContainer("python", args).
4.4 [ ] Add helper: runModule(String module, String... args) -> exec "python -m module ...".
4.5 [ ] Add helper: runScript(Path hostScript, String... args) with bind mount support.
4.6 [ ] Add helper: copyResult(Path containerPath, Path hostDestination) to retrieve artifacts.
4.7 [ ] Expose configuration for workingDir, env vars, bind mounts, network aliases.

## 5. JUnit Examples
5.1 [x] Provide a minimal test that runs a Python command and asserts version.
5.2 [ ] Add example test importing gradienttracer and printing __version__ (and assert format).
5.3 [ ] Add a sample trace invocation test and assert produced file/log content.
5.4 [ ] Document/implement CPU-only profile; note GPU option using withCreateContainerCmdModifier for NVIDIA runtime.

## 6. Documentation and Developer Experience
6.1 [x] Document goals and high-level plan in README.
6.2 [ ] Expand README with local prerequisites (Docker, Java, Maven) and step-by-step usage.
6.3 [ ] Provide Makefile tasks for build/test.
6.4 [ ] Add CI notes for GitHub Actions with Docker and caching.
6.5 [ ] Add troubleshooting section.

## 7. Directory Structure Hygiene
7.1 [x] Ensure docker/, java/, and README.md exist as per plan.
7.2 [ ] Add examples/minimal.py demonstrating a simple GT run.
7.3 [x] Keep PLAN.md; now complemented by tasks.md (this file).

## 8. Open Questions Follow-up
8.1 [ ] Decide whether to prefer a dedicated CLI command over `python -m` based on GT repo.
8.2 [ ] Identify datasets/model weights required for sample traces and caching approach in CI.
8.3 [ ] Determine if any ports must be exposed for visualization or API.

