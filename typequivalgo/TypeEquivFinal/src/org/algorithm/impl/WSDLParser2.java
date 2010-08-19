package org.algorithm.impl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.algorithm.impl.SimpleTypeImp.Simple;
import org.algorithm.intf.ComplexType;
import org.algorithm.intf.SimpleType;
import org.algorithm.intf.Type;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;


//Main Parser class for wsdl
public class WSDLParser2 {
	/*map contains all the Types obtained from a given wsdl.
	 * You could persist it every time you finish parsing a wsdl to permanently store parsed records.
	 * acts as an abstract dictionary
	 */
	
	private static Map<String, Type> localMap=new HashMap<String, Type>();
	/*
	private static Map<String, Type> localMap=null;
	
	public static void setLocalMap(TypeMap map) {
		WSDLParser2.localMap = map.getLocalMap();
	}
	

	public static Map<String, Type> getLocalMap() {
		return localMap;
	}
	*/
	public static Map<String, Type> getTypeMap(){
		return localMap;
	}
public static void process(Element element, Map<String, Type> localMap) {
	
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
	      
	      System.out.println("NOT A ROOT ELEMENT"); 
	  }else{
		  System.out.println("****ROOT ELEMENT*****"); 
	  }
	  
	  	
	  	SimpleTypeImp simple=null;
	  	SequenceType seqComplex=null;
	  	String qualifiedName = element.getQualifiedName();
	    System.out.println(" "+qualifiedName + ":");
	    
