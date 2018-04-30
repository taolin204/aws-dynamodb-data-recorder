package com.mwi.aws.dynamodb.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MenuDocument implements Serializable, Comparable<MenuDocument> {

	private String name;
	private Map params = new HashMap();
	
	public MenuDocument(String aName, Map aParams) {
		name = aName;
		params = aParams;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MenuDocument other = (MenuDocument) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
 
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
 
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public int compareTo(MenuDocument document) {
        return this.getName().compareTo(document.getName());
    }

}
