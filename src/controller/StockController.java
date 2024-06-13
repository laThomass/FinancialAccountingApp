package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.AlphaAPI;
import model.IAlphaAPIInterface;
import model.Portfolio;
import model.Stock;
import view.IView;

/**
 * Represents the controller that runs the program and takes in information.
 */
public class StockController implements Controller {
  final Map<String, List<Stock>> library;
  private final IView stockView;
  private final IAlphaAPIInterface api;
  private final List<Portfolio> loPortfolio;

  private final Readable in;
  private final Appendable out;

  String startDate;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Constructor for the StockController.
   *
   * @param stockView the view for the stock controller
   * @param in        the input for the stock controller
   * @param out       the output for the stock controller
   * @param api       the API for the stock controller
   */

  public StockController(IView stockView, Readable in, Appendable out, IAlphaAPIInterface api) {
    this.stockView = stockView;
    this.api = new AlphaAPI();
    this.loPortfolio = new ArrayList<>();
    this.library = new HashMap<>();
    this.in = in;
    this.out = out;
  }

  /**
   * Starts the controller and handles user interactions.
   * @throws Exception if an error occurs during the execution of the method
   */
  public void start() throws Exception {
    Scanner scanner = new Scanner(in); // Make 'scanner' a local variable within the method
    String stockChoice;
    String portfolioChoice = null;
    String portfolioName;
    int days;
    int quantity;
    boolean quit = false;

    while (!quit) {
      stockView.displayMenu();
      out.append("Please enter your choice as the associated number: ");
      int choice;

      while (!scanner.hasNextInt()) {
        out.append("Invalid input. Please enter a valid number.").append(System.lineSeparator());
        scanner.next(); // Consume the invalid input
        out.append("Please enter your choice as the associated number: ");
      }

      choice = scanner.nextInt();

      switch (choice) {
        case 1:
          while (true) {
            out.append("Please enter your desired stock in ticker form.")
                    .append(System.lineSeparator());
            stockChoice = scanner.next();
            if (!isValidTicker(stockChoice)) {
              out.append("Invalid stock ticker. Please enter a valid ticker.")
                      .append(System.lineSeparator());
              continue;
            }
            String startDate = promptForValidDate("Start date?", scanner);
            String endDate = promptForValidDate("End date?", scanner);
            try {
              out.append("" + Stock.viewGainLoss(stockChoice, startDate, endDate, api, library))
                      .append(System.lineSeparator());
              break;
            } catch (RuntimeException e) {
              out.append("Error: " + e.getMessage()).append(System.lineSeparator());
            }
          }
          out.append(System.lineSeparator());
          break;

        case 2:
          while (true) {
            out.append("Please enter your desired stock in ticker form.")
                    .append(System.lineSeparator());
            stockChoice = scanner.next();
            if (!isValidTicker(stockChoice)) {
              out.append("Invalid stock ticker. Please enter a valid ticker.")
                      .append(System.lineSeparator());
              continue;
            }
            startDate = promptForValidDate("Start date?", scanner);
            out.append("How many days?").append(System.lineSeparator());
            days = scanner.nextInt();
            try {
              out.append("Your average is: "
                              + Stock.viewXDayMovingAverage(stockChoice, startDate,
                              days, api, library))
                      .append(System.lineSeparator());
              break;
            } catch (RuntimeException e) {
              out.append("Error: " + e.getMessage()).append(System.lineSeparator());
            }
          }
          break;

        case 3:
          while (true) {
            out.append("Please enter your desired stock in ticker form.")
                    .append(System.lineSeparator());
            stockChoice = scanner.next();
            if (!isValidTicker(stockChoice)) {
              out.append("Invalid stock ticker. Please enter a valid ticker.")
                      .append(System.lineSeparator());
              continue;
            }
            startDate = promptForValidDate("Start date?", scanner);
            out.append("How many days?").append(System.lineSeparator());
            days = scanner.nextInt();
            try {
              out.append("The stock's crossovers in the last " + days + " were:")
                      .append(System.lineSeparator());
              for (String date :
                      Stock.viewXDayCrossOver(stockChoice, startDate, days, api, library)) {
                out.append(date).append(System.lineSeparator());
              }
              break;
            } catch (RuntimeException e) {
              out.append("Error: " + e.getMessage()).append(System.lineSeparator());
            }
          }
          break;
        case 4:
          out.append("Please enter the name of your new portfolio.")
                  .append(System.lineSeparator());
          portfolioName = scanner.next();
          out.append("Would you like to add stocks to your portfolio immediately?")
                  .append(System.lineSeparator());
          String answer = scanner.next().toLowerCase();
          if (answer.equals("yes")) {
            Map<String, Integer> stocks = new HashMap<>();
            boolean done = false;
            while (!done) {
              out.append("Please enter your desired stock in ticker form. "
                      + "To abort at any time, enter 'stop'.").append(System.lineSeparator());
              stockChoice = scanner.next().toLowerCase();
              if (stockChoice.equals("stop")) {
                done = true;
              } else {
                out.append("Please enter your desired quantity.")
                        .append(System.lineSeparator());
                quantity = scanner.nextInt();
                stocks.put(stockChoice, quantity);
              }
            }
            for (String symbol : stocks.keySet()) {
              try {
                api.fetchData(symbol, library);
              } catch (Exception e) {
                stocks.remove(symbol);
                out.append("We did not add stock " + symbol + " because the ticker was invalid")
                        .append(System.lineSeparator());
              }
            }
            loPortfolio.add(Portfolio.createPortfolio(portfolioName, stocks));
            out.append("Portfolio created with name " + portfolioName + " and "
                    + stocks.size() + " stocks.").append(System.lineSeparator());
          } else {
            loPortfolio.add(Portfolio.createPortfolio(portfolioName));
            out.append("Portfolio created with name " + portfolioName)
                    .append(System.lineSeparator());
          }
          break;
        case 5:
          out.append("Which portfolio would you like to add to?")
                  .append(System.lineSeparator());
          portfolioChoice = scanner.next();
          boolean done = false;
          for (Portfolio portfolio : loPortfolio) {
            if (portfolio.getName().equals(portfolioChoice)) {
              done = true;
              out.append("Please enter your stock and then the amount")
                      .append(System.lineSeparator());
              String symbol = scanner.next();
              quantity = scanner.nextInt();
              String date = promptForValidDate("Date?", scanner);
              portfolio.addStock(symbol, quantity, date);
            }
          }
          if (!done) {
            out.append("We could not find a portfolio with that name.")
                    .append(System.lineSeparator());
          } else {
            out.append("The stocks were added successfully.").append(System.lineSeparator());
          }
          break;
        case 6:
          out.append("Which portfolio would you like to check the value of?")
                  .append(System.lineSeparator());
          portfolioChoice = scanner.next();
          String date = promptForValidDate("Date?", scanner);
          double result = 0.0;
          boolean finished = false;

          for (Portfolio portfolio : loPortfolio) {
            if (portfolio.getName().equals(portfolioChoice)) {
              finished = true;
              result = portfolio.calculatePortfolioValue(date, api, library);
            }
          }
          if (!finished) {
            out.append("We could not find a portfolio with that name.")
                    .append(System.lineSeparator());
          } else {
            out.append("Your portfolio's value on date " + date + " is " + result + ".")
                    .append(System.lineSeparator());
          }
          break;
        case 7:
          out.append("Which portfolio would you like to save?")
                  .append(System.lineSeparator());
          portfolioChoice = scanner.next();
          finished = false;

          for (Portfolio portfolio : loPortfolio) {
            if (portfolio.getName().equals(portfolioChoice)) {
              Portfolio.savePortfolio(portfolio);
            }
          }
          if (!finished) {
            out.append("We could not find a portfolio with that name.")
                    .append(System.lineSeparator());
          } else {
            out.append("Saved ").append(portfolioChoice);
          }
        case 8:
          quit = true;
          break;
        default:
          out.append("Invalid choice").append(System.lineSeparator());
      }
    }
  }

