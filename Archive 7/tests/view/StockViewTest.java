package view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the StockView class.
 */

public class StockViewTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  public void testDisplayMenu() {
    StockView stockView = new StockView();
    stockView.displayMenu();

    String expectedOutput = "1. View Gain/Loss\n"
            + "2. View Moving Average\n"
            + "3. View X-Day Crossovers\n"
            + "4. Create Model.Portfolio\n"
            + "5. Add Model.Stock to Model.Portfolio\n"
            + "6. View Model.Portfolio Value\n"
            + "7. Save a portfolio\n"
            + "8. Load a portfolio\n"
            + "9. Sell Stock \n"
            + "10. View Composition\n"
            + "11. View Distribution\n"
            + "12. Rebalance Portfolio\n"
            + "13. View Performance\n"
            + "14. Exit\n";

    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplay() {
    String message = "yoooo!";
    StockView.display(message);

    assertEquals(message + "\n", outContent.toString());
  }
}