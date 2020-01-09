package solution;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import baseclasses.Aircraft;
import baseclasses.CabinCrew;
import baseclasses.Crew;
import baseclasses.DataLoadingException;
import baseclasses.ICrewDAO;
import baseclasses.Pilot;
import baseclasses.Pilot.Rank;
import baseclasses.Aircraft.Manufacturer;

/**
 * The CrewDAO is responsible for loading data from JSON-based crew files 
 * It contains various methods to help the scheduler find the right pilots and cabin crew
 */
public class CrewDAO implements ICrewDAO {
	
	//Going to use more space by making 2 other arrays 1 for pilots and 1 for cabincrew.
	public List<Crew> crewList = new ArrayList<Crew>();
	public List<Pilot> pilotList = new ArrayList<Pilot>();
	public List<CabinCrew> cabinCrewList = new ArrayList<CabinCrew>();
	
	/**
	 * Loads the crew data from the specified file, adding them to the currently loaded crew
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadCrewData(Path p) throws DataLoadingException {
		try {
			//open the file
			String content = new String((Files.readAllBytes(p)));
			JSONObject obj = new JSONObject(content);
			//Getting the pilots data.
			JSONArray pilots = obj.getJSONArray("pilots");
			for(int i = 0;i < pilots.length();i++)
			{
				Pilot pilot = new Pilot();
				//Getting the object so we can find each pilot's attributes.
				JSONObject z = pilots.getJSONObject(i);
				pilot.setForename(z.getString("forename"));
				pilot.setHomeBase(z.getString("homebase"));
				JSONArray typecodes = z.getJSONArray("typeRatings");
				int j = 0;
				for(;j<typecodes.length();)
				{
					pilot.setQualifiedFor(typecodes.getString(j));
					j++;
				}
				String rank = z.getString("rank");
				if(rank.equals("CAPTAIN"))
					pilot.setRank(Rank.CAPTAIN);
				else
					pilot.setRank(Rank.FIRST_OFFICER);
				
				pilot.setSurname(z.getString("surname"));
				pilotList.add(pilot);
				crewList.add(pilot);
				/*System.out.println(pilot.getForename() + " " + pilot.getHomeBase() + " " + 
				pilot.getSurname() + " " + pilot.getRank() + " " + pilot.getTypeRatings());*/
			}
			//Getting the Cabincrew data.
			JSONArray crew = obj.getJSONArray("cabincrew");
			for(int i = 0;i<crew.length();i++)
			{
				CabinCrew crewMember = new CabinCrew();
				//Getting the object so we can find each crew's member attributes.
				JSONObject z = crew.getJSONObject(i);
				crewMember.setForename(z.getString("forename"));
				crewMember.setHomeBase(z.getString("homebase"));
				crewMember.setSurname(z.getString("surname"));
				JSONArray typecodes = z.getJSONArray("typeRatings");
				int j = 0;
				for(;j<typecodes.length();)
				{
					crewMember.setQualifiedFor(typecodes.getString(j));
					j++;
				}
				
				crewList.add(crewMember);
				cabinCrewList.add(crewMember);
			}
			
			
			}
		catch (IOException ioe) {
			//There was a problem reading the file
			throw new DataLoadingException(ioe);
		}
		catch (Exception e)
		{
			throw new DataLoadingException(e);
		}
		
	}
	
	/**
	 * Returns a list of all the cabin crew based at the airport with the specified airport code
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at the airport with the specified airport code
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBase(String airportCode) {
		
		List<CabinCrew> output = new ArrayList<CabinCrew>();
		
		for(CabinCrew c : cabinCrewList)
		{
			if(c.getHomeBase().equalsIgnoreCase(airportCode))
				output.add(c);
		}
		return output;
	}

	/**
	 * Returns a list of all the cabin crew based at a specific airport AND qualified to fly a specific aircraft type
	 * @param typeCode the type of plane to find cabin crew for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at a specific airport AND qualified to fly a specific aircraft type
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		List<CabinCrew> output = new ArrayList<CabinCrew>();
		
		for(CabinCrew c : cabinCrewList)
		{
			if(c.getHomeBase().equalsIgnoreCase(airportCode) && c.isQualifiedFor(typeCode))
				output.add(c);
		}
		return output;
	}

	/**
	 * Returns a list of all the cabin crew currently loaded who are qualified to fly the specified type of plane
	 * @param typeCode the type of plane to find cabin crew for
	 * @return a list of all the cabin crew currently loaded who are qualified to fly the specified type of plane
	 */
	@Override
	public List<CabinCrew> findCabinCrewByTypeRating(String typeCode) {
		List<CabinCrew> output = new ArrayList<CabinCrew>();
		
		for(CabinCrew c : cabinCrewList)
		{
			if(c.isQualifiedFor(typeCode))
				output.add(c);
		}
		return output;
	}

	/**
	 * Returns a list of all the pilots based at the airport with the specified airport code
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at the airport with the specified airport code
	 */
	@Override
	public List<Pilot> findPilotsByHomeBase(String airportCode) {
		List<Pilot> output = new ArrayList<Pilot>();
		
		for(Pilot p : pilotList)
		{
			if(p.getHomeBase().equals(airportCode))
				output.add(p);
		}
		return output;
	}

	/**
	 * Returns a list of all the pilots based at a specific airport AND qualified to fly a specific aircraft type
	 * @param typeCode the type of plane to find pilots for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at a specific airport AND qualified to fly a specific aircraft type
	 */
	@Override
	public List<Pilot> findPilotsByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		List<Pilot> output = new ArrayList<Pilot>();
		
		for(Pilot p : pilotList)
		{
			if(p.getHomeBase().equals(airportCode) && p.isQualifiedFor(typeCode))
				output.add(p);
		}
		return output;
	}

	/**
	 * Returns a list of all the pilots currently loaded who are qualified to fly the specified type of plane
	 * @param typeCode the type of plane to find pilots for
	 * @return a list of all the pilots currently loaded who are qualified to fly the specified type of plane
	 */
	@Override
	public List<Pilot> findPilotsByTypeRating(String typeCode) {
		List<Pilot> output = new ArrayList<Pilot>();
		
		for(Pilot p : pilotList)
		{
			if(p.isQualifiedFor(typeCode))
				output.add(p);
		}
		return output;
	}

	/**
	 * Returns a list of all the cabin crew currently loaded
	 * @return a list of all the cabin crew currently loaded
	 */
	@Override
	public List<CabinCrew> getAllCabinCrew() {
		return cabinCrewList;
	}

	/**
	 * Returns a list of all the crew, regardless of type
	 * @return a list of all the crew, regardless of type
	 */
	@Override
	public List<Crew> getAllCrew() {
		return crewList;
	}

	/**
	 * Returns a list of all the pilots currently loaded
	 * @return a list of all the pilots currently loaded
	 */
	@Override
	public List<Pilot> getAllPilots() {
		return pilotList;
	}

	@Override
	public int getNumberOfCabinCrew() {
		return cabinCrewList.size();
	}

	/**
	 * Returns the number of pilots currently loaded
	 * @return the number of pilots currently loaded
	 */
	@Override
	public int getNumberOfPilots() {
		return pilotList.size();
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		cabinCrewList.clear();
		crewList.clear();
		pilotList.clear();

	}

}
