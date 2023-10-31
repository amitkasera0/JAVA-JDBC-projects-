package com.Bankingmanagement.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class User {

	private Connection con;
	private Scanner sc;
	
	public User(Connection con , Scanner sc) {
		this.con= con;
		this.sc = sc;
	}
	
	public void register() {
		sc.nextLine();
		System.out.print("Full Name : ");
		String fname = sc.nextLine();
		System.out.print("Enter Email :");
		String email = sc.nextLine();
		System.out.print("Enter Password : ");
		String pas = sc.nextLine();
		
		if(user_exist(email)) {
			System.out.println("User ALready Exists for this Email Address!!!");
			return;
		}
		try {
			PreparedStatement pst = con.prepareStatement("INSERT INTO User(full_name , email , password) VALUES(?,?,?)");
			pst.setString(1, fname);
			pst.setString(2, email);
			pst.setString(3, pas);
			
			int count = pst.executeUpdate();
			
			if(count>0) {
				System.out.println("Registration Successfull!");
			}else {
				System.out.println("Registration Faild!");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public String login(){
		sc.nextLine();
		System.out.println("Enter Email :");
		String email = sc.nextLine();
		System.out.println("Enter password : ");
		String pas = sc.nextLine();
		
		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM User WHERE email = ? AND password = ?");
			pst.setString(1, email);
			pst.setString(2, pas);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return email;
			}else {
				return null;
			}
			
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
		return null ;
	}
	public boolean user_exist(String eamil) {
		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM user WHERE email = ?");
			pst.setString(1, eamil);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
			
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
		return false;
	}
}
