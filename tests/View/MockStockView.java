package View;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of the StockViewInterface for testing purposes.
 */
public class MockStockView implements IView {
  private final List<String> output = new ArrayList<>();
  private final List<String> inputs;
  private int inputIndex = 0;

  public MockStockView(List<String> inputs) {
    this.inputs = inputs;
  }

  @Override
  public void displayMenu() {
    output.add("Displaying menu");
  }

  public void display(String message) {
    output.add(message);
  }

  public String next() {
    if (inputIndex < inputs.size()) {
      return inputs.get(inputIndex++);
    } else {
      throw new java.util.NoSuchElementException("No more input available");
    }
  }

  public int nextInt() {
    if (inputIndex < inputs.size()) {
      return Integer.parseInt(inputs.get(inputIndex++));
    } else {
      throw new java.util.NoSuchElementException("No more input available");
    }
  }

  public List<String> getOutput() {
    return output;
  }
}
