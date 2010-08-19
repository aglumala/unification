package org.algorithm.intf;

import java.io.Serializable;

import org.algorithm.impl.CannotAddTypeToCollectionException;

public interface Type extends Serializable{
	public static final String ALL="all";
	public static final String CHOICE="choice";
	public static final String SEQUENCE="sequence";
	public String getTypeName();
	public String getName();
	public void addElementType(String elementType, String elementName)throws CannotAddTypeToCollectionException;
}
