package org.algorithm.intf;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface ComplexType extends Type, Serializable {
	
	public static final String ELEMENT_TYPE_ALLCOMPLEX="all";
	public static final String ELEMENT_TYPE_CHOICE="choice";
	public static final String ELEMENT_TYPE_SEQUENCE="sequence";
	public List<Type> getElements();
	
}
