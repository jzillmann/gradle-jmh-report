package io.morethan.jmhreport.jmh

import org.jetbrains.spek.api.Spek
import org.assertj.core.api.Assertions.*;
import com.beust.klaxon.*

class BenchmarkTest : Spek({

    describe("A created Benchmark ID") {

        val benchmarkId = Benchmark("abc.xyz", "MyClass", "test1", JsonObject());
        it("should return correct values") {
            assertThat(benchmarkId.packagePath).isEqualTo("abc.xyz")
            assertThat(benchmarkId.className).isEqualTo("MyClass")
            assertThat(benchmarkId.methodName).isEqualTo("test1")
            assertThat(benchmarkId.id()).isEqualTo("abc.xyz.MyClass.test1")
        }
    }

    describe("A parsed Benchmark ID") {
        val benchmarkJson = json {
            obj(Pair("benchmark","abc.xyz.MyClass.test1"))
        }
       
        val benchmarkId = Benchmark.parse(benchmarkJson);//"abc.xyz.MyClass.test1");
        println(benchmarkId)
        it("should return correct values") {
            assertThat(benchmarkId.packagePath).isEqualTo("abc.xyz")
            assertThat(benchmarkId.className).isEqualTo("MyClass")
            assertThat(benchmarkId.methodName).isEqualTo("test1")
            assertThat(benchmarkId.id()).isEqualTo("abc.xyz.MyClass.test1")
        }
    }
})