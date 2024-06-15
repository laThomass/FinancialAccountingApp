package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Map of Dates with an attached portfolio.
 * This class is meant to display the data of a portfolio by
 * containing the data the portfolio should have at that date as a portfolio.
 */
public class PortfolioDateList implements IPortfolioDateList {
  Map<Date, Portfolio> portfolioDateMap = new HashMap<>();

  /**
   * Adds a portfolio to the class which is attached to a given date.
   *
   * @param portfolio Portfolio to add.
   * @param date      of the given portfolio.
   */
  public void addPortfolio(Portfolio portfolio, Date date) {
    portfolioDateMap.put(date, portfolio);
    for (Date d : portfolioDateMap.keySet()) {
      if (d.after(date)) {
        portfolioDateMap.replace(d, portfolio);
      }
    }
  }


}