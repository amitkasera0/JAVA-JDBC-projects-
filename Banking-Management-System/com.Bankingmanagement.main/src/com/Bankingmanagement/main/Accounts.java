package com.Bankingmanagement.main;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;





public class Accounts {

	private Connection con;
	private Scanner sc;
	
	public Accounts(Connection con , Scanner sc) {
		this.con= con;
		this.sc = sc;
	}
	
	public long open_account(String email) {
		if(!account_exist(email)) {
			sc.nextLine();
			System.out.print("Enter full name : ");
			String fname = sc.nextLine();
			System.out.print("Enter initial Amount : ");
			double  balance = sc.nextDouble();
			sc.nextLine();
			System.out.print("Enter Security pin : ");
			String pin = sc.nextLine();
	
		try {
			long AcNum = generateAccountNumber();
			PreparedStatement pst = con.prepareStatement("INSERT INTO Accounts (account_number, full_name, email, balance, security_pin) VALUES(?,?,?,?,?)");
			pst.setLong(1, AcNum);
			pst.setString(2, fname);
			pst.setString(3, email );
			pst.setDouble(4, balance);
			pst.setString(5, pin);
			
			int count = pst.executeUpdate();
			if(count>0) {
				return AcNum;
			}else {
				throw new RuntimeException("Account Creation failed!!!");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		}
		throw new RuntimeException("Account Already Exist!!");
	}
	
	public long getAccount_number(String email) {
	   try {
		   PreparedStatement pst = con.prepareStatement("SELECT account_number FROM Accounts WHERE email = ?");
		   pst.setString( 1, email);
		   ResultSet rs = pst.executeQuery();
		   
		   if(rs.next()) {
			   long accountnum = rs.getLong("account_number");
			   return accountnum;
		   }
		   
	   }catch (SQLException e) {
		   e.printStackTrace();
	   }	
	  throw new RuntimeException("Account number Dosen't exist !!");
	}
	
	private long generateAccountNumber() {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1");
			 
			if(rs.next()) {
				long last_ac_number = rs.getLong("account_number");
				return last_ac_number + 1;
			}else {
				return 10000100;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 10000100;
	}
	
	public boolean account_exist(String email) {
		try {
			PreparedStatement pst = con.prepareStatement("SELECT account_number FROM Accounts WHERE email = ? ");
			pst.setString(1, email);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				return true ;
			}else {
				return false;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
}

