package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents a Portfolio.
 * A portfolio represents a users collection of stocks and their associated quantities.
 */

public class Portfolio implements IPortfolio {
  private final String name;
  private final PortfolioDateList dateList;
  private Map<String, Integer> stocks;

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

  public Portfolio(String name, Map<String, Integer> stocks) {
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
   * Create a new portfolio with no stocks.
   *
   * @param name for portfolio.
   * @return new portfolio.
   */
  public static Portfolio createPortfolio(String name, Map<String, Integer> stocks) {
    return new Portfolio(name, stocks);
  }

  /**
   * Save a Portfolio to a file with all information able to be retrieved.
   */
  // Serialization
  // Save object into a file.
  public static void savePortfolio(Portfolio p, File file) throws Exception {
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(p.getName());
      writer.write(",");
      writer.write(p.getStocks().toString());
      writer.close();
    } catch (Exception e) {
      throw new Exception("Error saving portfolio: " + e.getMessage());
    }
  }

  public static Portfolio loadPortfolio(File file) throws Exception {
    Portfolio result = null;
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String name;
      Map<String, Integer> stocks = new HashMap<>();
      String line = reader.readLine();
      String[] parts = line.split(",", 2);
      name = parts[0];
      String[] parts2 = parts[1].replace("{", "")
              .replace("}", "").split(", ");

      for(String part : parts2) {
        String[] parts3 = part.split("=");
        stocks.put(parts3[0], Integer.parseInt(parts3[1]));
      }
      result = new Portfolio(name, stocks);

    } catch (Exception e) {
      throw new Exception("Exception thrown");
    }
    return result;
  }

  /**
   * Adds a stock to an existing portfolio with the given quantity.
   *
   * @param stockSymbol Stock ticker.
   * @param quantity    Quantity of shares.
   */

  @Override
  public void addStock(String stockSymbol, int quantity, String date) throws IllegalArgumentException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }
    stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + quantity);
    this.dateList.addPortfolio(this, new Date(date));
  }

  public void removeStock(String stockSymbol, int quantity, String date) throws IllegalArgumentException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }
    stocks.remove(stockSymbol, stocks.getOrDefault(stockSymbol, 0) - quantity);
    this.dateList.addPortfolio(this, new Date(date));
  }

  public void addStock(String stockSymbol, int quantity) throws IOException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }
    stocks.put(stockSymbol, stocks.getOrDefault(stockSymbol, 0) + quantity);
  }

  /**
   * Calculates the value of the portfolio on a certain date based on the value
   * of each stock ticker on that date.
   *
   * @param date    date to get value.
   * @param api     to get data with.
   * @param library to get data from.
   * @return double.
   */

  @Override
  public double calculatePortfolioValue(String date,
                                        IAlphaAPIInterface api, Map<String,
          List<Stock>> library) {
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
          throw new RuntimeException("No data available for the symbol: "
                  + symbol + " on date: " + date);
        }

        totalValue += closingPrice * quantity;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error calculating portfolio value: " + e.getMessage());
    }

    return totalValue;
  }

  public String performanceOverTime(String startDate, String endDate,
                                    IAlphaAPIInterface api, Map<String, List<Stock>> library) {

    return "";
  }

  public List<String> viewComposition(String date) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    StringBuilder stockList = new StringBuilder();
    return null;
  }

  /**
   * Rebalances a portfolio by giving each stock its own intended weight,
   * which is chosen by the user. It changes the number of shares so that the value of each
   * stock in the portfolio is the same. If the number of shares would be a decimal,
   * the number of shares is rounded down and the leftover money is theoretically refunded.
   *
   * @param date    date to rebalance portfolio around
   * @param api     api to get data with
   * @param library library to get data from
   * @throws IOException if the date is invalid
   */
  public void rebalancePortfolio(String date, IAlphaAPIInterface api, Map<String,
          List<Stock>> library, Map<String, Double> weights) throws IOException {
    double totalValue = calculatePortfolioValue(date, api, library);

    Map<String, Integer> newStocks = stocks;
    for (String symbol : stocks.keySet()) {
      List<Stock> data = api.fetchData(symbol, library);
      for (Stock stock : data) {
        if (stock.getDate().equals(date)) {
          double intendedValue = totalValue * weights.get(symbol) / 100;
          int numShares = (int) (intendedValue / stock.getClosingPrice());
          if (numShares == 0) {
            throw new RuntimeException("We could not balance the portfolio around this date.");
          }
          newStocks.put(symbol, numShares);
        }
      }
    }

    stocks = newStocks;

  }

  /**
   * Return name of the portfolio.
   *
   * @return String name of portfolio.
   */
  public String getName() {
    return this.name;
  }

  // Deserialization
  // Get object from a file.

  public Map<String, Integer> getStocks() {
    return new HashMap<>(this.stocks);
  }

}
