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
    IAlphaAPIInterface api = new AlphaAPI();
    Map<String, List<Stock>> library = new HashMap<>();

    // Add stock data to the library
    library.put("NFLX", createStockData("2022-01-01", 10, 10, 10, 10, 25)); // Netflix
    library.put("GOOGL", createStockData("2022-01-01", 25, 25, 25, 25, 10)); // Google
    library.put("MSFT", createStockData("2022-01-01", 10, 10, 10, 10, 25)); // Microsoft
    library.put("AAPL", createStockData("2022-01-01", 50, 50, 50, 50, 5));  // Apple

    // Create initial portfolio
    Map<String, List<Stock>> stocks = new HashMap<>();
    stocks.put("NFLX", createStockData("2022-01-01", 10, 10, 10, 10, 25)); // 25 shares of Netflix
    stocks.put("GOOGL", createStockData("2022-01-01", 25, 25, 25, 25, 10)); // 10 shares of Google
    stocks.put("MSFT", createStockData("2022-01-01", 10, 10, 10, 10, 25)); // 25 shares of Microsoft
    stocks.put("AAPL", createStockData("2022-01-01", 50, 50, 50, 50, 5));  // 5 shares of Apple

    Portfolio portfolio = new Portfolio("MyPortfolio", stocks);

    // Update stock prices for the rebalancing date
    library.put("NFLX", createStockData("2022-02-01", 15, 15, 15, 15, 25)); // Netflix
    library.put("GOOGL", createStockData("2022-02-01", 30, 30, 30, 30, 10)); // Google
    library.put("MSFT", createStockData("2022-02-01", 10, 10, 10, 10, 25)); // Microsoft
    library.put("AAPL", createStockData("2022-02-01", 30, 30, 30, 30, 5));  // Apple

    // Define the target weights
    Map<String, Double> weights = new HashMap<>();
    weights.put("NFLX", 0.25);
    weights.put("GOOGL", 0.25);
    weights.put("MSFT", 0.25);
    weights.put("AAPL", 0.25);

    // Rebalance portfolio
    portfolio.rebalancePortfolio("2022-02-01", api, library, weights);

    // Calculate expected shares after rebalancing
    double totalValue = portfolio.calculatePortfolioValue("2022-02-01", api, library);

    int expectedNetflixShares = (int) (0.25 * totalValue / 15);
    int expectedGoogleShares = (int) (0.25 * totalValue / 30);
    int expectedMicrosoftShares = (int) (0.25 * totalValue / 10);
    int expectedAppleShares = (int) (0.25 * totalValue / 30);

    System.out.println("Total Value: " + totalValue);
    System.out.println("Expected Netflix Shares: " + expectedNetflixShares);
    System.out.println("Expected Google Shares: " + expectedGoogleShares);
    System.out.println("Expected Microsoft Shares: " + expectedMicrosoftShares);
    System.out.println("Expected Apple Shares: " + expectedAppleShares);

    System.out.println("Actual Netflix Shares: " + portfolio.getStocks().get("NFLX").get(0).getVolume());
    System.out.println("Actual Google Shares: " + portfolio.getStocks().get("GOOGL").get(0).getVolume());
    System.out.println("Actual Microsoft Shares: " + portfolio.getStocks().get("MSFT").get(0).getVolume());
    System.out.println("Actual Apple Shares: " + portfolio.getStocks().get("AAPL").get(0).getVolume());

    assertEquals(expectedNetflixShares, portfolio.getStocks().get("NFLX").get(0).getVolume());
    assertEquals(expectedGoogleShares, portfolio.getStocks().get("GOOGL").get(0).getVolume());
    assertEquals(expectedMicrosoftShares, portfolio.getStocks().get("MSFT").get(0).getVolume());
    assertEquals(expectedAppleShares, portfolio.getStocks().get("AAPL").get(0).getVolume());
  }

  private List<Stock> createStockData(String date, double open, double high, double low, double close, int volume) {
    List<Stock> stockData = new ArrayList<>();
    stockData.add(new Stock(date, open, high, low, close, volume));
    return stockData;
  }
}
