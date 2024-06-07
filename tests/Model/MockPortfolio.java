package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A mock implementation of the PortfolioModel interface for testing purposes.
 */
public class MockPortfolio implements IPortfolio {
  private final String name;
  private final Map<String, Integer> stocks = new HashMap<>();

  public MockPortfolio(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addStock(String symbol, int quantity) {
    stocks.put(symbol, quantity);
  }

  @Override
  public double calculatePortfolioValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    // Return a mock value
    return 1000.0;
  }

  @Override
  public Map<String, Integer> getStocks() {
    return stocks;
  }

  public static MockPortfolio createPortfolio(String name, Map<String, Integer> stocks) {
    MockPortfolio portfolio = new MockPortfolio(name);
    portfolio.stocks.putAll(stocks);
    return portfolio;
  }
}
