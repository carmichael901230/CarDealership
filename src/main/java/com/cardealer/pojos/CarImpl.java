package com.cardealer.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cardealer.pojos.UserImpl;

public class CarImpl implements Serializable {
//	private static final long serialVersionUID = 4191879377524123695L;
	protected int id;
	protected String brand;
	protected String model;
	protected double price;
	protected String color;
	protected UserImpl owner;
	protected List<UserImpl> pendingList;
	public CarImpl(int id, String brand, String model, String color, double price, ArrayList<UserImpl> arrayList) {
		super();
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.price = price;
		this.color = color;
		this.owner = null;
		this.pendingList = arrayList;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public UserImpl getOwner() {
		return owner;
	}

	public void setOwner(UserImpl owner) {
		this.owner = owner;
	}

	public List<UserImpl> getPendingList() {
		return pendingList;
	}

	public void setPendingList(List<UserImpl> pendingList) {
		this.pendingList = pendingList;
	}

	@Override
	public String toString() {
		return "CarImpl [id=" + id + ", brand=" + brand + ", model=" + model + ", price=" + price + ", color=" + color
				+ ", owner=" + owner + ", pendingList=" + pendingList + "]";
	}
	public String display() {
		String pending = "";
		for (int i=0; i<this.pendingList.size(); i++) {
			pending += pendingList.get(i).getUserId() + ", ";
		}
		return String.format("%10s|%10s|%10s|%10.2f|%-20s", brand, model, color, price, pending);
	}
}
