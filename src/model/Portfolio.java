package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Portfolio.
 * A portfolio represents a user's collection of stocks and their associated quantities.
 */
public class Portfolio implements IPortfolio {
  private final String name;
  private final PortfolioDateList dateList;
  private Map<String, List<Stock>> stocks;

  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
    this.dateList = new PortfolioDateList();
  }

  /**
   * Constructor for portfolio with a list of stocks.
   *
   * @param name   of portfolio.
   * @param stocks Map of stock symbol and its associated quantity.
   */
  public Portfolio(String name, Map<String, List<Stock>> stocks) {
    this.name = name;
    this.stocks = new HashMap<>(stocks);
    this.dateList = new PortfolioDateList();
  }

  /**
   * Return stocks in portfolio.
   *
   * @return new Portfolio
   */
  public static Portfolio createPortfolio(String name) {
    return new Portfolio(name);
  }

  /**
   * Create a new portfolio with stocks.
   *
   * @param name   for portfolio.
   * @param stocks Map of stock symbol and its associated list of Stock objects.
   * @return new portfolio.
   */
  public static Portfolio createPortfolio(String name, Map<String, List<Stock>> stocks) {
    return new Portfolio(name, stocks);
  }

  /**
   * Save a Portfolio to a file with all information able to be retrieved.
   */
  public static void savePortfolio(Portfolio p, File file) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(p.getName());
      writer.write("\n");
      for (Map.Entry<String, List<Stock>> entry : p.getStocks().entrySet()) {
        for (Stock stock : entry.getValue()) {
          writer.write(entry.getKey() + "=" + stock.getVolume() + "," + stock.getDate());
          writer.write("\n");
        }
      }
    } catch (IOException e) {
      throw new IOException("Error saving portfolio: " + e.getMessage(), e);
    }
  }

  public static Portfolio loadPortfolio(File file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String name = reader.readLine();
      if (name == null || name.isEmpty()) {
        throw new IOException("Invalid portfolio file: missing name");
      }

      Map<String, List<Stock>> stocks = new HashMap<>();
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("=");
        String[] stockInfo = parts[1].split(",");
        String symbol = parts[0];
        int quantity = Integer.parseInt(stockInfo[0]);
        String date = stockInfo[1];
        Stock stock = new Stock(date, 0, 0, 0, 0, quantity);
        stocks.computeIfAbsent(symbol, k -> new ArrayList<>()).add(stock);
      }

      return new Portfolio(name, stocks);
    } catch (IOException e) {
      throw new IOException("Error loading portfolio: " + e.getMessage(), e);
    }
  }

  /**
   * Adds a stock to an existing portfolio with the given quantity and date.
   *
   * @param stockSymbol Stock ticker.
   * @param quantity    Quantity of shares.
   * @param date        Date of the transaction.
   */
  @Override
  public void addStock(String stockSymbol, int quantity, String date) throws IllegalArgumentException, ParseException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }
    stockSymbol = stockSymbol.toUpperCase();
    Stock stock = new Stock(date, 0, 0, 0, 0, quantity);
    stocks.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(stock);
    this.dateList.addPortfolio(this, new SimpleDateFormat("yyyy-MM-dd").parse(date));
  }

  /**
   * Removes a stock from an existing portfolio with the given quantity and date.
   *
   * @param stockSymbol Stock ticker.
   * @param quantity    Quantity of shares.
   * @param date        Date of the transaction.
   */
  public void removeStock(String stockSymbol, int quantity, String date) throws IllegalArgumentException, ParseException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }
    stockSymbol = stockSymbol.toUpperCase();
    List<Stock> stockList = stocks.get(stockSymbol);
    if (stockList == null) {
      throw new IllegalArgumentException("Stock not found in portfolio");
    }

    int remainingQuantity = quantity;
    for (int i = 0; i < stockList.size(); i++) {
      Stock stock = stockList.get(i);
      if (stock.getDate().compareTo(date) <= 0) {
        if (stock.getVolume() > remainingQuantity) {
          stockList.set(i, new Stock(stock.getDate(), stock.getOpeningPrice(), stock.getHighPrice(), stock.getLowPrice(), stock.getClosingPrice(), stock.getVolume() - remainingQuantity));
          remainingQuantity = 0;
          break;
        } else {
          remainingQuantity -= stock.getVolume();
          stockList.remove(i);
          i--;
        }
      }
    }
    if (remainingQuantity > 0) {
      throw new IllegalArgumentException("Not enough shares to sell");
    }

    this.dateList.addPortfolio(this, new SimpleDateFormat("yyyy-MM-dd").parse(date));
  }

  /**
   * Calculates the value of the portfolio on a certain date based on the value
   * of each stock ticker on that date.
   *
   * @param date    Date to get value.
   * @param api     API to get data with.
   * @param library Library to get data from.
   * @return double Value of the portfolio.
   */
  @Override
  public double calculatePortfolioValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    double totalValue = 0.0;
    try {
      for (Map.Entry<String, List<Stock>> entry : stocks.entrySet()) {
        String symbol = entry.getKey();
        List<Stock> stockList = entry.getValue();
        int totalQuantity = 0;

        for (Stock stock : stockList) {
          if (stock.getDate().compareTo(date) <= 0) {
            totalQuantity += stock.getVolume();
          }
        }

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

        totalValue += closingPrice * totalQuantity;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error calculating portfolio value: " + e.getMessage());
    }

    return totalValue;
  }

  /**
   * Determines the composition of the portfolio at a specific date.
   *
   * @param date Date to determine the composition.
   * @return Map of stock symbols and their quantities.
   */
  public Map<String, Integer> getComposition(String date) {
    Map<String, Integer> composition = new HashMap<>();
    for (Map.Entry<String, List<Stock>> entry : stocks.entrySet()) {
      String symbol = entry.getKey();
      List<Stock> stockList = entry.getValue();
      int totalQuantity = 0;

      for (Stock stock : stockList) {
        if (stock.getDate().compareTo(date) <= 0) {
          totalQuantity += stock.getVolume();
        }
      }

      if (totalQuantity > 0) {
        composition.put(symbol, totalQuantity);
      }
    }
    return composition;
  }

  /**
   * Calculates the distribution of value of the portfolio on a specific date.
   *
   * @param date    Date to get the distribution.
   * @param api     API to get data with.
   * @param library Library to get data from.
   * @return Map of stock symbols and their values.
   */
  public Map<String, Double> getDistributionOfValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    Map<String, Double> distribution = new HashMap<>();
    double totalValue = calculatePortfolioValue(date, api, library);

    for (Map.Entry<String, List<Stock>> entry : stocks.entrySet()) {
      String symbol = entry.getKey();
      List<Stock> stockList = entry.getValue();
      int totalQuantity = 0;

      for (Stock stock : stockList) {
        if (stock.getDate().compareTo(date) <= 0) {
          totalQuantity += stock.getVolume();
        }
      }

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

      double value = closingPrice * totalQuantity;
      distribution.put(symbol, value);
    }

    return distribution;
  }

  @Override
  public void rebalancePortfolio(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library, Map<String, Double> weights) throws IOException {

  }

  /**
   * Return name of the portfolio.
   *
   * @return String name of portfolio.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return stocks in portfolio.
   *
   * @return Map of stock symbols and their quantities.
   */
  public Map<String, List<Stock>> getStocks() {
    return new HashMap<>(this.stocks);
  }

  // Other methods like performanceOverTime, viewComposition, rebalancePortfolio...
}
