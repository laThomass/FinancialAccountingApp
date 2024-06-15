package model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Portfolio implements IPortfolio {
  private final String name;
  private final PortfolioDateList dateList;
  private Map<String, List<Stock>> stocks;

  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
    this.dateList = new PortfolioDateList();
  }

  public Portfolio(String name, Map<String, List<Stock>> stocks) {
    this.name = name;
    this.stocks = new HashMap<>(stocks);
    this.dateList = new PortfolioDateList();
  }

  public static Portfolio createPortfolio(String name) {
    return new Portfolio(name);
  }

  public static Portfolio createPortfolio(String name, Map<String, List<Stock>> stocks) {
    return new Portfolio(name, stocks);
  }

  public static void savePortfolio(IPortfolio p, File file) throws IOException {
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
        double quantity = Double.parseDouble(stockInfo[0]);
        String date = stockInfo[1];
        Stock stock = new Stock(date, 0, 0, 0, 0, quantity);
        stocks.computeIfAbsent(symbol, k -> new ArrayList<>()).add(stock);
      }

      return new Portfolio(name, stocks);
    } catch (IOException e) {
      throw new IOException("Error loading portfolio: " + e.getMessage(), e);
    }
  }

  @Override
  public void addStock(String stockSymbol, double quantity, String date) throws IllegalArgumentException, ParseException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }

    stockSymbol = stockSymbol.toUpperCase();

    if (!isValidTicker(stockSymbol)) {
      throw new IllegalArgumentException("Invalid stock ticker. Please enter a valid ticker.");
    }

    Stock stock = new Stock(date, 0, 0, 0, 0, quantity);
    stocks.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(stock);
    this.dateList.addPortfolio(this, new SimpleDateFormat("yyyy-MM-dd").parse(date));
  }

  public void removeStock(String stockSymbol, double quantity, String date) throws IllegalArgumentException, ParseException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Negative or 0 quantity not allowed");
    }
    stockSymbol = stockSymbol.toUpperCase();
    List<Stock> stockList = stocks.get(stockSymbol);
    if (stockList == null) {
      throw new IllegalArgumentException("Stock not found in portfolio");
    }

    double remainingQuantity = quantity;
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
        if (stockData == null || stockData.isEmpty()) {
          // Skip the stock if no data is available
//          System.err.println("No data available for the symbol: " + symbol);
          continue;
        }

        double closingPrice = 0.0;
        boolean priceFound = false;
        for (Stock stock : stockData) {
          if (stock.getDate().compareTo(date) <= 0) {
            closingPrice = stock.getClosingPrice();
            priceFound = true;
          }
        }

        if (!priceFound) {
          // Skip the stock if no data is available for the specific date
//          System.err.println("No data available for the symbol: " + symbol + " on date: " + date);
          continue;
        }

        totalValue += closingPrice * totalQuantity;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error calculating portfolio value: " + e.getMessage());
    }

    return totalValue;
  }

  public Map<String, Double> getComposition(String date) {
    Map<String, Double> composition = new HashMap<>();
    for (Map.Entry<String, List<Stock>> entry : stocks.entrySet()) {
      String symbol = entry.getKey();
      List<Stock> stockList = entry.getValue();
      double totalQuantity = 0;

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

  public Map<String, Double> getDistributionOfValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    Map<String, Double> distribution = new HashMap<>();
    double totalValue = calculatePortfolioValue(date, api, library);

    for (Map.Entry<String, List<Stock>> entry : stocks.entrySet()) {
      String symbol = entry.getKey();
      List<Stock> stockList = entry.getValue();
      double totalQuantity = 0;

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

  public void rebalancePortfolio(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library, Map<String, Double> weights) throws IOException {
    double totalWeight = 0.0;
    for (double weight : weights.values()) {
      totalWeight += weight;
    }
    if (Math.abs(totalWeight - 1.0) > 0.0001) {
      throw new IllegalArgumentException("Weights must add up to 1.0 (100%)");
    }

    double totalValue = calculatePortfolioValue(date, api, library);

    Map<String, List<Stock>> newStocks = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date rebalanceDate;

    try {
      rebalanceDate = sdf.parse(date);
    } catch (ParseException e) {
      throw new RuntimeException("Invalid date format: " + date);
    }

    for (String symbol : stocks.keySet()) {
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

      double intendedValue = totalValue * weights.getOrDefault(symbol, 0.0);
      double newQuantity = intendedValue / closingPrice;

      if (newQuantity < 0) {
        throw new RuntimeException("Rebalancing would result in a negative quantity for symbol: " + symbol);
      }

      List<Stock> stockList = new ArrayList<>();
      stockList.add(new Stock(date, 0, 0, 0, closingPrice, newQuantity));
      newStocks.put(symbol, stockList);
    }

    this.stocks = newStocks;
  }

  public String getName() {
    return this.name;
  }

  public Map<String, List<Stock>> getStocks() {
    return new HashMap<>(this.stocks);
  }

  public Map<String, Double> getPortfolioValuesOverTime(String startDate, String endDate, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    Map<String, Double> valuesOverTime = new LinkedHashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try {
      Date start = sdf.parse(startDate);
      Date end = sdf.parse(endDate);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(start);

      while (!calendar.getTime().after(end)) {
        String dateStr = sdf.format(calendar.getTime());
        double value = calculatePortfolioValue(dateStr, api, library);
        valuesOverTime.put(dateStr, value);
        calendar.add(Calendar.MONTH, 1); // Change this to Calendar.DAY_OF_MONTH or Calendar.YEAR as needed
      }
    } catch (ParseException e) {
      throw new RuntimeException("Invalid date format", e);
    }

    return valuesOverTime;
  }

  public void printPortfolioPerformanceChart(String startDate, String endDate, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    long totalDays = ChronoUnit.DAYS.between(start, end);
    int interval = (int) Math.ceil(totalDays / 30.0);
    if (totalDays < 5) {
      interval = 1; // Minimum interval of one day if the range is less than 5 days
    } else if (totalDays <= 30) {
      interval = 1; // Daily interval if the range is up to 30 days
    } else {
      interval = (int) Math.ceil(totalDays / 30.0); // Otherwise, calculate an interval to fit within 30 lines
    }

    System.out.println("Performance of portfolio " + name + " from " + startDate + " to " +
            endDate + "\n");

    // Calculate maximum portfolio value to determine the scale
    double maxValue = 0.0;
    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(interval)) {
      double value = calculatePortfolioValue(date.toString(), api, library);
      if (value > maxValue) {
        maxValue = value;
      }
    }

    // Determine the scale so that the maximum number of asterisks is no more than 50
    double scale = maxValue / 50.0;
    if (scale == 0.0) {
      scale = 1.0; // Prevent division by zero
    }

    // Print the portfolio performance chart with the determined scale
    int lineCount = 0;
    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(interval)) {
      double value = calculatePortfolioValue(date.toString(), api, library);
      int stars = (int) (value / scale);
      System.out.printf("%s: %s\n", date, "*".repeat(stars));
      lineCount++;
      if (lineCount >= 30) break;
    }

    System.out.println("\nScale: * = $" + scale);
  }

  private boolean isValidTicker(String ticker) {
    if (ticker == null || ticker.isEmpty()) {
      return false;
    }
    int length = ticker.length();
    if ((length < 1 || length > 4) && !ticker.matches("^[A-Z]{4,5}$")) {
      return false;
    }
    for (char c : ticker.toCharArray()) {
      if (!Character.isLetter(c) && c != '.' && c != '-') {
        return false;
      }
    }
    return true;
  }

}
