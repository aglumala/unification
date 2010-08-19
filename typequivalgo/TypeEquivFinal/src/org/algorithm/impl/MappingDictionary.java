package org.algorithm.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.algorithm.intf.ComplexType;
import org.algorithm.intf.Type;

public class MappingDictionary implements Serializable{

	//holds all the data types after parsing
	private static final Map<Type, Set<Type>> mappingDictionary=new HashMap<Type, Set<Type>>();
	
	public static void main(String[] args) {
		//create and persist the data dictionary
		//persist the data dictionay at the end of every mapping operation
		/*
		try {
			persistDictionary("C:/AgieSerial/mappingDictionary");
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (FileIsEmptyException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		*/
		try {
				readMappingDictionary("C:/AgieSerial/mappingDictionary");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (FileIsEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static synchronized void persistDictionary(String filename) 
	throws FileIsEmptyException, FileNotFoundException, IOException{
		
		File file=new File(filename);
		System.out.println("Exists "+file.exists());
		if(file.exists()){
			
			ObjectOutput objOut = new ObjectOutputStream(new FileOutputStream(file));
			
			//write the data dictionary to a file
			objOut.writeObject(mappingDictionary);
			//must close the stream
			objOut.close();
		}else{
			
			throw new FileIsEmptyException();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Map<Type, Set<Type>> readMappingDictionary(String filename)
	throws FileIsEmptyException, ClassNotFoundException{
		File file=new File(filename);
		Map<Type, Set<Type>> map=null;
		
		if(file.exists()){	
			ObjectInput objIn = null;
			
			try {
				
				objIn = new ObjectInputStream(new FileInputStream(file));
				//restore the object from the file
				map=(Map<Type, Set<Type>>)objIn.readObject();
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
			finally{
				try {
					objIn.close();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}
		}else{
			throw new FileIsEmptyException();
		}
		
		return map;
		
	}
	
	public static boolean isMapped(Type one, Type two){
		
		boolean mapped=false;
		Type first=null;
		Type second=null;
		Type entryKey=null;
		Type entryVal=null;
		Set<Type> keySet=mappingDictionary.keySet();
		 for (Type typeKey : keySet) {
			 entryKey=typeKey;
			Set<Type> valuesSet=mappingDictionary.get(typeKey);
			for (Type value : valuesSet) {
				entryVal=value;
				if((entryKey.equals(one)&& entryVal.equals(two)) ||
						(entryVal.equals(one) && entryKey.equals(two))){
					mapped=true;
					break;
				}else{
					if(entryKey.equals(one)){
						first=entryVal;
					}else if(entryVal.equals(one)){
						first=entryKey;
					}
					if(entryKey.equals(two)){
						second=entryVal;
					}else if(entryVal.equals(one)){
						second=entryKey;
					}
					if(first!=null && first.equals(second)){
						mapped=true;
					}else{
						continue;
					}
				}
			}
		}
		return mapped;
	}
	
	public static boolean simpleTypeEquivalence(Type one, Type two){
		
		boolean s_equiv=false;
		if(SimpleTypeImp.isSimple(one) && SimpleTypeImp.isSimple(two)){
			if(isMapped(one, two)){
				s_equiv=true;
			}else if(one.equals(two)){
				//put the two values in the mapping dictionary and set equvalence to true
				Set<Type> oneSet=new HashSet<Type>();
				oneSet.add(two);
				mappingDictionary.put(one, oneSet);
				//or use two as the key and put one in the value set.
				Set<Type> twoSet=new HashSet<Type>();
				twoSet.add(one);
				mappingDictionary.put(two, twoSet);
				s_equiv=true;
			}
		}
		return s_equiv;
	}
	
	public static boolean complexTypeEquivalence(Type one, Type two){
		boolean sc_equiv=false;
		if(SimpleTypeImp.isSimple(one)){
			sc_equiv=isSimpleComplexTypeEquivalent(one, two);
		}else if(SimpleTypeImp.isSimple(two)){
			sc_equiv=isSimpleComplexTypeEquivalent(one, two);
		}else{
			sc_equiv=isComplexComplexTypeEquivalence(one, two);
		}
		return sc_equiv;
		
	}
	
	private static boolean isComplexComplexTypeEquivalence(Type one, Type two) {
		boolean comp_equiv=false;

		if(ChoiceType.isChoiceType(one)){
			if (ChoiceType.isChoiceType(two)) {
				
				comp_equiv=isChoiceTypeEquivalent(one, two);
				
			} 
		}else if (AllComplexType.isAllComplex(one) && 
				AllComplexType.isAllComplex(two)) {
				AllComplexType complexOne=(AllComplexType)one;
				AllComplexType complexTwo=(AllComplexType)two;
				comp_equiv=complexOne.equals(complexTwo);
			
		} else if (AllComplexType.isAllComplex(one)&&
				SequenceType.isSequence(two)) {
			AllComplexType comp=(AllComplexType)one;
			SequenceType seq=(SequenceType)two;
			List<Type> seqSet=seq.getElements();
			
			List<Type> allComplexList=comp.getElements();
			for (Type type : allComplexList) {
				//System.out.println("CONTAINS "+((SimpleTypeImp)type).getName());
				if(!seqSet.contains(type)){
					break;
				}else{
					comp_equiv=true;
				}
			}
			
		
		} else if (SequenceType.isSequence(one)&&SequenceType.isSequence(two)){ //it's a sequence, sequence comparison.
			SequenceType seq1=(SequenceType)one;
			SequenceType seq2=(SequenceType)two;
			comp_equiv=seq1.equals(seq2);
			
		}else{
			
		}
		if (comp_equiv) {
			//update type one mapping in the dictionary
			if(mappingDictionary.keySet().contains(one)){
				Set<Type> oneMappingSet=mappingDictionary.get(one);
				oneMappingSet.add(two);
				
			}else{
				Set<Type> typeMapping=new HashSet<Type>();
				typeMapping.add(two);
				mappingDictionary.put(one, typeMapping);
				
			}
			//update type one mappinh in the dictionary
			if(mappingDictionary.keySet().contains(two)){
				Set<Type> oneMappingSet=mappingDictionary.get(two);
				oneMappingSet.add(one);
			}else{
				Set<Type> typeMapping=new HashSet<Type>();
				typeMapping.add(one);
				mappingDictionary.put(two, typeMapping);
			}
		}
		return comp_equiv;
	}

	public static boolean isSimpleComplexTypeEquivalent(Type one, Type two){
		
		boolean sc_equiv=false;
		if(SimpleTypeImp.isSimple(one)){
			if(AllComplexType.isAllComplex(two) || SequenceType.isSequence(two)){
				sc_equiv=false;
				
			}else if(ChoiceType.isChoiceType(two)){
				
				ChoiceType choice=(ChoiceType)two;
				
				if(!isMapped(one, two) || !choice.choiceContains(one)){
					
					sc_equiv=false;
					
				}else if(!isMapped(one, two) && choice.choiceContains(one)){ //not mapped but contains that element
					
					if(mappingDictionary.keySet().contains(two)){
						Set<Type> mappingSet=mappingDictionary.get(two);
						mappingSet.add(one);
						sc_equiv=true;
					}else{
						//update the mapping dictionary
						Set<Type> mappingSet=new HashSet<Type>();
						mappingSet.add(one);
						mappingDictionary.put(two, mappingSet);
						sc_equiv=true;
					}
				}
			}
		}
		return sc_equiv;
	}
	
	public static boolean intersect(Type one, Type two){
		boolean isIntersect=false;
		if(one instanceof ChoiceType && two instanceof ChoiceType){
			ChoiceType typeOne=(ChoiceType)one;
			ChoiceType typeTwo=(ChoiceType)two;
			List<Type> typeOneElts=typeOne.getElements();
			List<Type> typeTwoElts=typeTwo.getElements();
			
			for (Iterator<Type> iterator = typeOneElts.iterator(); iterator.hasNext();) {
				
				Type type = iterator.next();
				
				if(typeTwoElts.contains(type)){
					System.out.println("They Intersect");
					isIntersect=true;
					break;
				}
			}
		}
		return isIntersect;
	}
	
	private static Type priorMapping(Type one){
		
		//Set<Type> priorMappingSet=new HashSet<Type>();
		Type priorMapping=null;
		Set<Type> keySet=mappingDictionary.keySet();
		for (Iterator<Type> iterator = keySet.iterator(); iterator.hasNext();) {
			Type key = iterator.next();
			Set<Type> mappedValuesSet=mappingDictionary.get(key);
			if(mappedValuesSet.contains(one)){
				//add that value to prior mapping set
				//we might have the type mapped to other types but not the one 
				//we are trying to check the mapping.
				//priorMappingSet.add(key);
				priorMapping=key;
				break;
			}
		}
		//return priorMappingSet;
		return priorMapping;
	}

	private  static boolean randomChoice(Type one, Type two){
		
		Type chosenType=null;
		List<Type>commonElements=new ArrayList<Type>();
		if(one instanceof ChoiceType && two instanceof ChoiceType){
			ChoiceType typeOne=(ChoiceType)one;
			ChoiceType typeTwo=(ChoiceType)one;
			List<Type> typeOneElements=typeOne.getElements();
			List<Type> typeTwoElements=typeTwo.getElements();
			
			for (Type typeElement : typeOneElements) {
				if(typeTwoElements.contains(typeElement)){
					commonElements.add(typeElement);					
				}
			}
			
			//randomly choose a value from the list of common elements.
			if(commonElements.size()!=0){
				int size=commonElements.size();
				Random random=new Random();
				int index=random.nextInt(size);
				chosenType=commonElements.get(index);
				
			}else{
				System.out.println("there are no common elements randomChoice(Type one, Type two) method");
			}
			//map both Types to the chosen common random component
			//update dictionary
			if(mappingDictionary.keySet().contains(chosenType)){
				Set<Type> mappingSet=mappingDictionary.get(chosenType);
				mappingSet.add(one);
				mappingSet.add(two);
				
			}else{
				Set<Type> typeMapping=new HashSet<Type>();
				typeMapping.add(one);
				typeMapping.add(two);
				mappingDictionary.put(chosenType, typeMapping);
			}
			
			
		}else{
			System.out.println("The two types are not both choice types");
		}
		
		
		return true;
	}
	
	private static boolean completeMapping(Type one, Type priorTypeMapping){		
		boolean compMapping=false;
		if(one instanceof ChoiceType ){
			ChoiceType typeOne=(ChoiceType)one;
			List<Type> typeOneElts=typeOne.getElements();
			
				if(typeOneElts.contains(priorTypeMapping)){
					
					//update dictionary
					if(mappingDictionary.keySet().contains(priorTypeMapping)){
						Set<Type> mappingSet=mappingDictionary.get(priorTypeMapping);
						mappingSet.add(one);
						compMapping=true;
					}else{
						Set<Type> typeMapping=new HashSet<Type>();
						typeMapping.add(one);
						mappingDictionary.put(priorTypeMapping, typeMapping);
						compMapping=true;
					}
				}else{
					System.out.println("method: completeMapping(Type one, Type priorTypeMapping)" +
							"priorMapping type is not an element in the other type");
				}
			
		}
		return compMapping;
	}
	
	public static boolean isChoiceTypeEquivalent(Type choiceTypeOne, Type choiceTypeTwo){
		
		boolean ch_equiv=false;
		if(isMapped(choiceTypeOne, choiceTypeTwo)){
			ch_equiv=true;		
		}
		if(!intersect(choiceTypeOne, choiceTypeTwo)){
			ch_equiv=false;
			
		} else {
			Type choiceTypeOnePriorMapping=priorMapping(choiceTypeOne);
			Type choiceTypeTwoPriorMapping=priorMapping(choiceTypeTwo);
			
			if (choiceTypeOnePriorMapping==null &&
					choiceTypeTwoPriorMapping==null) {
				ch_equiv=randomChoice(choiceTypeOne, choiceTypeTwo);
				
			} else if(choiceTypeOnePriorMapping==null){
				ch_equiv=completeMapping(choiceTypeOne, choiceTypeTwoPriorMapping);
				
			}else if (choiceTypeTwoPriorMapping==null) {
				ch_equiv=completeMapping(choiceTypeTwo, choiceTypeOnePriorMapping);
			} 
		}
		return ch_equiv;
	}
		
}
