package org.algorirthm.util;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.algorithm.impl.CannotAddTypeToCollectionException;
import org.algorithm.impl.SequenceType;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import sun.security.action.GetLongAction;
import unif.type.AllComplexType;
import unif.type.ChoiceComplexType;
import unif.type.ComplexTypeImpl;
import unif.type.SequenceComplexType;
import unif.type.SimpleType;
import unif.type.SimpleTypeImpl;
import unif.type.Type;
import unif.type.TypeList;
import unif.type.TypeMapper;
import unif.type.TypeSet;

//Main Parser class for wsdl
public class WSDLParser {
	/*map contains all the Types obtained from a given wsdl.
	 * You could persist it every time you finish parsing a wsdl to permanently store parsed records.
	 */
	private static Map<String, Type> localMap=new HashMap<String, Type>();
	public static Map<String, Type> getTypeMap(){
		return localMap;
	}

public static void process(Element element, Map<String, Type> localMap) {
	
	// Recursively descend the tree
    //inspect(element);// working fine
	inspectHybrid(element, localMap);
    
    List content = element.getContent();
    Iterator iterator = content.iterator();
    
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof Element) {
        Element child = (Element) o;
        process(child, localMap);
        
      }
    }
   
    
  }

  public static void inspectHybrid(Element element, Map<String, Type> localMap) { 
	  
	  
	  if (!element.isRootElement()) {
	      
	      System.out.println(); 
	  }
	  
	  	
	  	SimpleTypeImpl simple=null;
	  	SequenceComplexType seqComplex=null;
	  	
	   
	    String qualifiedName = element.getQualifiedName();
	    System.out.println(" "+qualifiedName + ":");
	    
	    String type=null;
	    Namespace namespace = element.getNamespace();
	    if (namespace != Namespace.NO_NAMESPACE) {
	      String localName = element.getName();
	      
	      String elementName=element.getAttributeValue("name");
	      System.out.println(" Local name: " + localName);
	      System.out.println(" Element name ( "+elementName+")");
	      
	      Element parentElement=element.getParent();
	     
	      //handle sequence element <schema><complex><sequence><element>...</element></sequence></complex>....</schema>
	      if(parentElement!=null && parentElement.getName().equals("sequence")){
	    	  
	    	  type=getTypeAttribute(element);
	    	  
	    	  //Create a Sequence type
	    	  //seqComplex=new SequenceComplexType();
	    	  
	    	  System.out.println(" TYPE "+type);    	  
	    	  System.out.println(" Sequence Element ("+parentElement.getQualifiedName()+")");
	    	  Element parent2=parentElement.getParent();
	    	  
	    	  if(parent2!=null && parent2.getName().equals("complexType")){
	    		 
	    		  System.out.println(" Complex Element "+parent2.getQualifiedName());
	    		  Element parent3=parent2.getParent();
	    		  
	    		  if(parent3!=null && parent3.getName().equals("element")){
	    			  
	    			  System.out.println(" TYPE 1");
	    			  String complexTypeName=parent3.getAttributeValue("name");
	    			  System.out.println();
	    			  
	    			if(type!=null){
	    							
	    				  simple=new SimpleTypeImpl(type);
	    				  if(localMap.containsKey(complexTypeName)){
	    					  Type complexType=localMap.get(complexTypeName);
	    					  if(complexType instanceof SequenceComplexType){
	    						  ((SequenceComplexType)complexType).addElements(simple);
	    						  //update the hashmap
	    						  System.out.println("UPDATING SEQUENCE "+complexTypeName +" WITH ELEMENT "+simple.getSimpleType());
	    						  localMap.put(complexTypeName, complexType);
	    					  }
	    				  }else{	  
	    					//Create a Sequence type
	    					  System.out.println("NEW SEQUENCE "+complexTypeName +" WITH ELEMENT "+simple.getSimpleType());
	    			    	  seqComplex=new SequenceComplexType();
	    			    	  seqComplex.setName(complexTypeName);
	    			    	  seqComplex.addElements(simple);
	    			    	  localMap.put(complexTypeName, seqComplex);
	    			    	  //typeSet.addComponent(seqComplex);
	    				  }
	    				    				 
	    			  	  System.out.println(" Sequence Element=("+parent3.getName()+") Name =("+complexTypeName+")");
	    			  
	    				}
	    			
	    		  }else if(parent3!=null && parent3.getName().equals("schema")){
	    			  
	    			  System.out.println("TYPE 2 Sequence type");
	    			  
	    			  String complexTypeName=parent2.getAttributeValue("name");    			  
	    			  System.out.println("Parent Element "+parent2.getName()+" Name "+complexTypeName);
	    			  
	    			  if(type!=null){
	    				  //set the name of the sequence complex type
		    			 // simple=new SimpleTypeImpl(type);
		    			//seqComplex.addElements(simple);
		    			//seqComplex.setName(complexTypeName);
	    				  simple=new SimpleTypeImpl(type);
	    				  if(localMap.containsKey(complexTypeName)){
	    					  Type complexType=localMap.get(complexTypeName);
	    					  if(complexType instanceof SequenceComplexType){
	    						  ((SequenceComplexType)complexType).addElements(simple);
	    						  //update the hashmap
	    						  System.out.println("UPDATING SEQUENCE "+complexTypeName +" WITH ELEMENT "+simple.getSimpleType());
	    						  localMap.put(complexTypeName, complexType);
	    					  }
	    				  }else{	  
	    					//Create a Sequence type
	    					  System.out.println("NEW SEQUENCE "+complexTypeName +" WITH ELEMENT "+simple.getSimpleType());
	    			    	  seqComplex=new SequenceComplexType();
	    			    	  seqComplex.setName(complexTypeName);
	    			    	  seqComplex.addElements(simple);
	    			    	  localMap.put(complexTypeName, seqComplex);
	    			    	  //typeSet.addComponent(seqComplex);
	    				  }
		    			
		    				  
		    			System.out.println(" Sequence Element=("+parent3.getName()+") Name =("+complexTypeName+")");
		    			  
		    			}
	    			  System.out.println();
	    		  }
	    		  
	   //handle sequence element <schema><complex><complexContent><extension><sequence><element>...
	    //</element></sequence></extension></complexContent></complex>....</schema>
	    	      	  
	    	  }else if(parent2!=null && parent2.getName().equals("extension")){
	    		  
	    		  Element parentExt=parent2.getParent();
	    		  if(parentExt!=null && parentExt.getName().equals("complexContent")){
	    			  
	    			  Element parentCmplxContent=parentExt.getParent();
	    			  
	    			  if(parentCmplxContent!=null && parentCmplxContent.getName().equals("complexType")){
	    				  //get the name of the complex type and create a sequence
	    				  	
	    				  String complexTypeName=parentCmplxContent.getAttributeValue("name");
	    				  System.out.println("PROCESSING A SEQUENCE WITH EXTENSIONS "+complexTypeName);
	    				  if(type!=null){
	    					  simple=new SimpleTypeImpl(type);
		    				  if(localMap.containsKey(complexTypeName)){
		    					  Type complexType=localMap.get(complexTypeName);
		    					  if(complexType instanceof SequenceComplexType){
		    						  ((SequenceComplexType)complexType).addElements(simple);
		    						  //update the hashmap
		    						  System.out.println("UPDATING SEQUENCE WITH EXTENSION "+complexTypeName +" WITH ELEMENT "+simple.getSimpleType());
		    						  localMap.put(complexTypeName, complexType);
		    					  }
		    				  }else{	  
		    					//Create a Sequence type
		    					  System.out.println("NEW SEQUENCE WITH EXTENSION "+complexTypeName +" WITH ELEMENT "+simple.getSimpleType());
		    			    	  seqComplex=new SequenceComplexType();
		    			    	  seqComplex.setName(complexTypeName);
		    			    	  seqComplex.addElements(simple);
		    			    	  localMap.put(complexTypeName, seqComplex);
		    			    	  //typeSet.addComponent(seqComplex);
		    				  }
			    			
			    			
			    			System.out.println(" Sequence WITH EXTENSION Element=("+parent2.getName()+") " +
			    			  				"Name =("+complexTypeName+")");
			    			}
		    			  System.out.println();
	    				  
	    			  }
	    		  }
	    	  }
	     }
	      //handleStandaloneElement(element, allTypes);
	      //handleChoiceElement(element, allTypes);
	      //handleAllComplex(element, allTypes);
	      handleStandaloneElement(element, localMap);
	      handleChoiceElement(element, localMap);
	      handleAllComplex(element, localMap);
	    }
	   
	  }
  private static void handleAllComplex(Element element, Map<String, Type> localMap){
	  String type=null;
	  String simpleElementName=null;
	  Element parentElement=element.getParent();
	  AllComplexType allComplex=null;
	  
	  if(element.getName().equals("element")&& parentElement!=null && parentElement.getName().equals("complexType")
			  && parentElement.getParent().getName().equals("schema")){
		  
		  String allComplexTypeName=parentElement.getAttributeValue("name");
		  System.out.println("PROCESSING ALL COMPLEX NAMED "+allComplexTypeName);
		  
		  type = getTypeAttribute(element);
		
		  simpleElementName=element.getAttributeValue("name"); //the element name is not stored in the simpletype 4now
		if(type!=null){
			SimpleTypeImpl simple=new SimpleTypeImpl(type);
		
			if(localMap.containsKey(allComplexTypeName)){
				  Type complexType=localMap.get(allComplexTypeName);
				  if(complexType instanceof AllComplexType){
					  
					  ((AllComplexType)complexType).addElements(simple);
					  //update the hashmap
					  System.out.println("UPDATING SEQUENCE "+allComplexTypeName +" WITH ELEMENT "+simple.getSimpleType().getTypeName());
					  localMap.put(allComplexTypeName, complexType);
				  }
			  }else{	  
				//Create a Sequence type
				  System.out.println("NEW SEQUENCE "+allComplexTypeName +" WITH ELEMENT "+simple.getSimpleType());
				  allComplex=new AllComplexType();
				  allComplex.setName(allComplexTypeName);
		    	 
		    	  allComplex.addElements(simple);
		    	  localMap.put(allComplexTypeName, allComplex);
		    	  
			  }
			
			System.out.println("element "+simpleElementName+" Has been succesfully added to AllComplexType");
		}else{
			System.out.println("THE ALL COMPLEX TYPE IS "+type);
		}
	  }
  }
  private static void handleStandaloneElement(Element element, Map<String, Type> localMap){
	  
	  String type=null;
	  Element parentElement=element.getParent();
	  SimpleTypeImpl simpleType=null;
	  
	//handle standalone element without restrictions
      if(element.getName().equals("element")&& parentElement!=null && parentElement.getName().equals("schema")&& element.getChildren().isEmpty()){
    	  
    	  System.out.println("Type 3: (Simpe Type or User Defined Type)");
    	  String name=element.getAttributeValue("name");
    	  type=getTypeAttribute(element);
    	  System.out.println("Simple Element= ("+element.getName()+") Name = ("+name+")");
    	  
    	  //create a simple type
    	  simpleType=new SimpleTypeImpl(type);
    	  //add it to the type set.
    	  localMap.put(name, simpleType);
    	  
    	  //add the value to simpleTypeDictionary
    	  simpleType.addToSimpleTypeDictionary(simpleType.getSimpleType(), name);
    	  
    	  
    	  System.out.println();
    	  
      }
      if(parentElement!=null && parentElement.getName().equals("restriction")){
    	  
    	  type=parentElement.getAttributeValue("base");
    	  
    	  Element restrictionParent=parentElement.getParent();
    	  if(restrictionParent!=null && restrictionParent.getName().equals("simpleType")){
    		  
    		  String name=restrictionParent.getAttributeValue("name");
    		  
    		  
    		  
    		  if(restrictionParent.getParent()!=null && restrictionParent.getParent().getName().equals("schema")){
    			  //create a simple type with restrictions
    			  System.out.println("PROCESSING A RESTRICTED SIMPLE TYPE");
    			  simpleType=new SimpleTypeImpl(type);
        		  
        		  localMap.put(name, simpleType);
        		  //add the value to simpleTypeDictionary
        		  simpleType.addToSimpleTypeDictionary(simpleType.getSimpleType(), name);
    			  
    		  }
    	  }
    	  
      }
      
  }
  private static void handleChoiceElement(Element element, Map<String, Type> localMap){
	
	//handle choice element
	  SimpleTypeImpl simple=null;
	  ChoiceComplexType choiceComplex=null;
	  String type=null;
	  Element parentElement=element.getParent();
      if(element.getName().equals("element")&& parentElement!=null && parentElement.getName().equals("choice")){
    	  
    	  choiceComplex=new ChoiceComplexType();
    	  
    	  type=getTypeAttribute(element);
    	  System.out.println("TYPE "+type);
    	  
    	  System.out.println("Choice Element ("+parentElement.getQualifiedName()+")");
    	  Element parent2=parentElement.getParent();
    	  if(parent2!=null && parent2.getName().equals("complexType")){
    		  
    		  System.out.println("Complex Element "+parent2.getQualifiedName());
    		  Element parent3=parent2.getParent();
    		  
    		  if(parent3!=null && parent3.getName().equals("element")){
    			  
    			  System.out.println("TYPE 1 CHOICE");
    			  String complexTypeName=parent3.getAttributeValue("name");
    			  System.out.println("Parent Element =("+parent3.getName()+") Name =("+complexTypeName+")");  
    			  if(type!=null){
	    				
	    			 
	    			//Create Sequence type
	    			simple=new SimpleTypeImpl(type);
	    				  
	    			choiceComplex.addElements(simple);
	    			choiceComplex.setName(complexTypeName);
	    			//add the choice complex to the map
	    			localMap.put(complexTypeName, choiceComplex);
	    			
	    			System.out.println(" Choice Element=("+parent3.getName()+") Name =("+complexTypeName+")");
	    			
	    		}
    			  System.out.println();
    			  
    			  
    		  }else if(parent3!=null && parent3.getName().equals("schema")){
    			  
    			  System.out.println("TYPE 2 CHOICE");
    			  String complexTypeName=parent2.getAttributeValue("name");
    			  //System.out.println("Parent Element "+parent2.getName()+" Name "+complexTypeName);

    			  //Create Sequence type
    			  simple=new SimpleTypeImpl(type);
    			  choiceComplex.addElements(simple);
    			  choiceComplex.setName(complexTypeName);
    			  
    			  //add the choice complex to the set
    			  localMap.put(complexTypeName, choiceComplex);
    			  
    			  System.out.println(" Choice Element=("+parent3.getName()+") Name =("+complexTypeName+")");
    			  System.out.println();
    		  }
    	  }
    }
  }
  
  
  private static String getTypeAttribute(Element element){
	  List attributes = element.getAttributes();
	  String value=null;
	    if (!attributes.isEmpty()) {
	      Iterator iterator = attributes.iterator();
	      while (iterator.hasNext()) {
	        Attribute attribute = (Attribute) iterator.next();
	        String name = attribute.getName();
	        value = attribute.getValue();
	        
	        Namespace attributeNamespace = attribute.getNamespace();
	        
	        if (attributeNamespace == Namespace.NO_NAMESPACE) {
	        
	        	if("type".equals(name)){
	        	  //get the type of the element
	        	  System.out.println("TYPE "+value);
	        	 return value;
	        	}
	        }
	        else {
	          String prefix = attributeNamespace.getPrefix();
	          
	          if("type".equals(name)){
	        	  //System.out.println("TYPE "+value);
	        	  System.out.println("  " + prefix + ":" + name + "=\"" + value + "\"");
	        	  return value;      	   
	          }	          
	        }
	      }
	    }
	    
	    return value;
  }
  
  public static void main(String[] args) {
 
	  String url = "https://admin.benchmarktracker.com/services/SciLearn.asmx?WSDL";
    //String url="http://ws.cdyne.com/delayedstockquote/delayedstockquote.asmx?wsdl";
	 //String url="http://web.newhotel.com/WSNewHotelSrv/WSNewHotel.asmx?WSDL";
    //String url="http://www.webservicex.com/ValidateEmail.asmx?WSDL";
    //String url="http://www.xignite.com/xsecurity.asmx?WSDL";
    
    try {
      SAXBuilder parser = new SAXBuilder();
      
      //Parse the document
      Document document = parser.build(url); 
      
      //Process the root element
      process(document.getRootElement(), localMap);
     
      
    Map<String, Type> map= getTypeMap();
    
    if(!map.isEmpty()){
    	Set<String> keys=map.keySet();
    	for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			//System.out.println("KEY "+string);
			Type type=map.get(string);
			displaySequenceTypes(type);
			displayChoiceTypes(type);
			displaySimpleTypes(type);
		}
    }
    
    //test the type equivalence using the map of types.
    //compareTypes(map);
     
     
    }
    catch (JDOMException e) {
      System.out.println(url + " is not well-formed.");
    }
     
  } // end main
  
  //compare all the Types acquired from the WSDL
  public static void compareTypes(Map<String, Type> map){
	  if(!map.isEmpty()){
		  
	    	Set<String> keys=map.keySet();
	    	for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
	    		
				String string = (String) iterator.next();
				System.out.println("COMPARISON KEY "+string);
				Type type=map.get(string);
				if (type instanceof ComplexTypeImpl) {
					ComplexTypeImpl complexType1 = (ComplexTypeImpl) type;
					for(String stringKey: keys){
						
						//if(!stringKey.equals(string)){
							System.out.println("KEY "+stringKey);
							ComplexTypeImpl complexType2 = (ComplexTypeImpl) map.get(stringKey);
							System.out.println("IS IT EQUIVALENT "+complexType1.isEquivalentToComplex(complexType2));
						//}
					
					}
					}
				}
					
				}
}
  
