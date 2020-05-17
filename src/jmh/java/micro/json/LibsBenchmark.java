package micro.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LibsBenchmark {

    private static final String       exampleSmall;
    private static final String       exampleTweet;
    private static final String       exampleNumbers;
    private static final ObjectMapper jacksonMapper = new ObjectMapper();

    static {
        try {
            exampleSmall = Files.readString(Path.of("src/jmh/resources/small.json"));
            exampleTweet = Files.readString(Path.of("src/jmh/resources/tweet.json"));
            exampleNumbers = Files.readString(Path.of("src/jmh/resources/numbers.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public Object smallMicro() {
        return Json.parse(exampleSmall);
    }

    @Benchmark
    public JsonNode smallJackson() throws JsonProcessingException {
        return jacksonMapper.readTree(exampleSmall);
    }

    @Benchmark
    public JsonElement smallGson() {
        return com.google.gson.JsonParser.parseString(exampleSmall);
    }


    @Benchmark
    public Object emptyObjectMicro() {
        return Json.parse("{ }");
    }

    @Benchmark
    public JsonNode emptyObjectJackson() throws JsonProcessingException {
        return jacksonMapper.readTree("{ }");
    }

    @Benchmark
    public JsonElement emptyObjectGson() {
        return com.google.gson.JsonParser.parseString("{ }");
    }


    @Benchmark
    public Object tweetMicro() {
        return Json.parse(exampleTweet);
    }

    @Benchmark
    public JsonNode tweetJackson() throws JsonProcessingException {
        return jacksonMapper.readTree(exampleTweet);
    }

    @Benchmark
    public JsonElement tweetGson() {
        return com.google.gson.JsonParser.parseString(exampleTweet);
    }
}
