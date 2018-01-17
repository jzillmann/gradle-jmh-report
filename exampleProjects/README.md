# JMH example projects

There is:
- java-benchmarks: benchmarking simple things of Java
- kotlin-benchmarks: benchmarking simple things of Kotlin

h1. How to execute

```
./gradlew jar; ./gradlew -p exampleProjects/java-benchmarks/ jmh 
```

```
./gradlew jar; ./gradlew -p exampleProjects/kotlin-benchmarks/ jmh 
```