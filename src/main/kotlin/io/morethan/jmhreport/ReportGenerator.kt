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
package io.morethan.jmhreport

import java.io.File
import com.beust.klaxon.*
import kotlinx.html.*
import kotlinx.html.stream.*
import io.morethan.jmhreport.jmh.Benchmark
import io.morethan.jmhreport.html.*

class ReportGenerator {

    fun createHtmlReport(jsonReport: JsonArray<JsonObject>, outputFolder: File) {
        outputFolder.mkdirs()
        check(outputFolder.isDirectory, { "Given output-folder ${outputFolder.canonicalFile} cannot be created!" })

        val benchmarks: List<Benchmark> = jsonReport.map { Benchmark.parse(it) }.toList();
        val childPages: MutableList<Link> = mutableListOf()
        benchmarks.groupBy { benchmark -> benchmark.packagePath }.map {
            val benchmarkPackage = it.key
            val childPagePath = benchmarkPackage.replace('.', '-') + ".html"
            val childPageLink = Link(lable = benchmarkPackage, url = childPagePath)
            childPages.add(childPageLink)

            val charts = it.value.groupBy { it.className }.map { entry ->
                val datasets = entry.value.map { benchmark ->
                    val xValue = "current"
                    val yValue = benchmark.jsonOriginal.obj("primaryMetric")!!.double("score")!!.toLong().toString()
                    Dataset(benchmark.methodName, listOf(Pair(xValue, yValue)))
                }
                Chart(title = entry.key, datasets = datasets)
            }

            val benchmarkPage = ChartJsPage(
                    title = "Benchmarks for $benchmarkPackage",
                    charts = charts);
            val childPageHtml = createHTML().html {
                addHeader(this, benchmarkPage)
                body() {
                    benchmarkPage.createBody(this)
                    addFooter(this)
                }
            }
            File(outputFolder, childPagePath).writeText(childPageHtml)
        }

        val indexPage: IndexPage = IndexPage(childPages);
        val html = createHTML().html {
            addHeader(this, indexPage)
            body() {
                indexPage.createBody(this)
                span(){+ "Produced from this "}
                a("original-report.json") { +"JMH result file." }
                
                addFooter(this)
            }
        }
//        println(html)

        File(outputFolder, "index.html").writeText(html)
        File(outputFolder, "original-report.json").writeText(jsonReport.toJsonString(true))
    }

}