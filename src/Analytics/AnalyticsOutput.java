package Analytics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DBContollerPackage.DBAnalytics;
import cs.rit.edu.DBConn;

public class AnalyticsOutput {
	public void generateOutput(DBConn conn) {
		DBAnalytics analytics = new DBAnalytics(conn);
		//final 2d array that stores everything
		List<List<String>> data = new ArrayList<>(); //TODO;

		//first, need to collect all the data into one list of lists.

		List<List<String>> mostLentTypesData = analytics.mostLentType();

		data.addAll(mostLentTypesData);




		StringBuilder result = new StringBuilder();


		//builds final "result" string to be printed to file
		for(int i = 0; i < data.size(); i++){
			for(int j = 0; j < data.get(0).size(); j++){
				result.append(data.get(i).get(j));
				result.append(", ");
			}
			result.append("\n");
		}
		
		
		try {
			File file = new File("output.csv");
			
			FileWriter out = new FileWriter(file);
			out.write(result.toString());
			out.flush();
			out.close();
			
		} catch (IOException e) {
			System.out.println("Unable to open file.");
			e.printStackTrace();
		}
	}
}
