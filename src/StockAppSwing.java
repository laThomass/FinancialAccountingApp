//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.text.ParseException;
//
//public class StockAppSwing {
//  private JFrame frame;
//  private Controller.StockController stockController;
//
//  public static void main(String[] args) {
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        try {
//          StockAppSwing window = new StockAppSwing();
//          window.frame.setVisible(true);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    });
//  }
//
//  public StockAppSwing() {
//    stockController = new Controller.StockController(new View.StockView());
//    initialize();
//  }
//
//  private void initialize() {
//    frame = new JFrame();
//    frame.setTitle("Model.Stock App");
//    frame.setBounds(100, 100, 450, 300);
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    frame.getContentPane().setLayout(null);
//
//    JButton btnViewGainLoss = new JButton("1. View Gain/Loss");
//    btnViewGainLoss.setBounds(140, 30, 200, 25);
//    frame.getContentPane().add(btnViewGainLoss);
//    btnViewGainLoss.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        showViewGainLossDialog();
//      }
//    });
//
//    JButton btnViewMovingAverage = new JButton("2. View Moving Average");
//    btnViewMovingAverage.setBounds(140, 60, 200, 25);
//    frame.getContentPane().add(btnViewMovingAverage);
//
//    JButton btnViewXDayCrossovers = new JButton("3. View X-Day Crossovers");
//    btnViewXDayCrossovers.setBounds(140, 90, 200, 25);
//    frame.getContentPane().add(btnViewXDayCrossovers);
//
//    JButton btnCreatePortfolio = new JButton("4. Create Model.Portfolio");
//    btnCreatePortfolio.setBounds(140, 120, 200, 25);
//    frame.getContentPane().add(btnCreatePortfolio);
//
//    JButton btnAddStockToPortfolio = new JButton("5. Add Model.Stock to Model.Portfolio");
//    btnAddStockToPortfolio.setBounds(140, 150, 200, 25);
//    frame.getContentPane().add(btnAddStockToPortfolio);
//
//    JButton btnViewPortfolioValue = new JButton("6. View Model.Portfolio Value");
//    btnViewPortfolioValue.setBounds(140, 180, 200, 25);
//    frame.getContentPane().add(btnViewPortfolioValue);
//
//    JButton btnExit = new JButton("7. Exit");
//    btnExit.setBounds(140, 210, 200, 25);
//    frame.getContentPane().add(btnExit);
//    btnExit.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        System.exit(0);
//      }
//    });
//  }
//
//  private void showViewGainLossDialog() {
//    JDialog dialog = new JDialog(frame, "View Gain/Loss", true);
//    dialog.setSize(400, 300);
//    dialog.setLayout(null);
//
//    JLabel lblStockTicker = new JLabel("Model.Stock Ticker:");
//    lblStockTicker.setBounds(10, 30, 120, 25);
//    dialog.add(lblStockTicker);
//
//    JTextField stockTickerField = new JTextField();
//    stockTickerField.setBounds(140, 30, 200, 25);
//    dialog.add(stockTickerField);
//
//    JLabel lblStartDate = new JLabel("Start Date (yyyy-MM-dd):");
//    lblStartDate.setBounds(10, 70, 200, 25);
//    dialog.add(lblStartDate);
//
//    JTextField startDateField = new JTextField();
//    startDateField.setBounds(140, 70, 200, 25);
//    dialog.add(startDateField);
//
//    JLabel lblEndDate = new JLabel("End Date (yyyy-MM-dd):");
//    lblEndDate.setBounds(10, 110, 200, 25);
//    dialog.add(lblEndDate);
//
//    JTextField endDateField = new JTextField();
//    endDateField.setBounds(140, 110, 200, 25);
//    dialog.add(endDateField);
//
//    JButton btnCalculate = new JButton("Calculate");
//    btnCalculate.setBounds(140, 150, 200, 25);
//    dialog.add(btnCalculate);
//
//    JLabel resultLabel = new JLabel("");
//    resultLabel.setBounds(140, 190, 200, 25);
//    dialog.add(resultLabel);
//
//    btnCalculate.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        try {
//          String stockTicker = stockTickerField.getText();
//          String startDate = startDateField.getText();
//          String endDate = endDateField.getText();
//
//          double gainLoss = stockController.viewGainLoss(stockTicker, startDate, endDate);
//          resultLabel.setText(String.format("Gain/Loss: %.2f%%", gainLoss));
//        } catch (IOException ex) {
//          resultLabel.setText("Error: " + ex.getMessage());
//          ex.printStackTrace();
//        }
//      }
//    });
//
//    dialog.setVisible(true);
//  }
//}
