package com.cardealer.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.cardealer.util.LoggerUtil.*;
import com.cardealer.pojos.UserImpl;
import com.cardealer.util.MD5;
import com.cardealer.pojos.BoughtCar;
import com.cardealer.pojos.CarImpl;
import com.cardealer.pojos.TransactionImpl;

public class Services {
	// login status, 0=>not log in, 1=>customer log in, 2=>employee log in
	private int loggedIn; 	
	private UserImpl curUser;		// current logged in user
	private int validEnter;
	private int c;			// user input;
	
	public Services() {
		this.loggedIn = 0;
		this.curUser = null;
		this.validEnter = 1;
		this.c = -1;
	}
	
//	create a newUser and store into DAO file UserPool.dat with following steps
//	1. prompt user to enter info
//	2. read UserPool.dat to get the proper userID for newUser, also store UserPool as Map object userPool
//	3. create user object new UserImpl(id, userId, password, fullName, userType, carList)
//	4. push newUser to userPoll and write userPool to UserPool.dat
	@SuppressWarnings("unchecked")
	public UserImpl createUser(Scanner cin) {
		boolean readsuccess = false;
		UserImpl newUser = null;
		String fileName = "./database/UserPool.dat";
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		FileOutputStream outFile = null;
		ObjectOutputStream writer = null;
		
		String userId = null;
		String password = null;
		String fullName = null;
		
		// getting user info from user input
		try {
			System.out.println("Creating new User");
			System.out.print("Enter UserID: ");
			userId = cin.nextLine();
			System.out.print("Enter PassWord: ");
			password = cin.nextLine();
			System.out.print("Enter Your FullName: ");
			fullName = cin.nextLine();
			System.out.println();
			readsuccess = true;
		}catch(Exception e) {
			error("CreateUser: fail to read user input from console");
			return null;
		}
		if (readsuccess) {
			HashMap<String, UserImpl> userPool = null;
			boolean readPoolsuccess = false;
			
			// read userPool
			try {
				inFile = new FileInputStream(fileName);
				reader = new ObjectInputStream(inFile);
				userPool = (HashMap<String, UserImpl>) reader.readObject();
				readPoolsuccess = true;
			}catch (IOException e) {
				error("CreateUser: fail to read existing userPool before adding newUser");
				return null;
			}catch (ClassNotFoundException e) {
				error("CreateUser: ClassNotFoundException when reading existing userPool before adding newUser");
				return null;
			}finally {
				try {
					if (reader != null) reader.close();
					if (inFile != null) inFile.close();
				}catch (IOException e) {
					error("CreateUser: fail to close reader or inFile after reading userPool");
				}
			}
			if (readPoolsuccess) {
				// create newUser object
				newUser = new UserImpl(userId, password, fullName, 0, new ArrayList<BoughtCar>());
				
				// push newUser to userPool
				userPool.put(userId, newUser);
				info("CreateUser: successfully created user: "+newUser.toString());
				
				// write new userPool to file 
				try {
					outFile = new FileOutputStream(fileName);
					writer = new ObjectOutputStream(outFile);
					writer.writeObject(userPool);
					info("CreateUser: successfully write newUser to file: "+newUser.toString());
					return newUser;
				} catch (IOException e) {
					error("CreateUser: fail to write userPool to file");
					return null;
				}finally {
					try {
						if (reader != null) writer.close();
						if (inFile != null) outFile.close();
					}catch (IOException e) {
						error("CreateUser: fail to close writer or outFile after writting userPool");
					}
				}
				
			}
			else {
				error("CreateUser: fail to read UserPool");
				return null;
			}
		}
		else {
			error("CreateUser: fail to create user");
			return null;
		}
	}
	
//	customer/employee login their account, and return UserImpl object 
// 	1. prompt user to enter userId and password
//	2. use userId and password and look into UserPool.dat, check if user info is presented and match
//	3. return the UserImpl object if match otherwise return null
	@SuppressWarnings("unchecked")
	public UserImpl login(Scanner cin) {
		// prompt user to enter userId and password
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		String fileName = "./database/UserPool.dat";
		HashMap<String, UserImpl> userPool = null;
		try {
			String userId;
			String password;
			System.out.println("Login in");
			System.out.print("Enter UserID: ");
			userId = cin.nextLine();
			System.out.print("Enter Password: ");
			password = cin.nextLine();
			// read UserPool.dat check if entered info matches
			try {

				inFile = new FileInputStream(fileName);
				reader = new ObjectInputStream(inFile);
				userPool = (HashMap<String, UserImpl>) reader.readObject();		// cast suppress
				// lookup user entered id and password in the UserPool.dat
				if (userPool.containsKey(userId)) {
					UserImpl loginUser = userPool.get(userId);
					// if userId and password are match, return loginUser
					if (loginUser.getPassword().equals(password)) {
						if (loginUser.getUserType() == 1) {
							info("Login: login as employee ["+userId+"]");
							return loginUser;
						}
						else {
							info("Login: login as customer ["+userId+"]");
							return loginUser;
						}
					}
					// if password doesn't match return null
					else {
						error("Login: fail to login due to password doesn't match");
						return null;
					}
				}
				else {
					error("Login: fail to login due to userId no found");
					return null;
				}
			}catch (IOException e) {
				e.printStackTrace();
				error("Login: fail to login due to error when reading UserPool.dat");
				return null;
			}catch (ClassNotFoundException e) {
				error("Login: fail to login due to ClassNotFoundError when reading UserPool.dat");
				return null;
			}finally {
				reader.close();
				inFile.close();
			}
		}catch (Exception e) {
			error("Login: fail to get user input from console");
			return null;
		}
	}
	
//	Add new car into car pool, with following steps
//	1. prompt user to enter car info, brand model price
//	2. open and read CarPool.dat
//	3. get nextId by finding current max carId then + 1
//	4. create newCar object and push into carPool hashmap
//	5. write carPool hashmap into CarPool.dat
	@SuppressWarnings("unchecked")
	public boolean addCar(Scanner cin) {
		
		String brand = null;
		String model = null;
		String color = null;
		double price = 0.0;
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		FileOutputStream outFile = null;
		ObjectOutputStream writer = null;
		String fileName = "./database/CarPool.dat";
		CarImpl newCar = null;
		HashMap<Integer, CarImpl> carPool = null;
		
		boolean readSuccess = false;
		
		// prompt employee to enter car info
		try {
			System.out.println("Adding new Car to Lot");
			System.out.print("Enter Brand: ");
			brand = cin.nextLine();
			System.out.print("Enter Model: ");
			model = cin.nextLine();
			System.out.print("Enter Color: ");
			color = cin.nextLine();
			System.out.print("Enter Price: ");
			price = Double.parseDouble(cin.nextLine());
				
			// read CarPool.dat find the nextId
			try {
				inFile = new FileInputStream(fileName);
				reader = new ObjectInputStream(inFile);
				carPool = (HashMap<Integer, CarImpl>) reader.readObject(); 	// cast suppress
				int nextId = Collections.max(carPool.keySet()) + 1;
				
				// create newCar object
				newCar = new CarImpl(nextId, brand, model, color, price, new ArrayList<UserImpl>());
				
				// push newCar into carPool<carId, newCarObj>
				carPool.put(nextId, newCar);
				info("Add Car: Successfully add newCar to carPool ["+newCar.toString()+"]");
				readSuccess = true;
			}catch (FileNotFoundException e) {
				error("Add Car: fail to read CarPool.dat due to file doesn't exist");
				return false;
			}catch (IOException e) {
				error("Add Car: fail to read existing carPool");
				return false;
			} finally {
				reader.close();
				inFile.close();
			}
		} catch(Exception e) {
			error("Add Car: fail to get user input of car info");
			return false;
		}
		
		// write updated carPool to CarPool.dat
		if (readSuccess) {
			try {
				outFile = new FileOutputStream(fileName);
				writer = new ObjectOutputStream(outFile);
				writer.writeObject(carPool);
				info("Add Car: Succussfully write carPool to CarPool.dat ["+newCar.toString()+"]");
				return true;
			} catch (IOException e) {
				error("Add Car: fail to write to CarPool.dat due to IO Error");
				return false;
			}finally {
				try {
					writer.close();
					outFile.close();
				} catch (IOException e) {
					error("Add Car: fail to close writer or outFile after updating CarPool");
				}
				
			}
		}
		return false;
	}
	
//	View cars on lot, and update CarPool.dat
//	return an ArrayList of all cars on lot, for further access
//	1. open CarPool.dat grab the HashMap of all cars
//	2. among those cars find the cars that car.owner == null meaning no owner yet
//	3. store no-owner cars into an ArrayList and return
	@SuppressWarnings("unchecked")
	public ArrayList<CarImpl> getCarLot() {
		String fileName = "./database/CarPool.dat";
		HashMap<Integer, CarImpl> carPool = null;
		ArrayList<CarImpl> carLot = new ArrayList<CarImpl>();
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		
		try {
			// read CarPool.dat retrieve HashMap carPool
			inFile = new FileInputStream(fileName);
			reader = new ObjectInputStream(inFile);
			carPool = (HashMap<Integer, CarImpl>) reader.readObject();		// cast suppress	
			// retrieve each car in carPool, if car.owner == null meaning no owner, then store into ArrayList carLot
			for (int key: carPool.keySet()) {
				if (carPool.get(key).getOwner() == null) {
					carLot.add(carPool.get(key));
				}
			}
		}catch (FileNotFoundException e) {
			error("View Carlot: fail to read CarPool.dat due to file doesn't exist");
		}catch (IOException e) {
			error("View Carlot: fail to read CarPool.dat dut to IO Error");
		}catch (ClassNotFoundException e) {
			error("View Carlot: fail to retrieve carPool due to ClassNotFound Error");
		}finally {
			try {
				reader.close();
				inFile.close();
			} catch (IOException e) {
				error("View Carlot: fail to close read or file after retrieving cars on lot");
			}
		}
		
		// display cars on lot
		this.displayCarLot(carLot);
		
		return carLot;
	}
	
//	customer make offer on a car, update CarImpl object, and update CarPool.dat
//	1. prompt customer to enter desired car number
//	2. update car.pendingList
//	3. Open CarPool.dat, retrieve carPool HashMap
//	4. update pendingList of selected car
	@SuppressWarnings("unchecked")
	public boolean makeOffer(ArrayList<CarImpl> carLot, UserImpl user, Scanner cin) {
		// prompt customer to choose a car
		System.out.print("Enter # of Car to Make an Offer: ");
		int choice = Integer.parseInt(cin.nextLine());
		
		// retrieve selected car from ArrayList, and update its pendingList
		CarImpl carChosen = carLot.get(choice-1);
		List<UserImpl> pendingList = carChosen.getPendingList();
		pendingList.add(user);
		// open CarPool.dat find existing car object and update it
		String fileName = "./database/CarPool.dat";
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		FileOutputStream outFile = null;
		ObjectOutputStream writer = null;
		HashMap<Integer, CarImpl> carPool = null;
		try {
			// open CarPool
			inFile = new FileInputStream(fileName);
			reader = new ObjectInputStream(inFile);
			carPool = (HashMap<Integer, CarImpl>) reader.readObject();		// cast suppress
			// find the selected car via car.id and update its pendingList
			carPool.get(carChosen.getId()).setPendingList(pendingList);
			info("Make Offer(customer): successfully updated carPool HashMap ["+carChosen.toString()+"]");
			
			// writer updated carPool to CarPool.dat
			try {
				outFile = new FileOutputStream(fileName);
				writer = new ObjectOutputStream(outFile);
				writer.writeObject(carPool);
				info("Make Offer(customer): successfully write updated carPool to CarPool.dat");
				return true;
			}catch (IOException e) {
				error("Make Offer(customer): fail to write CarPool.dat dut to IO Error");
				return false;
			}finally {
				try {
					writer.close();
					outFile.close();
				} catch (IOException e) {
					error("Make Offer(customer): fail to close writer or file after writting carPool");
				}
			}
		}catch (FileNotFoundException e) {
			error("Make Offer(customer): fail to read CarPool.dat due to file doesn't exist");
			return false;
		}catch (IOException e) {
			error("Make Offer(customer): fail to read CarPool.dat dut to IO Error");
			return false;
		}catch (ClassNotFoundException e) {
			error("Make Offer(customer): fail to retrieve carPool due to ClassNotFound Error");
			return false;
		}finally {
			try {
				reader.close();
				inFile.close();
			} catch (IOException e) {
				error("Make Offer(customer): fail to close reader or file at end of Making Offer");
			}
		}
	}
	
//	Employee reject an offer, receive the list of car on carLot, return true|false
//	1. display car list
//	2. prompt employee to enter car number to modify
//	3. display customers on pending list
//	4. prompt employee to enter customer number to reject
//	5. delete selected offer
//	6. open CarPool, and update
	@SuppressWarnings("unchecked")
	public boolean rejectOffer(ArrayList<CarImpl> carLot, Scanner cin) {
		CarImpl selectedCar = null;
		List<UserImpl> pendingList = null;
		String fileName = "./database/CarPool.dat";
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		FileOutputStream outFile = null;
		ObjectOutputStream writer = null;
		
		// prompt employee to select a car 
		System.out.print("Enter # of Car: ");
		int carSelect = Integer.parseInt(cin.nextLine());
		selectedCar = carLot.get(carSelect-1);
		// display pending customer name 
		pendingList = selectedCar.getPendingList();
		this.displayPendingList(pendingList);
		// prompt employee to enter customer number to reject offer
		System.out.println("Enter # of User to Reject the Offer: ");
		int userSelect = Integer.parseInt(cin.nextLine());
		pendingList.remove(userSelect-1);
		// open CarPool.dat read HashMap carPool
		try {
			inFile = new FileInputStream(fileName);
			reader = new ObjectInputStream(inFile);
			HashMap<Integer, CarImpl> carPool = (HashMap<Integer, CarImpl>) reader.readObject();		// cast suppress
			// updated selectedCar in carPool
			carPool.get(selectedCar.getId()).setPendingList(pendingList);
			info("Reject Offer: successfully updated pendinglist of car ["+selectedCar.toString()+"]");
			
			// write updated carPool to CarPool.dat
			try {
				outFile = new FileOutputStream(fileName);
				writer = new ObjectOutputStream(outFile);
				writer.writeObject(carPool);
				info("Reject Offer(employee): successfully write updated carPool to CarPool.dat");
				return true;
			} catch (IOException e) {
				error("Reject Offer(employee): file to write to CarPool.dat due to IO Error");
				return false;
			} finally {
				writer.close();
				outFile.close();
			}
		} catch (FileNotFoundException e) {
			error("Reject Offer(employee): fail to open file CarPool.dat due to FileNotFound Error");
			return false;
		} catch (IOException e) {
			error("Reject Offer(employee): fail to read carPool due to IO error");
			return false;
		} catch (ClassNotFoundException e) {
			error("Reject Offer(employee): fail to retrieve carPool due to ClassNotFound Error");
			return false;
		} finally {
			try {
				reader.close();
				inFile.close();
			} catch (IOException e) {
				error("Reject Offer(employee): fail to close reader or file at the end of Reject Offer");
			}
			
		}
	}
	
//	remove a car from lot
//	1. open CarPool.dat retrieve all the car HashMap carPool
//	2. find all the car on lot, car.owner=null and save into ArrayList
//	3. prompt employee to enter car # to remove
//	4. find the removed car in HashMap carPool and delete
//	5. write updated carPool to file
	public CarImpl removeCar(Scanner cin) {
		HashMap<Integer, CarImpl> carPool = null;
		ArrayList<CarImpl> carLot = null;
		
		// get cars on lot and display
		carLot = this.getCarLot();
		// prompt employee to enter # of car to remove
		System.out.println("Enter # of Car");
		int carSelected = 0;
		if (cin.hasNext())
			carSelected = Integer.parseInt(cin.nextLine());
		CarImpl removedCar = carLot.get(carSelected-1);
		info("Remove Car(employee): successfully selected car ["+removedCar.toString()+"]");
		
		// find removed car in carPool and remove
		carPool = readCarPool();
		carPool.remove(removedCar.getId());
		updateCarPool(carPool);
		info("Remove Car(employee): successfully removed car from lot ["+removedCar.toString()+"]");
		return removedCar;
	}
	
//	employee accept an offer
//	1. find all the cars on lot and save into ArrayList carLot
//	2. prompt employee to enter car # to accept offer on it
//	3. display pendingList of the CarImpl
//	4. prompt employee to enter customer # to accept offer
//	5. clear pendingList of the car, update owner of the car
//	6. retrieve carPool and update modified car
//	7. read UserPool, find the user and update user.carList
	public void acceptOffer(List<CarImpl> carLot, Scanner cin) {
		HashMap<Integer, CarImpl> carPool = null;
		HashMap<String, UserImpl> userPool = null;
		int c = 0;
		
		// prompt employee to choose which car to make offer
		System.out.println("Enter # Car: ");
		c = Integer.parseInt(cin.nextLine());
		carLot = this.getCarLot();
		CarImpl carSelect = carLot.get(c-1);
		// get and display pendingList of the car
		List<UserImpl> pendingList = carSelect.getPendingList();
		this.displayPendingList(pendingList);
		// prompt employee to choose which customer got the offer
		System.out.println("Enter # of Offer to Accpet: ");
		c = Integer.parseInt(cin.nextLine());
		UserImpl offerUser = pendingList.get(c-1);
		// add offered car to user.carList
		BoughtCar boughtCar = new BoughtCar(carSelect.getId(), carSelect.getBrand(), carSelect.getModel(), carSelect.getColor(), carSelect.getPrice(), 0.0);
		offerUser.getCarList().add(boughtCar);
		// update car.owner 
		carSelect.setOwner(offerUser);
		info("Accept Offer: successfully accepted an offer for ["+offerUser.getUserId()+"], ["+carSelect.toString()+"] is sold");
		// clear pendingList of car
		carSelect.getPendingList().clear();
		// update HashMap carPool
		carPool = readCarPool();
		carPool.replace(carSelect.getId(), carSelect);
		updateCarPool(carPool);
		// get HashMap userPool and update it
		userPool = this.readUserPool();
		// update customer got the offer and save to file
		userPool.replace(offerUser.getUserId(), offerUser);
		info("Accept Offer: successfully update CarPool and UserPool");
		this.updateUserPool(userPool);
	}
	
//	customer view the cars they own, receive UserImpl as parameter
//  1. receive customer as argument
//	2. display cust.List<BoughtCar> 
	public void displayCustCarList(UserImpl cust) {
		List<BoughtCar> carList = cust.getCarList();
		String header = String.format("%3s|%10s|%10s|%10s|%10s|%10s", "#", "Brand", "Model", "Color", "Price", "Paid Amt");
		System.out.println(header);
		System.out.println("----------------------------------------");
		if (carList != null) {
			for (int i=0; i<carList.size(); i++) {
				System.out.print(String.format("%3s|", i+1));
				System.out.println(carList.get(i).display());
			}
		}
		else {
			System.out.println("Bought List is empty, you don't own any car yet");
		}
	}
	
//	employee view all transactions
//	1. open Transaction.dat
//	2. retrieve all transactions and store into ArrayList<TransactionImpl>
//	3. display ArrayList<TransactionImpl>
	@SuppressWarnings("unchecked")
	public void viewTransaction() {
		String tranFile = "./database/TranPool.dat";
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		HashMap<Integer, TransactionImpl> trans = null;
		
		
		try {
			// open car, user, transaction files and get HashMap for each
			inFile = new FileInputStream(tranFile);
			reader = new ObjectInputStream(inFile);
			trans = (HashMap<Integer, TransactionImpl>) reader.readObject();
			this.dispalyTransList(trans);			
		} catch (FileNotFoundException e) {
			error("View Trans(employee): fail to view transactions due to FileNotFound");
		} catch (IOException e) {
			error("View Trans(employee): fail to view transactions due to IO Error");
		} catch (ClassNotFoundException e) {
			error("View Trans(employee): fail to view transactions due to data casting error");
		} finally {
			try {
				reader.close();
				inFile.close();
			} catch (IOException e) {
				error("View Trans(employee): fail to close TranPool.dat after reading transactions");
			}
		}
		
	}
	
//	=========== private helper functions =========
	
//	get carPool from CarPool.dat
	@SuppressWarnings("unchecked")
	private HashMap<Integer, CarImpl> readCarPool() {
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		String fileName = "./database/CarPool.dat";
		HashMap<Integer, CarImpl> carPool = null;
		try {
			inFile = new FileInputStream(fileName);
			reader = new ObjectInputStream(inFile);
			carPool = (HashMap<Integer, CarImpl>) reader.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				inFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return carPool;
	}
	

	
//	store carPool to CarPool.dat
	private void updateCarPool(Map<Integer, CarImpl> carPool) {
		FileOutputStream outFile = null;
		ObjectOutputStream writer = null;
		String fileName = "./database/CarPool.dat";
		try {
			outFile = new FileOutputStream(fileName);
			writer = new ObjectOutputStream(outFile);
			writer.writeObject(carPool);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				outFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//	get userPool from UserPool.dat
	@SuppressWarnings("unchecked")
	private HashMap<String, UserImpl> readUserPool() {
		FileInputStream inFile = null;
		ObjectInputStream reader = null;
		String fileName = "./database/UserPool.dat";
		HashMap<String, UserImpl> userPool = null;
		try {
			inFile = new FileInputStream(fileName);
			reader = new ObjectInputStream(inFile);
			userPool = (HashMap<String, UserImpl>) reader.readObject();		// cast suppress
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				inFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userPool;
	}
	
//	store userPool to UserPool.dat 
	private void updateUserPool(Map<String, UserImpl> userPool) {
		FileOutputStream outFile = null;
		ObjectOutputStream writer = null;
		String fileName = "./database/UserPool.dat";
		try {
			outFile = new FileOutputStream(fileName);
			writer = new ObjectOutputStream(outFile);
			writer.writeObject(userPool);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				outFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//	display car list, receive ArrayList<CarImpl> carLot, display in table format
	private void displayCarLot(List<CarImpl> carLot) {
		// display cars on lot
		String header = String.format("%3s|%10s|%10s|%10s|%10s|%-20s", "#", "Brand", "Model", "Color", "Price", "Pending List");
		System.out.println(header);
		System.out.println("----------------------------------------------------------------");
		if (carLot != null) {
			for (int i=0; i<carLot.size(); i++) {
				System.out.print(String.format("%3s|", i+1));
				System.out.println(carLot.get(i).display());
			}
		}
		else {
			System.out.println("Car lot is empty, please come back later. We are working very hard to register new cars");
		}
	}
	
//	display customer list, receive ArrayList<UserImpl> pendingList, display in table format
	private void displayPendingList(List<UserImpl> pendingList) {
		String header = String.format("%3s|%10s|%20s", "#", "User ID", "Full Name");
		System.out.println(header);
		System.out.println("----------------------------------------");
		if (pendingList != null) {
			for (int i=0; i<pendingList.size(); i++) {
				System.out.print(String.format("%3s|", i+1));
				System.out.println(pendingList.get(i).display());
			}
		}
		else {
			System.out.println("Pending List is empty. No customer has made offer to this car");
		}
	}
	
//	display transactions list, receive ArrayList<TransactionImpl> trans, dispaly in table format
	private void dispalyTransList(Map<Integer, TransactionImpl> trans) {
		String header = String.format("%3s|%15s|%15s|%10s|%10s", "#", "Car", "Customer", "Amount", "Date");
		System.out.println(header);
		TransactionImpl curTran = null;
		if (trans != null) {
			for (int i : trans.keySet()) {
				curTran = trans.get(i);
				System.out.print(String.format("%3s|", i+1));
				System.out.println(String.format("%15s|%15s|%10s|%10s", curTran.getCarId(), curTran.getUserId(), curTran.getPaymentAmt(), curTran.getPayDate().toString()));
			}
		}
		else {
			System.out.println("No transaction to show at this time. Transaction List is empty.");
		}
	}

	
//	display title of car dealer
	public static void displayTitle() {
		System.out.println("*******************************************");
		System.out.println("*               Car Dealer                *");
		System.out.println("*******************************************");
	}
	
//	splitter
	public static void lineSplitter() {
		System.out.println();
		System.out.println("===========================================");
		System.out.println();
	}
	
//	run Services
	public void run() {
		Scanner in = null;						    // handle input in run function
		Scanner fin = new Scanner(System.in);		// pass into other functions 
		loginLabel:
		while (this.loggedIn == 0) {
			if (curUser == null) {
				in = new Scanner(System.in);
				Services.lineSplitter();
				Services.displayTitle();
				System.out.println("1. Login");
				System.out.println("2. Register");
				if (this.validEnter == 0) {
					System.out.println("Invalid input, Choose again...");
				}
				System.out.println("Enter # Choice: ");
				c = Integer.parseInt(in.nextLine());
			}
			// choose to login
			if (c == 1) {
				if (curUser == null) {
					curUser = login(fin);
				}
				// login successful
				else {
					// login as customer
					customerLabel:
					while (curUser.getUserType() == 0) {
						this.loggedIn = 1;
						Services.lineSplitter();
						Services.displayTitle();
						System.out.println("1. View Cars on Lot");
						System.out.println("2. Make an Offer");
						System.out.println("3. View My Cars");
						System.out.println("0. Logout");
						if (this.validEnter == 0) {
							System.out.println("Invalid input, Choose again...");
						}
						System.out.println("Enter # Choice: ");
						c = Integer.parseInt(in.nextLine());
						// view car on lot
						if (c == 1) {
							this.getCarLot();
						}
						// make an offer
						else if (c == 2) {
							if (this.makeOffer(this.getCarLot(), curUser, fin)) {
								System.out.println("Offer has been placed");
							}
						}
						else if (c == 3) {
							this.displayCustCarList(curUser);
						}
						// logout
						else if (c == 0) {
							this.curUser = null;
							this.loggedIn = 0;
							continue loginLabel;
						}
						else {
							validEnter = 0;
						}
					}
					// login as employee
					employeeLabel:
					while (curUser.getUserType() == 1) {
						this.loggedIn = 2;
						Services.lineSplitter();
						Services.displayTitle();
						System.out.println("1. Add Car to Lot");
						System.out.println("2. Remove a Car");
						System.out.println("3. Accept an Offer");
						System.out.println("4. Reject an Offer");
						System.out.println("5. View Transactions");
						System.out.println("0. Logout");
						if (this.validEnter == 0) {
							System.out.println("Invalid input, Choose again...");
						}
						System.out.println("Enter # Choice: ");
						c = Integer.parseInt(in.nextLine());
						// add car to lot
						if (c == 1) {
							validEnter = 1;
							Services.lineSplitter();
							Services.displayTitle();
							if (addCar(fin)) {
								System.out.println("New Car is Added to Lot");
							}
							else {
								System.out.println("Fail to Add Car to Lot, please try again");
							}
						}
						// remove a car from lot
						else if (c == 2) {
							validEnter = 1;
							Services.lineSplitter();
							Services.displayTitle();
							if (this.removeCar(fin) != null) {
								System.out.println("Car is deleted");
							}
							else {
								System.out.println("Fail to delete the car, please try again");
							}
						}
						// accept an offer
						else if (c == 3) {
							validEnter = 1;
							Services.lineSplitter();
							Services.displayTitle();
							ArrayList<CarImpl> carLot = this.getCarLot();
							this.acceptOffer(carLot, fin);
							System.out.println("Offer is accepted");
						}
						// reject offer
						else if (c == 4) {
							validEnter = 1;
							Services.lineSplitter();
							Services.displayTitle();
							ArrayList<CarImpl> carLot = this.getCarLot();
							if (this.rejectOffer(carLot, fin)) {
								System.out.println("Offer is rejected");
							}
							else {
								System.out.println("Fail to reject offer, please try again");
							}
						}
						// view payment
						else if (c == 5) {
							validEnter = 1;
							Services.lineSplitter();
							Services.displayTitle();
							this.viewTransaction();
							
						}
						// log out
						else if (c == 0) {
							validEnter = 1;
							this.curUser = null;
							this.loggedIn = 0;
							continue loginLabel;
						}
	
						else {
							validEnter = 0;
						}
					}
				}
			}
			// choose to register
			else if (c == 2) {
				curUser = this.createUser(fin);
				if (curUser != null) {
					System.out.println("New user created");
					this.loggedIn = 0;
					c = 1;
					continue loginLabel;
				}
				else {
					System.out.println("Failed to create new user, please try again");
				}
				
			}
			// invalid choose
			else {
				validEnter = 0;
			}
		}
		in.close();
		fin.close();
	}
}
