package io.morethan.javabenchmarks.string;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@CompilerControl(org.openjdk.jmh.annotations.CompilerControl.Mode.EXCLUDE)
public class ConcatenatedStringBenchmark {

    private String[] _strings = new String[] { "This", " is", " a", " concatenated", " String!" };

    private String validate(String string) {
        assertThat(string).isEqualTo("This is a concatenated String!").hasSize(30);
        return string;
    }

    @Benchmark
    public String stringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : _strings) {
            stringBuilder.append(string);
        }
        return validate(stringBuilder.toString());
    }

    @Benchmark
    public String stringBuffer() {
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : _strings) {
            stringBuffer.append(string);
        }
        return validate(stringBuffer.toString());
    }

    @Benchmark
    public String pluses() {
        String result = "";
        for (String string : _strings) {
            result += string;
        }
        return validate(result);
    }

    @Benchmark
    public String stringBuilderPreSized() {
        StringBuilder stringBuilder = new StringBuilder(30);
        for (String string : _strings) {
            stringBuilder.append(string);
        }
        return validate(stringBuilder.toString());
    }

    @Benchmark
    public String stringBufferPreSized() {
        StringBuffer stringBuffer = new StringBuffer(30);
        for (String string : _strings) {
            stringBuffer.append(string);
        }
        return validate(stringBuffer.toString());
    }

}
