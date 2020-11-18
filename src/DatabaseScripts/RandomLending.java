package DatabaseScripts;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;

public class RandomLending {

	public static float lend_chance = 0.6f;
	public static float return_chance = 0.3f;
	
	public static void main(String[] args) {
		if(args.length > 1) {
			String username = args[0];
			String password = args[1];

			System.out.println("Doing random lends and to users");
            
			Random rand = new Random();
			
			DBConn conn = new DBConn(username, password);

			HashMap<Integer, String> users = conn.fetchAllUsers();
			ArrayList<User> usersList = new ArrayList<>();
			
			for (Integer id: users.keySet()) {
				usersList.add(new User(id, conn));
			}
			
			
			for(User user: usersList) {
            	for (Tool tool: user.getOwnedTools()) {
            		if(rand.nextFloat() > lend_chance) {
            			int rand_user = rand.nextInt(users.keySet().size());
            			while(rand_user != user.getUserID()) {
            				rand_user = rand.nextInt(users.keySet().size());
            			}
            			user.lendTool(tool, usersList.get(rand_user), new Date(System.currentTimeMillis() + rand.nextLong() % 3000000000l));
            		}
            	}
			}
            
//			for(User user: usersList) {
//				for (Tool tool: user.getToolCollection()) {
//					if(rand.nextFloat() > return_chance) {
//						user.returnTool(tool);
//					}
//				}
//			}
			
		}else {
			System.out.println("Please give the database username and password as commandline arguments");
		}
	}

}
