# GradientTracer Testcontainers Plan and Scaffold

This repository contains a proposed plan and a minimal scaffold for creating a custom Testcontainers module that runs Python-based GradientTracer inside a container that can be controlled from Java unit tests.

Repository referenced: https://github.com/sk-ai-net/gradienttracer

## Goals
- Allow Java tests (e.g., JUnit or TestNG) to start a containerized GradientTracer service.
- Provide a simple API to execute Python scripts or modules (e.g., tracer CLI) inside the container from Java.
- Enable result retrieval via logs, exit codes, bound volumes, or an HTTP/gRPC interface if available.
- Make the container image reproducible and pinned to versions.

## High-level Plan
1. Analyze GradientTracer repository to understand runtime requirements and entrypoints.
2. Build a container image for GradientTracer with pinned dependencies.
3. Create a Testcontainers custom module in Java that:
   - Pulls/builds the image,
   - Exposes necessary ports (if any),
   - Mounts test resources where needed,
   - Provides helper methods to run Python code and capture outputs.
4. Demonstrate Java JUnit usage with simple tests executing GradientTracer operations.
5. Document lifecycle, configuration, and examples.

See PLAN.md for details and task breakdown.

Architecture documentation (arc42) is available at docs/arc42/README.md.
