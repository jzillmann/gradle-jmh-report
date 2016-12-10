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
package io.morethan.jmhreport.html

import kotlinx.html.*
import kotlinx.html.stream.*
import io.morethan.jmhreport.Chart

//TODO increase to 12 ? http://colorbrewer2.org/#type=qualitative&scheme=Paired&n=12
val DEFAULT_COLOURS: List<String> = listOf(
		"rgba(241, 88, 84, 0.9)",
        "rgba(93, 165, 218, 0.9)",
        "rgba(96, 189, 104, 0.9)",
        "rgba(250, 164, 58, 0.9)",
        "rgba(241, 124, 176, 0.9)",
        "rgba(178, 145, 47, 0.9)",
        "rgba(178, 118, 178, 0.9)",
        "rgba(222, 207, 63, 0.9)",
        "rgba(77, 77, 77, 0.9)",
        "rgba(31, 120, 180, 0.9)",
        "rgba(51, 160, 44, 0.9)",
        "rgba(106, 61, 154, 0.9)")

class ChartJsPage(val title: String, val charts: List<Chart>) : HtmlPage {

    override fun title(): String {
        return title;
    }

    override fun scriptSources(): List<String> {
        return listOf("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.2.2/Chart.js")
    }

    override fun createBody(body: BODY) {
        val pageTitle = title
        body.apply {
            h1 { +"$pageTitle" }
            a("index.html") { +"Back" }
            charts.forEachIndexed() { i, chart ->
                h1 { +"${chart.title}" }
                val chartId = "chart-$i"
                canvas() {
                    id = chartId
                    width = "200"
                    height = "50"
                }
                script() {
                    unsafe {
                        +"var ctx = document.getElementById(\"${chartId}\")\n"
                        chart.datasets.forEachIndexed() { i, dataset ->
                            +"var data$i = ${dataset.dataPoints.map { it.second }}\n"
                        }
                        +"var myChart = new Chart(ctx, {\n"
                        +"type: 'horizontalBar',\n"
                        +"data: {\n"
                        +"labels: [\"current\"],\n"
                        +"datasets: [\n"
                        check(chart.datasets.size <= DEFAULT_COLOURS.size) { "There are ${chart.datasets.size} different datasets for '${pageTitle}.${chart.title}' but only ${DEFAULT_COLOURS.size} colors defined!" }
                        val colorIterator = DEFAULT_COLOURS.iterator()
                        chart.datasets.forEachIndexed() { i, dataset ->
                            val color: String = colorIterator.next()
                            +"{\n"
                            +"label: '${dataset.name}',\n"
                            +"data: data$i,\n"
                            +"fill: false,\n"
                            +"lineTension: 0.05,\n"
                            +"borderColor: '${color}',\n"
                            +"backgroundColor: '${color}',\n"
                            +"borderWidth: 5\n"
                            +"}\n,"
                        }
                        +"""
                    ]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero:true
                            }
                        }],
                        xAxes: [{
                            ticks: {
                                beginAtZero:true
                            }
                        }]
                    }
                }
            });

                            """
                    }
                }
                hr()
            }
        }
    }
}