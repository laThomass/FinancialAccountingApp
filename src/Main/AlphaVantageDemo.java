package Main;

import java.io.IOException;
import java.io.InputStreamReader;

import controller.StockController;
import Model.AlphaAPI;
import View.StockView;

public class AlphaVantageDemo {
  public static void main(String[] args) {
    StockView view = new StockView();
    AlphaAPI api = new AlphaAPI();
    StockController controller = new StockController(view, new InputStreamReader(System.in), System.out, api);
    try {
      controller.addOriginalStocksToLibrary("GOOG");
      controller.addOriginalStocksToLibrary("AAPL");
      controller.addOriginalStocksToLibrary("NVDA");
      controller.start();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}