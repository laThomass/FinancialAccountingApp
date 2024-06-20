package view;

import controller.StockController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * The StockViewGUI class represents the graphical user interface (GUI) for the stock application.
 * It implements the IGUIView interface and provides methods for displaying the GUI and interacting with the user.
 */
public class StockViewGUI extends JFrame implements IGUIView {
  private StockController controller;
  private JPanel mainPanel;
  private CardLayout cardLayout;

  public StockViewGUI() {
    // Set up the main frame
    setTitle("Stock Management Program");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Initialize the main panel with CardLayout
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // Add panels for each functionality
    mainPanel.add(createWelcomePanel(), "Welcome");
    mainPanel.add(createMainMenuPanel(), "MainMenu");
    mainPanel.add(createCreatePortfolioPanel(), "CreatePortfolio");
    mainPanel.add(createBuySellStockPanel(), "BuySellStock");
    mainPanel.add(createQueryPortfolioPanel(), "QueryPortfolio");
    mainPanel.add(createSaveLoadPortfolioPanel(), "SaveLoadPortfolio");

    add(mainPanel);
    cardLayout.show(mainPanel, "Welcome");
  }

  private JPanel createWelcomePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(255, 255, 204));
    JLabel welcomeLabel = new JLabel("Welcome to Stock Portfolio Management Client by Thomas and Kafig Co", SwingConstants.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
    JButton enterButton = new JButton("Enter");
    enterButton.setBackground(Color.GREEN);
    enterButton.setForeground(Color.BLACK);
    enterButton.setFont(new Font("Arial", Font.BOLD, 14));
    enterButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

    panel.add(welcomeLabel, BorderLayout.CENTER);
    panel.add(enterButton, BorderLayout.SOUTH);
    return panel;
  }

  private JPanel createMainMenuPanel() {
    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
    panel.setBackground(new Color(204, 229, 255));
    JButton createPortfolioButton = new JButton("Create Portfolio");
    JButton buySellStockButton = new JButton("Buy/Sell Stock");
    JButton queryPortfolioButton = new JButton("Query Portfolio");
    JButton saveLoadPortfolioButton = new JButton("Save/Load Portfolio");

    // Style buttons
    styleButton(createPortfolioButton);
    styleButton(buySellStockButton);
    styleButton(queryPortfolioButton);
    styleButton(saveLoadPortfolioButton);

    createPortfolioButton.addActionListener(e -> cardLayout.show(mainPanel, "CreatePortfolio"));
    buySellStockButton.addActionListener(e -> cardLayout.show(mainPanel, "BuySellStock"));
    queryPortfolioButton.addActionListener(e -> cardLayout.show(mainPanel, "QueryPortfolio"));
    saveLoadPortfolioButton.addActionListener(e -> cardLayout.show(mainPanel, "SaveLoadPortfolio"));

    panel.add(createPortfolioButton);
    panel.add(buySellStockButton);
    panel.add(queryPortfolioButton);
    panel.add(saveLoadPortfolioButton);
    return panel;
  }

  private JPanel createCreatePortfolioPanel() {
    JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
    panel.setBackground(new Color(255, 255, 204));
    JLabel nameLabel = new JLabel("Portfolio Name:");
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    JTextField nameField = new JTextField();
    JButton createButton = new JButton("Create");
    JButton backButton = new JButton("Back");

    styleButton(createButton);
    styleButton(backButton);

    createButton.addActionListener(e -> {
      String portfolioName = nameField.getText();
      if (portfolioName != null && !portfolioName.trim().isEmpty()) {
        try {
          controller.handleCreatePortfolioGUI(portfolioName, false);
          displayMessage("Portfolio created successfully.");
        } catch (IOException | ParseException ex) {
          displayMessage("Error creating portfolio: " + ex.getMessage());
        }
      } else {
        displayMessage("Please enter a valid portfolio name.");
      }
    });

    backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(createButton);
    panel.add(backButton);

    return panel;
  }

  private JPanel createBuySellStockPanel() {
    JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
    panel.setBackground(new Color(204, 255, 204));
    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    JTextField portfolioField = new JTextField();
    JLabel stockLabel = new JLabel("Stock Symbol:");
    JTextField stockField = new JTextField();
    JLabel quantityLabel = new JLabel("Quantity:");
    JTextField quantityField = new JTextField();
    JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
    JTextField dateField = new JTextField();
    JButton buyButton = new JButton("Buy");
    JButton sellButton = new JButton("Sell");
    JButton backButton = new JButton("Back");

    styleButton(buyButton);
    styleButton(sellButton);
    styleButton(backButton);

    buyButton.addActionListener(e -> {
      String portfolioName = portfolioField.getText();
      String stockSymbol = stockField.getText();
      String quantityStr = quantityField.getText();
      String date = dateField.getText();

      if (portfolioName == null || portfolioName.trim().isEmpty() ||
              stockSymbol == null || stockSymbol.trim().isEmpty() ||
              quantityStr == null || quantityStr.trim().isEmpty() ||
              date == null || date.trim().isEmpty()) {
        displayMessage("All fields must be filled.");
        return;
      }

      if (!isValidDate(date)) {
        displayMessage("Invalid date format. Please enter a date in yyyy-MM-dd format.");
        return;
      }

      if (!isPositiveInteger(quantityStr)) {
        displayMessage("Quantity must be a positive integer.");
        return;
      }

      int quantity = Integer.parseInt(quantityStr);

      try {
        controller.handleAddStockToPortfolioGUI(portfolioName, stockSymbol, quantity, date);
      } catch (Exception ex) {
        displayMessage("Error buying stock: " + ex.getMessage());
      }
    });

    sellButton.addActionListener(e -> {
      String portfolioName = portfolioField.getText();
      String stockSymbol = stockField.getText();
      String quantityStr = quantityField.getText();
      String date = dateField.getText();

      if (portfolioName == null || portfolioName.trim().isEmpty() ||
              stockSymbol == null || stockSymbol.trim().isEmpty() ||
              quantityStr == null || quantityStr.trim().isEmpty() ||
              date == null || date.trim().isEmpty()) {
        displayMessage("All fields must be filled.");
        return;
      }

      if (!isValidDate(date)) {
        displayMessage("Invalid date format. Please enter a date in yyyy-MM-dd format.");
        return;
      }

      if (!isPositiveInteger(quantityStr)) {
        displayMessage("Quantity must be a positive integer.");
        return;
      }

      int quantity = Integer.parseInt(quantityStr);

      try {
        controller.handleSellStockGUI(portfolioName, stockSymbol, quantity, date);
      } catch (Exception ex) {
        displayMessage("Error selling stock: " + ex.getMessage());
      }
    });

    backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

    panel.add(portfolioLabel);
    panel.add(portfolioField);
    panel.add(stockLabel);
    panel.add(stockField);
    panel.add(quantityLabel);
    panel.add(quantityField);
    panel.add(dateLabel);
    panel.add(dateField);
    panel.add(buyButton);
    panel.add(sellButton);
    panel.add(backButton);
    return panel;
  }

  private JPanel createQueryPortfolioPanel() {
    JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
    panel.setBackground(new Color(255, 229, 204));
    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    JTextField portfolioField = new JTextField();
    JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
    JTextField dateField = new JTextField();
    JButton valueButton = new JButton("Query Value");
    JButton compositionButton = new JButton("Query Composition");
    JButton backButton = new JButton("Back");

    styleButton(valueButton);
    styleButton(compositionButton);
    styleButton(backButton);

    valueButton.addActionListener(e -> {
      String portfolioName = portfolioField.getText();
      String date = dateField.getText();
      controller.handleViewPortfolioValueGUI(portfolioName, date);
    });

    compositionButton.addActionListener(e -> {
      String portfolioName = portfolioField.getText();
      String date = dateField.getText();
      controller.handleViewCompositionGUI(portfolioName, date);
    });

    backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

    panel.add(portfolioLabel);
    panel.add(portfolioField);
    panel.add(dateLabel);
    panel.add(dateField);
    panel.add(valueButton);
    panel.add(compositionButton);
    panel.add(backButton);
    return panel;
  }

  private JPanel createSaveLoadPortfolioPanel() {
    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
    panel.setBackground(new Color(229, 255, 204));
    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    JTextField portfolioField = new JTextField();
    JButton saveButton = new JButton("Save Portfolio");
    JButton loadButton = new JButton("Load Portfolio");
    JButton backButton = new JButton("Back");

    styleButton(saveButton);
    styleButton(loadButton);
    styleButton(backButton);

    saveButton.addActionListener(e -> {
      String portfolioName = portfolioField.getText();
      try {
        controller.handleSavePortfolioGUI(portfolioName);
      } catch (IOException ex) {
        displayMessage("Error saving portfolio: " + ex.getMessage());
      }
    });

    loadButton.addActionListener(e -> {
      String portfolioName = portfolioField.getText();
      try {
        controller.handleLoadPortfolioGUI(portfolioName);
      } catch (IOException ex) {
        displayMessage("Error loading portfolio: " + ex.getMessage());
      }
    });

    backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

    panel.add(portfolioLabel);
    panel.add(portfolioField);
    panel.add(saveButton);
    panel.add(loadButton);
    panel.add(backButton);
    return panel;
  }

  private void styleButton(JButton button) {
    button.setBackground(new Color(0, 102, 204));
    button.setForeground(Color.BLACK); // Set text color to black
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setFocusPainted(false);
    button.setPreferredSize(new Dimension(200, 50)); // Set a preferred size for the button
  }

  @Override
  public void setController(StockController controller) {
    this.controller = controller;
  }

  @Override
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void displayMenu() {
    // Method not needed for GUI
  }

  public JPanel getMainPanel() {
    return mainPanel;
  }

  private boolean isValidDate(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);
    try {
      sdf.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  private boolean isPositiveInteger(String number) {
    try {
      int value = Integer.parseInt(number);
      return value > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
