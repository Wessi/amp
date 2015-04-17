package monetmonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Shelter class for methods that couldn't find a home anywhere else 
 * @author acartaleanu
 *
 */
public class Utils {
	
	public static List<StatusShower> statusShowers = new ArrayList<StatusShower>();
	public static StatusShower logfile;
	/**
	 * Broadcasts the message across all available status showers
	 * @param statusMessage
	 * @throws Exception
	 */
	public static void broadcastStatus(String statusMessage) throws Exception {
		for (StatusShower st : statusShowers) {
			st.showStatus(statusMessage);
		}
	}
	/**
	 * Writes a string to the logfile only (logfile will append the timestap itself)
	 * @param status
	 * @throws Exception
	 */
	public static void toLog(String status) throws Exception{
		logfile.showStatus(status);
	}
}
