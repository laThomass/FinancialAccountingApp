package view;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of the StockViewInterface for testing.
 */
public class MockStockView implements IView {
  private final List<String> output = new ArrayList<>();
  private int inputIndex = 0;

  public MockStockView(List<String> inputs) {
    this.output.addAll(inputs);
  }

  @Override
  public void displayMenu() {
    output.add("Displaying menu");
  }

  @Override
  public void displayMessage(String message) {

  }

}