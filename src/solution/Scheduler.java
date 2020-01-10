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
		Schedule y = new Schedule(routes,arg4,arg5);
		List<FlightInfo> flights = s.getRemainingAllocations();
		//System.out.println(flights.size());
		/*int t = 0;
		System.out.println(flights.get(123).getFlight().getDepartureAirportCode());*/
		/*List<Aircraft> what = aircraft.findAircraftByStartingPosition(flights.get(123).getFlight().getDepartureAirportCode());
		int nP = passengers.getPassengerNumbersFor(flights.get(123).getFlight().getFlightNumber(), 
				flights.get(123).getDepartureDateTime().toLocalDate());
		for(int z = 0; z < what.size();z++) {
			System.out.println(what.get(z).getSeats() + "  " + nP);
		}*/
		for(int i= 0; i < flights.size();i++) {
			Route r = flights.get(i).getFlight();
			String arrivalAirport = r.getArrivalAirportCode();
			String depAirport = r.getDepartureAirportCode();
			int numberOfPassengers = passengers.getPassengerNumbersFor(r.getFlightNumber(), 
					flights.get(i).getDepartureDateTime().toLocalDate());
			List<Aircraft> ar = aircraft.findAircraftByStartingPosition(depAirport);
			List<Aircraft> ab = aircraft.findAircraftByStartingPosition(arrivalAirport);
			List<Aircraft> aAll = aircraft.getAllAircraft();
			//System.out.println(ar.size());
			for(Aircraft a : ar)
			{
				if(s.getAircraftFor(flights.get(i)) == null) {
					//System.out.println()
					if(!s.hasConflict(a, flights.get(i)) && a.getSeats() >= numberOfPassengers){
						try {
							//System.out.println(a.getStartingPosition() + "  " + a.getSeats() + "  " + a.getTypeCode());
	            s.allocateAircraftTo(a, flights.get(i));
	          } catch (DoubleBookedException e) {
	            e.printStackTrace();
	          }
					} else if(!s.hasConflict(a, flights.get(i)) && a.getSeats()<= numberOfPassengers) {
						try {
	            s.allocateAircraftTo(a, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else
					break;
				
			}
			
			for(Aircraft a : ab)
			{
				if(s.getAircraftFor(flights.get(i)) == null) {
					//System.out.println()
					if(!s.hasConflict(a, flights.get(i)) && a.getSeats() >= numberOfPassengers){
						try {
							//System.out.println(a.getStartingPosition() + "  " + a.getSeats() + "  " + a.getTypeCode());
	            s.allocateAircraftTo(a, flights.get(i));
	          } catch (DoubleBookedException e) {
	            e.printStackTrace();
	          }
					} else if(!s.hasConflict(a, flights.get(i)) && a.getSeats()<= numberOfPassengers) {
						try {
	            s.allocateAircraftTo(a, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else
					break;
				
			}
			
			for(Aircraft a : aAll)
			{
				if(s.getAircraftFor(flights.get(i)) == null) {
					//System.out.println()
					if(!s.hasConflict(a, flights.get(i)) && a.getSeats() >= numberOfPassengers){
						try {
							//System.out.println(a.getStartingPosition() + "  " + a.getSeats() + "  " + a.getTypeCode());
	            s.allocateAircraftTo(a, flights.get(i));
	          } catch (DoubleBookedException e) {
	            e.printStackTrace();
	          }
					} else if(!s.hasConflict(a, flights.get(i)) && a.getSeats()<= numberOfPassengers) {
						try {
	            s.allocateAircraftTo(a, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else
					break;
				
			}
			
			//System.out.println(s.getAircraftFor(flights.get(i)) == null);
			/*if(s.getAircraftFor(flights.get(i)) == null) {
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
			}*/
			//System.out.println(s.getAircraftFor(flights.get(i)));
			List<Pilot> p = crew.findPilotsByHomeBaseAndTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode(), depAirport);
			List<Pilot> pb = crew.findPilotsByHomeBaseAndTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode(), arrivalAirport);
			List<Pilot> ps = crew.findPilotsByTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode());
			List<Pilot> pAll = crew.getAllPilots();
			for(Pilot x : p)
			{
				if(s.getCaptainOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("CAPTAIN")) {
						try {
	            s.allocateCaptainTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
				}
				
				}else if(s.getFirstOfficerOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("FIRST_OFFICER")) {
						try {
	            s.allocateFirstOfficerTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else {
					break;
					}
			}
			
			for(Pilot x : pb)
			{
				if(s.getCaptainOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("CAPTAIN")) {
						try {
	            s.allocateCaptainTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
				}
				
				}else if(s.getFirstOfficerOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("FIRST_OFFICER")) {
						try {
	            s.allocateFirstOfficerTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else {
					break;
					}
			}
			
			for(Pilot x : ps)
			{
				if(s.getCaptainOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("CAPTAIN")) {
						try {
	            s.allocateCaptainTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
				}
				
				}else if(s.getFirstOfficerOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("FIRST_OFFICER")) {
						try {
	            s.allocateFirstOfficerTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else {
					break;
					}
			}
			
			for(Pilot x : pAll)
			{
				if(s.getCaptainOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("CAPTAIN")) {
						try {
	            s.allocateCaptainTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
				}
				
				}else if(s.getFirstOfficerOf(flights.get(i)) == null)
				{
					if(!s.hasConflict(x, flights.get(i)) && x.getRank().toString().equals("FIRST_OFFICER")) {
						try {
	            s.allocateFirstOfficerTo(x, flights.get(i));
	          } catch (DoubleBookedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }
					}
				}else {
					break;
					}
			}
			
			List<CabinCrew> c = crew.findCabinCrewByHomeBaseAndTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode(), depAirport);
			List<CabinCrew> cb = crew.findCabinCrewByHomeBaseAndTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode(), arrivalAirport);
			List<CabinCrew> cs = crew.findCabinCrewByTypeRating(s.getAircraftFor(flights.get(i)).getTypeCode());
			List<CabinCrew> cAll = crew.getAllCabinCrew();
			for(CabinCrew x : c)
			{
				if(s.getCabinCrewOf(flights.get(i)).size() < s.getAircraftFor(flights.get(i)).getCabinCrewRequired()) {
					if(!s.hasConflict(x,flights.get(i))){
						try {
              s.allocateCabinCrewTo(x, flights.get(i));
            } catch (DoubleBookedException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            }
					}
				}else
					break;
			}
			
			for(CabinCrew x : cb)
			{
				if(s.getCabinCrewOf(flights.get(i)).size() < s.getAircraftFor(flights.get(i)).getCabinCrewRequired()) {
					if(!s.hasConflict(x,flights.get(i))){
						try {
              s.allocateCabinCrewTo(x, flights.get(i));
            } catch (DoubleBookedException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            }
					}
				}else
					break;
			}
			
			for(CabinCrew x : cs)
			{
				if(s.getCabinCrewOf(flights.get(i)).size() < s.getAircraftFor(flights.get(i)).getCabinCrewRequired()) {
					if(!s.hasConflict(x,flights.get(i))){
						try {
              s.allocateCabinCrewTo(x, flights.get(i));
            } catch (DoubleBookedException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            }
					}
				}else
					break;
			}
			
			for(CabinCrew x : cAll)
			{
				if(s.getCabinCrewOf(flights.get(i)).size() < s.getAircraftFor(flights.get(i)).getCabinCrewRequired()) {
					if(!s.hasConflict(x,flights.get(i))){
						try {
              s.allocateCabinCrewTo(x, flights.get(i));
            } catch (DoubleBookedException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            }
					}
				}else
					break;
			}
			
			//System.out.println(s.getCaptainOf(flights.get(i)));
			//System.out.println(s.getFirstOfficerOf(flights.get(i)));
			//System.out.println(s.getCabinCrewOf(flights.get(i)).size() == s.getAircraftFor(flights.get(i)).getCabinCrewRequired());
			//System.out.println(s.getCabinCrewOf(flights.get(i)));
			/*if(s.getCaptainOf(flights.get(i)) == null) {
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
				}*/
			//System.out.println(s.isValid(flights.get(i)));
			if(s.isValid(flights.get(i))) {
				try {
          s.completeAllocationFor(flights.get(i));
        } catch (InvalidAllocationException e) {
          // TODO Auto-generated catch block
        e.printStackTrace();
        }
			}
			
		}
		System.out.println(s.isCompleted());
		//System.out.println(s == null);
		return s;
	}

	@Override
	public void setSchedulerRunner(SchedulerRunner arg0) {
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
