package Analytics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import DBContollerPackage.DBAnalytics;
import cs.rit.edu.DBConn;

public class AnalyticsOutput {
	public void generateOutput(DBConn conn) {
		DBAnalytics analytics = new DBAnalytics(conn);
		
		
		try {
			File file = new File("output.csv");
			
			FileWriter out = new FileWriter(file);
			

			analytics.mostLentType();
			
		} catch (IOException e) {
			System.out.println("Unable to open file.");
			e.printStackTrace();
		}
	}
}
