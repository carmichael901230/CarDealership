package com.cardealer.pojos;

import java.util.ArrayList;

public class BoughtCar extends CarImpl{

	private double paidAmt;
	
	public BoughtCar(int id, String brand, String model, String color, double price, double paidAmt) {
		super(id, brand, model, color, price, new ArrayList<UserImpl>());
		this.paidAmt = paidAmt;
	}

	public double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(double paidAmt) {
		this.paidAmt = paidAmt;
	}
	
	public String display() {
		return String.format("%10s|%10s|%10s|%10s|%10s", this.brand, this.model, this.color, this.price, this.paidAmt);
	}
}
