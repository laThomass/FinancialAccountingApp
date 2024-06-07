//package Controller;
//
//import Model.AlphaAPI;
//import Model.Stock;
//import View.StockView;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.*;
//import java.util.*;
//
//import static org.junit.Assert.*;
//
//interface Interactio  {
//  void apply(StringBuilder in, StringBuilder out);
//}
//
//class InputInteraction implements Interaction {
//  private final String input;
//
//  public InputInteraction(String input) {
//    this.input = input;
//  }
//
//  public InputInteraction(int input) {
//    this.input = String.valueOf(input);
//  }
//
//  @Override
//  public void apply(StringBuilder in, StringBuilder out) {
//    in.append(input).append("\n");
//  }
//}
//
//class PrintInteraction implements Interaction {
//  private final String output;
//
//  public PrintInteraction(String output) {
//    this.output = output;
//  }
//
//  @Override
//  public void apply(StringBuilder in, StringBuilder out) {
//    out.append(output).append("\n");
//  }
//}
//
//
//
