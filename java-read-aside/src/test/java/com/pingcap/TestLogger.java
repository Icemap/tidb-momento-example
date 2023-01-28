package com.pingcap;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestLogger implements LambdaLogger {
  public TestLogger(){}
  public void log(String message){
    System.out.println(message);
  }
  public void log(byte[] message){
    System.out.println(new String(message));
  }
}
