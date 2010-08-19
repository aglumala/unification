package org.algorithm.impl;

public class CannotAddTypeToCollectionException extends Exception{

	private String exceptionType="Cannot Add element to the set";
	public CannotAddTypeToCollectionException(){
		
	}
	@Override
	public String toString() {

		return exceptionType;
	}
}
