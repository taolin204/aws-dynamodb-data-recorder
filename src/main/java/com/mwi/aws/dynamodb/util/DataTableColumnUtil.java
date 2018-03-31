package com.mwi.aws.dynamodb.util;

import com.mwi.aws.dynamodb.ui.ColumnModel;
import com.thoughtworks.xstream.XStream;

public class DataTableColumnUtil {

	
	public static void main(String[] args) {
		ColumnModel column = new ColumnModel();
		column.setHeader("column1");
		column.setValue("property1");
		column.setType("String");
		

		XStream xstream = new XStream();
		xstream.alias("req", ColumnModel.class);

		String xml = xstream.toXML(column);
		System.out.println(xml);
	}
}
