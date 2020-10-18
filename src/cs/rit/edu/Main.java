package cs.rit.edu;

public class Main {
	public static void main(String [] args) {
		if(args.length > 1) {
			String username = args[0];
			String password = args[1];

			DBConn conn = new DBConn(username, password);
			conn.getTestVal();
		}else {
			System.out.println("Please give the database username and password as commandline arguments");
		}
	}
}
