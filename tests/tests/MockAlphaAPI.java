package tests;

import Model.AlphaAPI;
import Model.Stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockAlphaAPI extends AlphaAPI {
  private Map<String, List<Stock>> mockStockData;

  public MockAlphaAPI(Map<String, List<Stock>> mockStockData) {
    this.mockStockData = mockStockData;
  }

  @Override
  public List<Stock> fetchData(String symbol, Map<String, List<Stock>> library) throws IOException {
    if (mockStockData.containsKey(symbol)) {
      return mockStockData.get(symbol);
    } else {
      return new ArrayList<>();
    }
  }
}