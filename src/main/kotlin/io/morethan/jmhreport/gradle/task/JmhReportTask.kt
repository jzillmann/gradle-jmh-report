/**
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.morethan.jmhreport.gradle.task

import com.beust.klaxon.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import io.morethan.jmhreport.gradle.JmhReportExtension
import java.io.File
import io.morethan.jmhreport.ReportGenerator

/**
 * Task for generating a jmh-report from the default report location (configured through JmhReportExtension)
 **/
open class JmhReportTask : DefaultTask() {

    //TODO declare input as input
    @Suppress("UNCHECKED_CAST")
    @TaskAction
    fun generateReport() {
        val extension: JmhReportExtension = project.extensions.getByType(JmhReportExtension::class.java);
        val reportFile = File(extension.jmhResultPath)
        val outputFolder = File(extension.jmhReportOutput)

        check(reportFile.exists(), { "Input '${reportFile.canonicalFile}' does not exists!" })

        val benchmarksJsons = Parser().parse(reportFile.inputStream()) as JsonArray<JsonObject>
        ReportGenerator().createHtmlReport(benchmarksJsons, outputFolder)

        println("JMH Report generated, please open: file://$outputFolder/index.html")
    }
}