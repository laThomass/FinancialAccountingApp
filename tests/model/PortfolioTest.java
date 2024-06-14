package model;

import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for the Portfolio class.
 */
public class PortfolioTest {

  private AlphaAPI api;
  private Map<String, List<Stock>> library;

//  @Before
//  public void setUp() {
//    api = new AlphaAPI();
//    library = new HashMap<>();
//
//    List<Stock> googStocks = Arrays.asList(
//            new Stock("2020-03-04", 1000,
//                    1100, 950, 1050, 10000),
//            new Stock("2020-03-09", 1050,
//                    1150, 1000, 1100, 12000)
//    );
//
//    List<Stock> aaplStocks = Arrays.asList(
//            new Stock("2020-03-04", 200, 210,
//                    190, 205, 15000),
//            new Stock("2020-03-09", 205, 215,
//                    195, 210, 16000)
//    );
//
//    library.put("GOOG", googStocks);
//    library.put("AAPL", aaplStocks);
//  }
//
//  @Test
//  public void testAddStock() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    portfolio.addStock("AAPL", 10);
//
//    assertTrue(portfolio.getStocks().containsKey("AAPL"));
//    assertEquals(10, (int) portfolio.getStocks().get("AAPL"));
//
//    portfolio.addStock("AAPL", 5);
//
//    assertEquals(15, (int) portfolio.getStocks().get("AAPL"));
//  }
//
//  @Test
//  public void testAddMultipleStocks() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    portfolio.addStock("AAPL", 10);
//    portfolio.addStock("GOOG", 5);
//
//    assertTrue(portfolio.getStocks().containsKey("AAPL"));
//    assertTrue(portfolio.getStocks().containsKey("GOOG"));
//    assertEquals(10, (int) portfolio.getStocks().get("AAPL"));
//    assertEquals(5, (int) portfolio.getStocks().get("GOOG"));
//  }
//
//  @Test
//  public void testCalculatePortfolioValue() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    portfolio.addStock("GOOG", 10);
//    portfolio.addStock("AAPL", 20);
//
//    double value = portfolio.calculatePortfolioValue("2020-03-09",
//            api, library);
//
//    assertEquals(10 * 1100 + 20 * 210, value, 0.001);
//    // Expected value: 10*1100 + 20*210
//  }
//
//  @Test
//  public void testCalculatePortfolioValueWithMissingData() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    portfolio.addStock("GOOG", 10);
//    portfolio.addStock("AAPL", 20);
//
//    try {
//      portfolio.calculatePortfolioValue("2020-03-10", api, library);
//      fail("Expected RuntimeException not thrown");
//    } catch (RuntimeException e) {
//      assertTrue(e.getMessage().contains("No data available for the symbol"));
//    }
//  }
//
//  @Test
//  public void testGetName() {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    assertEquals("Test Portfolio", portfolio.getName());
//  }
//
//  @Test
//  public void testCreatePortfolio() {
//    Portfolio portfolio = Portfolio.createPortfolio("Test Portfolio");
//
//    assertEquals("Test Portfolio", portfolio.getName());
//    assertTrue(portfolio.getStocks().isEmpty());
//
//    Map<String, Integer> stocks = new HashMap<>();
//    stocks.put("AAPL", 10);
//    stocks.put("GOOG", 5);
//    Portfolio portfolioWithStocks = Portfolio.createPortfolio("Stocked Portfolio", stocks);
//
//    assertEquals("Stocked Portfolio", portfolioWithStocks.getName());
//    assertEquals(10, (int) portfolioWithStocks.getStocks().get("AAPL"));
//    assertEquals(5, (int) portfolioWithStocks.getStocks().get("GOOG"));
//  }
//
//  @Test
//  public void testGetStocks() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    portfolio.addStock("AAPL", 10);
//    portfolio.addStock("GOOG", 5);
//
//    Map<String, Integer> stocks = portfolio.getStocks();
//    assertEquals(10, (int) stocks.get("AAPL"));
//    assertEquals(5, (int) stocks.get("GOOG"));
//  }
//
//  // Edge Cases
//
//  @Test
//  public void testAddNegativeStockQuantity() {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//    try {
//      portfolio.addStock("AAPL", -5);
//      fail("Expected IllegalArgumentException not thrown");
//    } catch (IllegalArgumentException | IOException e) {
//      assertTrue(e.getMessage().contains("Negative or 0 quantity not allowed"));
//    }
//  }
//
//  @Test
//  public void testAddNonExistentStock() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//    portfolio.addStock("FAdasKE", 10);
//    assertTrue(portfolio.getStocks().containsKey("FAdasKE"));
//  }
//
//  @Test
//  public void testCalculatePortfolioValueWithEmptyLibrary() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//    portfolio.addStock("GOOG", 10);
//    portfolio.addStock("AAPL", 20);
//
//    Map<String, List<Stock>> emptyLibrary = new HashMap<>();
//
//    try {
//      portfolio.calculatePortfolioValue("2020-03-09", api, emptyLibrary);
//      fail("Expected RuntimeException not thrown");
//    } catch (RuntimeException e) {
//      assertTrue(e.getMessage().contains("No data available for the symbol"));
//    }
//  }
//
//  @Test
//  public void testCalculatePortfolioValueWithNullLibrary() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//    portfolio.addStock("GOOG", 10);
//    portfolio.addStock("AAPL", 20);
//
//    try {
//      portfolio.calculatePortfolioValue("2020-03-09", api, null);
//      fail("RuntimeException  not thrown");
//    } catch (RuntimeException e) {
//      // Expected exception
//    }
//  }
//
//  @Test
//  public void testCalculatePortfolioValueOnNonTradingDay() throws IOException {
//    Portfolio portfolio = new Portfolio("Test Portfolio");
//
//    portfolio.addStock("GOOG", 10);
//    portfolio.addStock("AAPL", 20);
//
//    try {
//      portfolio.calculatePortfolioValue("2020-03-08", api, library);
//      fail("Expected RuntimeException not thrown");
//    } catch (RuntimeException e) {
//      assertTrue(e.getMessage().contains("No data available for the symbol"));
//    }
//  }

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


}
