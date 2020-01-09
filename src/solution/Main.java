package solution;

import java.nio.file.Paths;
import java.time.LocalDate;

import baseclasses.DataLoadingException;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;

/**
 * This class allows you to run the code in your classes yourself, for testing and development
 */
public class Main {

	public static void main(String[] args) {	
		IAircraftDAO aircraft = new AircraftDAO();
		ICrewDAO crew = new CrewDAO();
		IRouteDAO route = new RouteDAO();
		IPassengerNumbersDAO passengers = new PassengerNumbersDAO();
		
		try {
			aircraft.loadAircraftData(Paths.get("./data/aircraft.csv"));
			//aircraft.loadAircraftData(Paths.get("./data/mini_aircraft.csv"));
			//aircraft.loadAircraftData(Paths.get("./data/malformed_aircraft1.csv"));
			//System.out.println(aircraft.findAircraftBySeats(329));
			//System.out.println(aircraft.findAircraftByTailCode("G-AAAC"));
			crew.loadCrewData(Paths.get("./data/crew.json"	));
			route.loadRouteData(Paths.get("./data/routes.xml"));
			
			//passengers.loadPassengerNumbersData(Paths.get("./data/passengernumbers.db"));
			//passengers.loadPassengerNumbersData(Paths.get("./data/mini_passengers.db"));
			passengers.loadPassengerNumbersData(Paths.get("./data/passengernumbers.db"));
			
			LocalDate x = LocalDate.now();
			/*System.out.println(x.getDayOfWeek().toString());
			System.out.println(route.findRoutesbyDate(x));*/
			LocalDate x2 = LocalDate.parse("2020-08-02");
			LocalDate x1 = LocalDate.parse("2020-08-01");
			Scheduler s = new Scheduler();
			System.out.println(s.generateSchedule(aircraft, crew, route, passengers, x1, x2));
			//System.out.println(passengers.getNumberOfEntries());
			//System.out.println(route.findRoutesbyDate(x1).size());
			//System.out.println(passengers.getPassengerNumbersFor(618, x1));
			
		}
		catch (DataLoadingException dle) {
			System.err.println("Error loading aircraft data");
			dle.printStackTrace();
		}
	}

}
