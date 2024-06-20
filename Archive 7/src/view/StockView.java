package view;

import java.util.Scanner;

/**
 * The StockView class represents the view component of the stock application.
 * It implements the IView interface and provides methods for displaying the menu
 * and other information to the user.
 */
public class StockView implements IView {


  /**
   * Constructs a new StockView object and initializes the Scanner for user input.
   */
  public StockView() {
    Scanner scanner;
    scanner = new Scanner(System.in);
  }

  /**
   * Displays the menu options to the user.
   */
  @Override
  public void displayMenu() {
    System.out.println("1. View Gain/Loss");
    System.out.println("2. View Moving Average");
    System.out.println("3. View X-Day Crossovers");
    System.out.println("4. Create Model.Portfolio");
    System.out.println("5. Add Model.Stock to Model.Portfolio");
    System.out.println("6. View Model.Portfolio Value");
    System.out.println("7. Save a portfolio");
    System.out.println("8. Load a portfolio");
    System.out.println("9. Sell Stock ");
    System.out.println("10. View Composition");
    System.out.println("11. View Distribution");
    System.out.println("12. Rebalance Portfolio");
    System.out.println("13. View Performance");
    System.out.println("14. Exit");


  }

  /**
   * Displays the specified string to the user.
   *
   * @param s the string to be displayed
   */
  public static void display(String s) {
    System.out.println(s);
  }
}