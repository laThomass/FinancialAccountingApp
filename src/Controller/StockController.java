package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Model.Portfolio;
import View.StockView;
import Model.Stock;
import Model.AlphaAPI;

import java.time.format.DateTimeFormatter;

public class StockController {
  private final StockView stockView;
  private final AlphaAPI api;
  private final List<Portfolio> loPortfolio;
  private final Map<String, List<Stock>> library;
  private final Scanner scanner;

  private final Readable in;
  private final Appendable out;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public StockController(StockView stockView, Readable in, Appendable out, AlphaAPI api) {
    this.stockView = stockView;
    this.api = new AlphaAPI();
    this.loPortfolio = new ArrayList<>();
    this.library = new HashMap<>();
    this.in = in;
    this.out = out;
    this.scanner = new Scanner(in);
  }

  public void start() throws Exception {
    String stockChoice;
    String portfolioChoice = null;
    String portfolioName;
    int days;
    int quantity;
    boolean quit = false;

    while (!quit) {
      stockView.displayMenu();
      System.out.print("Please enter your choice as the associated number: ");
      int choice = scanner.nextInt();
      switch (choice) {
        case 1:
          StockView.display("Please enter your desired stock in ticker form.");
          stockChoice = scanner.next();
          String startDate = promptForValidDate("Start date?");
          String endDate = promptForValidDate("End date?");
          StockView.display("" + Stock.viewGainLoss(stockChoice, startDate, endDate, api, library));

          System.out.println();
          break;
        case 2:
          StockView.display("Please enter your desired stock in ticker form.");
          stockChoice = scanner.next();
          startDate = promptForValidDate("Start date?");
          StockView.display("How many days?");
          days = scanner.nextInt();
          StockView.display("Your average is: " + Stock.viewXDayMovingAverage(stockChoice, startDate, days, api, library));
          break;
        case 3:
          StockView.display("Please enter your desired stock in ticker form.");
          stockChoice = scanner.next();
          startDate = promptForValidDate("Start date?");
          StockView.display("How many days?");
          days = scanner.nextInt();
          System.out.println("The stock's crossovers in the last " + days + " were:");
          for (String date : Stock.viewXDayCrossOver(stockChoice, startDate, days, api, library)) {
            System.out.println(date);
          }
          break;
        case 4:
          StockView.display("Please enter the name of your new portfolio.");
          portfolioName = scanner.next();
          StockView.display("Would you like to add stocks to your portfolio immediately?");
          String answer = scanner.next().toLowerCase();
          if (answer.equals("yes")) {
            Map<String, Integer> stocks = new HashMap<>();
            boolean done = false;
            while (!done) {
              System.out.println("Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.");
              stockChoice = scanner.next().toLowerCase();
              if (stockChoice.equals("stop")) {
                done = true;
              } else {
                StockView.display("Please enter your desired quantity.");
                quantity = scanner.nextInt();
                stocks.put(stockChoice, quantity);
              }
            }
            for (String symbol : stocks.keySet()) {
              try {
                api.fetchData(symbol, library);
              } catch (Exception e) {
                stocks.remove(symbol);
                StockView.display("We did not add stock " + symbol + " because the ticker was invalid");
              }
            }
            loPortfolio.add(Portfolio.createPortfolio(portfolioName, stocks));
            StockView.display("Portfolio created with name " + portfolioName + " and " + stocks.size() + " stocks.");
          } else {
            loPortfolio.add(Portfolio.createPortfolio(portfolioName));
            StockView.display("Portfolio created with name " + portfolioName);
          }
          break;
        case 5:
          StockView.display("Which portfolio would you like to add to?");
          portfolioChoice = scanner.next();
          boolean done = false;
          for (Portfolio portfolio : loPortfolio) {
            if (portfolio.getName().equals(portfolioChoice)) {
              done = true;
              StockView.display("Please enter your stock and then the amount");
              String symbol = scanner.next();
              quantity = scanner.nextInt();
              portfolio.addStock(symbol, quantity);
            }
          }
          if (!done) {
            StockView.display("We could not find a portfolio with that name.");
          } else {
            StockView.display("The stocks were added successfully.");
          }
          break;
        case 6:
          StockView.display("Which portfolio would you like to check the value of?");
          portfolioChoice = scanner.next();
          String date = promptForValidDate("Date?");
          double result = 0.0;
          boolean finished = false;

          for (Portfolio portfolio : loPortfolio) {
            if (portfolio.getName().equals(portfolioChoice)) {
              finished = true;
              result = portfolio.calculatePortfolioValue(date, api, library);
            }
          }
          if (!finished) {
            StockView.display("We could not find a portfolio with that name.");
          } else {
            StockView.display("Your portfolio's value on date " + date + " is " + result + ".");
          }
          break;
        case 7:
          quit = true;
          break;
        default:
          StockView.display("Invalid choice");
      }
    }
  }

  private String promptForValidDate(String prompt) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);
    String dateStr;
    while (true) {
      StockView.display(prompt);
      dateStr = scanner.next();
      try {
        sdf.parse(dateStr);
        break;
      } catch (ParseException e) {
        StockView.display("Invalid date format. Please enter the date in yyyy-MM-dd format.");
      }
    }
    return dateStr;
  }

  public void addOriginalStocksToLibrary(String name) throws IOException {
    List<Stock> stocks = new ArrayList<>();
    Set<String> uniqueDates = new HashSet<>();

    // Define multiple date formats
    SimpleDateFormat[] possibleFormats = new SimpleDateFormat[]{
            new SimpleDateFormat("M/d/yyyy"),
            new SimpleDateFormat("yyyy-MM-dd")
    };
    SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

    BufferedReader reader = new BufferedReader(new FileReader("resources/" + name + ".csv"));
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
