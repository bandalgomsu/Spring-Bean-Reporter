# Spring-Bean-Reporter

A lightweight runtime tool that provides insights into your Spring Beans.

If you're looking for a simple utility to assist in refactoring or performance optimization, this tool is highly
recommended.

## Features

1. FatBean – Beans with too many dependencies
2. UnusedBean – Beans that are not used anywhere
3. CircularBean – Beans involved in circular dependencies
4. SlowBean – Beans that take a long time to initialize

## How to Use

### Annotaion

```jsx
@EnableBeanReporter
```

Just one line and the runner will automatically start.

### Custom Configuration

You can customize thresholds and scanning packages via .yml(or .properties)

```jsx
bean:
    report:
        initThresholdMs: 100 //default = 100
fatBeanDependencyThreshold: 6 // default = 6
includeBasePackages: "com.my-project" // default = 전체 패키지
```

## Contribute

If you find any issues or have suggestions for improvement, feel free to open an issue or submit a PR

---

## License

MIT License
Copyright (c) 2025