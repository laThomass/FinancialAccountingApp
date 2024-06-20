package model;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Interface representing a Portfolio model.
 * A Portfolio consists of a collection of stocks identified by their symbols and quantities.
 */
public interface IPortfolio {

  /**
   * Saves a portfolio to a text file that can be read and loaded later.
   *
   * @param p    portfolio.
   * @param file file.
   * @throws IOException if the file cannot be written.
   */
  static void savePortfolio(Portfolio p, File file) throws IOException {
    //Saves portfolio to a file.
  }


  /**
   * Loads a portfolio from a text file that can be edited.
   *
   * @param file file.
   * @return a new portfolio.
   * @throws IOException if the file cannot be read.
   */
  static Portfolio loadPortfolio(File file) throws IOException {
    return null;
  }

  /**
   * Adds a specified quantity of a stock to the portfolio.
   * If the stock already exists in the portfolio, the quantity is increased.
   *
   * @param stockSymbol the symbol of the stock to be added
   * @param quantity    the quantity of the stock to be added
   */
  void addStock(String stockSymbol, double quantity, String date)
          throws IllegalArgumentException, ParseException;

  /**
   * Calculates the total value of the portfolio on a given date.
   *
   * @param date    the date for which the portfolio value is to be calculated, yyyy-MM-dd format.
   * @param api     the AlphaAPI instance used to fetch stock data.
   * @param library a map containing stock data for various symbols.
   * @return the total value of the portfolio on the specified date.
   */
  double calculatePortfolioValue(String date,
                                 IAlphaAPIInterface api, Map<String, List<Stock>> library);

  /**
   * Gets tge Distribution of the value of the portfolio on a given date.
   *
   * @param date   the date for which the portfolio value is to be calculated, yyyy-MM-dd format.
   * @param api   the AlphaAPI instance used to fetch stock data.
   * @param library a map containing stock data for various symbols.
   * @return distribution of the value of the portfolio on the specified date.
   */
  Map<String, Double> getDistributionOfValue(String date,
                                             IAlphaAPIInterface api,
                                             Map<String, List<Stock>> library);

  /**
   * Rebalances the portfolio on a specific day with specific weights for each stock.
   * Changes the number of shares so that after rebalancing, each stock's value in
   * the portfolio matches its intended weight.
   *
   * @param date    at which to rebalance.
   * @param api     to get data with.
   * @param library to get data from.
   * @param weights intended for each stock.
   * @throws IOException if invalid input.
   */
  void rebalancePortfolio(String date, IAlphaAPIInterface api, Map<String,
          List<Stock>> library, Map<String, Double> weights) throws IOException;

  /**
   * Gets the name of the portfolio.
   *
   * @return the name of the portfolio.
   */
  String getName();

  /**
   * Gets the stocks in the portfolio along with their quantities.
   *
   * @return a map where the keys are stock symbols and the values are quantities.
   */
  Map<String, List<Stock>> getStocks();

  /**
   * Get the composition of a portfolio based on its stocks and the number of shares of each stock.
   *
   * @param date at which to check the composition.
   * @return a map of the stocks and the number of their shares.
   */
  public Map<String, Double> getComposition(String date);

  /**
   * Graph the value of the portfolio over time. The number of intervals and length of each
   * interval depend on the number of days.
   *
   * @param startDate start date.
   * @param endDate   end date.
   * @param api       api to get data with.
   * @param library   library to get data from.
   */
  public void printPortfolioPerformanceChart(String startDate,
                                             String endDate, IAlphaAPIInterface api,
                                             Map<String, List<Stock>> library);

  /**
   * Removes a specified quantity of a stock from the portfolio.
   * @param stockSymbol the symbol of the stock to be removed.
   * @param quantity the quantity of the stock to be removed.
   * @param date the date of the transaction.
   * @throws IllegalArgumentException if the stock does not exist in the portfolio or the quantity.
   * @throws ParseException if the date is invalid.
   */

  public void removeStock(String stockSymbol, double quantity, String date)
          throws IllegalArgumentException, ParseException;


}