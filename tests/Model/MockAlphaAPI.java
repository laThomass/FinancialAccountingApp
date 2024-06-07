package Model;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock implementation of the AlphaAPIInterface for testing purposes.
 */
public class MockAlphaAPI implements IAlphaAPIInterface {
  private final Map<String, List<Stock>> mockLibrary = new HashMap<>();

  public MockAlphaAPI() {
    // Preload mock data
    List<Stock> stocks = new ArrayList<>();
    stocks.add(new Stock("2020-01-01", 100.0, 110.0, 90.0, 105.0, 10000));
    stocks.add(new Stock("2023-01-02", 106.0, 115.0, 95.0, 110.0, 15000));
    stocks.add(new Stock("2023-01-03", 111.0, 120.0, 100.0, 115.0, 20000));
    mockLibrary.put("AAPL", stocks);
  }
@Override
  public List<Stock> fetchData(String symbol, Map<String, List<Stock>> library) throws IOException {
    // Use the preloaded data
    if (mockLibrary.containsKey(symbol)) {
      if (library != null && !library.containsKey(symbol)) {
        library.put(symbol, mockLibrary.get(symbol));
      }
      return mockLibrary.get(symbol);
    } else {
      throw new IOException("Mock data not available for symbol: " + symbol);
    }
  }
}
