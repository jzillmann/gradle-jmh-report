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

class IndexPage(val childPages: List<Link>) : HtmlPage {

    override fun title(): String {
        return "Home"
    }

    override fun scriptSources(): List<String> {
        return listOf()
    }

    override fun createBody(body: BODY) {

        body.apply {
            h1 { +"Benchmarks" }
            ul() {
                childPages.forEach {
                    li() {
                        a(it.url) { +it.lable }
                    }
                }
            }
        }
    }


}