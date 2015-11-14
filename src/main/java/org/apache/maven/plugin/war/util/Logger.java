package org.apache.maven.plugin.war.util;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class Logger {
  private static Log log = new SystemStreamLog();

  public static void info(String msg) {
    log.info(msg);
  }

  public static void error(String msg, Exception ex) {
    log.error(msg, ex);
  }

  public static void debug(String msg) {
    // log.debug(msg);
  }

  public static void systemError(Exception ex) {
    log.error(ex);
    System.exit(-1);
  }

  public static void systemError(String msg, Exception ex) {
    log.error(msg, ex);
    System.exit(-1);
  }
}
