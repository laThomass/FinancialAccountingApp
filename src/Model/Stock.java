package Model;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Stock {
  private final String date;
  private final double openingPrice;
  private final double closingPrice;
  private final double highPrice;
  private final double lowPrice;
  private final int volume;

  public Stock(String date, double openingPrice, double highPrice, double lowPrice, double closingPrice, int volume) {
    this.date = date;
    this.openingPrice = openingPrice;
    this.closingPrice = closingPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.volume = volume;
  }

  public String getDate() {
    return date;
  }

  public double getOpeningPrice() {
    return openingPrice;
  }

  public double getClosingPrice() {
    return closingPrice;
  }

  public double getHighPrice() {
    return highPrice;
  }

  public double getLowPrice() {
    return lowPrice;
  }

  public int getVolume() {
    return volume;
  }

  public static double viewGainLoss(String symbol, String startDateStr, String endDateStr, AlphaAPI api, Map<String, List<Stock>> library) throws IOException {
    List<Stock> stocks = api.fetchData(symbol, library);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    if (stocks.isEmpty()) {
      throw new RuntimeException("No data available for the symbol: " + symbol);
    }

    boolean startPriceFound = false;
    boolean endPriceFound = false;
    double startPrice = 0.0;
    double endPrice = 0.0;

    try {
      Date startDate = sdf.parse(startDateStr);
      Date endDate = sdf.parse(endDateStr);
      System.out.println("Parsed Start Date: " + startDate); // Debug statement
      System.out.println("Parsed End Date: " + endDate); // Debug statement

      for (Stock stock : stocks) {
        Date stockDate = sdf.parse(stock.getDate());
//        System.out.println("Checking Stock Date: " + stock.getDate() + " | Parsed Stock Date: " + stockDate); // Debug statement
        if (stockDate.equals(startDate)) {
          startPrice = stock.getClosingPrice();
          startPriceFound = true;
          System.out.println("Found Start Date: " + stock.getDate() + " with price: " + startPrice); // Debug statement
        }
        if (stockDate.equals(endDate)) {
          endPrice = stock.getClosingPrice();
          endPriceFound = true;
          System.out.println("Found End Date: " + stock.getDate() + " with price: " + endPrice); // Debug statement
        }
      }

      if (!startPriceFound) {
        throw new RuntimeException("Could not find price for the start date.");
      }
      if (!endPriceFound) {
        throw new RuntimeException("Could not find price for the end date.");
      }

    } catch (Exception e) {
      throw new RuntimeException("Date parsing error: " + e.getMessage());
    }

    return ((endPrice - startPrice) / startPrice) * 100;
  }


  public static double viewXDayMovingAverage(String symbol, String dateStr, int days, AlphaAPI api, Map<String, List<Stock>> library) throws Exception {
    double change = 0;
    int index = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Date date = sdf.parse(dateStr);
    List<Stock> stocks = api.fetchData(symbol, library);

    for (Stock stock : stocks) {
      Date stockDate = sdf.parse(stock.getDate());
      if (date.equals(stockDate)) {
        index = stocks.indexOf(stock);
        break;
      }
    }

    try {
      for (int i = index; i < days + index; i++) {
        change += stocks.get(i).getClosingPrice();
      }
    } catch (IndexOutOfBoundsException e) {
      throw new RuntimeException("Number of days goes too far back, or we could not find an average for the date provided.");
    }

    return change / days;
  }

  public static List<String> viewXDayCrossOver(String symbol, String dateStr, int days, AlphaAPI api, Map<String, List<Stock>> library) throws Exception {
    List<String> xcDays = new ArrayList<>();
    List<Stock> stocks = api.fetchData(symbol, library);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = sdf.parse(dateStr);

    if (stocks.size() < days) {
      throw new RuntimeException("Insufficient data points to calculate the " + days + "-day moving average.");
    }

    for (int i = days - 1; i < stocks.size(); i++) {
      Stock currentStock = stocks.get(i);
      Date currentDate = sdf.parse(currentStock.getDate());

      if (currentDate.compareTo(startDate) >= 0) {
        double currentPrice = currentStock.getClosingPrice();
        double movingAverage = calculateMovingAverage(stocks, i, days);

        if (currentPrice > movingAverage) {
          xcDays.add(currentStock.getDate() + " (Bullish Crossover)");
        } else if (currentPrice < movingAverage) {
          xcDays.add(currentStock.getDate() + " (Bearish Crossover)");
        }
      }
    }

    return xcDays;
  }

  private static double calculateMovingAverage(List<Stock> stocks, int index, int days) {
    double sum = 0;
    for (int i = index - days + 1; i <= index; i++) {
      sum += stocks.get(i).getClosingPrice();
    }
    return sum / days;
  }

  @Override
  public String toString() {
    return "Model.Stock{" +
            "date='" + date + '\'' +
            ", openingPrice=" + openingPrice +
            ", closingPrice=" + closingPrice +
            ", highPrice=" + highPrice +
            ", lowPrice=" + lowPrice +
            ", volume=" + volume +
            '}';
  }
}
