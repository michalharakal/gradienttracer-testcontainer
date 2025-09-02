# GradientTracer Testcontainer (CPU-only)
FROM python:3.11-slim

ENV PIP_DISABLE_PIP_VERSION_CHECK=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential git curl ca-certificates \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Pin pip toolchain
RUN python -m pip install --upgrade pip~=24.2 setuptools~=75.0 wheel~=0.44

# Version/commit can be overridden at build time: --build-arg GT_REF=<tag-or-sha>
ARG GT_REF=main
RUN pip install "git+https://github.com/sk-ai-net/gradienttracer@${GT_REF}"

# Non-root user
RUN useradd -ms /bin/bash appuser
USER appuser

# Keep the container alive so Testcontainers can exec commands
CMD ["python", "-c", "import time; time.sleep(10**9)"]
