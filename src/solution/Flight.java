package solution;

import java.util.Date;

public class Flight {
	// A wrapper class to help with datastructure used in PassengarNumberDAO
	public int flightNumber;
	public String d;
	public Flight(int flightNumber, String d)
	{
		this.flightNumber = flightNumber;
		this.d = d;
	}
	
	public Flight() {
		
		
	}
	
  public int getFlightNumber() {
  return flightNumber;
  }
  
  public void setFlightNumber(int flightNumber) {
  this.flightNumber = flightNumber;
  }
  
  public String getDdate() {
  return d;}
  
  public void setD(String d) {
  this.d = d;
  }
}
