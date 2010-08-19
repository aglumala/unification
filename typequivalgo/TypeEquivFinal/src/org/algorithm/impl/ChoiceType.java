package org.algorithm.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.algorithm.impl.SimpleTypeImp.Simple;
import org.algorithm.intf.ComplexType;
import org.algorithm.intf.Type;

public class ChoiceType implements ComplexType {

	private static List<Type> elementTypes=new ArrayList<Type>();
	private String name=null;
	
	public ChoiceType(){
		
	}
	public ChoiceType(String name){
		this.setName(name);
	}
	

	public String getTypeName(){
		return ComplexType.ELEMENT_TYPE_SEQUENCE;
	}
	public List<Type> getElements(){
		return elementTypes;
	}

	
	@Override
	public String getName() {	
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}
	
	public void addElementType(String elementType, String elementName) throws CannotAddTypeToCollectionException{
		
		if(elementType.equalsIgnoreCase(ELEMENT_TYPE_SEQUENCE)){
			
			
			
		}else if(elementType.equalsIgnoreCase(ELEMENT_TYPE_ALLCOMPLEX)){
			AllComplexType ctype=new AllComplexType(elementName);
			elementTypes.add(ctype);
			
		}else if((Simple.getSimpleType(elementType))!=null){
			SimpleTypeImp type=new SimpleTypeImp(elementType, elementName);
			elementTypes.add(type);
		}
	}
	
	public boolean choiceContains(Type simpleType){
		
		if(simpleType instanceof SimpleTypeImp){
			return elementTypes.contains(simpleType);
		}
		return false;
	}
	@Override
	public boolean equals(Object obj) {

			if(obj!=null && obj instanceof ChoiceType){
				
				ChoiceType complexObj=(ChoiceType)obj;
				List<Type> elements=complexObj.getElements();
				/*if(elements.size()!=elementTypes.size()){
					return false;
				}
				*/
				//the order must be the same.
				Type[]typeArray=(Type[])elementTypes.toArray();
				Type[]typeArray2=(Type[])elements.toArray();
				for (int i=0;i<elementTypes.size();i++) {
					//the elements in both arrays must be in the same order
					
					if(!typeArray2[i].equals(typeArray[i])){
						return false;
					}
				}
				
			}
			return true;
		}
	@Override
	public int hashCode() {
			
			return this.getName().hashCode()+elementTypes.hashCode();
	}
	public static boolean isChoiceType(Type type){

		if(type instanceof ChoiceType){
			return true;
		}
		return false;
	}



}

