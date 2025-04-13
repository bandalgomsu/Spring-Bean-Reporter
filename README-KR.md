# Spring-Bean-Reporter

가벼운 런타임 중 Bean 정보를 알려주는 도구 입니다.

리팩토링 또는 성능 최적화를 하려고 할 떄 가벼운 Bean 정보 도구를 찾으시면 추천드립니다

## 제공 정보

1. FatBean (의존성이 많은 Bean)
2. UnusedBean (사용하지 않는 Bean)
3. CircularBean (순환참조 Bean)
4. SlowBean (초기화가 오래 걸리는 Bean)

### 실행 화면

```
🚀 Bean Reporter Start !!

🧠 Spring Bean Structure Analysis Results

📦 Fat Beans (dependencies ≥ 6):

📭 Unused Beans (possibly dead code):

♻️ Circular Dependencies:

🐢 Slow Bean Initializations (>1000ms):
```

## 사용 방법

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

한줄이면 자동으로 Runner가 실행됩니다.

### Custom Configuration

.yml(or .properties) 을 통해서 slow bean 기준 , fat bean 기준, 탐색할 패키지를 설정 할 수 있습니다.

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

## 기여하기

취약점이나 개선점을 발견하면 이슈 등록 또는 PR 부탁드립니다.

---

## 라이선스

MIT License
Copyright (c) 2025