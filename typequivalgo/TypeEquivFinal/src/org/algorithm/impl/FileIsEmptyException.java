package org.algorithm.impl;

public final class FileIsEmptyException extends Exception {

	private String description="The file cannot be created: It doesnot exist";
	public FileIsEmptyException(){
		
	}
	public FileIsEmptyException(String desc){
		this.description=desc;
	}
	@Override
	public String toString() {
		
		return description;
	}
}
