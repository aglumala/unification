package org.algorithm.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.algorithm.intf.SimpleType;
import org.algorithm.intf.Type;


public class AbstractDictionary {

	//holds all the data types after parsing
	private static final Map<String, Type> abstractDict=new HashMap<String, Type>();
	
	
	private AbstractDictionary(){
		
	}
	
	public void addDataEntry(Type type) throws CannotAddTypeToCollectionException{
		
		if(type!=null){
			String name=type.getName();
			abstractDict.put(name, type);
		}
		
		throw new CannotAddTypeToCollectionException();
	}
	
	public boolean containsKey (String name){
		return abstractDict.containsKey(name);
	}
	public Type getDataType(String name){
		
		return abstractDict.get(name);
	}
	
}
