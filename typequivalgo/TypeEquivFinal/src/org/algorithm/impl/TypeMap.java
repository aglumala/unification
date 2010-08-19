package org.algorithm.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.algorithm.intf.Type;

public class TypeMap implements Serializable {

	private  Map<String, Type> localMap=new HashMap<String, Type>();
	
	private String fileName;
	public TypeMap(String fileName, Map<String, Type> localMap){
		this.fileName=fileName;
		this.localMap=localMap;
	}

	public void setLocalMap(Map<String, Type> localMap) {
		this.localMap = localMap;
	}

	public Map<String, Type> getLocalMap() {
		return localMap;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof TypeMap){
			TypeMap map=(TypeMap)obj;
			if(map.getFileName().equals(fileName)){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		
		return fileName.hashCode();
	}
}
