package model;

import java.util.Date;

/**
 * Represents an Interface for a PortfolioDateList that is meant to display
 * the data of a portfolio by
 * containing the data the portfolio should have at that date as a portfolio.
 */
public interface IPortfolioDateList {

  /**
   * Adds a portfolio to the class which is attached to a given date.
   *
   * @param portfolio Portfolio to add.
   * @param date      of the given portfolio.
   */
  void addPortfolio(Portfolio portfolio, Date date);

}