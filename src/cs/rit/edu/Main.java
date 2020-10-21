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
			DBUser dbu = new DBUser(conn) ;
			DBTool dbt = new DBTool(conn);

			User u1 = dbu.createUserObject(1);
			System.out.println(u1.toString());

			Tool t1 = dbt.fetchTool(1);
			System.out.println(t1.toString());

		}else {
			System.out.println("Please give the database username and password as commandline arguments");
		}
	}
}
