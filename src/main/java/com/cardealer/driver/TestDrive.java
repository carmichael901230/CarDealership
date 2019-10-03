package com.cardealer.driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.cardealer.pojos.*;
import com.cardealer.services.Services;

import static com.cardealer.util.LoggerUtil.*;

public class TestDrive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Services serve = new Services();
		serve.run();
		
		
		
		
		
		
		
//		FileInputStream inFile = null;
//		ObjectInputStream reader = null;
//		FileOutputStream outFile = null;
//		ObjectOutputStream writer = null;
//		HashMap<String, UserImpl> userPool = null;
//		UserImpl curUser = null;
//		
//		try {
//			inFile = new FileInputStream("./database/UserPool.dat");
//			reader = new ObjectInputStream(inFile);
//			userPool = (HashMap<String, UserImpl>) reader.readObject();
//			curUser = userPool.get("customer");
//			curUser.getCarList().get(0).setPaidAmt(10000);
//			userPool.put("customer", curUser);
//			outFile = new FileOutputStream("./database/UserPool.dat");
//			writer = new ObjectOutputStream(outFile);
//			writer.writeObject(userPool);
//			System.out.print("Done");
//		}catch (Exception e) {
//			e.printStackTrace();
//		}

		
// For creating the first user in UserPool
//		UserImpl u = new UserImpl("cardealer", "cardealer", "ricky wang", 1, new ArrayList<BoughtCar>());
//		HashMap<String, UserImpl> h = new HashMap<String, UserImpl>();
//		h.put("cardealer", u);
//		FileOutputStream outFile = null;
//		ObjectOutputStream writer = null;
//		String fileName = "./database/UserPool.dat";
//		try {
//			outFile = new FileOutputStream(fileName);
//			writer = new ObjectOutputStream(outFile);
//			writer.writeObject(h);
//		} catch (IOException e) {
//			error("fail to write userPool to file");
//		}finally {
//			try {
//				if (writer != null) writer.close();
//				if (outFile != null) outFile.close();
//			}catch (IOException e) {
//				error("fail to close writer or outFile after writting userPool");
//			}
//		}
//		
// For creating the first car in CarPool
//		CarImpl c = new CarImpl(0, "Toyota", "Camry", "gray", 20000.0, new ArrayList<UserImpl>());
//		HashMap<Integer, CarImpl> h = new HashMap<Integer, CarImpl>();
//		h.put(Integer.valueOf(0), c);
//		FileOutputStream outFile = null;
//		ObjectOutputStream writer = null;
//		String fileName = "./database/CarPool.dat";
//		try {
//			outFile = new FileOutputStream(fileName);
//			writer = new ObjectOutputStream(outFile);
//			writer.writeObject(h);
//		} catch (IOException e) {
//			error("fail to write carPool to file");
//		}finally {
//			try {
//				if (writer != null) writer.close();
//				if (outFile != null) outFile.close();
//			}catch (IOException e) {
//				error("fail to close writer or outFile after writting carPool");
//			}
//		}
 
// creating first transaction in Transactio.dat
//		TransactionImpl tran1 = new TransactionImpl(1, "Toyota Camry", "customer customer", 5000, new Date(119, 10, 1));
//		TransactionImpl tran2 = new TransactionImpl(2, "Toyota Camry", "customer customer", 5000, new Date(119,10,2));
//		HashMap<Integer, TransactionImpl> tranPool = new HashMap<Integer, TransactionImpl>();
//		tranPool.put(1, tran1);
//		tranPool.put(2, tran2);
//		FileOutputStream outFile = null;
//		ObjectOutputStream writer = null;
//		
//		try {
//			outFile = new FileOutputStream("./database/TranPool.dat");
//			writer = new ObjectOutputStream(outFile);
//			writer.writeObject(tranPool);
//			System.out.println("Done");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
