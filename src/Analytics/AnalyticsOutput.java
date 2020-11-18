package Analytics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
			File file = new File(filename);

			FileWriter out = new FileWriter(file);
			out.write(result.toString());
			out.flush();
			out.close();

		} catch (IOException e) {
			System.out.println("Unable to open file.");
			e.printStackTrace();
		}
	}


	public void generateOutput(DBConn conn, int userID) {
		DBAnalytics analytics = new DBAnalytics(conn);

		writeFile(analytics.mostLentTool(), "analytics/mostlenttool.csv");
		writeFile(analytics.mostLentType(), "analytics/mostlenttype.csv");
		writeFile(analytics.overdueTools(), "analytics/overduetools.csv");
		writeFile(analytics.toolsIHaveLent(userID), "analytics/toolsIhavelent.csv");
		writeFile(analytics.toolsIAmLendihgOut(userID), "analytics/toolsIamlending.csv");
		writeFile(analytics.mostRecentLending(userID), "analytics/myrecentlending.csv");
		writeFile(analytics.userWithMostLends(), "analytics/mostlends.csv");
		writeFile(analytics.userWithMostToolsInCollection(), "analytics/mosttoolsincollection.csv");
		writeFile(analytics.userWithMostToolsOwned(), "analytics/mosttoolsowned.csv");
		writeFile(analytics.userCollectionToolTypes(userID), "analytics/mycollectiontooltypes.csv");
		writeFile(analytics.userOwnedToolTypes(userID), "analytics/myownedtooltypes.csv");
		//todo: repeat this for each analytics type

	}
}
