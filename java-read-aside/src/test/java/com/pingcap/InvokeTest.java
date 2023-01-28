package com.pingcap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.strategy.sampling.NoSamplingStrategy;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;
import java.io.IOException;
import java.nio.file.Files;

class InvokeTest {
  public InvokeTest() {
    AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
    builder.withSamplingStrategy(new NoSamplingStrategy());
    AWSXRay.setGlobalRecorder(builder.build());
  }

  @Test
  void invokeTest() {
    AWSXRay.beginSegment("blank-java-test");

    Context context = new TestContext();
    Handler handler = new Handler();

    String path = "src/test/resources/event.json";
    String eventString = loadJsonFile(path);
    HandlerRequest req = new Gson().fromJson(eventString, HandlerRequest.class);

    String result = handler.handleRequest(req, context);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        // Parse the result. If it occurs error. Here will throw it.
        sdf.parse(result);
        System.out.println(result);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      AWSXRay.endSegment();
  }

  private static String loadJsonFile(String path)
  {
    StringBuilder stringBuilder = new StringBuilder();
    try (Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8))
    {
      stream.forEach(s -> stringBuilder.append(s));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }
}
