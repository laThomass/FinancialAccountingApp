package Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Interface representing a Stock model.
 * A Stock consists of various attributes like date, opening price, closing price, high price, low price, and volume.
 */
public interface IStock {

  /**
   * Gets the date of the stock.
   *
   * @return the date of the stock in yyyy-MM-dd format
   */
  String getDate();

  /**
   * Gets the opening price of the stock.
   *
   * @return the opening price of the stock
   */
  double getOpeningPrice();

  /**
   * Gets the closing price of the stock.
   *
   * @return the closing price of the stock
   */
  double getClosingPrice();

  /**
   * Gets the high price of the stock.
   *
   * @return the high price of the stock
   */
  double getHighPrice();

  /**
   * Gets the low price of the stock.
   *
   * @return the low price of the stock
   */
  double getLowPrice();

  /**
   * Gets the volume of the stock.
   *
   * @return the volume of the stock
   */
  int getVolume();

  /**
   * Calculates the gain or loss percentage of a stock over a specified period.
   *
   * @param symbol the stock symbol
   * @param startDateStr the start date in yyyy-MM-dd format
   * @param endDateStr the end date in yyyy-MM-dd format
   * @param api the AlphaAPI instance used to fetch stock data
   * @param library a map containing stock data for various symbols
   * @return the gain or loss percentage over the specified period
   * @throws IOException if there is an issue fetching data
   */
  static double viewGainLoss(String symbol, String startDateStr, String endDateStr, IAlphaAPIInterface api, Map<String, List<Stock>> library) throws IOException {
    return Stock.viewGainLoss(symbol, startDateStr, endDateStr, api, library);
  }

  /**
   * Calculates the X-day moving average of a stock's closing price.
   *
   * @param symbol the stock symbol
   * @param dateStr the date in dd-MM-yyyy format
   * @param days the number of days for the moving average
   * @param api the AlphaAPI instance used to fetch stock data
   * @param library a map containing stock data for various symbols
   * @return the X-day moving average
   * @throws Exception if there is an issue fetching data or calculating the average
   */
  static double viewXDayMovingAverage(String symbol, String dateStr, int days, IAlphaAPIInterface api, Map<String, List<Stock>> library) throws Exception {
    return Stock.viewXDayMovingAverage(symbol, dateStr, days, api, library);
  }

  /**
   * Finds the days when a stock's closing price crosses its X-day moving average.
   *
   * @param symbol the stock symbol
   * @param dateStr the start date in yyyy-MM-dd format
   * @param days the number of days for the moving average
   * @param api the AlphaAPI instance used to fetch stock data
   * @param library a map containing stock data for various symbols
   * @return a list of dates when crossovers occur
   * @throws Exception if there is an issue fetching data or calculating the crossovers
   */
  static List<String> viewXDayCrossOver(String symbol, String dateStr, int days, IAlphaAPIInterface api, Map<String, List<Stock>> library) throws Exception {
    return Stock.viewXDayCrossOver(symbol, dateStr, days, api, library);
  }
}
