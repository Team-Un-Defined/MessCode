package com.messcode.client;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Start {
  private static final Logger log4j = LogManager.getLogger(RunApp.class);
  public static void main(String[] args) {
        log4j.debug("Debug Message Logged !!!", new NullPointerException("NullError"));
      log4j.error("Error Message Logged !!!", new NullPointerException("NullError"));
      log4j.info("INFO INFO !!!", new NullPointerException("NullError"));
      
    Application.launch(RunApp.class);
  }
}
