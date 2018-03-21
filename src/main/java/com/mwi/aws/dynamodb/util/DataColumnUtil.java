package com.mwi.aws.dynamodb.util;

import com.mwi.aws.dynamodb.ui.ColumnModel;
import com.thoughtworks.xstream.XStream;

public class DataColumnUtil {

	
	public static void main(String[] args) {
		ColumnModel column = new ColumnModel();
		column.setHeader("column1");
		column.setProperty("property1");
		column.setType(String.class);
		

		XStream xstream = new XStream();
		xstream.alias("req", ColumnModel.class);

		String xml = xstream.toXML(column);
		System.out.println(xml);
	}
}