//handle all SequenceTypes in the wsdl
  public static void displaySequenceTypes(Object object){
	  if(object!=null && object instanceof SequenceComplexType) {

			SequenceComplexType type = (SequenceComplexType) object;
			TypeList typeList=type.getElements();
			List<Type> list=typeList.getElements();
			
			if(!list.isEmpty()){
				for (Type type2 : list) {
					SimpleTypeImpl simpleType=(SimpleTypeImpl)type2;
					
					if(simpleType.getSimpleType()!=null){
						System.out.println( "Sequence Name ("+type.getName()+")" +
								" Element Type("+simpleType.getSimpleType()+")");
					}else{
						System.out.println("User Defined Element in Sequence Named "+type.getName());
					}
					
				}
					
			}else{
				System.out.println("No Elements in the sequence "+type.getName());
			}
		}
  }
  public static void displayChoiceTypes(Object object){
	  if(object!=null && object instanceof ChoiceComplexType) {

			ChoiceComplexType type = (ChoiceComplexType) object;
			TypeSet types=type.getElements();
			Set<Type> set=types.getElements();
			
			if(!set.isEmpty()){
				for (Type type2 : set) {
					SimpleTypeImpl simpleType=(SimpleTypeImpl)type2;
					if(simpleType.getSimpleType()!=null){
						
						System.out.println( "Choice Type Name ("+type.getName()+")" +
								" Element ("+simpleType.getSimpleType()+")");
					}else{
						System.out.println("User defined Element Simple Type  for Choice Type "+type.getName());
					}
					
				}
					
			}else{
				System.out.println("No Elements in the Choice Type "+type.getName());
			}
		}
  }
  public static void displaySimpleTypes(Object object){
	  if(object!=null && object instanceof SimpleTypeImpl) {

			SimpleTypeImpl type = (SimpleTypeImpl) object;
			
			SimpleType simple=type.getSimpleType();
			if(simple!=null){
				System.out.println("Simple Type "+simple);
			}else{
				System.out.println("User defined Simple Type ");
			}
			
		}
  }
  public static void displayAllComplexTypes(Object object){
	  if(object!=null && object instanceof AllComplexType) {

			AllComplexType type = (AllComplexType) object;
			
			TypeSet types=type.getElements();
			Set<Type> set=types.getElements();
			
			if(!set.isEmpty()){
				for (Type type2 : set) {
					SimpleTypeImpl simpleType=(SimpleTypeImpl)type2;
					System.out.println( "All Complex Type Name ("+type.getName()+")" +
							" Element ("+simpleType.getSimpleType()+")");	
				}
					
			}else{
				System.out.println("No Elements in the All Complex Type "+type.getName());
			}
		}
  }
