package org.home.jpmc;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Main class to start the Super Simple Stock Market application. 
 */
public class StockMarket {

	private Map<String, List<StockDetails>> portfolio = new HashMap<String, List<StockDetails>>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StockMarket stockMarket = new StockMarket();
		try {
			stockMarket.run();
		} catch (Exception e) {
			System.out.println("Error occurred. Exiting system. " + e);
		}
	}
	
	public void run() throws Exception {
		StockExchange stock = new StockExchange();
		Scanner keys = new Scanner(System.in);	
		int num;
	
	    System.out.println("\n************************ STOCK EXCHANGE - GBCE ******************************");
	    System.out.println("\n"+stock.showOptions());
		System.out.println("\nEnter an option from above: ");
		
		while((num = readInteger(keys)) > 0) {
			Scanner stockScan = new Scanner(System.in);
			
			if(num == 1) {
				print("Available Stocks in exchange: \n" + stock.displayStocks());
			}
			
			else if(num == 2) {
				print("Details of Available Stocks: \n" + stock.displayAllStockDetails().toString().replaceAll(",", "\n"));
			}
			
			else if(num == 3) {
				stockDetailOptions(stock, stockScan);
			} 
			
			else if(num == 4) {
				calculateGeometricMean();
			} 
			
			else if(num ==5) {
				print("Your Portfolio: " + (portfolio.size() < 1 ? "Empty. No stocks bought yet." : portfolio.toString()));
			} 
			
			else {
				System.out.println("Invalid option entered");
			}
			
			Thread.sleep(1000);
			System.out.println("\n"+stock.showOptions());
			System.out.println("\nEnter an option from above: ");
			
		} 
		{
				System.out.println("Exiting from System...");
		}
	
	}
	
	/*
	 * All options and details captured and calculated for a given stock, with dividend, PE Ratio, buy, sell and weighted stock price
	 */
	private void stockDetailOptions(StockExchange stock, Scanner stockScan) {

		System.out.println("Enter Stock Name:");
		String stockName = readStr(stockScan).toUpperCase();
		
		if(!stock.stockExists(stockName)) {
			print("Entered stock " + stockName + " is invalid and does not exists in the Exchange");
		} else {
		
			System.out.println(stock.showStockOptions());
			System.out.println("Enter Options from above for selected stock " + stockName +": ");
			int stockOption = readInteger(stockScan);
			
			// Dividend Yield for a input stock price
			if(stockOption == 1) {
				System.out.println("Enter stock price: ");
				double price = readDouble(stockScan);
				if(price <= 0 ) {
					print("Price can't be 0 or less");
				} else {
					print("Dividend Yield for stocks " +stockName +" : " + calculateDividend(stock, stockName, price));
				}
				
			// PE Ratio for a given stock and price	
			} else if(stockOption == 2) {
				System.out.println("Enter stock price: ");
				double price = readDouble(stockScan);
				if(price <= 0 ) {
					print("Price can't be 0 or less");
				} else {
					print("P/E Ratio for stock " +stockName +" : " + calculatePERatio(stock, stockName, price));
				}
		
			//Buy or sell stock and add or remove those from the portfolio with validations
			} else if(stockOption == 3) {
				System.out.println("Enter Buy(B) / Sell(S): ");
				String indicator = readStr(stockScan).toUpperCase();
				indicator = readStr(stockScan).toUpperCase();
				
				if(indicator.equals("B") || indicator.equals("BUY") ) {
					System.out.println("Enter total number of quantity to buy: ");
					int quantity = readInteger(stockScan);
					StockDetails stockBuy = buyStock(stockName, quantity, stock.lookupStockPrice(stockName));
					print("Stock buy successful with below details" + stockBuy.toString());
					
				} else if(indicator.equals("S") || indicator.equals("SELL") ) {
					System.out.println("Enter total number of quantity to sell: ");
					int quantity = readInteger(stockScan);
					sellStock(stockName, quantity, stock.lookupStockPrice(stockName));
					
				} else {
					print("Invalid Option entered");
				}
			} else if(stockOption == 4) {
				calculateVolumeWeightedPrice(stockName, 15);
			}				
		} // if else stock exists
		
	}
	
	/*
	 * Function to calculate geometric mean based on prices of all the stocks available in the portfolio.
	 */
	private void calculateGeometricMean() {
		List<StockDetails> currentShares = null;
		double product = 1;
		double mean = 0;
		int counter = 0;
		for(String stockName: portfolio.keySet()) {
			currentShares = portfolio.get(stockName);
			
			for(StockDetails sk : currentShares) {
				product *= sk.getTradedPrice();
				counter++;
			}
		}
		mean = Math.pow(product, 1.0 / counter);
		if(counter == 0) 
			print("Geometric Mean: No stocks in the portfolio to calculate the mean.");
		else 
			print("Geometric Mean: " + mean);
	}
	
	/*
	 * Calculate volume weighted stock price for a given stock name, and it is based on the stocks which are traded in last number of minutes passed.
	 */
	private void calculateVolumeWeightedPrice(String stockName, int mins)
	{
		List<StockDetails> currentShares = portfolio.get(stockName);
		double volumeWeighPrice = 0;
		int totalTradedQuantity = 0;
		double totalTradedPrice = 0;
		
		if(currentShares == null || currentShares.isEmpty()) {
			print("Can't calculate volume weighted price since there are no stocks available in the portfolio for " + stockName);
			return;
		}
		
		for(StockDetails sk : currentShares) {
			if(sk.getTimeStamp().isAfter(LocalTime.now().minusMinutes(mins))) {
				totalTradedQuantity += sk.getQuantity();
				totalTradedPrice += sk.getTradedPrice();	
			}	
		}
		
		if(totalTradedQuantity == 0) {
			print("No stocks traded in last " + mins + " minutes.");
			return;
			
		} else {
			volumeWeighPrice = (totalTradedPrice * totalTradedQuantity ) / totalTradedQuantity;
			print("Volume Weighted Stock Price for " + stockName + " is: " + volumeWeighPrice );
		}
		
	}
	
	/*
	 * Function to calculate the dividend yield for a given stock name and price. Calculation differs based on the type of the stock, i.e. common / preferred
	 */
	public double calculateDividend(StockExchange stockEx, String stockName, double price) {
		StockExchangeData stock = stockEx.getStockData().get(stockName);
		double dividend = 0;
		if(stock != null) {
			if(stock.getType() == "Common") {
				dividend = stock.getLastDividend() / price;
			} else {
				dividend = stock.getFixedDividend() * stock.getParValue() / price;
			}
		}
		return dividend;
	}
	
	/*
	 * Calculates the PE ratio for the given stock and price.
	 */
	public double calculatePERatio(StockExchange stockEx, String stockName, double price) {
		StockExchangeData stock = stockEx.getStockData().get(stockName);
		double peRatio = 0;
		if(stock != null) {
			peRatio = price / stock.getLastDividend();
		}
		if(peRatio == Double.POSITIVE_INFINITY) {
			System.out.println("Infinity error...returning 0");
			return 0;
		}
		return peRatio;
	}
	
	/*
	 * Function called when a stock is bought based on quantity and price. If no shares exists in the portfolio, then create the object and store in the portfolio.
	 */
	private StockDetails buyStock(String stock, int quantity, double price)
	{
    // get the current number of shares owned
		List currentShares = portfolio.get(stock);
		StockDetails newStock = new StockDetails(stock, quantity, price, LocalTime.now(),true);
		
	    // if none owned, add an entry to the porfolio
	    if( currentShares == null ) {	
	    	currentShares = new ArrayList<StockDetails>();
	    	currentShares.add(newStock);
	    	portfolio.put(stock, currentShares);
	    	return newStock;
	    }   
	    currentShares.add(newStock);
	    return newStock;
	}
	 
	/*
	 * Function called when a stock is sold based on quantity and price. If no shares exists in the portfolio or less quantity entered, 
	 * then checks in the portfolio and does'nt trade and shows the message accordingly else adjusts the portfolio to update the stock details.
	 */
	private StockDetails sellStock(String stock, int quantity, double price)
	{
		
		List<StockDetails> currentShares = portfolio.get(stock);
		int checkExistingQuantity = 0;
		
		if(currentShares == null || currentShares.isEmpty()) {
			print("Can't sell stock as no stock exists in the portfolio for " + stock);
			return null;
		}
		
		for(StockDetails sk : currentShares) {
			if(sk.isBuy) 
				checkExistingQuantity += sk.getQuantity();
			else 
				checkExistingQuantity -= sk.getQuantity();
		}
		
		if(checkExistingQuantity < quantity) {
			print("Quantity entered to sell is more than the shares owned. \nEnter quantity less than shares owned which is " + checkExistingQuantity);
			return null;
		}
		StockDetails newStock = new StockDetails(stock, quantity, price, LocalTime.now(), false);
		
	    // if none owned, add an entry to the portfolio
	    currentShares.add(newStock);
	    return newStock;
	}
	
	public int readInteger(Scanner in) {
        int integer = 0;
            try
                {
                    integer = in.nextInt();
                }
                catch(Exception e)
                {
                    System.out.printf("Only numbers are allowed. Enter options from above.");
                    in.nextLine();
                    integer = readInteger(in);
                }
        return integer;
    }
	
	public double readDouble(Scanner in) {
        double value = 0;
            try
                {
                    value = in.nextDouble();
                }
                catch(Exception e)
                {
                    System.out.printf("only int/double values are allowed");
                    in.nextLine();
                    value = readDouble(in);
                }
        return value;
    }
	
	public String readStr(Scanner in) {
        String str;
            try {
            		str = in.nextLine();
                }
                catch(Exception e) {
                    System.out.printf("only strings are allowed");
                    in.nextLine();
                    str = readStr(in);
                }
        return str;
    }
	
	private void print(String str) {
		System.out.println("****************************** O/P ******************************");
		System.out.println(str);
		System.out.println("****************************** END ******************************");
	}
	
}
