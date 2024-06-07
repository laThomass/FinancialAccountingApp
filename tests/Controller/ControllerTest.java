package Controller;

import Model.MockAlphaAPI;
import View.MockStockView;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControllerTest {

  @Test
  public void testStart() throws Exception {
    // Simulated user input
    String simulatedUserInput = "1\nGOOG\n2020-10-20\n2020-10-30\n2\nAAPL\n2023-01-01\n5\n7\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller = new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    // Capture the actual output
    String actualOutput = outputStream.toString().trim();
    // Split the output based on new lines and trim each line
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    // Expected output
    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: " +
                    "Please enter your desired stock in ticker form.",
            "Start date?",
            "End date?",
            "4.182707448278516",
            "",
            "Please enter your choice as the associated number: " +
            "Please enter your desired stock in ticker form.",
            "Start date?",
            "How many days?",
            "Your average is: 194.196",
            "Please enter your choice as the associated number:"
    );

    // Verify the output
    assertEquals(expectedOutput, actualOutputLines);
  }
}
