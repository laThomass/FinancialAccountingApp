package main;

import java.io.IOException;
import java.io.InputStreamReader;

import controller.StockController;
import model.AlphaAPI;
import view.StockView;

/**
 * The AlphaVantageDemo class is the initializer of the Stock Management Program.
 * It initializes the necessary components, adds three stock libraries on startup,
 * and starts the controller.
 */
public class AlphaVantageDemo {

  /**
   * The main method is the starting point of the program.
   * It creates instances of the StockView, AlphaAPI, and StockController classes,
   * adds three stock libraries (GOOG, AAPL, NVDA) to the controller, and starts the controller.
   *
   * @param args the command line arguments (not used in this program)
   */

  public static void main(String[] args) {
    StockView view = new StockView();
    AlphaAPI api = new AlphaAPI();
    StockController controller = new StockController(view,
            new InputStreamReader(System.in), System.out, api);
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