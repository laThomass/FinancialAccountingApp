package model;

import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for the Portfolio class.
 */
public class PortfolioTest {

  private AlphaAPI api;
  private Map<String, List<Stock>> library;

  @Test
  public void testCreatePortfolio() {
    Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
    assertEquals("TestPortfolio", portfolio.getName());
    assertTrue(portfolio.getStocks().isEmpty());
  }

  @Test
  public void testCreatePortfolioWithStocks() {
    Map<String, List<Stock>> stocks = new HashMap<>();
    Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio", stocks);
    assertEquals("TestPortfolio", portfolio.getName());
    assertEquals(stocks, portfolio.getStocks());
  }

  @Test
  public void testSaveAndLoadPortfolio() {
    try {
      // Create a portfolio and add stocks
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");

      // Save the portfolio
      File file = File.createTempFile("portfolio", ".txt");
      Portfolio.savePortfolio(portfolio, file);

      // Load the portfolio
      Portfolio loadedPortfolio = Portfolio.loadPortfolio(file);

      // Verify the loaded portfolio
      assertEquals(portfolio.getName(), loadedPortfolio.getName());
      assertEquals(portfolio.getStocks(), loadedPortfolio.getStocks());

      // Clean up the temp file
      assertTrue(file.delete());
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testAddStock() {
    try {
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");

      Map<String, List<Stock>> stocks = portfolio.getStocks();
      assertEquals(1, stocks.size());
      assertTrue(stocks.containsKey("AAPL"));
      assertEquals(10, stocks.get("AAPL").get(0).getVolume(), 0.001);
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testRemoveStock() {
    try {
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");
      portfolio.removeStock("AAPL", 5, "2020-01-01");

      Map<String, List<Stock>> stocks = portfolio.getStocks();
      assertEquals(1, stocks.size());
      assertTrue(stocks.containsKey("AAPL"));
      assertEquals(5, stocks.get("AAPL").get(0).getVolume(), 0.001);
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testCalculatePortfolioValue() {
    try {
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");

      MockAlphaAPI mockAPI = new MockAlphaAPI();
      Map<String, List<Stock>> library = new HashMap<>();
      mockAPI.fetchData("AAPL", library);

      double value = portfolio.calculatePortfolioValue("2020-01-01", mockAPI, library);
      assertEquals(1050.0, value, 0.001);
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testGetComposition() {
    try {
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");

      Map<String, Double> composition = portfolio.getComposition("2020-01-01");
      assertEquals(1, composition.size());
      assertTrue(composition.containsKey("AAPL"));
      assertEquals(10, composition.get("AAPL"), 0.001);
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testGetDistributionOfValue() {
    try {
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");

      MockAlphaAPI mockAPI = new MockAlphaAPI();
      Map<String, List<Stock>> library = new HashMap<>();
      mockAPI.fetchData("AAPL", library);

      Map<String, Double> distribution = portfolio.getDistributionOfValue("2020-01-01", mockAPI, library);
      assertEquals(1, distribution.size());
      assertTrue(distribution.containsKey("AAPL"));
      assertEquals(1050.0, distribution.get("AAPL"), 0.001);
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testGetPortfolioValuesOverTime() {
    try {
      Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
      portfolio.addStock("AAPL", 10, "2020-01-01");

      MockAlphaAPI mockAPI = new MockAlphaAPI();
      Map<String, List<Stock>> library = new HashMap<>();
      mockAPI.fetchData("AAPL", library);

      Map<String, Double> valuesOverTime = portfolio.getPortfolioValuesOverTime("2020-01-01", "2020-03-01", mockAPI, library);
      assertEquals(3, valuesOverTime.size());
      assertTrue(valuesOverTime.containsKey("2020-01-01"));
      assertTrue(valuesOverTime.containsKey("2020-02-01"));
      assertTrue(valuesOverTime.containsKey("2020-03-01"));
      assertEquals(1050.0, valuesOverTime.get("2020-01-01"), 0.001);
      assertEquals(1100.0, valuesOverTime.get("2020-02-01"), 0.001);
      assertEquals(1150.0, valuesOverTime.get("2020-03-01"), 0.001);
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testRebalancePortfolio() throws Exception {
    // Set up initial stock data
    Map<String, List<Stock>> library = new HashMap<>();
    library.put("NFLX", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 0)));
    library.put("GOOG", List.of(new Stock("2022-01-01", 0, 0, 0, 25, 0)));
    library.put("MSFT", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 0)));
    library.put("AAPL", List.of(new Stock("2022-01-01", 0, 0, 0, 50, 0)));

    Map<String, List<Stock>> stocks = new HashMap<>();
    stocks.put("NFLX", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 25)));
    stocks.put("GOOG", List.of(new Stock("2022-01-01", 0, 0, 0, 25, 10)));
    stocks.put("MSFT", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 25)));
    stocks.put("AAPL", List.of(new Stock("2022-01-01", 0, 0, 0, 50, 5)));

    Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio", stocks);

    // Change stock prices
    library.put("NFLX", List.of(new Stock("2022-02-01", 0, 0, 0, 15, 0)));
    library.put("GOOG", List.of(new Stock("2022-02-01", 0, 0, 0, 30, 0)));
    library.put("MSFT", List.of(new Stock("2022-02-01", 0, 0, 0, 10, 0)));
    library.put("AAPL", List.of(new Stock("2022-02-01", 0, 0, 0, 30, 0)));

    // Rebalance portfolio
    Map<String, Double> weights = new HashMap<>();
    weights.put("NFLX", 0.25);
    weights.put("GOOG", 0.25);
    weights.put("MSFT", 0.25);
    weights.put("AAPL", 0.25);

    portfolio.rebalancePortfolio("2022-02-01", new AlphaAPI(), library, weights);

    // Verify new stock quantities
    Map<String, List<Stock>> rebalancedStocks = portfolio.getStocks();
    double delta = 0.0001; // Acceptable delta for floating-point comparisons

    assertEquals(17.916666666666668, rebalancedStocks.get("NFLX").get(0).getVolume(), delta);
    assertEquals(8.958333333333334, rebalancedStocks.get("GOOG").get(0).getVolume(), delta);
    assertEquals(26.875, rebalancedStocks.get("MSFT").get(0).getVolume(), delta);
    assertEquals(8.958333333333334, rebalancedStocks.get("AAPL").get(0).getVolume(), delta);
  }

  @Test
  public void testRebalancePortfolioInvalidWeights() throws Exception {
    // Set up initial stock data
    Map<String, List<Stock>> library = new HashMap<>();
    library.put("NFLX", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 0)));
    library.put("GOOG", List.of(new Stock("2022-01-01", 0, 0, 0, 25, 0)));
    library.put("MSFT", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 0)));
    library.put("AAPL", List.of(new Stock("2022-01-01", 0, 0, 0, 50, 0)));

    // Create and add stocks to the portfolio
    Map<String, List<Stock>> stocks = new HashMap<>();
    stocks.put("NFLX", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 25))); // 25 shares at $10
    stocks.put("GOOG", List.of(new Stock("2022-01-01", 0, 0, 0, 25, 10))); // 10 shares at $25
    stocks.put("MSFT", List.of(new Stock("2022-01-01", 0, 0, 0, 10, 25))); // 25 shares at $10
    stocks.put("AAPL", List.of(new Stock("2022-01-01", 0, 0, 0, 50, 5)));  // 5 shares at $50

    Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio", stocks);

    // Change stock prices for the rebalance date
    library.put("NFLX", List.of(new Stock("2022-02-01", 0, 0, 0, 15, 0))); // New price $15
    library.put("GOOG", List.of(new Stock("2022-02-01", 0, 0, 0, 30, 0))); // New price $30
    library.put("MSFT", List.of(new Stock("2022-02-01", 0, 0, 0, 10, 0))); // New price $10
    library.put("AAPL", List.of(new Stock("2022-02-01", 0, 0, 0, 30, 0))); // New price $30

    // Specify weights for rebalancing (weights do not add up to 100%)
    Map<String, Double> invalidWeights = new HashMap<>();
    invalidWeights.put("NFLX", 0.30);
    invalidWeights.put("GOOG", 0.30);
    invalidWeights.put("MSFT", 0.20);
    invalidWeights.put("AAPL", 0.10);

    // Expect an exception to be thrown
    try {
      portfolio.rebalancePortfolio("2022-02-01", new AlphaAPI(), library, invalidWeights);
      fail("Expected IllegalArgumentException due to invalid weights");
    } catch (IllegalArgumentException e) {
      assertEquals("Weights must add up to 1.0 (100%)", e.getMessage());
    }
  }

