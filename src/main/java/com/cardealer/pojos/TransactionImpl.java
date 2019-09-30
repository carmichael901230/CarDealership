package com.cardealer.pojos;

import java.util.Date;

public class TransactionImpl {
	private int id;
	private int carId;
	private int userId;
	private double paymentAmt;
	private Date payDate;
	
	public TransactionImpl(int id, int carId, int userId, double paymentAmt, Date payDate) {
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
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
	
	
}
