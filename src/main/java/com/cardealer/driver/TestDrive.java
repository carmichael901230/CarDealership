package com.cardealer.driver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.cardealer.pojos.BoughtCar;
import com.cardealer.pojos.CarImpl;
import com.cardealer.pojos.UserImpl;
import com.cardealer.services.Services;

import static com.cardealer.util.LoggerUtil.*;

public class TestDrive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Services serve = new Services();
		serve.run();
		
//		// register 
//		test.createUser();
//	
//		// login 
	
//		UserImpl loginUser = test.login(cin);
//		
//		// add car
//		System.out.println(test.addCar(cin));
//		
//		// is loginUser is valid
//		if (loginUser != null) {
//			// get cars on lot
//			ArrayList<CarImpl> carLot = test.getCarLot();
//			
//			// pick a car on carLot and make offer
//			System.out.println(test.makeOffer(carLot, loginUser, cin));
//			
//			// rejcet an offer 
//			System.out.println(test.rejectOffer(carLot, cin));
//		}
//		
//		
//		cin.close();
		
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
 
		
	}
}
