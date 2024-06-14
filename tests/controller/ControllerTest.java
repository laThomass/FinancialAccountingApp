package controller;

import model.MockAlphaAPI;
import view.MockStockView;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for controller class.
 */
public class ControllerTest {

  @Test
  public void testControllerViewGainAverage() throws Exception {
    // Simulated user input
    String simulatedUserInput = "1\nGOOG\n2020-10-20\n2020-10-30\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: "
                    + "Please enter your desired stock in ticker form.",
            "Start date?",
            "End date?",
            "Gain/Loss for GOOG from 2020-10-20 to 2020-10-30: $4.18",
            "Please enter your choice as the associated number:"
    );


    assertEquals(expectedOutput, actualOutputLines);
  }

  @Test
  public void testViewMovingAverage() throws Exception {
    String simulatedUserInput = "2\nGOOG\n2020-10-20\n2\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: "
                    + "Please enter your desired stock in ticker form.",
            "Start date?",
            "How many days?",
            "The 2-day moving average for GOOG starting from 2020-10-20 is $1545.27",
            "Please enter your choice as the associated number:"
    );

    assertEquals(expectedOutput, actualOutputLines);
  }

  @Test
  public void testAddPortfolio() throws Exception {
    String simulatedUserInput = "4\nNewP\nYes\nGOOG\n20\n2020-10-20\nstop\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: Please enter the name of your new portfolio.",
            "Would you like to add stocks to your portfolio immediately?",
            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
            "Please enter your desired quantity.",
            "Date?",
            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
            "Portfolio created with name NewP and 1 stocks.",
            "Please enter your choice as the associated number:"
    );

    assertEquals(expectedOutput, actualOutputLines);
  }

  @Test
  public void testCalculatePortfolio() throws Exception {
    String simulatedUserInput = "4\nNewP\nYes\nGOOG\n20\n2020-10-20\nstop\n14\n6\nNewP\n2020-10-20\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller = new StockController(view, new InputStreamReader(inputStream),
            printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: Please enter the name of your new portfolio.",
            "Would you like to add stocks to your portfolio immediately?",
            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
            "Please enter your desired quantity.",
            "Date?",
            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
            "Portfolio created with name NewP and 1 stocks.",
            "Please enter your choice as the associated number:"
    );

    assertEquals(expectedOutput, actualOutputLines);
  }

//  @Test
//  public void testCalculatePortfolioThatNoExist() throws Exception {
//    String simulatedUserInput = "4\nNewP\nYes\nGOOG\n20\nstop\n6\nOldP\n2020-10-20\n7\n";
//    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    PrintStream printStream = new PrintStream(outputStream);
//
//    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
//    MockAlphaAPI api = new MockAlphaAPI();
//
//    StockController controller = new StockController(view, new InputStreamReader(inputStream),
//            printStream, api);
//    controller.start();
//
//    String actualOutput = outputStream.toString().trim();
//    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));
//
//    List<String> expectedOutput = Arrays.asList(
//            "Please enter your choice as the associated number: "
//                    + "Please enter the name of your new portfolio.",
//            "Would you like to add stocks to your portfolio immediately?",
//            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
//            "Please enter your desired quantity.",
//            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
//            "Portfolio created with name NewP and 1 stocks.",
//            "Please enter your choice as the associated number: "
//                    + "Which portfolio would you like to check the value of?",
//            "Date?",
//            "We could not find a portfolio with that name.",
//            "Please enter your choice as the associated number:"
//    );
//
//    assertEquals(expectedOutput, actualOutputLines);
//  }

  @Test
  public void testSellStockFromPortfolio() throws Exception {
    String simulatedUserInput = "4\nNewP\nYes\nGOOG\n20\n2020-10-20\nstop\n9\nNewP\nGOOG\n10\n2020-10-20\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: Please enter the name of your new portfolio.",
            "Would you like to add stocks to your portfolio immediately?",
            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
            "Please enter your desired quantity.",
            "Date?",
            "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
            "Portfolio created with name NewP and 1 stocks.",
            "Please enter your choice as the associated number: Which portfolio would you like to sell from?",
            "Please enter your desired stock in ticker form.",
            "Please enter your desired quantity.",
            "Date?",
            "The stocks were sold successfully.",
            "Please enter your choice as the associated number:"
    );

    assertEquals(expectedOutput, actualOutputLines);
  }


  @Test
  public void testLoadPortfolio() throws Exception {
    String simulatedUserInput = "8\nNew5\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number:"
                    + " Which portfolio would you like to "
                    + "load?",
            "Loaded New5",
            "Please enter your choice as the associated number:"
    );


    assertEquals(expectedOutput, actualOutputLines);
  }

  @Test
  public void testViewPerformance() throws Exception {
    String simulatedUserInput = "8\nNew5\n10\nNew5\n2020-10-30\n14";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number: Which portfolio would you like to load?",
            "Loaded New5",
            "Please enter your choice as the associated number: Which portfolio would you like to view the composition of?",
            "Date?",
            "Composition on 2020-10-30: ",
            "NVDA: 44.88 shares",
            "GOOG: 43.15 shares",
            "AAPL: 267.72 shares",
            "Please enter your choice as the associated number:"
    );


    assertEquals(expectedOutput, actualOutputLines);
  }

  @Test
  public void testViewCompostion() throws Exception {
    String simulatedUserInput = "8\nNew5\n11\nNew5\n2020-10-30\n14";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
            "Please enter your choice as the associated number:"
                    + " Which portfolio would you like to "
                    + "load?",
            "Loaded New5",
            "Please enter your choice as the associated number:"
    );


    assertEquals(expectedOutput, actualOutputLines);
  }

  @Test
  public void testAddStockToPortfolio() throws Exception {
    String simulatedUserInput = "4\nNewP\nYes\nGOOG\n20\n2020-10-20\nstop\n5\nNewP" +
            "\nNVDA\n10\n2020-10-30\n14\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    MockStockView view = new MockStockView(Arrays.asList(simulatedUserInput.split("\n")));
    MockAlphaAPI api = new MockAlphaAPI();

    StockController controller =
            new StockController(view, new InputStreamReader(inputStream), printStream, api);
    controller.start();

    String actualOutput = outputStream.toString().trim();
    List<String> actualOutputLines = Arrays.asList(actualOutput.split("\\r?\\n"));

    List<String> expectedOutput = Arrays.asList(
                    "Please enter your choice as the associated number: " +
                    "Please enter the name of your new portfolio.",
                    "Would you like to add stocks to your portfolio immediately?",
                    "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
                    "Please enter your desired quantity.",
                    "Date?",
                    "Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.",
                    "Portfolio created with name NewP and 1 stocks.",
                    "Please enter your choice as the associated number: " +
                    "Which portfolio would you like to add to?",
                    "Please enter your desired stock in ticker form.",
                    "Please enter your desired quantity.",
                    "Date?",
                    "The stocks were added successfully.",
                    "Please enter your choice as the associated number:"
    );


    assertEquals(expectedOutput, actualOutputLines);
  }


}
