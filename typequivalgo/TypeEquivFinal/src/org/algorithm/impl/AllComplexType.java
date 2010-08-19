package org.algorithm.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.algorithm.impl.SimpleTypeImp.Simple;
import org.algorithm.intf.ComplexType;
import org.algorithm.intf.Type;

public class AllComplexType implements ComplexType {
	
/**
	 * 
	 */
private static final long serialVersionUID = 1L;

private String name=null;

private List <Type> elementTypes=new ArrayList <Type>();

	public AllComplexType(){
	
	}
	public AllComplexType(String allComplexName){
		this.setName(allComplexName);
	}

	public String getName(){
		return this.name;
	}
	public String getTypeName(){
		return AllComplexType.ELEMENT_TYPE_ALLCOMPLEX;
	}
public List<Type> getElements(){
	return this.elementTypes;
}
@Override
public void addElementType(String elementType, String elementName) throws CannotAddTypeToCollectionException{
	
	if(!elementType.equalsIgnoreCase(ELEMENT_TYPE_CHOICE)&&
			!elementType.equalsIgnoreCase(ELEMENT_TYPE_ALLCOMPLEX)&&
			!elementType.equalsIgnoreCase(ELEMENT_TYPE_SEQUENCE)){
		
		if((Simple.getSimpleType(elementType))!=null){
			Type type=new SimpleTypeImp(elementType, elementName);
			this.elementTypes.add(type);
		}else{
			System.out.println("SIMPLE TYPE IS NULL");
		}
	}else{
		throw new CannotAddTypeToCollectionException();
	}
	
	
}

@Override
	public boolean equals(Object obj) {

		boolean isEqual=false;
		if(obj!=null && obj instanceof AllComplexType){
			AllComplexType complexObj=(AllComplexType)obj;
			List <Type> elements=complexObj.getElements();
			
			for (Type type : elements) {
				//if it doesnot contain any of the elements of the object, then they are not equal
				if(!elementTypes.contains(type)){
					return isEqual;
				}else{
					isEqual=true;
					continue;
				}
			}
			
		}
		return isEqual;
	}
@Override
	public int hashCode() {
		
		return this.name.hashCode()+elementTypes.hashCode();
	}

public static boolean isAllComplex(Type type){

	if(type instanceof AllComplexType){
		return true;
	}
	return false;
}
public void setName(String name) {
	this.name = name;
}

}
