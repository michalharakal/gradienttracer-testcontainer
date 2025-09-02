# 11. Risks and Technical Debt

- Build time risk: first-time Docker builds can be slow in CI.
- Upstream changes: GradientTracer repository API/installation may change; pin to tags.
- Python dependency resolution: potential conflicts; need to lock versions if issues arise.
- Optional GPU support introduces complexity in CI runners.
