# Gradle-JMH-Report

**_gradle-jmh-report_** is a [Gradle](http://gradle.org) plugin to manage and visualize the result of your [JMH](http://openjdk.java.net/projects/code-tools/jmh/) benchmarks.

**Features:**
- Create a HTML report for one benchmark run

**Wanted:**
- Create a HTML report for multiple benchmark runs 
- Archive your benchmark results in a structured way
- Switch to using visualizations from [jmh-visualizer](http://jmh.morethan.io)

## News
- 2016/12/10 - 0.1.0 Release

## Getting Started
- Setup JMH for your project (However you wanne do it, you can use a [plugin](https://github.com/melix/jmh-gradle-plugin) or a simple [do-it-your-self script](exampleProjects/jmh.gradle). 
- Add the plugin:
```
plugins {
  id "io.morethan.jmhreport" version "0.1.0"
}
```
- Configure the plugin:
```
jmhReport {
    jmhResultPath = project.file('build/reports/jmh/result.json')
    jmhReportOutput = project.file('build/reports/jmh')
}
tasks.jmh.finalizedBy tasks.jmhReport 
```
Now every time you execute your jmh benchmarks, the _jmhReport_ task will run at the end and point you to the location of the corresponding index.html.

## This is how it looks

!(https://cloud.githubusercontent.com/assets/148472/21073653/f9d42102-bee4-11e6-9590-79530c2f8c77.png)

## Building the plugin
- Prepare the project for import into Eclipse: _./gradlew cleanEclipse eclipse_
- Execute the examples: _./gradlew jar ;./gradlew -p exampleProjects/java-benchmarks/ jmh_
- How to publish the Gradle plugin: TBD