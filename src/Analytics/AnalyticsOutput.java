package Analytics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DBContollerPackage.DBAnalytics;
import cs.rit.edu.DBConn;

public class AnalyticsOutput {

	public void writeFile(List<List<String>> data, String filename){
		StringBuilder result = new StringBuilder();

		//builds final "result" string to be printed to file
		for(List<String> datalist:data){
			for(String element:datalist){
				result.append(element);
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



	public void generateOutput(DBConn conn) {
		DBAnalytics analytics = new DBAnalytics(conn);

		writeFile(analytics.mostLentTool(), "mostlenttool.csv");
		writeFile(analytics.mostLentType(), "mostlenttype.csv");
		writeFile(analytics.overdueTools(), "overduetools.csv");
		//todo: repeat this for each analytics type

	}
}
