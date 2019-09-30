package com.cardealer.pojos;

import static com.cardealer.util.LoggerUtil.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserImpl implements Serializable {
	private static final long serialVersionUID = 4191879377524123696L;
	private String userId;
	private String password;
	private String fullName;
	private int userType;		// 0=>customer, 1=>employee
	private List<BoughtCar> carList;
	
	public UserImpl(String userId, String password, String fullName, int userType, List<BoughtCar> carList) {
		super();
		this.userId = userId;
		this.password = password;
		this.fullName = fullName;
		this.userType = userType;
		this.carList = carList;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public List<BoughtCar> getCarList() {
		return carList;
	}
	public void setCarList(List<BoughtCar> carList) {
		this.carList = carList;
	}
	
	@Override
	public String toString() {
		return "UserImpl, userId=" + userId + ", password=" + password + ", fullName=" + fullName
				+ ", userType=" + userType + ", carList=" + carList + "]";
	}
	
	public String display() {
		return String.format("%10s|%20s", this.userId, this.fullName);
		
	}
	
}
