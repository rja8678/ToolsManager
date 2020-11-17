package Analytics;

import DBContollerPackage.DBAnalytics;
import cs.rit.edu.DBConn;

public class AnalyticsOutput {
	public void generateOutput(DBConn conn) {
		DBAnalytics analytics = new DBAnalytics(conn);
		
		
	}
}
