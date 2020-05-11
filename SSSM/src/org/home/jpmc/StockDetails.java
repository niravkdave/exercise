package org.home.jpmc;

import java.time.LocalTime;

/**
 * @author Nirav
 * Stock Data Object to store Traded stock specific details.
 */
public class StockDetails {

	private String symbol;
	private int quantity;
	private double tradedPrice;
	private LocalTime timeStamp;
	boolean isBuy;

	public StockDetails(String symbol, int quantity, double tradedPrice, LocalTime timestamp, boolean isBuy) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.tradedPrice = tradedPrice;
		this.timeStamp = timestamp;
		this.isBuy = isBuy;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTradedPrice() {
		return tradedPrice;
	}
	public void setTradedPrice(double tradedPrice) {
		this.tradedPrice = tradedPrice;
	}
	public LocalTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public String toString() {
		return "\nSymbol: " + this.symbol + " Quantity: " + this.quantity + " Traded Price: " + this.tradedPrice 
				+ " Timestamp: " + this.timeStamp + " Indicator: " + (isBuy ? "Buy" : "Sell");
	}
	
}
