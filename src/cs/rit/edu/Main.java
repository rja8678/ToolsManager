package cs.rit.edu;

import DBContollerPackage.DBTool;
import DBContollerPackage.DBUser;
import ObjectClasses.*;

import java.sql.Date;
import java.util.ArrayList;

public class Main {
	public static void main(String [] args) {
		if(args.length > 1) {
			String username = args[0];
			String password = args[1];

			DBConn conn = new DBConn(username, password);

			User u1 = new User(1, conn);
			System.out.println(u1.toString());

			User u2 = new User(2, conn);
			System.out.println(u2.toString());


			//Create new tool example below
//			ArrayList<Integer> tooltypes = new ArrayList<>();
//			tooltypes.add(1);
//			tooltypes.add(2);
//			tooltypes.add(3);
//			Tool test = new Tool("Fresh New Multi-tool", new Date(System.currentTimeMillis()), true, tooltypes, conn);
//			System.out.println(test.toString());
//
//			System.out.println(new DBTool(conn).fetchTool(4).toString());

			// BELOW WORKS, UNCOMMENT TO LEND A TOOL FROM u1's collection to u2
//			u1.lendTool(u1.getToolCollection().getFirst(), u2);

			System.out.println("User1 Collection: " + u1.getToolCollection().toString());
			System.out.println("User2 Collection: " + u2.getToolCollection().toString());
		}else {
			System.out.println("Please give the database username and password as commandline arguments");
		}
	}
}
