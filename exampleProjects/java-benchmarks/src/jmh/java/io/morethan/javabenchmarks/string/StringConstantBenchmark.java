package io.morethan.javabenchmarks.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
public class StringConstantBenchmark {

    private static final String STRING_SEPERATOR = ",";
    private static final char CHAR_SEPERATOR = ',';
    private String[] _tokens = new String[] { "a", "b", "c", "d", "e", "f", "g" };

    private String validate(String string) {
        assertThat(string).isEqualTo("a,b,c,d,e,f,g,");
        return string;
    }

    @Benchmark
    public String buildStringWithSeperatorAsStringConstant() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(STRING_SEPERATOR);
        }
        return validate(builder.toString());
    }

    @Benchmark
    public String buildStringWithSeperatorAsCharConstant() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(CHAR_SEPERATOR);
        }
        return validate(builder.toString());
    }

    @Benchmark
    public String buildStringWithStringSeperator() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(",");
        }
        return validate(builder.toString());
    }

    @Benchmark
    public String buildStringWithCharSeperator() {
        StringBuilder builder = new StringBuilder();
        for (String string : _tokens) {
            builder.append(string).append(',');
        }
        return validate(builder.toString());
    }
}
