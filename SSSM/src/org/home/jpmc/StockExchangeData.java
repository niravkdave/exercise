package org.home.jpmc;

/**
 * @author Nirav
 * Stock Exchange Data Object to hold all Exchange stock specific default data.
 */
public class StockExchangeData {
	
	private String symbol;
	private String type;
	private int lastDividend;
	private int fixedDividend;
	private double parValue;
	
	public StockExchangeData(String symbol, String type, int lastDividend, int fixedDividend, double parValue) {
		super();
		this.symbol = symbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLastDividend() {
		return lastDividend;
	}
	public void setLastDividend(int lastDividend) {
		this.lastDividend = lastDividend;
	}
	public int getFixedDividend() {
		return fixedDividend;
	}
	public void setFixedDividend(int fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
	public double getParValue() {
		return parValue;
	}
	public void setParValue(double parValue) {
		this.parValue = parValue;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Symbol: " + this.symbol + " Type: " + this.type + " Last Dividend: " + this.lastDividend 
				+ " Fixed Dividend: " + this.fixedDividend + " Par Value: " + this.parValue;
	}
	
	
}
