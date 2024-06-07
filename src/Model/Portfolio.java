package Model;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class Portfolio {
  private final String name;
  private final Map<String, Integer> stocks;

  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
  }

  public Portfolio(String name, Map<String, Integer> stocks) {
    this.name = name;
    this.stocks = new HashMap<>(stocks);
  }

  public void addStock(String stockSymbol, int quantity) {
    stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + quantity);
  }

  public double calculatePortfolioValue(String date, AlphaAPI api, Map<String, List<Stock>> library) {
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

  public String getName() {
    return this.name;
  }

  public static Portfolio createPortfolio(String name) {
    return new Portfolio(name);
  }

  public static Portfolio createPortfolio(String name, Map<String, Integer> stocks) {
    return new Portfolio(name, stocks);
  }
}
