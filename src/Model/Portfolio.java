package Model;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * Represents a Portfolio.
 * A portfolio represents a users collection of stocks and their associated quantities.
 */

public class Portfolio implements IPortfolio {
  private final String name;
  private final Map<String, Integer> stocks;

  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
  }

  /**
   * Constructor for portfolio with a list of stocks.
   * @param name of portfolio.
   * @param stocks Map of stock symbol and its associated quantity.
   */

  public Portfolio(String name, Map<String, Integer> stocks) {
    this.name = name;
    this.stocks = new HashMap<>(stocks);
  }

  /**
   * Adds a stock to an existing portfolio with the given quantity.
   * @param stockSymbol Stock ticker.
   * @param quantity Quantity of shares.
   */

@Override
  public void addStock(String stockSymbol, int quantity) {
    stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + quantity);
  }

  /**
   * Calculates the value of the portfolio on a certain date based on the value
   * of each stock ticker on that date.
   * @param date date to get value.
   * @param api to get data with.
   * @param library to get data from.
   * @return double.
   */

@Override
  public double calculatePortfolioValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    double totalValue = 0.0;
    try {
      for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
        String symbol = entry.getKey();
        int quantity = entry.getValue();
        List<Stock> stockData = library.get(symbol);
        if (stockData == null) {
          throw new RuntimeException("No data available for the symbol: " + symbol);
        }

        double closingPrice = 0.0;
        for (Stock stock : stockData) {
          if (stock.getDate().equals(date)) {
            closingPrice = stock.getClosingPrice();
            break;
          }
        }

        if (closingPrice == 0.0) {
          throw new RuntimeException("No data available for the symbol: " + symbol + " on date: " + date);
        }

        totalValue += closingPrice * quantity;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error calculating portfolio value: " + e.getMessage());
    }

    return totalValue;
  }

  /**
   * Return name of the portfolio.
   * @return String name of portfolio.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return stocks in portfolio.
   * @return Map<String, Int> stocks in portfolio.
   */
  public static Portfolio createPortfolio(String name) {
    return new Portfolio(name);
  }

  /**
   * Create a new portfolio with no stocks.
   * @param name for portfolio.
   * @return new portfolio.
   */
  public static Portfolio createPortfolio(String name, Map<String, Integer> stocks) {
    return new Portfolio(name, stocks);
  }


  public Map<String, Integer> getStocks() {
    return this.stocks;
  }

}
