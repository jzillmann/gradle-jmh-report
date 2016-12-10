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
package io.morethan.jmhreport.jmh

import com.beust.klaxon.JsonObject


data class Benchmark(val packagePath: String, val className: String, val methodName: String, val jsonOriginal: JsonObject) {

    fun id(): String {
        return "$packagePath.$className.$methodName"
    }

    companion object {
        public fun parse(json: JsonObject): Benchmark {
            val benchmarkId = json["benchmark"] as String;
            val idComponents: List<String> = benchmarkId.split('.')
            val methodName = idComponents.last()
            val className = idComponents.dropLast(1).last()
            val packagePath = idComponents.dropLast(2).joinToString(".")
            return Benchmark(packagePath, className, methodName, json)
        }
    }
}