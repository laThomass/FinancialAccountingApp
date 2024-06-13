package model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for the Stock class.
 */
public class StockTest {

  private AlphaAPI api;
  private Map<String, List<Stock>> library;

  @Before
  public void setUp() {
    api = new AlphaAPI(); // Mock or real API object
    library = new HashMap<>();

    // Mock data for stocks
    List<Stock> googStocks = Arrays.asList(
            new Stock("2020-03-04", 1000, 1100, 950,
                    1050, 10000),
            new Stock("2020-03-09", 1050, 1150, 1000,
                    1100, 12000)
    );

    List<Stock> aaplStocks = Arrays.asList(
            new Stock("2020-03-04", 200, 210,
                    190, 205, 15000),
            new Stock("2020-03-09", 205, 215,
                    195, 210, 16000)
    );

    library.put("GOOG", googStocks);
    library.put("AAPL", aaplStocks);
  }

  @Test
  public void testViewGainLoss() throws IOException {
    // Test for GOOG
    double gainLoss = Stock.viewGainLoss("GOOG", "2020-03-04",
            "2020-03-09", api, library);
    assertEquals(4.76, gainLoss, 0.01); // ((1100 - 1050) / 1050) * 100

    // Test for AAPL
    gainLoss = Stock.viewGainLoss("AAPL", "2020-03-04", "2020-03-09",
            api, library);
    assertEquals(2.44, gainLoss, 0.01); // ((210 - 205) / 205) * 100
  }

  @Test
  public void testViewGainLossWithMissingData() {
    try {
      // Test with missing data
      Stock.viewGainLoss("GOOG", "2020-03-04",
              "2020-03-10", api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertEquals("Could not find price for the end date.", e.getMessage());
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayMovingAverage() throws Exception {
    // Test for GOOG
    double movingAverage = Stock.viewXDayMovingAverage("GOOG", "2020-03-04",
            2, api, library);
    assertEquals(1075.0, movingAverage, 0.01); // (1050 + 1100) / 2

    // Test for AAPL
    movingAverage = Stock.viewXDayMovingAverage("AAPL", "2020-03-04", 2,
            api, library);
    assertEquals(207.5, movingAverage, 0.01); // (205 + 210) / 2
  }

  @Test
  public void testViewXDayMovingAverageWithInsufficientData() {
    try {
      // Test with insufficient data points
      Stock.viewXDayMovingAverage("GOOG", "2020-03-04", 3, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertEquals("Number of days goes too far back, or we"
              + " could not find an average for the date provided.", e.getMessage());
    } catch (Exception e) {
      fail("Unexpected Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayCrossOver() throws Exception {
    // Test for GOOG
    List<String> crossovers = Stock.viewXDayCrossOver("GOOG",
            "2020-03-04", 2, api, library);
    assertEquals(1, crossovers.size());
    assertTrue(crossovers.contains("2020-03-09 (Bullish Crossover)"));

    // Test for AAPL
    crossovers = Stock.viewXDayCrossOver("AAPL", "2020-03-04",
            2, api, library);
    assertEquals(1, crossovers.size());
    assertTrue(crossovers.contains("2020-03-09 (Bullish Crossover)"));
  }

  @Test
  public void testViewXDayCrossOverWithInsufficientData() {
    try {
      // Test with insufficient data points
      Stock.viewXDayCrossOver("GOOG", "2020-03-04", 3, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertEquals("Insufficient data points to calculate the 3-day moving average.",
              e.getMessage());
    } catch (Exception e) {
      fail("Unexpected Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetters() {
    Stock stock = new Stock("2020-03-04", 1000, 1100, 950,
            1050, 10000);

    assertEquals("2020-03-04", stock.getDate());
    assertEquals(1000, stock.getOpeningPrice(), 0.01);
    assertEquals(1100, stock.getHighPrice(), 0.01);
    assertEquals(950, stock.getLowPrice(), 0.01);
    assertEquals(1050, stock.getClosingPrice(), 0.01);
    assertEquals(10000, stock.getVolume());
  }


  @Test
  public void testViewGainLossWithInvalidSymbol() {
    try {
      Stock.viewGainLoss("INVALID", "2020-03-04", "2020-03-09",
              api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertEquals("No data available for the symbol: INVALID", e.getMessage());
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewGainLossWithInvalidDates() {
    try {
      Stock.viewGainLoss("GOOG", "invalid-date", "2020-03-09",
              api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Date parsing error"));
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }

    try {
      Stock.viewGainLoss("GOOG", "2020-03-04",
              "invalid-date", api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Date parsing error"));
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayMovingAverageWithInvalidSymbol() {
    try {
      Stock.viewXDayMovingAverage("INVALID", "2020-03-04", 2, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertEquals("No data available for the symbol: INVALID", e.getMessage());
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayMovingAverageWithInvalidDate() {
    try {
      Stock.viewXDayMovingAverage("GOOG", "invalid-date", 2, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Date parsing error"));
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayCrossOverWithInvalidSymbol() {
    try {
      Stock.viewXDayCrossOver("INVALID", "2020-03-04", 2, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertEquals("Insufficient data points to calculate the 2-day moving average.",
              e.getMessage());
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayCrossOverWithInvalidDate() {
    try {
      Stock.viewXDayCrossOver("GOOG", "invalid-date", 2, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Date parsing error"));
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testViewXDayCrossOverWithNegativeDays() {
    try {
      Stock.viewXDayCrossOver("GOOG", "2020-03-04", -1, api, library);
      fail("Expected RuntimeException not thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("Insufficient data points"));
    } catch (IOException e) {
      fail("Unexpected IOException thrown: " + e.getMessage());
    }
  }


}