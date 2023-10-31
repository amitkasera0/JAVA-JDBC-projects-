package com.Bankingmanagement.main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;



public class Bankingapp {

	public static void main(String[] args) {
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system?useSSL=false","root","root");
			
			Scanner sc = new Scanner(System.in);
			User us = new User(con,sc);
			Accounts ac = new Accounts(con,sc);
			AccountManager am = new AccountManager(con,sc); 
			
			String email;
			long account_number;
			
			while(true) {
				System.out.println("---- WELCOME TO BANKING SYSTEM ----");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. login");
				System.out.println("3. Exit");
				System.out.println("Enter your choice");
				
				int choice1 = sc.nextInt();
				
				switch(choice1) {
				case 1 :
					us.register();
					break;
				case 2 :
					email = us.login();
					if(email!=null) {
						System.out.println();
						System.out.println("User logged In!");
						if(!ac.account_exist(email)) {
							System.out.println();
							System.out.println("1. Open a new Bank Account ");
							System.out.println("2. Exit");
						if(sc.nextInt() == 1) {
							account_number = ac.open_account(email);
							System.out.println("Account Created Successfully");
							System.out.println("Your Account number is : " + account_number);
						}else {
							break;
						}
											
						}
						account_number = ac.getAccount_number(email);
						int choice2 = 0;
						while(choice2!=5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. credit money");
							System.out.println("3. Transfer Money");
							System.out.println("4. Check Balance");
							System.out.println("5. Log Out");
							System.out.print("Enter your choice :");
                            choice2 = sc.nextInt();
                            
                            switch(choice2) {
                            case 1: 
                            	am.debit_money(account_number);
                            	break;
                            case 2 :
                            	am.credit_money(account_number);
                            	break;
                            case 3 :
                            	am.transfer_money(account_number);
                            case 4 : 
                            	am.getBalance(account_number);
                            	break;
                            case 5 :
                            	break;
                            	
                            default : 
                            	System.out.println("Enter valid choice");
                            	break;
                         
                            }
						}
					}else {
						System.out.println("Incrroct Email or password");
					}
					
				case 3:
					System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
					System.out.println("Exiting System!");
					return;
			    
		        default: 
		        	System.out.println("Enter valid choice");
				}
			}
		}
		catch (ClassNotFoundException |SQLException exc ) {
			exc.printStackTrace();
		}
	}
}
