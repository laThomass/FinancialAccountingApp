package model;

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

  /**
   * Constructor for the mock API.
   */
  public MockAlphaAPI() {
    // Preload mock data
    List<Stock> stocks = new ArrayList<>();
    stocks.add(new Stock("2020-01-01", 100.0, 110.0, 90.0, 105.0, 10000));
    stocks.add(new Stock("2020-02-01", 106.0, 115.0, 95.0, 110.0, 15000));
    stocks.add(new Stock("2020-03-01", 111.0, 120.0, 100.0, 115.0, 20000));
    stocks.add(new Stock("2020-04-01", 115.0, 125.0, 105.0, 120.0, 18000));
    stocks.add(new Stock("2020-05-01", 120.0, 130.0, 110.0, 125.0, 16000));
    stocks.add(new Stock("2020-06-01", 125.0, 135.0, 115.0, 130.0, 14000));
    stocks.add(new Stock("2020-07-01", 130.0, 140.0, 120.0, 135.0, 13000));
    stocks.add(new Stock("2020-08-01", 135.0, 145.0, 125.0, 140.0, 12000));
    stocks.add(new Stock("2020-09-01", 140.0, 150.0, 130.0, 145.0, 11000));
    stocks.add(new Stock("2020-10-01", 145.0, 155.0, 135.0, 150.0, 10000));
    stocks.add(new Stock("2020-11-01", 150.0, 160.0, 140.0, 155.0, 9000));
    stocks.add(new Stock("2020-12-01", 155.0, 165.0, 145.0, 160.0, 8000));
    stocks.add(new Stock("2021-01-01", 160.0, 170.0, 150.0, 165.0, 7000));
    stocks.add(new Stock("2021-02-01", 165.0, 175.0, 155.0, 170.0, 6000));
    stocks.add(new Stock("2021-03-01", 170.0, 180.0, 160.0, 175.0, 5000));
    stocks.add(new Stock("2021-04-01", 175.0, 185.0, 165.0, 180.0, 4000));
    stocks.add(new Stock("2021-05-01", 180.0, 190.0, 170.0, 185.0, 3000));
    stocks.add(new Stock("2021-06-01", 185.0, 195.0, 175.0, 190.0, 2000));
    stocks.add(new Stock("2021-07-01", 190.0, 200.0, 180.0, 195.0, 1000));
    stocks.add(new Stock("2021-08-01", 195.0, 205.0, 185.0, 200.0, 500));
    stocks.add(new Stock("2021-09-01", 200.0, 210.0, 190.0, 205.0, 250));
    stocks.add(new Stock("2021-10-01", 205.0, 215.0, 195.0, 210.0, 100));
    stocks.add(new Stock("2021-11-01", 210.0, 220.0, 200.0, 100.0, 50));
    stocks.add(new Stock("2021-12-01", 215.0, 225.0, 205.0, 120.0, 25));
    stocks.add(new Stock("2022-01-01", 220.0, 230.0, 210.0, 130, 12));
    stocks.add(new Stock("2022-02-01", 225.0, 235.0, 215.0, 140.0, 6));
    stocks.add(new Stock("2022-03-01", 230.0, 240.0, 220.0, 155.0, 3));
    stocks.add(new Stock("2022-04-01", 235.0, 245.0, 225.0, 1600.0, 2));
    stocks.add(new Stock("2022-05-01", 240.0, 250.0, 230.0, 175.0, 1));
    stocks.add(new Stock("2022-06-01", 245.0, 255.0, 235.0, 1800.0, 0.5));
    stocks.add(new Stock("2022-07-01", 250.0, 260.0, 240.0, 255.0, 0.25));
    stocks.add(new Stock("2022-08-01", 255.0, 265.0, 245.0, 260.0, 0.12));
    stocks.add(new Stock("2022-09-01", 260.0, 270.0, 250.0, 265.0, 0.06));
    stocks.add(new Stock("2022-10-01", 265.0, 275.0, 255.0, 270.0, 0.03));
    stocks.add(new Stock("2022-11-01", 270.0, 280.0, 260.0, 275.0, 0.015));
    stocks.add(new Stock("2022-12-01", 275.0, 285.0, 265.0, 280.0, 0.0075));
    stocks.add(new Stock("2023-01-01", 280.0, 290.0, 270.0, 285.0, 0.00375));
    stocks.add(new Stock("2023-01-02", 285.0, 295.0, 275.0, 290.0, 0.001875));
    stocks.add(new Stock("2023-01-03", 290.0, 300.0, 280.0, 295.0, 0.0009375));
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
