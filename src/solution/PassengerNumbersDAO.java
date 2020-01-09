package solution;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import baseclasses.DataLoadingException;
import baseclasses.FlightInfo;
import baseclasses.IPassengerNumbersDAO;

/**
 * The PassengerNumbersDAO is responsible for loading an SQLite database
 * containing forecasts of passenger numbers for flights on dates
 */
public class PassengerNumbersDAO implements IPassengerNumbersDAO {
	
	public HashMap<Integer, List<Pair<String,Integer>>> data = new HashMap<Integer, List<Pair<String,Integer>>>();
	public int counter = 0;
	/**
	 * Returns the number of passenger number entries in the cache
	 * @return the number of passenger number entries in the cache
	 */
	@Override
	public int getNumberOfEntries() {
		return counter;
	}

	/**
	 * Returns the predicted number of passengers for a given flight on a given date, or -1 if no data available
	 * @param flightNumber The flight number of the flight to check for
	 * @param date the date of the flight to check for
	 * @return the predicted number of passengers, or -1 if no data available
	 */
	@Override
	public int getPassengerNumbersFor(int flightNumber, LocalDate date) {
		if(data.containsKey(flightNumber)) {
			List<Pair<String,Integer>> listP = data.get(flightNumber);
			for(Pair p : listP)
			{
				if(p.getT1().equals(date.toString())) {
					return p.getT2();
				}
			}
		}
		return -1;
	}

	/**
	 * Loads the passenger numbers data from the specified SQLite database into a cache for future calls to getPassengerNumbersFor()
	 * Multiple calls to this method are additive, but flight numbers/dates previously cached will be overwritten
	 * The cache can be reset by calling reset() 
	 * @param p The path of the SQLite database to load data from
	 * @throws DataLoadingException If there is a problem loading from the database
	 */
	@Override
	public void loadPassengerNumbersData(Path p) throws DataLoadingException {
		String uri = p.toString();
		Connection conn = null;
		Statement smt = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection("jdbc:sqlite:" + uri);
            System.out.println("Connection to SQLite has been established.");
            conn.setAutoCommit(false);
            smt = conn.createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM PASSENGERNUMBERS");
            while(rs.next()){
            	int flightNumber = rs.getInt("flightnumber");
            	int passengers = rs.getInt("passengers");
            	//x.setFlightNumber(flightNumber);
            	String date = rs.getString("date");
            	Pair tuple = new Pair(date,passengers);
            	List<Pair<String,Integer>> listP = new ArrayList<Pair<String,Integer>>();
            	//x.setD(date);
            	//System.out.println(flightNumber + "   " + date + "   " + passengers);
            	if(data.containsKey(flightNumber))
            	{
            		List<Pair<String,Integer>> values = data.get(flightNumber);
            		values.add(tuple);
            		data.replace(flightNumber, values);
            	}
            	else {
            		listP.add(tuple);
            		data.put(flightNumber, listP);
            	}
            	counter++;
            }
            conn.close();
        } catch(Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        	throw new DataLoadingException(e);
        }
	}

	/**
	 * Removes all data from the DAO, ready to start again if needed
	 */
	@Override
	public void reset() {
		data.clear();
		counter = 0;
	}

}
