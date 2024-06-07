package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A mock implementation of the AlphaAPIInterface for testing purposes.
 */
public class MockAlphaAPI implements IAlphaAPIInterface {
  @Override
  public List<Stock> fetchData(String symbol, Map<String, List<Stock>> library) throws IOException {
    // Create mock stock data
    List<Stock> stocks = new ArrayList<>();
    stocks.add(new Stock("2023-01-01", 100.0, 110.0, 90.0, 105.0, 10000));
    stocks.add(new Stock("2023-01-02", 106.0, 115.0, 95.0, 110.0, 15000));
    stocks.add(new Stock("2023-01-03", 111.0, 120.0, 100.0, 115.0, 20000));

    // If library is provided and does not contain the symbol, add the mock data to the library
    if (library != null && !library.containsKey(symbol)) {
      library.put(symbol, stocks);
    }

    return stocks;
  }
}
