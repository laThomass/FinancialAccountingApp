package view;

/**
 * Interface representing the view component of the Stock application.
 * It defines methods for displaying menus and messages to the user.
 */
public interface IView  {
  /**
   * Displays the main menu.
   */
  public void displayMenu();

  /**
   * Displays a message to the user.
   *
   * @param message the message to be displayed
   */
  public static void display(String message) {
    System.out.println(message);
  }

  public void displayMessage(String message);
}
