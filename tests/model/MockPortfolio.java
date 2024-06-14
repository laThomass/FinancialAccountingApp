//package model;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.List;
//
///**
// * A mock implementation of the PortfolioModel interface for testing purposes.
// */
//public class MockPortfolio implements IPortfolio {
//  private final String name;
//  private final Map<String, Integer> stocks = new HashMap<>();
//
//  /**
//   * Constructor for the mock portfolio.
//   *
//   * @param name of the portfolio.
//   */
//
//  public MockPortfolio(String name) {
//    this.name = name;
//  }
//
//  @Override
//  public String getName() {
//    return name;
//  }
//
//  public void addStock(String symbol, int quantity) {
//    stocks.put(symbol, quantity);
//  }
//
//  @Override
//  public void addStock(String stockSymbol, int quantity, String date) throws IOException {
//
//  }
//
//  @Override
//  public double calculatePortfolioValue(String date,
//                                        IAlphaAPIInterface api,
//                                        Map<String,
//                                                List<Stock>> library) {
//    // Return a mock value
//    return 1000.0;
//  }
//
//  @Override
//  public Map<String, Double> getDistributionOfValue(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
//    return null;
//  }
//
//  public String performanceOverTime(String startDate, String endDate, IAlphaAPIInterface api, Map<String, List<Stock>> library) {
//    return "";
//  }
//
//  @Override
//  public void rebalancePortfolio(String date, IAlphaAPIInterface api, Map<String, List<Stock>> library, Map<String, Double> weights) throws IOException {
//
//  }
//
//  @Override
//  public Map<String, Integer> getStocks() {
//    return stocks;
//  }
//
//}
