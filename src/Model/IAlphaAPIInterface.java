package Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Interface representing the AlphaAPI component.
 * It defines methods for fetching stock data.
 */
public interface IAlphaAPIInterface {
  /**
   * Fetches stock data for a given symbol.
   *
   * @param symbol the stock symbol
   * @param library a map containing cached stock data
   * @return a list of Stock objects representing the stock data
   * @throws IOException if there is an issue fetching data
   */
  public List<Stock> fetchData(String symbol, Map<String, List<Stock>> library) throws IOException;
}
