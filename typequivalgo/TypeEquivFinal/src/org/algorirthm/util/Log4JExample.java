package org.algorirthm.util;
import org.algorithm.impl.WSDLParser3;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * A simple Java Log4j example class.
 * @author alvin alexander, devdaily.com
 */
public class Log4JExample
{
  
  public static final Logger logger=Logger.getLogger(WSDLParser3.class);

  static final String LOG_PROPERTIES_FILE = "/home/lumala/Desktop/unifalgo/TypeEquivFinal/sampleFiles/Log4J.properties";

  public static void main(String[] args)
  {
    // call our constructor
    new Log4JExample();

    // Log4J is now loaded; try it
    logger.info("leaving the main method of Log4JDemo");
  }

  public Log4JExample()
  {
    initializeLogger();
    logger.info( "Log4JExample - leaving the constructor ..." );
  }

  private void initializeLogger()
  {
	  PropertyConfigurator.configure(LOG_PROPERTIES_FILE);

     logger.info("Logging initialized.");
  }
  public void log(String logThis){
	  logger.info(logThis);
  }
}

