//
//import Model.AlphaAPI;
//import Model.Portfolio;
//import Model.Stock;
//import View.StockView;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//
//public class StockControllerTest {
//  private StockView view;
//  private AlphaAPI api;
//
//  @Before
//  public void setUp() {
//    view = new StockView();
//    api = new MockAlphaAPI();
//  }
//
//  @Test
//  public void testViewGainLoss() throws Exception {
//    StringBuilder fakeUserInput = new StringBuilder();
//    StringBuilder expectedOutput = new StringBuilder();
//
//    Interaction[] interactions = new Interaction[]{
//            TestHelper.inputs("3"),
//            TestHelper.prompts("Please enter your desired stock in ticker form.", "AAPL"),
//            TestHelper.prompts("Start date?", "2020-10-27"),
//            TestHelper.prompts("End date?", "2020-10-30"),
//            TestHelper.prompts("Could not find price for the start date. Please enter a new start date:", "2022-01-02"),
//            TestHelper.prompts("Could not find price for the end date. Please enter a new end date:", "2022-12-31"),
//            TestHelper.prints("Gain/Loss: 10.5%"),
//            TestHelper.inputs("7")
//    };
//
//    for (Interaction interaction : interactions) {
//      interaction.apply(fakeUserInput, expectedOutput);
//    }
//
//    StringReader input = new StringReader(fakeUserInput.toString());
//    StringBuilder actualOutput = new StringBuilder();
//
//    StockController controller = new StockController(view, input, actualOutput, api);
//    controller.start();
//
//
//    assertEquals(expectedOutput.toString(), actualOutput.toString());
//  }
//
//
//  private static class MockAlphaAPI extends AlphaAPI {
//    @Override
//    public List<Stock> fetchData(String symbol, Map<String, List<Stock>> library) throws IOException {
//      // Return mock stock data for testing
//      List<Stock> stocks = new ArrayList<>();
//      stocks.add(new Stock("2022-01-01", 100.0, 110.0, 90.0, 105.0, 1000));
//      stocks.add(new Stock("2022-12-31", 150.0, 160.0, 140.0, 155.0, 2000));
//      return stocks;
//    }
//  }
//}
//
//
//class TestHelper {
//  static Interaction prints(String... lines) {
//    return (input, output) -> {
//      for (String line : lines) {
//        output.append(line).append('\n');
//      }
//    };
//  }
//
//  static Interaction inputs(String in) {
//    return (input, output) -> {
//      input.append(in).append('\n');
//    };
//  }
//
//  static Interaction prompts(String prompt, String response) {
//    return (input, output) -> {
//      output.append(prompt).append("\n");
//      input.append(response).append('\n');
//    };
//  }
//}
//}
//
//
//class TestHelper {
//  static Interaction prints(String... lines) {
//    return (input, output) -> {
//      for (String line : lines) {
//        output.append(line).append('\n');
//      }
//    };
//  }
//
//  static Interaction inputs(String in) {
//    return (input, output) -> {
//      input.append(in).append('\n');
//    };
//  }
//
//  static Interaction prompts(String prompt, String response) {
//    return (input, output) -> {
//      output.append(prompt).append("\n");
//      input.append(response).append('\n');
//    };
//  }
//}