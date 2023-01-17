package com.pingcap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.strategy.sampling.NoSamplingStrategy;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class InvokeTest {
  public InvokeTest() {
    AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
    builder.withSamplingStrategy(new NoSamplingStrategy());
    AWSXRay.setGlobalRecorder(builder.build());
  }

  @Test
  void invokeTest() {
    AWSXRay.beginSegment("blank-java-test");
    String jdbcStr = "jdbc:mysql://localhost:4000/test?user=root";
    Context context = new TestContext();
    String requestId = context.getAwsRequestId();
    Handler handler = new Handler();
    String result = handler.handleRequest(jdbcStr, context);
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
}
