package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The AlphaAPI class implements the
 * IAlphaAPIInterface and provides functionality to fetch stock data
 * from the Alpha Vantage API.
 */
public class AlphaAPI implements IAlphaAPIInterface {
  /**
   * The API key for accessing the Alpha Vantage API.
   */
  public static final String API_KEY = "W0M1JOKC82EZEQA8";

  /**
   * The base URL for the Alpha Vantage API.
   */
  public static final String BASE_URL =
       "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=full&symbol=";

  /**
   * Fetches stock data for the specified symbol from the Alpha Vantage API or the library cache.
   *
   * @param symbol  the stock symbol to fetch data for
   * @param library the library cache to store and retrieve stock data
   * @return a list of Stock objects containing the fetched stock data
   * @throws IOException if an error occurs during the API request
   */
  @Override
  public List<Stock> fetchData(String symbol, Map<String, List<Stock>> library)
          throws IOException {
    if (library != null && library.containsKey(symbol)) {
      System.out.println("Data collected from cache");
      return library.get(symbol);
    } else {
      List<Stock> stocks = new ArrayList<>();
      String urlString = BASE_URL + symbol + "&apikey=" + API_KEY + "&datatype=csv";

      URL url = new URL(urlString);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        boolean isFirstLine = true;
        System.out.println("Data collected from API");
        while ((line = reader.readLine()) != null) {
          if (isFirstLine) {
            isFirstLine = false;
            continue; // Skip the header line
          }

          String[] parts = line.split(",");
          if (parts.length >= 6) {
            String date = parts[0];
            double open = Double.parseDouble(parts[1]);
            double high = Double.parseDouble(parts[2]);
            double low = Double.parseDouble(parts[3]);
            double close = Double.parseDouble(parts[4]);
            int volume = Integer.parseInt(parts[5]);
            stocks.add(new Stock(date, open, high, low, close, volume));
          } else {
            System.out.println("Skipping line: " + line);
          }
        }

        reader.close();
      } else {
        throw new IOException("HTTP request failed with response code: " + responseCode);
      }

      if (library != null && !library.containsKey(symbol)) {
        library.put(symbol, stocks);
      }

      return stocks;
    }
  }
}