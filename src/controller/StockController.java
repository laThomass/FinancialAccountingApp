package controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

  public StockController(IView stockView, Readable in, Appendable out, IAlphaAPIInterface api) {
    this.stockView = stockView;
    this.api = new AlphaAPI();
    this.loPortfolio = new ArrayList<>();
    this.library = new HashMap<>();
    this.in = in;
    this.out = out;
  }

  public void start() throws Exception {
    Scanner scanner = new Scanner(in);
    boolean quit = false;

    while (!quit) {
      stockView.displayMenu();
      out.append("Please enter your choice as the associated number: ");
      int choice = getUserChoice(scanner);

      switch (choice) {
        case 1:
          handleViewGainLoss(scanner);
          break;
        case 2:
          handleViewMovingAverage(scanner);
          break;
        case 3:
          handleViewXDayCrossovers(scanner);
          break;
        case 4:
          handleCreatePortfolio(scanner);
          break;
        case 5:
          handleAddStockToPortfolio(scanner);
          break;
        case 6:
          handleViewPortfolioValue(scanner);
          break;
        case 7:
          handleSavePortfolio(scanner);
          break;
        case 8:
          handleLoadPortfolio(scanner);
          break;
        case 9:
          handleSellStock(scanner);
          break;
        case 10:
          handleViewComposition(scanner);
          break;
        case 11:
          handleViewDistribution(scanner);
          break;
        case 12:
          handleRebalancePortfolio(scanner);
          break;
        case 13:
          handleViewPerformance(scanner);
          break;
        case 14:
          quit = true;
          break;
        default:
          out.append("Invalid choice").append(System.lineSeparator());
      }
    }
  }

  private int getUserChoice(Scanner scanner) throws IOException {
    while (!scanner.hasNextInt()) {
      out.append("Invalid input. Please enter a valid number.").append(System.lineSeparator());
      scanner.next();
      out.append("Please enter your choice as the associated number: ");
    }
    return scanner.nextInt();
  }

  private void handleViewGainLoss(Scanner scanner) throws IOException {
    String stockChoice = getValidStockTicker(scanner);
    String startDate = promptForValidDate("Start date?", scanner);
    String endDate = promptForValidDate("End date?", scanner);

    try {
      double gainLoss = Stock.viewGainLoss(stockChoice, startDate, endDate, api, library);
      out.append(String.format("Gain/Loss for %s from %s to %s: $%.2f", stockChoice, startDate, endDate, gainLoss))
              .append(System.lineSeparator());
    } catch (RuntimeException e) {
      out.append("Error: " + e.getMessage()).append(System.lineSeparator());
    }
  }

  private void handleViewMovingAverage(Scanner scanner) throws IOException {
    String stockChoice = getValidStockTicker(scanner);
    String startDate = promptForValidDate("Start date?", scanner);
    int days = promptForValidDays("How many days?", scanner);

    try {
      double average = Stock.viewXDayMovingAverage(stockChoice, startDate, days, api, library);
      out.append(String.format("The %d-day moving average for %s starting from %s is $%.2f", days, stockChoice, startDate, average))
              .append(System.lineSeparator());
    } catch (RuntimeException e) {
      out.append("Error: " + e.getMessage()).append(System.lineSeparator());
    }
  }

  private void handleViewXDayCrossovers(Scanner scanner) throws IOException {
    String stockChoice = getValidStockTicker(scanner);
    String startDate = promptForValidDate("Start date?", scanner);
    int days = promptForValidDays("How many days?", scanner);

    try {
      List<String> crossovers = Stock.viewXDayCrossOver(stockChoice, startDate, days, api, library);
      out.append(String.format("The %d-day crossovers for %s starting from %s are:", days, stockChoice, startDate))
              .append(System.lineSeparator());
      for (String date : crossovers) {
        out.append(date).append(System.lineSeparator());
      }
    } catch (RuntimeException e) {
      out.append("Error: " + e.getMessage()).append(System.lineSeparator());
    }
  }

  private void handleCreatePortfolio(Scanner scanner) throws IOException, ParseException {
    out.append("Please enter the name of your new portfolio.").append(System.lineSeparator());
    String portfolioName = scanner.next();
    out.append("Would you like to add stocks to your portfolio immediately?").append(System.lineSeparator());
    String answer = scanner.next().toLowerCase();

    if (answer.equals("yes")) {
      Map<String, List<Stock>> stocks = getStocksFromUser(scanner);
      loPortfolio.add(Portfolio.createPortfolio(portfolioName, stocks));
      out.append(String.format("Portfolio created with name %s and %d stocks.", portfolioName, stocks.size()))
              .append(System.lineSeparator());
    } else {
      loPortfolio.add(Portfolio.createPortfolio(portfolioName));
      out.append("Portfolio created with name ").append(portfolioName).append(System.lineSeparator());
    }
  }

  private void handleAddStockToPortfolio(Scanner scanner) throws IOException, ParseException {
    out.append("Which portfolio would you like to add to?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      String stockChoice = getValidStockTicker(scanner);
      int quantity = promptForValidQuantity("Please enter your desired quantity.", scanner);
      String date = promptForValidDate("Date?", scanner);
      portfolio.addStock(stockChoice, quantity, date);
      out.append("The stocks were added successfully.").append(System.lineSeparator());
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleViewPortfolioValue(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to check the value of?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    String date = promptForValidDate("Date?", scanner);
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      double result = portfolio.calculatePortfolioValue(date, api, library);
      out.append(String.format("Your portfolio's value on date %s is $%.2f.", date, result)).append(System.lineSeparator());
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleSavePortfolio(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to save?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      File file = new File(portfolio.getName() + ".txt");
      Portfolio.savePortfolio(portfolio, file);
      out.append("Saved ").append(portfolioChoice).append(System.lineSeparator());
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleLoadPortfolio(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to load?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();

    try {
      File file = new File(portfolioChoice + ".txt");
      Portfolio loadedPortfolio = Portfolio.loadPortfolio(file);
      if (!portfolioExists(portfolioChoice)) {
        loPortfolio.add(loadedPortfolio);
        out.append("Loaded ").append(portfolioChoice).append(System.lineSeparator());
      } else {
        out.append("Portfolio with that name already exists.").append(System.lineSeparator());
      }
    } catch (Exception e) {
      out.append("We could not load the portfolio: ").append(e.getMessage()).append(System.lineSeparator());
    }
  }

  private void handleSellStock(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to sell from?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      try {
        out.append("Please enter your desired stock in ticker form.").append(System.lineSeparator());
        String symbol = scanner.next().toUpperCase();
        int quantity = promptForValidQuantity("Please enter your desired quantity.", scanner);
        String date = promptForValidDate("Date?", scanner);

        portfolio.removeStock(symbol, quantity, date);
        out.append("The stocks were sold successfully.").append(System.lineSeparator());
      } catch (IllegalArgumentException e) {
        out.append("Error: ").append(e.getMessage()).append(System.lineSeparator());
      } catch (ParseException e) {
        out.append("Error: Invalid date format.").append(System.lineSeparator());
      }
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleViewComposition(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to view the composition of?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    String date = promptForValidDate("Date?", scanner);
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      Map<String, Double> composition = portfolio.getComposition(date);
      out.append("Composition on ").append(date).append(": ").append(System.lineSeparator());
      for (Map.Entry<String, Double> entry : composition.entrySet()) {
        out.append(String.format("%s: %.2f shares", entry.getKey(), entry.getValue()))
                .append(System.lineSeparator());
      }
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleViewDistribution(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to view the distribution of value of?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    String date = promptForValidDate("Date?", scanner);
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      try {
        Map<String, Double> distribution = portfolio.getDistributionOfValue(date, api, library);
        out.append("Distribution of value on ").append(date).append(": ").append(System.lineSeparator());
        for (Map.Entry<String, Double> entry : distribution.entrySet()) {
          out.append(String.format("%s: $%.2f", entry.getKey(), entry.getValue()))
                  .append(System.lineSeparator());
        }
      } catch (RuntimeException e) {
        out.append("Error: ").append(e.getMessage()).append(System.lineSeparator());
      }
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleRebalancePortfolio(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to rebalance?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      String date = promptForValidDate("Date?", scanner);
      Map<String, Double> weights = getWeightsFromUser(scanner);

      try {
        portfolio.rebalancePortfolio(date, api, library, weights);
        out.append("Portfolio rebalanced successfully.").append(System.lineSeparator());
      } catch (Exception e) {
        out.append("Error during rebalancing: ").append(e.getMessage()).append(System.lineSeparator());
      }
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private void handleViewPerformance(Scanner scanner) throws IOException {
    out.append("Which portfolio would you like to view performance for?").append(System.lineSeparator());
    String portfolioChoice = scanner.next();
    Portfolio portfolio = getPortfolioByName(portfolioChoice);

    if (portfolio != null) {
      String startDate = promptForValidDate("Start date?", scanner);
      String endDate = promptForValidDate("End date?", scanner);

      try {
        portfolio.printPortfolioPerformanceChart(startDate, endDate, api, library);
      } catch (Exception e) {
        out.append("Error during performance view: ").append(e.getMessage()).append(System.lineSeparator());
      }
    } else {
      out.append("We could not find a portfolio with that name.").append(System.lineSeparator());
    }
  }

  private boolean portfolioExists(String portfolioName) {
    for (Portfolio portfolio : loPortfolio) {
      if (portfolio.getName().equals(portfolioName)) {
        return true;
      }
    }
    return false;
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
    if (ticker == null || ticker.isEmpty()) {
      return false;
    }
    int length = ticker.length();
    if (length < 1 || length > 5) {
      return false;
    }
    ticker = ticker.toUpperCase();
    for (char c : ticker.toCharArray()) {
      if (!Character.isLetterOrDigit(c)) {
        return false;
      }
    }
    return true;
  }

  private String getValidStockTicker(Scanner scanner) throws IOException {
    String stockChoice;
    while (true) {
      out.append("Please enter your desired stock in ticker form.").append(System.lineSeparator());
      stockChoice = scanner.next();
      if (isValidTicker(stockChoice)) {
        break;
      } else {
        out.append("Invalid stock ticker. Please enter a valid ticker.")
                .append(System.lineSeparator());
      }
    }
    return stockChoice;
  }

  private int promptForValidQuantity(String prompt, Scanner scanner) throws IOException {
    int quantity;
    while (true) {
      out.append(prompt).append(System.lineSeparator());
      if (scanner.hasNextInt()) {
        quantity = scanner.nextInt();
        if (quantity > 0) {
          break;
        } else {
          out.append("Quantity must be a positive number.").append(System.lineSeparator());
        }
      } else {
        out.append("Invalid input. Please enter a valid number.").append(System.lineSeparator());
        scanner.next();
      }
    }
    return quantity;
  }

  private int promptForValidDays(String prompt, Scanner scanner) throws IOException {
    int days;
    while (true) {
      out.append(prompt).append(System.lineSeparator());
      if (scanner.hasNextInt()) {
        days = scanner.nextInt();
        if (days > 0) {
          break;
        } else {
          out.append("Days must be a positive number.").append(System.lineSeparator());
        }
      } else {
        out.append("Invalid input. Please enter a valid number.").append(System.lineSeparator());
        scanner.next();
      }
    }
    return days;
  }

  private Portfolio getPortfolioByName(String portfolioName) {
    for (Portfolio portfolio : loPortfolio) {
      if (portfolio.getName().equals(portfolioName)) {
        return portfolio;
      }
    }
    return null;
  }

  private Map<String, Double> getWeightsFromUser(Scanner scanner) throws IOException {
    Map<String, Double> weights = new HashMap<>();
    boolean done = false;
    while (!done) {
      out.append("Please enter stock symbol and its weight (e.g., AAPL 0.25). Enter 'stop' to finish.")
              .append(System.lineSeparator());
      String input = scanner.next();
      if (input.equalsIgnoreCase("stop")) {
        done = true;
      } else {
        String symbol = input.toUpperCase();
        double weight = scanner.nextDouble();
        weights.put(symbol, weight);
      }
    }
    return weights;
  }

  private Map<String, List<Stock>> getStocksFromUser(Scanner scanner) throws IOException, ParseException {
    Map<String, List<Stock>> stocks = new HashMap<>();
    boolean done = false;
    while (!done) {
      out.append("Please enter your desired stock in ticker form. To abort at any time, enter 'stop'.")
              .append(System.lineSeparator());
      String stockChoice = scanner.next().toLowerCase();
      if (stockChoice.equals("stop")) {
        done = true;
      } else {
        int quantity = promptForValidQuantity("Please enter your desired quantity.", scanner);
        String date = promptForValidDate("Date?", scanner);
        Stock stock = new Stock(date, 0, 0, 0, 0, quantity);
        stocks.computeIfAbsent(stockChoice.toUpperCase(), k -> new ArrayList<>()).add(stock);
      }
    }
    for (String symbol : stocks.keySet()) {
      try {
        api.fetchData(symbol, library);
      } catch (Exception e) {
        stocks.remove(symbol);
        out.append("We did not add stock ").append(symbol).append(" because the ticker was invalid")
                .append(System.lineSeparator());
      }
    }
    return stocks;
  }

  public void addOriginalStocksToLibrary(String name) throws IOException {
    List<Stock> stocks = new ArrayList<>();
    Set<String> uniqueDates = new HashSet<>();
    SimpleDateFormat[] possibleFormats = new SimpleDateFormat[]{
            new SimpleDateFormat("M/d/yyyy"),
            new SimpleDateFormat("yyyy-MM-dd")
    };
    SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name + ".csv");
    if (inputStream == null) {
      throw new IOException("Resource not found: " + name + ".csv");
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    reader.readLine(); // Skip the header
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(",");
      String date = parts[0];
      double open = Double.parseDouble(parts[1]);
      double high = Double.parseDouble(parts[2]);
      double low = Double.parseDouble(parts[3]);
      double close = Double.parseDouble(parts[4]);
      int volume = Integer.parseInt(parts[5]);
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
      String formattedDate = targetFormat.format(parsedDate);
      if (!uniqueDates.contains(formattedDate)) {
        uniqueDates.add(formattedDate);
        stocks.add(new Stock(formattedDate, open, high, low, close, volume));
      }
    }
    reader.close();
    library.put(name, stocks);
  }
}
