# Spring-Bean-Reporter

[README-KR](https://github.com/bandalgomsu/Spring-Bean-Reporter/blob/main/README-KR.md)

A lightweight runtime tool that provides insights into your Spring Beans.

If you're looking for a simple utility to assist in refactoring or performance optimization, this tool is highly
recommended.

## Features

1. FatBean – Beans with too many dependencies
2. UnusedBean – Beans that are not used anywhere
3. CircularBean – Beans involved in circular dependencies
4. SlowBean – Beans that take a long time to initialize

### Execution View

```
🚀 Bean Reporter Start !!

🧠 Spring Bean Structure Analysis Results

📦 Fat Beans (dependencies ≥ 6):

📭 Unused Beans (possibly dead code):

♻️ Circular Dependencies:

🐢 Slow Bean Initializations (>1000ms):
```

## How to Use

### Dependency

Maven

```
<dependency>
    <groupId>io.github.bandalgomsu</groupId>
    <artifactId>spring-bean-reporter</artifactId>
    <version>1.0.1</version>
</dependency>
```

Gradle

```
implementation 'io.github.bandalgomsu:spring-bean-reporter:1.0.1'
```

### Annotaion

```jsx
@EnableBeanReporter
```

Just one line and the runner will automatically start.

### Custom Configuration

You can customize thresholds and scanning packages via .yml(or .properties)

.yml

```
bean:
    report:
        initThresholdMs: 100 //default = 100
fatBeanDependencyThreshold: 6 // default = 6
includeBasePackages: "com.my-project" // default = all pacakage
```

.properties

```
bean.report.init-threshold-ms=1000
bean.report.fat-bean-dependency-threshold=6
bean.report.include-base-packages=com.example
```

## Contribute

If you find any issues or have suggestions for improvement, feel free to open an issue or submit a PR

---

## License

MIT License
Copyright (c) 2025