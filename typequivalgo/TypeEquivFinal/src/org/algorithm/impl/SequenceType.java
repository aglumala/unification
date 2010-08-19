package org.algorithm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.algorithm.impl.SimpleTypeImp.Simple;
import org.algorithm.intf.ComplexType;
import org.algorithm.intf.Type;

public class SequenceType implements ComplexType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//maintains the order in which elements are inserted
	private  List<Type> elementTypes=new ArrayList<Type>();
	
	
	private String name=null;

	public SequenceType(){
		
	}

	public SequenceType(String name){
		
		this.setName(name);
	}

	@Override
	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getTypeName(){
		return this.getName();
	}
	public List<Type> getElements(){
		return elementTypes;
	}

	public void addElementType(String elementType, String elementName) throws CannotAddTypeToCollectionException{
		
		//System.out.println("Adding element Type ("+elementType+") Name= ("+elementName+")");
	
			
		if( elementType.equalsIgnoreCase(ELEMENT_TYPE_CHOICE)){
			
			ChoiceType type=new ChoiceType(elementName);
			elementTypes.add(type);
			
		}else if(elementType.equalsIgnoreCase(ELEMENT_TYPE_ALLCOMPLEX)){
			AllComplexType ctype=new AllComplexType(elementName);
			
			elementTypes.add(ctype);
			
		}else if((Simple.getSimpleType(elementType))!=null){
			
			//System.out.println("Calling SequenceType: Simple element added");
			
			SimpleTypeImp type=new SimpleTypeImp(elementType, elementName);
			elementTypes.add(type);
		}
		
	}

	@Override
		public boolean equals(Object obj) {
		System.out.println(obj);
		System.out.println(this);
			if(obj!=null && obj instanceof SequenceType){
				SequenceType complexObj=(SequenceType)obj;
				List<Type> elements=complexObj.getElements();
				if(elements.size()!=elementTypes.size()){
					return false;
				}
			
			
				//the order must be the same.
				Object[]typeArr=elementTypes.toArray();
				
				Type[] typeArray=new Type[typeArr.length];
				
				for (int i = 0; i < typeArr.length; i++) {
					typeArray[i]=(Type)typeArr[i];
				}
				
				Object[]typeArr2=elements.toArray();
				
				Type[]typeArray2=new Type[typeArr2.length];
				
				for (int i = 0; i < typeArr2.length; i++) {
					typeArray2[i]=(Type)typeArr2[i];

				}
				
				for (int i=0;i<typeArray.length;i++) {
					
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

	public static boolean isSequence(Type type){

		if(type instanceof SequenceType){
			return true;
		}
		return false;
	}

	
	@Override
	public String toString() {
		
		StringBuffer sb=new StringBuffer();
		for (Iterator iterator = elementTypes.iterator(); iterator.hasNext();) {
			Type type = (Type) iterator.next();
			if(type instanceof SimpleTypeImp) {
				SimpleTypeImp simple = (SimpleTypeImp) type;
				sb.append(simple.getTypeName()).append(" ").append(simple.getName()).append(" ");
			}
			
			
		}
		return sb.toString();
	}
	
}
