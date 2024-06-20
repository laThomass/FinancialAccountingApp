package view;

import javax.swing.*;

import controller.StockController;

/**
 * Interface representing the GUI view component of the Stock application.
 * It defines methods for displaying messages to the user and setting the controller.
 */
public interface IGUIView extends IView {
  void setController(StockController controller);

  void displayMessage(String message);

  JPanel getMainPanel(); // Add this method to get the main panel
}
