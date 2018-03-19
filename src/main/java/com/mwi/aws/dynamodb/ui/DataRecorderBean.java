package com.mwi.aws.dynamodb.ui;

import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.component.datatable.DataTable;

import com.mwi.aws.dynamodb.model.Employee;
import com.mwi.aws.dynamodb.service.CarService;
import com.mwi.aws.dynamodb.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.util.*;

@ManagedBean
@ViewScoped
public class DataRecorderBean {
	
	private List<ColumnModel> columns = new ArrayList<ColumnModel>(0);
    private List<String> selectedColumns = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();
    private Map<String, String> columnMap = new LinkedHashMap<>();

    private String text;
    
    private List<Object> data;
    
    //@ManagedProperty(value = "#{table}")
    //private DataTable table;
    private String tableTag = ":form:myTable";
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
     
    public void handleKeyEvent() {
        if(text != null) {
        	text = text.toUpperCase();
        	if(text.equals("12")) {
        		System.out.println("text " + text);
        		initColumnProperties2();
        		DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(tableTag);
        		if(table != null) {
        			
        	        columns = new ArrayList<ColumnModel>();
        	        ColumnModel columnDescriptor = new ColumnModel();
                    columnDescriptor.setProperty("username");         
                    columnDescriptor.setHeader("Username");        
                    columnDescriptor.setType(String.class);        
                    columns.add(columnDescriptor);
                    
                    columnDescriptor = new ColumnModel();
                    columnDescriptor.setProperty("password");         
                    columnDescriptor.setHeader("Password");        
                    columnDescriptor.setType(String.class);        
                    columns.add(columnDescriptor);
                    
                    UserService service = new UserService();
        			data = service.getUsers();
//        			table.setValueExpression("sortBy", null);
//	        		for(UIColumn column : table.getColumns()) {
//	        			System.out.println(column.getColumnKey());
//	        		}
        		} else {
        			System.out.println("not able to find table component by " + tableTag);
        		}
        		
//        		UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(tableTag);
//        		if(table != null) {
//        			System.out.println("table is not null");
//        			//table.setValueExpression("sortBy", null);
//        		} else {
//        			System.out.println("not able to find table component by " + tableTag);
//        		}
        		
        	} else if(text.equals("123")) {
        		//System.out.println("text " + text);
        		//initColumnProperties();
        		initColumnProperties2();
        		
        		System.out.println("init car table start 1");
        		columns = new ArrayList<ColumnModel>();
    	        ColumnModel columnDescriptor = new ColumnModel();
                columnDescriptor.setProperty("carNum");         
                columnDescriptor.setHeader("CarNum");        
                columnDescriptor.setType(String.class);        
                columns.add(columnDescriptor);
                
                System.out.println("init car table start 2");
                columnDescriptor = new ColumnModel();
                columnDescriptor.setProperty("carOwner");         
                columnDescriptor.setHeader("CarOwner");        
                columnDescriptor.setType(String.class);        
                columns.add(columnDescriptor);
        		
                System.out.println("init car table start 3");
                columnDescriptor = new ColumnModel();
                columnDescriptor.setProperty("price");         
                columnDescriptor.setHeader("Price");        
                columnDescriptor.setType(String.class);        
                columns.add(columnDescriptor);
                
                System.out.println("init car table start 4");
                CarService carService = new CarService();
                data = carService.getCars();
                
                System.out.println("init car table end ");
        	}
        }
    }
    
    @PostConstruct
    private void postConstruct() {
        initColumnProperties();
        initEmployeeList();
        
    }

    private void initColumnProperties2() {
    	selectedColumns = new ArrayList<>();
    	columnMap = new LinkedHashMap<>();
        addColumn("id", "ID");
        addColumn("name", "Name");
        addColumn("phoneNumber", "Phone Number");
        selectedColumns.addAll(columnMap.keySet());
    }
    
    private void initColumnProperties() {
    	selectedColumns = new ArrayList<>();
    	columnMap = new LinkedHashMap<>();
        addColumn("id", "ID");
        addColumn("name", "Name");
        addColumn("phoneNumber", "Phone Number");
        addColumn("address", "Address");
        selectedColumns.addAll(columnMap.keySet());
    }

    private void addColumn(String propertyName, String displayName) {
        columnMap.put(propertyName, displayName);
    }

    private void initEmployeeList() {
        DataFactory dataFactory = new DataFactory();
        for (int i = 1; i < 20; i++) {
            Employee employee = new Employee();
            employee.setId(i);
            employee.setName(dataFactory.getName());
            employee.setPhoneNumber(String.format("%s-%s-%s", dataFactory.getNumberText(3),
                    dataFactory.getNumberText(3),
                    dataFactory.getNumberText(4)));
            employee.setAddress(dataFactory.getAddress() + "," + dataFactory.getCity());
            employeeList.add(employee);
        }
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }
    
    public List<ColumnModel> getColumns() {
		return columns;
	}
    
    public List<Object> getData() {
    	return data;
//    	UserService service = new UserService();
//    	return service.getUsers();
    }
}