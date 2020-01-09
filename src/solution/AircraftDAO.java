package solution;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import baseclasses.Aircraft;
import baseclasses.Aircraft.Manufacturer;
import baseclasses.DataLoadingException;
import baseclasses.IAircraftDAO;


/**
 * The AircraftDAO class is responsible for loading aircraft data from CSV files
 * and contains methods to help the system find aircraft when scheduling
 */
public class AircraftDAO implements IAircraftDAO {
	
	public List<Aircraft> airCrafts = new ArrayList<Aircraft>();
	
	/**
	 * Loads the aircraft data from the specified file, adding them to the currently loaded aircraft
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
     *
	 * Initially, this contains some starter code to help you get started in reading the CSV file...
	 */
	@Override
	public void loadAircraftData(Path p) throws DataLoadingException {	
		try {
			//open the file
			BufferedReader reader = Files.newBufferedReader(p);
			
			//read the file line by line
			String line = "";
			
			//skip the first line of the file - headers
			reader.readLine();
			
			while( (line = reader.readLine()) != null) {
				//each line has fields separated by commas, split into an array of fields
				String[] fields = line.split(",");
				
				
				//Creating an Aircraft object
				Aircraft loadingIn = new Aircraft();
				
				//put some of the fields into variables: check which fields are where atop the CSV file itself
				loadingIn.setTailCode(fields[0]);
				loadingIn.setTypeCode(fields[1]);
				//Checking the manufacturer and setting it
				Manufacturer manufacturer = null;
				String mString = fields[2];
				if(mString.equals("Boeing"))
					manufacturer = Manufacturer.BOEING;
				else if(mString.equals("Airbus"))
					manufacturer = Manufacturer.AIRBUS;
				else if(mString.equals("Bombardier"))
					manufacturer = Manufacturer.BOMBARDIER;
				else if(mString.equals("Embraer"))
					manufacturer = Manufacturer.EMBRAER;
				else if(mString.equals("Fokker"))
					manufacturer = Manufacturer.FOKKER;
				else
					manufacturer = Manufacturer.ATR;
                loadingIn.setManufacturer(manufacturer);
				loadingIn.setModel(fields[3]);
				loadingIn.setSeats(Integer.parseInt(fields[4]));
				loadingIn.setCabinCrewRequired(Integer.parseInt(fields[5]));
				loadingIn.setStartingPosition(fields[6]);
				airCrafts.add(loadingIn);
				
				//print a line explaining what we've found
				/*System.out.println("Tail Code: " + loadingIn.getTailCode() + " TypeCode: " + loadingIn.getTailCode() +
						" Manufacturer: " + loadingIn.getManufacturer() + " Model: " + loadingIn.getModel() +
						" Seats and CabinCrew : " + loadingIn.getSeats() + "   " + loadingIn.getCabinCrewRequired()
						+ " Starting Pos. " + loadingIn.getStartingPosition());*/
			}
		}
		
		catch (IOException ioe) {
			//There was a problem reading the file
			throw new DataLoadingException(ioe);
		}
		catch (NumberFormatException e)
		{
			System.err.println("Error loading aircraft data");
			DataLoadingException dle = new DataLoadingException();
			throw dle;
		}

	}
	
	/**
	 * Returns a list of all the loaded Aircraft with at least the specified number of seats
	 * @param seats the number of seats required
	 * @return a List of all the loaded aircraft with at least this many seats
	 */
	@Override
	public List<Aircraft> findAircraftBySeats(int seats) {
		/*
		 * We will need to read through our arrayList and return only 
		 * the Aircrafts with the specified seats number.
		 * Initializing a new array for output.
		 */
		
		List<Aircraft> output = new ArrayList<Aircraft>();
		
		for(Aircraft x : airCrafts)
		{
			if(x.getSeats() >= seats)
				output.add(x);
		}
		return output;
	}

	/**
	 * Returns a list of all the loaded Aircraft that start at the specified airport code
	 * @param startingPosition the three letter airport code of the airport at which the desired aircraft start
	 * @return a List of all the loaded aircraft that start at the specified airport
	 */
	@Override
	public List<Aircraft> findAircraftByStartingPosition(String startingPosition) {
		/*
		 * We will need to read through our arrayList and return only 
		 * the Aircrafts with the specified starting pos.
		 * Initializing a new array for output.
		 */
		
		List<Aircraft> output = new ArrayList<Aircraft>();
		
		for(Aircraft x : airCrafts)
		{
			if(x.getStartingPosition().equals(startingPosition))
				output.add(x);
		}
		return output;
	}

	/**
	 * Returns the individual Aircraft with the specified tail code.
	 * @param tailCode the tail code for which to search
	 * @return the aircraft with that tail code, or null if not found
	 */
	@Override
	public Aircraft findAircraftByTailCode(String tailCode) {
		/*
		 * We will need to read through our arrayList and return only 
		 * the Aircraft with the specified tailcode.
		 */
		for(Aircraft x : airCrafts)
		{
			if(x.getTailCode().equals(tailCode))
				return x;
		}
		return null;	
	}

	/**
	 * Returns a List of all the loaded Aircraft with the specified type code
	 * @param typeCode the type code of the aircraft you wish to find
	 * @return a List of all the loaded Aircraft with the specified type code
	 */
	@Override
	public List<Aircraft> findAircraftByType(String typeCode) {
		/*
		 * We will need to read through our arrayList and return only 
		 * the Aircrafts with the specified starting pos.
		 * Initializing a new array for output.
		 */
		
		List<Aircraft> output = new ArrayList<Aircraft>();
		for(Aircraft x : airCrafts)
		{
			if(x.getTypeCode().equals(typeCode))
				output.add(x);
		}
		return output;
	}

	/**
	 * Returns a List of all the currently loaded aircraft
	 * @return a List of all the currently loaded aircraft
	 */
	@Override
	public List<Aircraft> getAllAircraft() {
		return airCrafts;
	}

	/**
	 * Returns the number of aircraft currently loaded 
	 * @return the number of aircraft currently loaded
	 */
	@Override
	public int getNumberOfAircraft() {
		return airCrafts.size();
	}

	/**
	 * Unloads all of the aircraft currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		airCrafts.clear();
	}

}
