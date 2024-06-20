package controller;

import java.io.IOException;

/**
 * The Controller interface defines the contract for classes
 * that control the flow of the stock application.
 * It provides methods for starting the controller and adding original stocks to the library.
 */
public interface Controller {
  /**
   * Starts the controller and handles user interactions.
   *
   * @throws Exception if an error occurs during the execution of the method
   */
  void start() throws Exception;

  /**
   * Adds the original stocks to the library based on provided name.
   * Stock data is read from the files under resources. GOOG, NVDA, AAPL
   * are the sample stocks that have been provided
   *
   * @param name the name of the stock data file
   * @throws IOException if an I/O error occurs while reading the stock data
   */
  public void addOriginalStocksToLibrary(String name) throws IOException;
}