package com.cardealer.pojos;

import java.io.Serializable;
import java.util.Date;

public class TransactionImpl implements Serializable {
	private int id;
	private String carId;
	private String userId;
	private double paymentAmt;
	private Date payDate;
	
	public TransactionImpl(int id, String carId, String userId, double paymentAmt, Date payDate) {
		super();
		this.id = id;
		this.carId = carId;
		this.userId = userId;
		this.paymentAmt = paymentAmt;
		this.payDate = payDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getPaymentAmt() {
		return paymentAmt;
	}
	public void setPaymentAmt(double paymentAmt) {
		this.paymentAmt = paymentAmt;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	@Override
	public String toString() {
		return "TransactionImpl [id=" + id + ", carId=" + carId + ", userId=" + userId + ", paymentAmt=" + paymentAmt
				+ ", payDate=" + payDate + "]";
	}
	
	public String display() {
		return null;
	}
	
}