	    String type=null;
	    Namespace namespace = element.getNamespace();
	    try{ //all adElementType() methods will throw  a CannotAddTypeToCollectionException
			
	    	
	    if (namespace != Namespace.NO_NAMESPACE) {
	      String localName = element.getName();
	      
	      String elementName=element.getAttributeValue("name");
	      //System.out.println(" Local name: " + localName);
	      System.out.println(" Element name ( "+elementName+")");
	      
	      Element parentElement=element.getParent();
	     
	      //handle sequence element <schema><complex><sequence><element>...</element></sequence></complex>....</schema>
	      if(parentElement!=null && parentElement.getName().equals("sequence")){
	    	  
	    	  type=getTypeAttribute(element);
	    	  
	    	  //System.out.println(" TYPE "+type);    	  
	    	  System.out.println(" Sequence Element ("+parentElement.getQualifiedName()+")");
	    	  Element parent2=parentElement.getParent();
	    	  
	    	  if(parent2!=null && parent2.getName().equals("complexType")){
	    		 
	    		  System.out.println(" Complex Element "+parent2.getQualifiedName());
	    		  Element parent3=parent2.getParent();
	    		  
	    		  if(parent3!=null && parent3.getName().equals("element")){
	    			  
	    			  //System.out.println(" TYPE 1");
	    			  String complexTypeName=parent3.getAttributeValue("name");
	    			  System.out.println();
	    			  
	    			if(type!=null){
	    				simple=new SimpleTypeImp(type, elementName);
	    				  if(localMap.containsKey(complexTypeName)){
	    					  Type complexType=localMap.get(complexTypeName);
	    					  if(complexType instanceof SequenceType){
	    						  ((SequenceType)complexType).addElementType(type, elementName);
								localMap.put(complexTypeName, complexType);
	    					  }
	    					  
	    				  }else{	  
	    					  seqComplex=new SequenceType();
	    			    	  seqComplex.setName(complexTypeName);
	    			    	  
	    			    	  seqComplex.addElementType(type, elementName);
							
	    			    	  localMap.put(complexTypeName, seqComplex);
	    			    	  
	    				  }
	    				    				 
	    			  	  System.out.println(" Sequence Element=("+parent3.getName()+") Name =("+complexTypeName+")");
	    			  
	    				}
	    			
	    		  }else if(parent3!=null && parent3.getName().equals("schema")){
	    			  
	    			  System.out.println("TYPE 2 Sequence type");
	    			  
	    			  String complexTypeName=parent2.getAttributeValue("name");    			  
	    			  System.out.println("Parent Element "+parent2.getName()+" Name "+complexTypeName);
	    			  
	    			  if(type!=null){
	    				  //set the name of the sequence complex type
		    			 
	    				  simple=new SimpleTypeImp(type, elementName);
	    				  
	    				  if(localMap.containsKey(complexTypeName)){
	    					  Type complexType=localMap.get(complexTypeName);
	    					  if(complexType instanceof SequenceType){
	    						  
	    						  ((SequenceType)complexType).addElementType(type, elementName);
	    						  
	    						  localMap.put(complexTypeName, complexType);
	    					  }
	    				  }else{	  
	    					seqComplex=new SequenceType();
	    			    	seqComplex.setName(complexTypeName);
	    			    	seqComplex.addElementType(type, elementName);
							localMap.put(complexTypeName, seqComplex);
	    			    	  
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
	    				  	
	    				  String complexTypeName=parentCmplxContent.getAttributeValue("name");
	    				  System.out.println("PROCESSING A SEQUENCE WITH EXTENSIONS "+complexTypeName);
	    				  if(type!=null){
	    					  simple=new SimpleTypeImp(type, elementName);
		    				  if(localMap.containsKey(complexTypeName)){
		    					  Type complexType=localMap.get(complexTypeName);
		    					  if(complexType instanceof SequenceType){
		    						  ((SequenceType)complexType).addElementType(type, elementName);
		    						  //update the hashmap
		    						  //System.out.println("UPDATING SEQUENCE WITH EXTENSION "+complexTypeName +" WITH ELEMENT "+simple.getSimple().getTypeName());
		    						  localMap.put(complexTypeName, complexType);
		    					  }
		    				  }else{	  
		    					//Create a Sequence type
		    					  //System.out.println("NEW SEQUENCE WITH EXTENSION "+complexTypeName +" WITH ELEMENT "+simple.getSimple().getTypeName());
		    			    	  seqComplex=new SequenceType();
		    			    	  seqComplex.setName(complexTypeName);
		    			    	  seqComplex.addElementType(type, elementName);
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
	     
	      handleStandaloneElement(element, localMap);
	      handleChoiceElement(element, localMap);
	      handleAllComplex(element, localMap);
	    }
	    
  		} catch (CannotAddTypeToCollectionException e) {
		
  			e.printStackTrace();
  		}
	   
	  }
  private static void handleAllComplex(Element element, Map<String, Type> localMap){
	  String type=null;
	  String elementName=null;
	  Element parentElement=element.getParent();
	  AllComplexType allComplex=null;
	  
	  if(element.getName().equals("element")&& parentElement!=null && parentElement.getName().equals("complexType")
			  && parentElement.getParent().getName().equals("schema")){
		  
		  String allComplexTypeName=parentElement.getAttributeValue("name");
		  System.out.println("PROCESSING ALL COMPLEX NAMED "+allComplexTypeName);
		  
		  type = getTypeAttribute(element);
		
		  elementName=element.getAttributeValue("name"); //the element name is not stored in the simpletype 4now
		if(type!=null){
			
			SimpleTypeImp simple=new SimpleTypeImp(type, elementName);
		
			if(localMap.containsKey(allComplexTypeName)){
				  Type complexType=localMap.get(allComplexTypeName);
				  if(complexType instanceof AllComplexType){
					  
					  try {
						((AllComplexType)complexType).addElementType(type, elementName);
					} catch (CannotAddTypeToCollectionException e) {
						
						e.printStackTrace();
					}
					  
					  //System.out.println("UPDATING SEQUENCE "+allComplexTypeName +" WITH ELEMENT "+simple.getSimpleType().getTypeName());
					  localMap.put(allComplexTypeName, complexType);
				  }
			  }else{
				  //System.out.println("NEW SEQUENCE "+allComplexTypeName +" WITH ELEMENT "+simple.getSimpleType());
				  allComplex=new AllComplexType();
				  allComplex.setName(allComplexTypeName);
		    	 
		    	  try {
					allComplex.addElementType(type, elementName);
				} catch (CannotAddTypeToCollectionException e) {
					
					e.printStackTrace();
				}
		    	  localMap.put(allComplexTypeName, allComplex);
		    	  
			  }
			
			System.out.println("element "+elementName+" Has been succesfully added to AllComplexType");
		}else{
			System.out.println("THE ALL COMPLEX TYPE IS "+type);
		}
	  }
  }
  private static void handleStandaloneElement(Element element, Map<String, Type> localMap){
	  
	  String type=null;
	  Element parentElement=element.getParent();
	  SimpleTypeImp simpleType=null;
	  
	//handle standalone element without restrictions
      if(element.getName().equals("element")&& parentElement!=null && parentElement.getName().equals("schema")&& element.getChildren().isEmpty()){
    	  
    	  System.out.println("Type 3: (Simpe Type or User Defined Type)");
    	  String name=element.getAttributeValue("name");
    	  type=getTypeAttribute(element);
    	  System.out.println("Simple Element= ("+element.getName()+") Name = ("+name+")");
    	  
    	  //create a simple type
    	  simpleType=new SimpleTypeImp(type, name);
    	  //add it to the type set.
    	  localMap.put(name, simpleType);
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
    			  simpleType=new SimpleTypeImp(type, name);
        		  
        		  localMap.put(name, simpleType);
        		  //add the value to simpleTypeDictionary
        		  //simpleType.addToSimpleTypeDictionary(simpleType.getSimpleType(), name);
    			  
    		  }
    	  }
    }
  }
  private static void handleChoiceElement(Element element, Map<String, Type> localMap){
	
	  String name=element.getAttributeValue("name");
	
	  SimpleType simple=null;
	  ChoiceType choiceComplex=null;
	  String type=null;
	  Element parentElement=element.getParent();
      if(element.getName().equals("element")&& parentElement!=null && parentElement.getName().equals("choice")){
    	  
    	  choiceComplex=new ChoiceType();
    	  
    	  type=getTypeAttribute(element);
    	  System.out.println("TYPE "+type);
    	  
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
	    			simple=new SimpleTypeImp(type, name);
	    				  
	    			try {
						choiceComplex.addElementType(type, name);
					} catch (CannotAddTypeToCollectionException e) {
						
						e.printStackTrace();
					}
	    			choiceComplex.setName(complexTypeName);
	    			//add the choice complex to the map
	    			localMap.put(complexTypeName, choiceComplex);
	    			
	    			System.out.println(" Choice Element=("+parent3.getName()+") Name =("+complexTypeName+")");
	    			
	    		}
    			  System.out.println();
    		}else if(parent3!=null && parent3.getName().equals("schema")){
    			  
    			  System.out.println("TYPE 2 CHOICE");
    			  String complexTypeName=parent2.getAttributeValue("name");
    			  
    			  //Create Sequence type
    			  simple=new SimpleTypeImp(type, name);
    			  choiceComplex.setName(complexTypeName);
    			  try {
					choiceComplex.addElementType(type, name);
				} catch (CannotAddTypeToCollectionException e) {
					
					e.printStackTrace();
				}
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
 
	  //String plan="C:/eclipseProjects/TravelScenario.xsd";
	  String plan="http://ws.henoo.net/flights.asmx?wsdl";
	  
	  final Set<TypeMap> parsedWsdlSet=new HashSet<TypeMap>();
	  
	  String[] wsdlURL={
			 plan,
			 "C:/eclipseProjects/TypeEquiv/sampleFiles/benchMarkTackerXMLSchema.xsd",
			//"https://admin.benchmarktracker.com/services/SciLearn.asmx?WSDL",
			//"http://ws.cdyne.com/delayedstockquote/delayedstockquote.asmx?wsdl",
		    //"http://web.newhotel.com/WSNewHotelSrv/WSNewHotel.asmx?WSDL",
		    //"http://www.webservicex.com/ValidateEmail.asmx?WSDL",
			//"http://www.xignite.com/xsecurity.asmx?WSDL",
		    //"http://webservices.flightexplorer.com/FastTrack.asmx?WSDL", 
		    /*
			"http://217.34.152.155/FlightRoute/FlightLookup.wso?WSDL", 
			"http://81.144.197.83/blackbox2/flightReservation?WSDL ",
			"http://ws.henoo.net/flights.asmx?wsdl", 
			"http://81.144.197.83/blackbox2/flightFareSearch?WSDL", 
			"http://www.raj.travel/BE_WebService/BE_FlightsCity.asmx?WSDL", 
			"http://webservices.sembo.se/partners/flight.asmx?WSDL", 
			"http://83.138.178.95/Flights.asmx?wsdl", 
			"http://ws.keyfortravel.com/Air/airreservations.asmx?wsdl", 
			"http://81.144.197.83/blackbox2/flightSeatsMap?WSDL", 
			"http://81.144.197.83/blackbox2/flightFareRules?WSDL", 
			"http://webconnect.akbartravelsonline.com/service.asmx?WSDL", 
			"http://fboweb.com/ws/PlaneXMLbeta1.asmx?WSDL", 
			"http://sws-challenge.org/shipper/v2/runner.wsdl",
			"http://ts.afnt.co.uk/travelSearch.asmx?WSDL", 
			"http://ts.afnt.co.uk/travelSearch.asmx?WSDL", 
			"http://www.elsyarres.net/elsyarreswebservice.asmx?wsdl", 
			"http://81.144.197.83/blackbox2/offers?wsdl", 
			"http://81.144.197.83/blackbox2/flightInfo?WSDL"
			*/
	 };
	  
     //parse all wsdls in the array
    String CurLine = ""; // Line read from standard in
    
    System.out.println("Enter 'q' to exit: Enter any other letter to continue ");
    InputStreamReader converter = new InputStreamReader(System.in);
    BufferedReader in = new BufferedReader(converter);
    try {
    	
    //CurLine = in.readLine();
    while (!((CurLine=in.readLine()).equals("q"))){
    	
    System.out.println("You typed: " + CurLine);
      for (String url : wsdlURL) {
    	  if(!url.equals(plan)){
    		  
    		 System.out.println("PROCESS WSDL");
    		Map<String, Type> newlocalMap=new HashMap<String, Type>();
    		WSDLParser2.setLocalMap(newlocalMap);
    		TypeMap WSDLTypeMap=processWSDL(url, newlocalMap);
		    	parsedWsdlSet.add(WSDLTypeMap);
		    }else{
		    	System.out.println("PROCESS PLAN");
		    	TypeMap planTypeMap=processWSDL(url, localMap);
		    	  parsedWsdlSet.add(planTypeMap);
		    }
    	  
    	
          
      }	//end for loop
	}//end while loop
    
    	System.out.println("YOU HAVE QUIT THE LOOP");
    	
    	processWSDLSet(parsedWsdlSet, plan);
    	System.out.println("SIZE OF WSDL SET "+parsedWsdlSet.size());
    	
    	for (Iterator iterator = parsedWsdlSet.iterator(); iterator.hasNext();) {
			TypeMap typeMap = (TypeMap) iterator.next();
			if(!typeMap.getFileName().equals(plan)){
				System.out.println("File NAME "+typeMap.getFileName());
				Set<String>keys=typeMap.getLocalMap().keySet();
				for (String string : keys) {
					System.out.println("KEY "+string);
				}
			}
			
			
		}
    	
    } catch (IOException e) {
		
		e.printStackTrace();
	}
    
  } 
  private static void processWSDLSet(final Set<TypeMap> parsedWsdlSet, String plan){
	  TypeMap planWSDL=null;
  	
  	for (Iterator iterator = parsedWsdlSet.iterator(); iterator.hasNext();) {
  		TypeMap typeMap = (TypeMap) iterator.next();
  		
  		if(typeMap.getFileName().equals(plan)){
  			planWSDL=typeMap;
  			System.out.println("PLAN "+typeMap.getFileName());
  			break;
  		}
		
  	}
  	
  	for (Iterator iterator = parsedWsdlSet.iterator(); iterator.hasNext();) {
  		
  		TypeMap typeMap = (TypeMap) iterator.next();
  		
  		System.out.println(typeMap.getFileName());
  		
  		if(plan!=null){
  			Map<String, Type> planTypeMap=planWSDL.getLocalMap();
  			Set<String> keySet=planTypeMap.keySet();
  			
  			
  			if(!typeMap.getFileName().equals(plan)){
  				//obtain all other parsed wsdls except the plan
  				Map<String, Type> otherTypes=typeMap.getLocalMap();
  				Set<String> otherTypesKEYS=otherTypes.keySet();
  				for (String key : keySet) {
  					//System.out.println("PLAN Key::: "+key);
  					//get each type in the plan and compare it with each type in the parsed WSDL
						Type planType=planTypeMap.get(key);
						if(planType instanceof SequenceType){
							
							List<Type> list=((SequenceType)planType).getElements();
							//System.out.println("ELEMENTs");
							//System.out.println("SIZE "+list.size());
							for (Type type : list) {
								SimpleTypeImp stype=(SimpleTypeImp)type;
								System.out.println("PLAN ELEMENT "+stype.getSimple().getTypeName());
							}
						}
						
						for (String otherWSDLKey : otherTypesKEYS) {
							//System.out.println("WSDL Key::: "+otherWSDLKey);
							Type wsdlType=otherTypes.get(otherWSDLKey);
							if(planType instanceof SequenceType){
								
								List<Type> list=((SequenceType)wsdlType).getElements();
								//System.out.println("WSDL ELEMENT");
								System.out.println("COMPLEX ELEMENT SIZE "+list.size());
								for (Type type : list) {
									SimpleTypeImp stype=(SimpleTypeImp)type;
									System.out.println("ELEMENT TYPE "+stype.getSimple().getTypeName());
								}
							}
							
							if(MappingDictionary.complexTypeEquivalence(planType, wsdlType)){
								System.out.println("Complex ("+planType.getName()+") Matched ("+wsdlType.getName()+
										") From WSDL ("+typeMap.getFileName());
								
							}else if(MappingDictionary.isSimpleComplexTypeEquivalent(planType, wsdlType)){
								System.out.println("Simple Complex ("+planType.getName()+") Matched ("+wsdlType.getName()+
										") From WSDL ("+typeMap.getFileName());
							}else if(MappingDictionary.simpleTypeEquivalence(planType, wsdlType)){
								System.out.println("Simple Simple ("+planType.getName()+") Matched ("+wsdlType.getName()+
										") From WSDL ("+typeMap.getFileName());
							}else{
								System.out.println(planType.getName()+" NOT MATCHING WITH "+wsdlType.getName());
							}
							
						}
					}
  			}
  		}
  	}
  }
  public static TypeMap processWSDL(String url, Map<String, Type> localMap){
	  TypeMap typemap=null;
	  SAXBuilder parser = new SAXBuilder();
	  Document document;
	  try {
		  document = parser.build(url);
		  process(document.getRootElement(), localMap);
		} catch (JDOMException e) {
			e.printStackTrace();
		} 
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
    	typemap=new TypeMap(url, map);
    	parser=null;
    	document=null;
    	return typemap;
  }
  //compare all the Types acquired from the WSDL
  public static void compareTypes(Map<String, Type> map){
	  if(!map.isEmpty()){
		  
	    	Set<String> keys=map.keySet();
	    	Type type =  map.get("GetLargestDeclines");
	    	SequenceType one = (SequenceType) type;
	    	for (String key : keys) {
	    		//if(key.equals("Variation")){
	    			Type type2 =  map.get(key);
			    	if (type2 instanceof SequenceType) {
			    		
			    		SequenceType complexType2 = (SequenceType) type2;
			    	}
	    		
	    	}
	    }
}
  
//handle all SequenceTypes in the wsdl
  public static void displaySequenceTypes(Object object){
	  if(object!=null && object instanceof SequenceType) {

			SequenceType type = (SequenceType) object;
			List<Type> list=type.getElements();
				
			if(!list.isEmpty()){
				for (Type type2 : list) {
					SimpleTypeImp simpleType=(SimpleTypeImp)type2;
					
					if(simpleType.getSimple().getTypeName()!=null){
						System.out.println( "Sequence Name ("+type.getName()+")" +
								" Element Type("+simpleType.getSimple().getTypeName()+")");
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
	  
	  if(object!=null && object instanceof ChoiceType) {

			ChoiceType type = (ChoiceType) object;
			List<Type> set=type.getElements();
			
			
			if(!set.isEmpty()){
				for (Type type2 : set) {
					SimpleTypeImp simpleType=(SimpleTypeImp)type2;
					if(simpleType.getSimple()!=null){
						
						System.out.println( "Choice Type Name ("+type.getName()+")" +
								" Element ("+simpleType.getSimple().getTypeName()+")");
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
	  if(object!=null && object instanceof SimpleType) {

			SimpleTypeImp type = (SimpleTypeImp) object;
			
			Simple simple=type.getSimple();
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
			
			List<Type> set=type.getElements();
			
			
			if(!set.isEmpty()){
				for (Type type2 : set) {
					SimpleTypeImp simpleType=(SimpleTypeImp)type2;
					System.out.println( "All Complex Type Name ("+type.getName()+")" +
							" Element ("+simpleType.getSimple().getTypeName()+")");	
				}
					
			}else{
				System.out.println("No Elements in the All Complex Type "+type.getName());
			}
		}
  }
public static void setLocalMap(Map<String, Type> localMap) {
	WSDLParser2.localMap = localMap;
}


}


