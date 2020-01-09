package solution;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import baseclasses.Aircraft;
import baseclasses.CabinCrew;
import baseclasses.DoubleBookedException;
import baseclasses.FlightInfo;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.InvalidAllocationException;
import baseclasses.Pilot;
import baseclasses.Route;
import baseclasses.Schedule;
import baseclasses.SchedulerRunner;

public class Scheduler implements IScheduler {

	@Override
	public Schedule generateSchedule(IAircraftDAO arg0, ICrewDAO arg1, IRouteDAO arg2, IPassengerNumbersDAO arg3,
			LocalDate arg4, LocalDate arg5) {
		RouteDAO routes = (RouteDAO) arg2;
		AircraftDAO aircraft = (AircraftDAO) arg0;
		CrewDAO crew = (CrewDAO) arg1;
		PassengerNumbersDAO passengers = (PassengerNumbersDAO) arg3;
		Schedule s = new Schedule(routes,arg4,arg5);
		List<FlightInfo> flights = s.getRemainingAllocations();
		System.out.println(flights.size());
		int t = 0;
		for(int i= 0; i < flights.size();i++) {
			Route r = flights.get(i).getFlight();
			String arrivalAirport = r.getArrivalAirportCode();
			String depAirport = r.getDepartureAirportCode();
			int numberOfPassengers = passengers.getPassengerNumbersFor(r.getFlightNumber(), 
					flights.get(i).getDepartureDateTime().toLocalDate());
			List<Aircraft> ar = aircraft.findAircraftByStartingPosition(depAirport);
			
			for(Aircraft a : ar)
			{
				//System.out.println()
				if(!s.hasConflict(a, flights.get(i)) && a.getSeats()>= numberOfPassengers){
					try {
            s.allocateAircraftTo(a, flights.get(i));
            break;
          } catch (DoubleBookedException e) {
            e.printStackTrace();
          }
				} else if(!s.hasConflict(a, flights.get(i)) && a.getSeats()<= numberOfPassengers) {
					try {
            s.allocateAircraftTo(a, flights.get(i));
            break;
          } catch (DoubleBookedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
				}
			}
			if(s.getAircraftFor(flights.get(i)) == null) {
				List<Aircraft> br = aircraft.getAllAircraft();
				for(Aircraft a : br) {
					//System.out.println()
					if(!s.hasConflict(a, flights.get(i)) && a.getSeats()>= numberOfPassengers && a.getStartingPosition().equals(depAirport)){
						try {
	            s.allocateAircraftTo(a, flights.get(i));
	            break;
	          } catch (DoubleBookedException e) {
	            e.printStackTrace();
	          }
					} else if(!s.hasConflict(a, flights.get(i))) {
						try {
	            s.allocateAircraftTo(a, flights.get(i));
	            break;
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}
			}
			System.out.println(s.getAircraftFor(flights.get(i)));
			List<Pilot> p = crew.findPilotsByHomeBaseAndTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode(), depAirport);
			for(Pilot x : p)
			{
				if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("CAPTAIN")) {
					try {
            s.allocateCaptainTo(x, flights.get(i));
          } catch (DoubleBookedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
				}else {
					if(!s.hasConflict(x, flights.get(i))) {
						try {
              s.allocateFirstOfficerTo(x, flights.get(i));
              break;
            } catch (DoubleBookedException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            }
					}
				}
				if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("FIRST_OFFICER")) {
					try {
            s.allocateFirstOfficerTo(x, flights.get(i));
            break;
          } catch (DoubleBookedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
				}else {
					if(!s.hasConflict(x, flights.get(i))) {
						try {
				              s.allocateCaptainTo(x, flights.get(i));
				              break;
				            } catch (DoubleBookedException e) {
				              // TODO Auto-generated catch block
				            e.printStackTrace();
				            }
					}
				}
			}
			System.out.println(s.getCaptainOf(flights.get(i)));
			System.out.println(s.getFirstOfficerOf(flights.get(i)));
			if(s.getCaptainOf(flights.get(i)) == null) {
				List<Pilot> all = crew.getAllPilots();
				for(Pilot p1 : all) {
					if(!s.hasConflict(p1, flights.get(i))) {
						try {
              s.allocateCaptainTo(p1, flights.get(i));
              break;
            } catch (DoubleBookedException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            }
					}
				}
			}
		}
		return s;
	}

	@Override
	public void setSchedulerRunner(SchedulerRunner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
