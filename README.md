# Gradle-JMH-Report

**_gradle-jmh-report_** is a [Gradle](http://gradle.org) plugin to manage and visualize the result of your [JMH](http://openjdk.java.net/projects/code-tools/jmh/) benchmarks.

*Note:* This plugin is not for executing benchmarks, its for reporting on already executed benchmarks. There are multiple ways how you can setup your JMH benchmark execution in Gradle, i've written in this [blog post](https://blog.morethan.io/jmh-with-gradle-from-easy-to-simple-dc872d57cf7f) about it!


## News

- 2017/07/30 - 0.5.0 Release - On pair with jmh-visualizer-0.5 / Focusing on Benchmarks
- 2017/05/30 - 0.4.0 Release - On pair with jmh-visualizer-0.4 / Report on secondary metrics
- 2017/05/14 - 0.2.0 Release - Integrate https://github.com/jzillmann/jmh-visualizer
- 2016/12/10 - 0.1.0 Release



## Features

- Create a HTML report for one benchmark run

**Wanted:**
- Create a HTML report for multiple benchmark runs 
- Archive your benchmark results in a structured way


## Getting Started
- Setup JMH for your project (However you wanne do it, you can use a [plugin](https://github.com/melix/jmh-gradle-plugin) or a simple [do-it-your-self script](exampleProjects/jmh.gradle). 
- Add the plugin:
```
plugins {
  id "io.morethan.jmhreport" version "0.5.0"
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

![screenshot](https://cloud.githubusercontent.com/assets/148472/26032319/ace91322-3890-11e7-9d0e-7314020a8953.png)


## Building the plugin
- Prepare the project for import into Eclipse: _./gradlew cleanEclipse eclipse_
- Execute the examples: _./gradlew jar ;./gradlew -p exampleProjects/java-benchmarks/ jmh_
- How to publish the Gradle plugin:
  - (Optional) Integrate new version of https://github.com/jzillmann/jmh-visualizer
    - npm run providedZip
    - mv jmh-visualizer.zip ../../eclipse/gradle-jmh-report/src/main/resources/
  - Increase version in _gradle.properties_
  - Update _News_ and _Getting Started_ in _README.md_
  - Update version number of in build.gradle for all example projects
  - Test report: ```gw jar ;gw  jmh -p exampleProjects/java-benchmarks/ -Pinclude=".*QuickBenchmark.*"```
  - Commit & Push
  - Execute: _./gradlew publishPlugins_
