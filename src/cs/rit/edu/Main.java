package cs.rit.edu;

import DBContollerPackage.DBTool;
import DBContollerPackage.DBUser;
import ObjectClasses.*;

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

			// BELOW WORKS, UNCOMMENT TO LEND A TOOL FROM u1's collection to u2
//			u1.lendTool(u1.getToolCollection().getFirst(), u2);

			System.out.println("User1 Collection: " + u1.getToolCollection().toString());
			System.out.println("User2 Collection: " + u2.getToolCollection().toString());


		}else {
			System.out.println("Please give the database username and password as commandline arguments");
		}
	}
}