  @Test
  public void testPrintPortfolioPerformanceChart() {
    // Create a portfolio and add stocks
    Portfolio portfolio = Portfolio.createPortfolio("TestPortfolio");
    try {
      portfolio.addStock("AAPL", 10, "2020-01-01");
      portfolio.addStock("AAPL", 15, "2020-02-01");
      portfolio.addStock("AAPL", 20, "2020-03-01");
    } catch (Exception e) {
      fail("Setup failed: " + e.getMessage());
    }

    // Create a mock API and preload the library with mock data
    IAlphaAPIInterface mockAPI = new AlphaAPI();
    Map<String, List<Stock>> library = new HashMap<>();
    try {
      mockAPI.fetchData("AAPL", library);
    } catch (IOException e) {
      fail("Failed to fetch mock data: " + e.getMessage());
    }

    // Redirect the console output to capture the chart
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    // Call the method to print the performance chart
    portfolio.printPortfolioPerformanceChart("2020-01-01", "2020-07-01", mockAPI,
            library);

    // Reset the standard output
    System.setOut(System.out);

    // Verify the output
    String expectedOutput = "Performance of portfolio TestPortfolio from 2020-01-01 to 2020-07-01\n"
            +
            "\n" +
            "2020-01-01: \n" +
            "2020-01-08: *********\n" +
            "2020-01-15: *********\n" +
            "2020-01-22: *********\n" +
            "2020-01-29: *********\n" +
            "2020-02-05: ************************\n" +
            "2020-02-12: ************************\n" +
            "2020-02-19: ************************\n" +
            "2020-02-26: **********************\n" +
            "2020-03-04: *****************************************\n" +
            "2020-03-11: *************************************\n" +
            "2020-03-18: *********************************\n" +
            "2020-03-25: *********************************\n" +
            "2020-04-01: *********************************\n" +
            "2020-04-08: ************************************\n" +
            "2020-04-15: ***************************************\n" +
            "2020-04-22: *************************************\n" +
            "2020-04-29: ***************************************\n" +
            "2020-05-06: *****************************************\n" +
            "2020-05-13: ******************************************\n" +
            "2020-05-20: *******************************************\n" +
            "2020-05-27: *******************************************\n" +
            "2020-06-03: ********************************************\n" +
            "2020-06-10: ************************************************\n" +
            "2020-06-17: ************************************************\n" +
            "2020-06-24: *************************************************\n" +
            "2020-07-01: **************************************************\n" +
            "\n" +
            "Scale: * = $327.699\n";

    assertEquals(expectedOutput, outputStream.toString());
  }



}




