package View;

import java.io.PrintStream;
import java.util.Scanner;

public class StockView {
  private Scanner scanner;
  public StockView() {
    scanner = new Scanner(System.in);
  }



  public void displayMenu() {
    System.out.println("1. View Gain/Loss");
    System.out.println("2. View Moving Average");
    System.out.println("3. View X-Day Crossovers");
    System.out.println("4. Create Model.Portfolio");
    System.out.println("5. Add Model.Stock to Model.Portfolio");
    System.out.println("6. View Model.Portfolio Value");
    System.out.println("7. Exit");
  }

  public static void display(String s){
    System.out.println(s);
  }

}
