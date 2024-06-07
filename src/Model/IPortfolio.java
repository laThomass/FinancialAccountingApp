package Model;

import java.util.Map;
import java.util.List;

/**
 * Interface representing a Portfolio model.
 * A Portfolio consists of a collection of stocks identified by their symbols and quantities.
 */
public interface IPortfolio {

  /**
   * Adds a specified quantity of a stock to the portfolio.
   * If the stock already exists in the portfolio, the quantity is increased.
   *
   * @param stockSymbol the symbol of the stock to be added
   * @param quantity the quantity of the stock to be added
   */
  public void addStock(String stockSymbol, int quantity);

  /**
   * Calculates the total value of the portfolio on a given date.
   *
   * @param date the date for which the portfolio value is to be calculated, in yyyy-MM-dd format
   * @param api the AlphaAPI instance used to fetch stock data
   * @param library a map containing stock data for various symbols
   * @return the total value of the portfolio on the specified date
   */
  public double calculatePortfolioValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library);

  /**
   * Gets the name of the portfolio.
   *
   * @return the name of the portfolio
   */
  public String getName();

  /**
   * Gets the stocks in the portfolio along with their quantities.
   *
   * @return a map where the keys are stock symbols and the values are quantities
   */
  Map<String, Integer> getStocks();
}
