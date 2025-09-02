# 8. Crosscutting Concepts

- Configuration: image build arg `GT_REF` to pin GradientTracer revision.
- Observability: use container logs, stdout/stderr from exec results in assertions.
- Security: run as non-root user in container; minimize packages installed.
- Reproducibility: pin pip/setuptools/wheel versions; use slim base.
- Testing: deterministic tests avoiding network when possible; cache model/data if needed.
