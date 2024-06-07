package Controller;

import Model.MockAlphaAPI;
import View.MockStockView;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestStockController {

  @Test
  public void testStart() throws Exception {
    // Simulated user input
    List<String> inputs = Arrays.asList(
            "1", "AAPL", "2023-01-01", "2023-01-10",
            "2", "AAPL", "2023-01-01", "5",
            "7"
    );

    MockStockView view = new MockStockView(inputs);
    MockAlphaAPI api = new MockAlphaAPI();
    StringBuffer out = new StringBuffer();

    StockController controller = new StockController(view, new StringReader(""), out, api);
    controller.start();

    // Verify the output
    List<String> expectedOutput = Arrays.asList(
            "Displaying menu",
            "Please enter your choice as the associated number: ",
            "Please enter your desired stock in ticker form.",
            "Start date?",
            "End date?",
            "Displaying menu",
            "Please enter your choice as the associated number: ",
            "Please enter your desired stock in ticker form.",
            "Start date?",
            "How many days?",
            "Displaying menu",
            "Please enter your choice as the associated number: "
    );

    assertEquals(expectedOutput, view.getOutput());
  }
}