  private String promptForValidDate(String prompt, Scanner scanner) throws IOException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);
    String dateStr;
    while (true) {
      out.append(prompt).append(System.lineSeparator());
      dateStr = scanner.next();
      try {
        sdf.parse(dateStr);
        break;
      } catch (ParseException e) {
        out.append("Invalid date format. Please enter the date in yyyy-MM-dd format.")
                .append(System.lineSeparator());
      }
    }
    return dateStr;
  }

  private boolean isValidTicker(String ticker) {
    // Check if the ticker is null or empty
    if (ticker == null || ticker.isEmpty()) {
      return false;
    }

    // Check the length of the ticker
    int length = ticker.length();
    if (length < 1 || length > 5) {
      return false;
    }

    // Convert the ticker to uppercase
    ticker = ticker.toUpperCase();

    // Check if the ticker contains only alphanumeric characters
    for (char c : ticker.toCharArray()) {
      if (!Character.isLetterOrDigit(c)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Adds the original stocks to the library based on provided name.
   * Stock data is read from the files under resources. GOOG, NVDA, AAPL
   * are the sample stocks that have been provided
   *
   * @param name the name of the stock data file
   * @throws IOException if an I/O error occurs while reading the stock data
   */
  public void addOriginalStocksToLibrary(String name) throws IOException {
    List<Stock> stocks = new ArrayList<>();
    Set<String> uniqueDates = new HashSet<>();

    // Define multiple date formats
    SimpleDateFormat[] possibleFormats = new SimpleDateFormat[]{
        new SimpleDateFormat("M/d/yyyy"),
        new SimpleDateFormat("yyyy-MM-dd")
    };
    SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Use getResourceAsStream to read the file from the JAR
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name + ".csv");
    if (inputStream == null) {
      throw new IOException("Resource not found: " + name + ".csv");
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    String date;
    double open;
    double close;
    double high;
    double low;
    int volume;
    reader.readLine(); // Skip the header

    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(",");
      date = parts[0];
      open = Double.parseDouble(parts[1]);
      high = Double.parseDouble(parts[2]);
      low = Double.parseDouble(parts[3]);
      close = Double.parseDouble(parts[4]);
      volume = Integer.parseInt(parts[5]);

      // Try parsing the date with multiple formats
      Date parsedDate = null;
      for (SimpleDateFormat format : possibleFormats) {
        try {
          parsedDate = format.parse(date);
          break;
        } catch (ParseException e) {
          // Continue to try the next format
        }
      }
      if (parsedDate == null) {
        throw new RuntimeException("Unparseable date: " + date);
      }

      // Format the parsed date to the target format
      String formattedDate = targetFormat.format(parsedDate);

      // Ensure only unique dates are added
      if (!uniqueDates.contains(formattedDate)) {
        uniqueDates.add(formattedDate);
        stocks.add(new Stock(formattedDate, open, high, low, close, volume));
      }
    }
    reader.close();

    library.put(name, stocks);
  }

}