//Print the properties of each element
  public static void inspect(Element element) {
    
    if (!element.isRootElement()) {
      // Print a blank line to separate it from the previous
      // element.
      System.out.println(); 
    }
    
    String qualifiedName = element.getQualifiedName();
    System.out.println(" "+qualifiedName + ":");
    
    String type=null;
    Namespace namespace = element.getNamespace();
    if (namespace != Namespace.NO_NAMESPACE) {
    	
      String localName = element.getName();
      String uri = element.getNamespaceURI();
      String prefix = element.getNamespacePrefix();
      
      String elementName=element.getAttributeValue("name");
      System.out.println(" Local name: " + localName);
      System.out.println(" Element name ( "+elementName+")");
      
      Element parentElement=element.getParent();
      //handle sequence element
      if(parentElement!=null && parentElement.getName().equals("sequence")){
    	  
    	  type=getTypeAttribute(element);
    	  System.out.println("TYPE "+type);
    	  
    	  System.out.println("Sequence Element ("+parentElement.getQualifiedName()+")");
    	  Element parent2=parentElement.getParent();
    	  
    	  if(parent2!=null && parent2.getName().equals("complexType")){
    		  
    		  System.out.println("Complex Element "+parent2.getQualifiedName());
    		  Element parent3=parent2.getParent();
    		  
    		  if(parent3!=null && parent3.getName().equals("element")){
    			  
    			  System.out.println("TYPE 1");
    			  String complexTypeName=parent3.getAttributeValue("name");
    			  System.out.println("Parent Element =("+parent3.getName()+") Name =("+complexTypeName+")");
    			  
    			  System.out.println();
    			  
    		  }else if(parent3!=null && parent3.getName().equals("schema")){
    			  
    			  System.out.println("TYPE 2");
    			  String complexTypeName=parent2.getAttributeValue("name");
    			  System.out.println("Parent Element "+parent2.getName()+" Name "+complexTypeName);
    			  System.out.println();
    		  }
    	  }
    }
      //handle standalone element
      if(parentElement!=null && parentElement.getName().equals("schema")&& element.getChildren().isEmpty()){
    	  System.out.println("Type 3: Simple type or User Defined Type");
    	  String name=element.getAttributeValue("name");
    	  type=getTypeAttribute(element);
    	  System.out.println("Simple Element= ("+element.getName()+") Name = ("+name+")");
    	  System.out.println("TYPE "+type);
    	  System.out.println();
    	  
    	  
      }
      
    //handle choice element
      if(parentElement!=null && parentElement.getName().equals("choice")){
    	  
    	  type=getTypeAttribute(element);
    	  System.out.println("TYPE "+type);
    	  
    	  System.out.println("Choice Element ("+parentElement.getQualifiedName()+")");
    	  Element parent2=parentElement.getParent();
    	  if(parent2!=null && parent2.getName().equals("complexType")){
    		  
    		  System.out.println("Complex Element "+parent2.getQualifiedName());
    		  Element parent3=parent2.getParent();
    		  
    		  if(parent3!=null && parent3.getName().equals("element")){
    			  
    			  System.out.println("TYPE 1 CHOICE");
    			  String complexTypeName=parent3.getAttributeValue("name");
    			  System.out.println("Parent Element =("+parent3.getName()+") Name =("+complexTypeName+")");
    			  
    			  System.out.println();
    			  
    		  }else if(parent3!=null && parent3.getName().equals("schema")){
    			  
    			  System.out.println("TYPE 2 CHOICE");
    			  String complexTypeName=parent2.getAttributeValue("name");
    			  System.out.println("Parent Element "+parent2.getName()+" Name "+complexTypeName);
    			  System.out.println();
    		  }
    	  }
    }
      
      //handle all complex type
     // System.out.println("  Namespace URI: " + uri);
      if (!"".equals(prefix)) {
        //System.out.println("  Namespace prefix: " + prefix);
      }
    }
    
    
  }
}


