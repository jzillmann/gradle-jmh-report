package io.morethan.jmhreport

import com.beust.klaxon.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.i
import kotlinx.html.stream.createHTML
import org.jetbrains.spek.api.Spek
import java.io.File
import io.morethan.jmhreport.*
import io.morethan.jmhreport.html.*
import io.morethan.jmhreport.jmh.*

fun Any.print() = println(this.toString())

class RenderingTest : Spek({

//    @Suppress("UNCHECKED_CAST")
//    given("Something") {
//        val outputFolder = File("build/output")
//        outputFolder.mkdirs()
//        var inputFile = File("exampleProjects/java-benchmarks/build/reports/jmh/results.json")
//        check(inputFile.exists(), { "Input '${inputFile.canonicalFile}' does not exists!" })
//        val benchmarksJsons = Parser().parse(inputFile.inputStream()) as JsonArray<JsonObject>
//        
//        ReportGenerator().createHtmlReport(benchmarksJsons, outputFolder)
//    }
})