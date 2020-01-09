package solution;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import baseclasses.Aircraft;
import baseclasses.DataLoadingException;
import baseclasses.IRouteDAO;
import baseclasses.Route;
import baseclasses.Aircraft.Manufacturer;

/**
 * The RouteDAO parses XML files of route information, each route specifying
 * where the airline flies from, to, and on which day of the week
 */
public class RouteDAO implements IRouteDAO {
	
	public List<Route> routesList = new ArrayList<Route>();
	
	/**
	 * Finds all flights that depart on the specified day of the week
	 * @param dayOfWeek A three letter day of the week, e.g. "Tue"
	 * @return A list of all routes that depart on this day
	 */
	@Override
	public List<Route> findRoutesByDayOfWeek(String dayOfWeek) {
		List<Route> output = new ArrayList<Route>();
		
		for(Route r : routesList)
		{
			if(r.getDayOfWeek().equalsIgnoreCase(dayOfWeek))
				output.add(r);
		}
		return output;
	}

	/**
	 * Finds all of the flights that depart from a specific airport on a specific day of the week
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @param dayOfWeek the three letter day of the week code to searh for, e.g. "Tue"
	 * @return A list of all routes from that airport on that day
	 */
	@Override
	public List<Route> findRoutesByDepartureAirportAndDay(String airportCode, String dayOfWeek) {
		List<Route> output = new ArrayList<Route>();
		
		for(Route r : routesList)
		{
			if(r.getDayOfWeek().equalsIgnoreCase(dayOfWeek) && r.getDepartureAirportCode().equalsIgnoreCase(airportCode))
				output.add(r);
		}
		return output;
	}

	/**
	 * Finds all of the flights that depart from a specific airport
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @return A list of all of the routes departing the specified airport
	 */
	@Override
	public List<Route> findRoutesDepartingAirport(String airportCode) {
		List<Route> output = new ArrayList<Route>();
		
		for(Route r : routesList)
		{
			if(r.getDepartureAirportCode().equalsIgnoreCase(airportCode))
				output.add(r);
		}
		return output;
	}

	//Revisit
	/**
	 * Finds all of the flights that depart on the specified date
	 * @param date the date to search for
	 * @return A list of all routes that dpeart on this date
	 */
	@Override
	public List<Route> findRoutesbyDate(LocalDate date) {
		List<Route> output = new ArrayList<Route>();
		String day = date.getDayOfWeek().toString().substring(0, 3);
		//System.out.println(day);
		for(Route r : routesList)
		{
			if(r.getDayOfWeek().equalsIgnoreCase(day))
				output.add(r);
		}
		return output;
	}

	/**
	 * Returns The full list of all currently loaded routes
	 * @return The full list of all currently loaded routes
	 */
	@Override
	public List<Route> getAllRoutes() {
		return routesList;
	}

	/**
	 * Returns The number of routes currently loaded
	 * @return The number of routes currently loaded
	 */
	@Override
	public int getNumberOfRoutes() {
		return routesList.size();
	}

	/**
	 * Loads the route data from the specified file, adding them to the currently loaded routes
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadRouteData(Path p) throws DataLoadingException {
		
		try {
			File xmlFile = p.toFile();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = factory.newDocumentBuilder();
	        Document doc = dBuilder.parse(xmlFile);
	        doc.getDocumentElement().normalize();
	        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList rList = doc.getElementsByTagName("Route");
	        for(int i = 0;i<rList.getLength();i++)
	        {
	        	Route route = new Route();
	        	Node r = rList.item(i);	
	        	if(r.getNodeType()==Node.ELEMENT_NODE) {
	        		Element routes = (Element) r;
	        		int flightNumber = Integer.parseInt(routes.getElementsByTagName("FlightNumber").item(0).getTextContent());
	        		route.setFlightNumber(flightNumber);
	        		route.setDayOfWeek(routes.getElementsByTagName("DayOfWeek").item(0).getTextContent());
	        		LocalTime dtime = LocalTime.parse(routes.getElementsByTagName("DepartureTime").item(0).getTextContent());
	        		route.setDepartureTime(dtime);
	        		LocalTime atime = LocalTime.parse(routes.getElementsByTagName("ArrivalTime").item(0).getTextContent());
	        		route.setArrivalTime(atime);
	        		route.setArrivalAirport(routes.getElementsByTagName("ArrivalAirport").item(0).getTextContent());
	        		route.setArrivalAirportCode(routes.getElementsByTagName("ArrivalAirportCode").item(0).getTextContent());
	        		route.setDepartureAirport(routes.getElementsByTagName("DepartureAirport").item(0).getTextContent());
	        		route.setDepartureAirportCode(routes.getElementsByTagName("DepartureAirportCode").item(0).getTextContent());
	        		Duration d = Duration.parse(routes.getElementsByTagName("Duration").item(0).getTextContent());
	        		route.setDuration(d);
	        		routesList.add(route);
	        		//System.out.println(route.getDuration());
	        	}
	        }
		}
		catch (IOException ioe) {
			//There was a problem reading the file
			throw new DataLoadingException(ioe);
		} catch (SAXException e) {
			throw new DataLoadingException(e);
    } catch (ParserConfigurationException e) {
    	throw new DataLoadingException(e);
    }catch (Exception e) {
			throw new DataLoadingException(e);
		}

	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		routesList.clear();
	}
	
}
