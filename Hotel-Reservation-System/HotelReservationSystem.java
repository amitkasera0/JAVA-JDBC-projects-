package com.jdbcconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.management.RuntimeErrorException;


public class HotelReservationSystem {
public static void main(String args[]) {
	try {
        Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db?useSSL=false","root","root");
		while(true) {
			System.out.println();
			System.out.println("HOTEL MANAGEMENT SYSTEM");
			Scanner sc = new Scanner(System.in);
			System.out.println("1. Reserve a room");
			System.out.println("2. View Reservations");
			System.out.println("3. Get Room Number");
			System.out.println("4. Update Reservations");
			System.out.println("5. Delete Reservations");
			System.out.println("0. Exit");
			System.out.print("Choose an option");
			int choice = sc.nextInt();
		
			switch(choice) {
			case 1 : 
				reserveRoom(con, sc);
				break;
			case 2 : 
				viewReservation(con);
				break;
			case 3 : 
				getRoomNumber(con, sc);
				break;
			case 4 : 
				updateReservation(con, sc);
				break;
			case 5 : 
				deleteReservation(con, sc);
				break;
			case 0 : 
				exit();
				sc.close();
				return;
			default:
				System.out.println("Invalid choice, Try again.");
			}
		}
	}catch (ClassNotFoundException  | SQLException  e) {
		e.printStackTrace();
	}catch(InterruptedException e) {
		throw new RuntimeException(e);
	}
}
	private static void reserveRoom(Connection con , Scanner sc) {
		
		try {
	    System.out.print("Eneter guest name : ");
		String guestName = sc.next();
		System.out.print("Enter room number : ");
		int roomNumber = sc.nextInt();
		System.out.print("Enter contact number  : ");
		String contactNumber = sc.next();
		Statement st = con.createStatement();
		int count = st.executeUpdate("INSERT INTO reservations (guest_name , room_number , contact_number) "
				         + "values('"+ guestName + "' , "+roomNumber+",'"+contactNumber+"' )");
		if(count>0) {
			System.out.println("Reservation successfully");
		}else{
			System.out.println("Reservation faild");
		}
		}catch(SQLException e) {
		   e.printStackTrace();
		}
}
	private static void viewReservation (Connection con) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from reservations");
			
			System.out.println("Current Reservation : ");
			System.out.println("+------------------+---------------+---------------+-----------------+-------------------------+");
			System.out.println("| Reservaation ID  | Guest         | Room Number   | Contact Number  |Reservation date         |");
			System.out.println("+------------------+---------------+---------------+-----------------+-------------------------+");
			
			while(rs.next()) {
				int rsId = rs.getInt("reservation_id");
				String name = rs.getString("guest_name");
				int rno = rs.getInt("room_number");
				String cno = rs.getString("contact_number");
				String rstime = rs.getTimestamp("reservation_date").toString();
				
				System.out.printf("| %-16d | %-13s | %-13d | %-15s | %-23s |\n",
						rsId,name,rno,cno,rstime);
			}
			System.out.println("+------------------+---------------+---------------+-----------------+-------------------------+");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	private static void getRoomNumber(Connection con , Scanner sc) {
		try {
			System.out.println("Enter reservation ID : ");
			int rsId = sc.nextInt();
			System.out.println("Enter guest name : ");
			String name = sc.next();
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT room_number FROM reservations WHERE reservation_id = '"+rsId+"' AND guest_name = '"+name+"' ");
	        
			 if(rs.next()) {
				 int rnumber = rs.getInt("room_number");
				 System.out.println(" Room number for reservation ID " + rsId + " and guest " + name + " is : " + rnumber);
			 }else {
				 System.out.println("Reservation not found for given id and guset name");
			 }
		}
		catch(SQLException e){
	       e.printStackTrace();
		}
	}
	private static void updateReservation(Connection con , Scanner sc) {
	try {
		System.out.print("Enter reservation ID to update : ");
		int rsid = sc.nextInt();
		sc.nextLine();
		
		if(!reservationExists(con,rsid)) {
			System.out.println("Reservation not found for the given ID"); 
			return;
		}
		
		System.out.println("Enter new guest name : ");
		String name = sc.nextLine();
		System.out.println("Enter new room number : ");
		int rno= sc.nextInt();
		System.out.println("Enter new contact number : ");
		String cno = sc.next();
		
		Statement st = con.createStatement();
		int count = st.executeUpdate("UPDATE reservations SET guest_name = '"+ name +"',"
				+   "room_number = '"+ rno +"',"
						+ "contact_number = '"+cno+"'"
								+ "WHERE reservation_id = '"+rsid+"'");
		
		if (count>0) {
			System.out.println("Reservation updated successfully!");
		}else {
			System.out.println("Reservation uodated faild!");
		}
	}catch (SQLException e) {
		e.printStackTrace();
	}
	}
	private static void deleteReservation(Connection con , Scanner sc) {
		try {
			
			System.out.println("Enter reservation ID to delete : ");
			int rsid = sc.nextInt();
			
			if(!reservationExists(con,rsid)) {
				System.out.println("Reservation not found for the given ID"); 
				return;
			}
			
			Statement st = con.createStatement();
			int count = st.executeUpdate("DELETE FROM reservations WHERE reservation_id = '"+rsid+"'");
			
			if(count>0) {
				System.out.println("Reservation deleted successfully");

			}
			else {
				System.out.println("Reservation deletion faild.");
			}			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean reservationExists(Connection con , int rsid) {
		try {
		
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT reservation_id FROM reservations WHERE reservation_id = '"+rsid+"'");
			
			
			return rs.next();
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
		
	private static void exit() throws InterruptedException {
		System.out.print("Exiting System");
		int i = 5;
		while(i!=0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}
		System.out.println();
		System.out.println("Thank you for Using Hotel Reservation System!");
		Thread.sleep(1000);
		System.out.println();
		System.out.println("process finished with exit code 0");
	}
	
}
