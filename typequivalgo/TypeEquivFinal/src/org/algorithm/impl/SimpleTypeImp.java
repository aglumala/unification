package org.algorithm.impl;


import org.algorithm.intf.SimpleType;
import org.algorithm.intf.Type;

public class SimpleTypeImp implements SimpleType{
	
	private static final long serialVersionUID = 1L;
	private static final String SIMPLE_TYPE="simple";
	Simple simpleElement;
	String name;
	
	public enum Simple{
		STRING("s:string"),
	    CHARACTER("s:string"),
	    DECIMAL("s:decimal"),
		INTEGER("s:int"),
	    INT("s:int"),
	    BOOLEAN("s:boolean"),
	    NONNEGATIVEINTEGER("s:int"),
	    NEGATIVEINTEGER("s:int"),
	    NONPOSITIVEINTEGER("s:int"),
	    POSITIVEINTEGER("s:int"),
	    UNSIGNEDINT("s:int"),
	    BYTE("s:byte"),
	    UNSIGNEDBYTE("s:byte"),
	    SHORT("s:short"),
	    UNSIGNEDSHORT("s:short"),
	    LONG("s:long"),
	    UNSIGNEDLONG("s:long"),
	    DURATION("s:long"),
	    FLOAT("s:double"),
	    DOUBLE("s:double"),
		DATE("s:dateTime"),
		GYEARMONTH("s:dateTime"),
	    GYEAR("s:dateTime"),
	    GDAY("s:dateTime"),
	    GMONTHDAYYEAR("s:dateTime"),
	    GMONTH("s:dateTime"),
	    HEXBINARY("nil"),
	    BASE64BINARY("nil"),
	    ID("nil"),
	    IDREF("nil"),
	    IDREFS("nil"),
	    ENTITY("nil"),
	    ENTITIES("nil"),
	    ANYURI("nil"),
	    NORMALIZEDSTRING("nil"),
	    TOKEN("nil"),
	    NMTOKEN("nil"),
	    NMTOKENS("nil"),
	    NOTATION("nil"),
	    QName("nil"),
	    NCNAME("nil"),
	    LANGUAGE("nil");
		

		private String typeName;

		Simple(String typeName){
			this.typeName = typeName;
		}

		public String getTypeName(){
	        return typeName;
		}

		public static Simple getSimpleType(String typeName){
			
	        Simple simpleTypeInst = null;
	       
			for(Simple simpleType: Simple.values()){
				
				if(typeName.equals(simpleType.getTypeName())){
					
					simpleTypeInst = simpleType;
					break;
				}
			}
			return simpleTypeInst;
		}
	}
	
	
	public SimpleTypeImp(){
		
	}
	public SimpleTypeImp(String type, String simpleName){
		
		simpleElement=Simple.getSimpleType(type);
		name=simpleName;
		
	}
	public Simple getSimple(){
		return simpleElement;
	}
	@Override
	public boolean equals(Object obj) {

		if(obj instanceof SimpleTypeImp){
			SimpleTypeImp st=(SimpleTypeImp)obj;
			if(st.getSimple().getTypeName().equals(this.getSimple().getTypeName())){
				
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		
		return name.hashCode();
	}
	
	@Override
	public void addElementType(String elementType, String elementName)
			throws CannotAddTypeToCollectionException {
	}
	@Override
	public String getName() {
		
		return name;
	}
	@Override
	public String getTypeName() {
		return SIMPLE_TYPE;
	}
	
	public static boolean isSimple(Type type){
		
		if(type instanceof SimpleTypeImp){
			return true;
		}
		return false;
	}
	
}
