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
import kotlinx.html.dom.*
import kotlinx.html.stream.*

fun addHeader(html: HTML, page: HtmlPage) {
    html.apply {
        head {
            meta {
                httpEquiv = "content-type"
                content = "text/html; charset=utf-8"
            }
            title { +page.title() }
            page.scriptSources().forEach {
                script {
                    src = it
                }
            }
        }
    }
}

fun addFooter(body: BODY) {
    body.apply {
        br()
        br()
        hr()
        div {
            i() {
                +"Brought to you by "
                a("https://github.com/jzillmann/gradle-jmh-report") { +"JMH-Report!" }
            }

        }

    }
}

/**
 * A representation of a html page.
 */
interface HtmlPage {

    fun title(): String;

    fun scriptSources(): List<String>;

    fun createBody(body: BODY);

}