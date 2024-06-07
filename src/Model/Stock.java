package Model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a Stock.
 * A Stock is data associated with a particular date with a particular ticker.
 * A Stock does not contain the symbol of the stock,
 * only the data and the data associated with that date, whatever the ticker may be.
 */

public class Stock implements IStock {
  private final String date;
  private final double openingPrice;
  private final double closingPrice;
  private final double highPrice;
  private final double lowPrice;
  private final int volume;

  /**
   * Constructor for stock.
   *
   * @param date         String date.
   * @param openingPrice double opening price for the day.
   * @param highPrice    double high price for the day.
   * @param lowPrice     double low price for the day.
   * @param closingPrice double closing price for the day.
   * @param volume       int volume number of shares traded for the day.
   */

  public Stock(String date, double openingPrice,
               double highPrice, double lowPrice,
               double closingPrice, int volume) {
    this.date = date;
    this.openingPrice = openingPrice;
    this.closingPrice = closingPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.volume = volume;
  }

  /**
   * Returns the date.
   *
   * @return String date.
   */

  public String getDate() {
    return date;
  }

  /**
   * Returns the opening price.
   *
   * @return double openingPrice.
   */

  public double getOpeningPrice() {
    return openingPrice;
  }

  /**
   * Returns the closing price.
   *
   * @return double closingPrice.
   */

  public double getClosingPrice() {
    return closingPrice;
  }

  /**
   * Returns the high price.
   *
   * @return double highPrice.
   */


  public double getHighPrice() {
    return highPrice;
  }

  /**
   * Returns the low price.
   *
   * @return double lowPrice.
   */
  public double getLowPrice() {
    return lowPrice;
  }

  /**
   * Returns the volume.
   *
   * @return int volume
   */
  public int getVolume() {
    return volume;
  }

  /**
   * Calculates the change between the closing prices of two different days.
   *
   * @param symbol       stock ticker.
   * @param startDateStr String startDate.
   * @param endDateStr   String endDate.
   * @param api          AlphaAPI to use to get data.
   * @param library      library to get data from.
   * @return double change in prices.
   * @throws IOException if no data available.
   */

  public static double viewGainLoss(String symbol, String startDateStr, String endDateStr, IAlphaAPIInterface api, Map<String, List<Stock>> library) throws IOException {
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

      for (Stock stock : stocks) {
        Date stockDate = sdf.parse(stock.getDate());
        if (stockDate.equals(startDate)) {
          startPrice = stock.getClosingPrice();
          startPriceFound = true;
        }
        if (stockDate.equals(endDate)) {
          endPrice = stock.getClosingPrice();
          endPriceFound = true;
        }
      }

      if (!startPriceFound) {
        throw new RuntimeException("Could not find price for the start date.");
      }
      if (!endPriceFound) {
        throw new RuntimeException("Could not find price for the end date.");
      }

    } catch (ParseException e) {
      throw new RuntimeException("Date parsing error: " + e.getMessage());
    }

    return ((endPrice - startPrice) / startPrice) * 100;
  }

  /**
   * Return moving average of a stock for a particular date and a particular number of days.
   *
   * @param symbol  String ticker.
   * @param dateStr String start date.
   * @param days    int days.
   * @param api     api to get data from.
   * @param library library to get data from.
   * @return double moving average.
   * @throws Exception if no data available or days < 0
   */

  public static double viewXDayMovingAverage(String symbol, String dateStr, int days, IAlphaAPIInterface api, Map<String, List<Stock>> library) throws IOException {
    double change = 0;
    int index = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Stock> stocks = api.fetchData(symbol, library);

    try {
      Date date = sdf.parse(dateStr);

      for (Stock stock : stocks) {
        Date stockDate = sdf.parse(stock.getDate());
        if (date.equals(stockDate)) {
          index = stocks.indexOf(stock);
          break;
        }
      }

      for (int i = index; i < days + index; i++) {
        change += stocks.get(i).getClosingPrice();
      }
    } catch (ParseException e) {
      throw new RuntimeException("Date parsing error: " + e.getMessage());
    } catch (IndexOutOfBoundsException e) {
      throw new RuntimeException("Number of days goes too far back, or we could not find an average for the date provided.");
    }

    return change / days;
  }

  /**
   * Returns the list of days for which the stock crosses over.
   *
   * @param symbol  String ticker.
   * @param dateStr String start date.
   * @param days    int days.
   * @param api     api to get data from.
   * @param library to get data from.
   * @return List <String>.
   * @throws Exception if no data or if days < 0.
   */

  public static List<String> viewXDayCrossOver(String symbol, String dateStr, int days, IAlphaAPIInterface api, Map<String, List<Stock>> library) throws IOException {
    List<String> xcDays = new ArrayList<>();
    List<Stock> stocks = api.fetchData(symbol, library);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try {
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
    } catch (ParseException e) {
      throw new RuntimeException("Date parsing error: " + e.getMessage());
    }

    return xcDays;
  }

  /**
   * Helper to help calculate the moving average.
   *
   * @param stocks list of stocks.
   * @param index  int index.
   * @param days   int days.
   * @return double average.
   */
  private static double calculateMovingAverage(List<Stock> stocks, int index, int days) {
    double sum = 0;
    for (int i = index - days + 1; i <= index; i++) {
      sum += stocks.get(i).getClosingPrice();
    }
    return sum / days;
  }

  /**
   * Returns the stocks data as a String.
   *
   * @return String.
   */
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
