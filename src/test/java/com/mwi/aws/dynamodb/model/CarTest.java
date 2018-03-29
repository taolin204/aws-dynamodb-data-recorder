package com.mwi.aws.dynamodb.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class CarTest {

	public static void main(String[] args) {
		Class obj;
		try {
			obj = Class.forName("com.mwi.aws.dynamodb.model.Car");
			
			Method[] crunchifyMethods = obj.getDeclaredMethods();
			for (Method method : crunchifyMethods) {
				Class<?> type = method.getReturnType();
				String methodName = method.getName();
				if(type.equals(String.class)) {
				}
				System.out.println("method name : " + methodName + " , type : " + type);
			}
			
			
			Field[] crunchifyFields = obj.getDeclaredFields();
			for (Field field : crunchifyFields) {
				Class<?> type = field.getType();
				if(type.equals(String.class)) {
				}
				System.out.println("field name : " + field.getName() + " , type : " + type);
			}
			
			PodamFactory factory = new PodamFactoryImpl();

			// This will use constructor with minimum arguments and
			// then setters to populate POJO
			Car car = factory.manufacturePojo(Car.class);
			System.out.println(car.toString());
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
