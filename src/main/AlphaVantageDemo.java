package main;

import java.io.IOException;
import java.io.InputStreamReader;

import controller.StockController;
import model.AlphaAPI;
import view.StockView;
import view.IGUIView;
import view.StockViewGUI;

import javax.swing.*;

public class AlphaVantageDemo {

  public static void main(String[] args) {
    String interfaceType = "GUI"; // Change this to "Text" to use the text-based interface

    if (interfaceType.equalsIgnoreCase("Text")) {
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
    } else if (interfaceType.equalsIgnoreCase("GUI")) {
      SwingUtilities.invokeLater(() -> {
        AlphaAPI api = new AlphaAPI();
        IGUIView guiView = new StockViewGUI();
        StockController controller = new StockController(guiView,
                new InputStreamReader(System.in), System.out, api);
        guiView.setController(controller);

        // Create and show the GUI
        JFrame frame = new JFrame("Stock Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(guiView.getMainPanel()); // Assuming getMainPanel() returns the main JPanel of the GUI
        frame.pack();
        frame.setVisible(true);

        try {
          controller.addOriginalStocksToLibrary("GOOG");
          controller.addOriginalStocksToLibrary("AAPL");
          controller.addOriginalStocksToLibrary("NVDA");
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } else {
      System.out.println("Invalid interface type. Please choose 'Text' or 'GUI'.");
    }
  }
}
