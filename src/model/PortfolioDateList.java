package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioDateList implements IPortfolioDateList {
  Map<Date, Portfolio> portfolioDateMap = new HashMap<>();

  public void addPortfolio(Portfolio portfolio, Date date) {
    portfolioDateMap.put(date, portfolio);
    for(Date d : portfolioDateMap.keySet()) {
      if(d.after(date)) {
        portfolioDateMap.replace(d, portfolio);
      }
    }
  }


}
