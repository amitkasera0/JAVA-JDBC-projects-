package com.Bankingmanagement.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class AccountManager {
 
	private Connection con;
	private Scanner sc;
	
	public AccountManager(Connection con , Scanner sc) {
		this.con= con;
		this.sc = sc;
	}
	
	public void credit_money(long AcNum) throws SQLException {
		try {
		sc.nextLine();
		System.out.println("Enter Amount : ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter Security pin : ");
		String pin = sc.nextLine();

		con.setAutoCommit(false);
		if(AcNum!=0) {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
			pst.setLong(1, AcNum);
			pst.setString(2, pin);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				
					PreparedStatement pst1 = con.prepareStatement("UPDATE Accounts SET balance = balance + ? WHERE account_number = ?");
					pst1.setDouble(1, amount);
					pst1.setLong(2, AcNum);
					int count = pst1.executeUpdate();
					if(count>0) {
						System.out.println("Rs." +amount+" credit Successfully");	
						con.commit();
						con.setAutoCommit(true);
						return;
						}else {
							System.out.println("Transaction Failed!!");
							con.rollback();
							con.setAutoCommit(true);
						}
					
			}else {
				System.out.println("Invalid Pin!");
			}
		}
	
	}catch(SQLException exc) {
		exc.printStackTrace();
	}
	con.setAutoCommit(true);
}
	
	public void debit_money(long AcNum) throws SQLException {
		try {
			sc.nextLine();
			System.out.println("Enter Amount : ");
			double amount = sc.nextDouble();
			sc.nextLine();
			System.out.println("Enter Security pin : ");
			String pin = sc.nextLine();

			con.setAutoCommit(false);
			if(AcNum!=0) {
				PreparedStatement pst = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
				pst.setLong(1, AcNum);
				pst.setString(2, pin);
				ResultSet rs = pst.executeQuery();
				
				if(rs.next()) {
					double balance = rs.getDouble("balance");
					if(amount <= balance) {
						PreparedStatement pst1 = con.prepareStatement("UPDATE Accounts SET balance = balance- ? WHERE account_number = ?");
						pst1.setDouble(1, amount);
						pst1.setLong(2, AcNum);
						int count = pst1.executeUpdate();
						if(count>0) {
							System.out.println("Rs. " +amount+" debited Successfully");	
							con.commit();
							con.setAutoCommit(true);
							return;
							}else {
								System.out.println("Transaction Failed!!");
								con.rollback();
								con.setAutoCommit(true);
							}
						}else {
							System.out.println("Insufficient Balance!");
						}
				}else {
					System.out.println("Invalid Pin!");
				}
			}
		
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
		con.setAutoCommit(true);
	}

	public void getBalance(long AcNum) {
		sc.nextLine();
		System.out.print("Enter Security pin ");
		String pin = sc.nextLine();
		
		try {
			PreparedStatement pst = con.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ? ");
			pst.setLong(1, AcNum);
			pst.setString(2,pin);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				double balance  = rs.getDouble("balance");
				System.out.println("Balance : " + balance);
			} else {
				System.out.println("Invalid pin");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

    public void transfer_money(long sender_ac_num) throws SQLException {
    	sc.nextLine();
    	System.out.println("Enter Reciver Account Number");
    	long reciver_ac_num = sc.nextLong();
    	System.out.println("Enter Amount : ");
    	double amount = sc.nextDouble();
    	sc.nextLine();
    	System.out.println("Enter Security pin : ");
    	String pin = sc.nextLine();
    	
    	try {
    		con.setAutoCommit(false);
    		if(sender_ac_num!=0 && reciver_ac_num!=0) {
    			PreparedStatement pst = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
				pst.setLong(1, sender_ac_num);
				pst.setString(2, pin);
				ResultSet rs = pst.executeQuery();
				
				if(rs.next()) {
					double balance = rs.getDouble("balance");
					if(amount<=balance) {
						PreparedStatement credit_pst = con.prepareStatement("UPDATE Accounts SET balance = balance + ? WHERE account_number = ?");
						credit_pst.setDouble(1, amount);
						credit_pst.setLong(2, reciver_ac_num);
						PreparedStatement debit_pst = con.prepareStatement("UPDATE Accounts SET balance = balance- ? WHERE account_number = ?");
						debit_pst.setDouble(1, amount);
					    debit_pst.setLong(2, sender_ac_num);
					    
					    int count = debit_pst.executeUpdate();
					    int count1 = credit_pst.executeUpdate();
					    
					    if(count>0 && count1>0) {
					    	System.out.println("Transaction Successful!");
					    	System.out.println("Rs. "+amount+ " Transferred Successfully");
					    	con.commit();
					    	con.setAutoCommit(true);
	                        return;
					    }else {
					    	System.out.println("Transaction Failed!!");
					    	con.rollback();
					    	con.setAutoCommit(true);
					    }
					}else {
						System.out.println("Insufficient Balance");
					}
				}else {
					System.out.println("Invalid security pin");
				}
    		}else{
    			System.out.println("Invalid account number!!");
    		}    		
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	con.setAutoCommit(true);    
    }
}
