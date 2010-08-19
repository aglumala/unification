package org.algorirthm.util;

public class TNSObject {
	private String tnsName;
	private boolean filled;
	private String parentComplex;
	private int insertionOrder;
	
	public String getTnsName() {
		return tnsName;
	}

	
	public boolean isFilled() {
		return filled;
	}


	public void setParentComplex(String parentComplex) {
		this.parentComplex = parentComplex;
	}


	public String getParentComplex() {
		return parentComplex;
	}


	public void setInsertionOrder(int insertionOrder) {
		this.insertionOrder = insertionOrder;
	}


	public int getInsertionOrder() {
		return insertionOrder;
	}
	
}
