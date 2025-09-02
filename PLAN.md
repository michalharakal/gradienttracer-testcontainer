# Detailed Plan for GradientTracer Testcontainers Module

This document outlines a concrete plan for building a Java Testcontainers module that runs the Python-based GradientTracer inside a container, enabling Java unit tests to execute Python code and capture results.

Note: The Testcontainers-managed container runs GradientTracer implemented in Python; all commands executed from Java are Python invocations inside the container (e.g., python -m gradienttracer).

Repository reference: https://github.com/sk-ai-net/gradienttracer

## Assumptions (to verify in Phase 1)
- GradientTracer is a Python package with installable requirements (setup.cfg/pyproject.toml) and a CLI entrypoint (e.g., `python -m gradienttracer` or `gradienttracer`).
- No GPU is strictly required for basic tracing; if optional, we can provide CPU-only images and a separate CUDA-enabled variant later.
- The project supports Python 3.10+ (to be confirmed) and runs on Linux x86_64.

## Phases and Tasks

1. Analyze GradientTracer repo
   - Identify install method: pip (pyproject.toml or setup.py), version tags/releases, and dependencies.  
   - Determine runtime artifacts: CLI, Python module entrypoints, environment variables.  
   - Check for external services (DB, message broker) and network ports.  
   - Decide what outputs we need to capture: logs, artifacts, files.  
   - Outcome: list of commands to run GradientTracer for a minimal demo.

2. Build container image(s)
   - Create a Dockerfile with slim Python base image (e.g., `python:3.11-slim`).  
   - Install system deps (build-essential, git, etc. if needed).  
   - Pin pip, setuptools, wheel; install gradienttracer from a specific tag/commit.  
   - Create user (non-root) and workdir.  
   - Add a small entrypoint wrapper that can:
     - Execute arbitrary `python -c` or `python -m` commands passed via `CMD`/`docker exec`.
     - Optionally start a simple HTTP server (if GT has such) to keep the container alive for interactive exec from Java.  
   - Expose any required ports (if applicable).  
   - Add healthcheck script for readiness.

3. Publish image or build locally in tests
   - Option A: Publish to a registry (e.g., ghcr.io/your-org/gradienttracer-testcontainer:<tag>).  
   - Option B: Use Testcontainers ImageFromDockerfile to build on-the-fly during the test run.  
   - Start with Option B to simplify setup; move to A later for speed.

4. Java Testcontainers module
   - Create a minimal Maven or Gradle module.  
   - Implement `GradientTracerContainer extends GenericContainer<GradientTracerContainer>`.  
   - Constructor accepts: image or Dockerfile build context, version pin, python module name, additional envs.  
   - Provide helper methods:
     - `execPython(String... args)` -> uses `container.execInContainer("python", args)` and returns stdout/stderr/exitCode.  
     - `runModule(String module, String... args)` -> exec `python -m module ...`.  
     - `runScript(Path hostScript, String... args)` -> mounts bind and executes.  
     - `copyResult(Path containerPath, Path hostDestination)` -> copy files out.  
   - Expose configuration: workingDir, env vars, bind mounts, network aliases.

5. JUnit examples
   - Demonstrate minimal test that runs `import gradienttracer; print(gradienttracer.__version__)`.  
   - Demonstrate running a sample trace invocation and assert on produced file/log content.  
   - Provide CPU-only profile; document GPU option via `withCreateContainerCmdModifier(cmd -> cmd.getHostConfig().withRuntime("nvidia"))` if needed.

6. Documentation and DX
   - Document local prerequisites (Docker, Java, Maven/Gradle).  
   - Provide Makefile tasks: build, test.  
   - Add notes for CI (GitHub Actions) enabling Docker.  
   - Add troubleshooting tips.

## Directory Structure (proposed)

- docker/
  - Dockerfile
  - healthcheck.sh
- java/
  - pom.xml (or build.gradle)
  - src/main/java/ai/tribit/gradienttracer/GradientTracerContainer.java
  - src/test/java/ai/tribit/gradienttracer/GradientTracerContainerTest.java
- examples/
  - minimal.py
- README.md (top-level)
- PLAN.md (this file)

## Draft Dockerfile (CPU-only)

```
FROM python:3.11-slim

ENV PIP_DISABLE_PIP_VERSION_CHECK=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential git curl ca-certificates \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Pin pip tools
RUN python -m pip install --upgrade pip~=24.2 setuptools~=75.0 wheel~=0.44

# Install gradienttracer from Git (pin by commit or tag)
ARG GT_REF=main
RUN pip install "git+https://github.com/sk-ai-net/gradienttracer@${GT_REF}"

# Optional: create non-root user
RUN useradd -ms /bin/bash appuser
USER appuser

# Default keep-alive command; Java will exec concrete commands
CMD ["python", "-c", "import time; time.sleep(10**9)"]
```

## Draft Java container class (sketch)

```
public class GradientTracerContainer extends GenericContainer<GradientTracerContainer> {
  public GradientTracerContainer(ImageFromDockerfile image) {
    super(image);
    withStartupTimeout(Duration.ofMinutes(5));
  }

  public ExecResult execPython(String... args) throws IOException, InterruptedException {
    String[] cmd = Stream.concat(Stream.of("python"), Arrays.stream(args)).toArray(String[]::new);
    return execInContainer(cmd);
  }

  public ExecResult runModule(String module, String... args) throws IOException, InterruptedException {
    String[] cmd = Stream.concat(Stream.of("python", "-m", module), Arrays.stream(args)).toArray(String[]::new);
    return execInContainer(cmd);
  }
}
```

## Open Questions
- Does GradientTracer expose a CLI command we should prefer over `python -m`?
- Are there datasets or model weights needed for sample traces? If so, how to cache them in CI?
- Any ports to expose for visualization or API?

## Next Steps
- Validate assumptions by inspecting the repo and trying a manual run.  
- Iterate on the Dockerfile to ensure installation works.  
- Implement the Java module and example tests.  
- Optimize image size and caching.
