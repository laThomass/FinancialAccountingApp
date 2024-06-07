package Model;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class PortfolioTest {

  private AlphaAPI api;
  private Map<String, List<Stock>> library;

  @Before
  public void setUp() {
    api = new AlphaAPI();
    library = new HashMap<>();

    List<Stock> googStocks = Arrays.asList(
            new Stock("2020-03-04", 1000, 1100, 950, 1050, 10000),
            new Stock("2020-03-09", 1050, 1150, 1000, 1100, 12000)
    );

    List<Stock> aaplStocks = Arrays.asList(
            new Stock("2020-03-04", 200, 210, 190, 205, 15000),
            new Stock("2020-03-09", 205, 215, 195, 210, 16000)
    );

    library.put("GOOG", googStocks);
    library.put("AAPL", aaplStocks);
  }

  @Test
  public void testAddStock() {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    portfolio.addStock("AAPL", 10);

    assertTrue(portfolio.getStocks().containsKey("AAPL"));
    assertEquals(10, (int) portfolio.getStocks().get("AAPL"));

    portfolio.addStock("AAPL", 5);

    assertEquals(15, (int) portfolio.getStocks().get("AAPL"));
  }

  @Test
  public void testAddMultipleStocks() {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    portfolio.addStock("AAPL", 10);
    portfolio.addStock("GOOG", 5);

    assertTrue(portfolio.getStocks().containsKey("AAPL"));
    assertTrue(portfolio.getStocks().containsKey("GOOG"));
    assertEquals(10, (int) portfolio.getStocks().get("AAPL"));
    assertEquals(5, (int) portfolio.getStocks().get("GOOG"));
  }

  @Test
  public void testCalculatePortfolioValue() {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    portfolio.addStock("GOOG", 10);
    portfolio.addStock("AAPL", 20);

    double value = portfolio.calculatePortfolioValue("2020-03-09", api, library);

    assertEquals(10 * 1100 + 20 * 210, value, 0.001); // Expected value: 10*1100 + 20*210
  }

  @Test
  public void testCalculatePortfolioValueWithMissingData() {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    portfolio.addStock("GOOG", 10);
    portfolio.addStock("AAPL", 20);

    try {
      portfolio.calculatePortfolioValue("2020-03-10", api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("No data available for the symbol"));
    }
  }

  @Test
  public void testGetName() {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    assertEquals("Test Portfolio", portfolio.getName());
  }

  @Test
  public void testCreatePortfolio() {
    Portfolio portfolio = Portfolio.createPortfolio("Test Portfolio");

    assertEquals("Test Portfolio", portfolio.getName());
    assertTrue(portfolio.getStocks().isEmpty());

    Map<String, Integer> stocks = new HashMap<>();
    stocks.put("AAPL", 10);
    stocks.put("GOOG", 5);
    Portfolio portfolioWithStocks = Portfolio.createPortfolio("Stocked Portfolio", stocks);

    assertEquals("Stocked Portfolio", portfolioWithStocks.getName());
    assertEquals(10, (int) portfolioWithStocks.getStocks().get("AAPL"));
    assertEquals(5, (int) portfolioWithStocks.getStocks().get("GOOG"));
  }

  @Test
  public void testGetStocks() {
    Portfolio portfolio = new Portfolio("Test Portfolio");

    portfolio.addStock("AAPL", 10);
    portfolio.addStock("GOOG", 5);

    Map<String, Integer> stocks = portfolio.getStocks();
    assertEquals(10, (int) stocks.get("AAPL"));
    assertEquals(5, (int) stocks.get("GOOG"));
  }
}
