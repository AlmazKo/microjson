package micro.json;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LibsBenchmark {

    private static final String          exampleEmpty      = "{ }";
    private static final byte[]          exampleEmptyBytes = "{ }".getBytes();
    private static final String          exampleSmall;
    private static final byte[]          exampleSmallBytes;
    private static final String          exampleTweet;
    private static final byte[]          exampleTweetBytes;
    private static final String          exampleNumbers;
    private static final ObjectMapper    jacksonMapper     = new ObjectMapper();
    private static final DslJson<Object> dslJson           = new DslJson<>();

    static {
        try {
            exampleSmall = Files.readString(Path.of("src/jmh/resources/small.json"));
            exampleSmallBytes = exampleSmall.getBytes();
            exampleTweet = Files.readString(Path.of("src/jmh/resources/tweet.json"));
            exampleTweetBytes = exampleTweet.getBytes();
            exampleNumbers = Files.readString(Path.of("src/jmh/resources/numbers.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //---- parse small json

    @Benchmark
    public Object smallMicro() {
        return Json.parse(exampleSmall);
    }

    @Benchmark
    public Object smallJackson() throws JsonProcessingException {
        return jacksonMapper.readTree(exampleSmall);
    }

    @Benchmark
    public Object smallGson() throws JsonProcessingException {
        return JsonParser.parseString(exampleSmall);
    }

    @Benchmark
    public Object smallDslJson() throws IOException {
        return dslJson.deserialize(Map.class, exampleSmallBytes, exampleSmallBytes.length);
    }



    //---- parse empty object

    @Benchmark
    public Object emptyObjectGson() {
        return JsonParser.parseString(exampleEmpty);
    }

    @Benchmark
    public Object emptyObjectMicro() {
        return Json.parseObject(exampleEmpty);
    }

    @Benchmark
    public Object emptyObjectJackson() throws JsonProcessingException {
        return jacksonMapper.readTree(exampleEmpty);
    }

    @Benchmark
    public Object emptyObjectDslJson() throws IOException {
        return dslJson.deserialize(Map.class, exampleEmptyBytes, exampleEmptyBytes.length);
    }



    //---- parse common tweet

    @Benchmark
    public Object tweetMicro() {
        return Json.parse(exampleTweet);
    }

    @Benchmark
    public Object tweetJackson() throws JsonProcessingException {
        return jacksonMapper.readTree(exampleTweet);
    }

    @Benchmark
    public Object tweetGson() {
        return JsonParser.parseString(exampleTweet);
    }

    @Benchmark
    public Object tweetDslJson() throws IOException {
        return dslJson.deserialize(Map.class, exampleTweetBytes, exampleTweetBytes.length);
    }

}
