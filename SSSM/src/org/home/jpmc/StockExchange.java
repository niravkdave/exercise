package org.home.jpmc;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Nirav
 * Stock Exchange class to Object to load default stocks, show available options, fetch the stock price and display all stock details.
 */
public class StockExchange {

	private Map<String, StockExchangeData> stocksMarket = null;
	
	public StockExchange() {
		loadStockExchange();
	}
	
	/**
	 * Load all default stocks primarily available in the exchange
	 */
	private void loadStockExchange() {
		stocksMarket = new HashMap<String, StockExchangeData>();
		loadStock("TEA", "Common", 0, 0, 100);
		loadStock("POP", "Common", 8, 0, 100);
		loadStock("ALE", "Common", 23, 0, 60);
		loadStock("GIN", "Preferred", 8, 2, 100);
		loadStock("JOE", "Common", 13, 0, 250);
	}

	/*
	 * Fetch the stock price for a given stock. 
	 * Just to make it a bit dynamic, have added rand function to add a random number from 1 to 10 to the stock price.
	 */
	public double lookupStockPrice(String stock)
	{
		Random rand = new Random();	
		StockExchangeData st = stocksMarket.get(stock);
		if( st == null )
	      throw new NoSuchElementException();
	    
	    double value = st.getParValue() + rand.nextInt(10);
	    return value;
	  }

	  /** 
	   * Load the stock based on input parameters and store in the exchange. This method can also be called to load the stocks in the exchange from the UI.
	   * @param symbol
	   * @param type
	   * @param lastDividend
	   * @param fixedDividend
	   * @param parValue
	   */
	private void loadStock(String symbol, String type, int lastDividend, int fixedDividend, int parValue) {
		stocksMarket.put(symbol, new StockExchangeData(symbol, type, lastDividend, fixedDividend, parValue));
	}
	
	public Map<String, StockExchangeData> getStockData() {
		return stocksMarket;
	}
	
	public String displayAllStockDetails() {
		return stocksMarket.toString();
	}
	
	public String displayStocks() {
		return stocksMarket.keySet().toString();
	}
	
	public boolean stockExists(String stockName) {
		return stocksMarket.get(stockName) != null;
	}
	
	public String showOptions() {
		return
				"1. Display available Stock Names in the Exchange\n"
				+ "2. Display Details of all available Stocks in Exchange\n"
				+ "3. Select a Stock for more options [eg. Dividend, PE Ratio, Buy/Sell Stock, Volume Weighted Stock Price]\n"
				+ "4. Calculate GBCE All share Index - Geometric Mean of prices for all stocks\n" 
				+ "5. Display your current Portfolio";
	}
	
	public String showStockOptions() {
		return "1. Given any price as input, calculate the Dividend Yield\n"
				+ "2. Given any price as input, calculate the P/E Ratio\n"
				+ "3. Record a trade, with timestamp, quantity of shares, buy or sell indicator and traded price\n"
				+ "4. Calculate Volume Weighted Stock Price based on trades in past 15 minutes\n";
	}
	
}
