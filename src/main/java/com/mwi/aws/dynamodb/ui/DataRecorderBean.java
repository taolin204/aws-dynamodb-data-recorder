package com.mwi.aws.dynamodb.ui;

import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.mwi.aws.dynamodb.model.Car;
import com.mwi.aws.dynamodb.model.Employee;
import com.mwi.aws.dynamodb.service.CarService;
import com.mwi.aws.dynamodb.service.UserService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.lang.reflect.Field;
import java.util.*;

@ManagedBean
@ViewScoped
public class DataRecorderBean {
	
	private List<ColumnModel> columns = new ArrayList<ColumnModel>(0);
    private List<String> selectedColumns = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();
    private Map<String, String> columnMap = new LinkedHashMap<>();

    private String text;
    
    private MenuModel model;
    private List<Object> data;
    
    private String mapKey;
    private Object selectedCar;
    private Object selectedCarKey;
    private List<Object> selectedCars;
    
    private String username;
    private String password;
    private boolean loggedIn;
    private List<ColumnModel> selColumns = new ArrayList<ColumnModel>(0);
    
    
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
                    columnDescriptor.setValue("username");         
                    columnDescriptor.setHeader("Username");        
                    columnDescriptor.setType("String");        
                    columns.add(columnDescriptor);
                    
                    columnDescriptor = new ColumnModel();
                    columnDescriptor.setValue("password");         
                    columnDescriptor.setHeader("Password");        
                    columnDescriptor.setType("String");        
                    columns.add(columnDescriptor);
                    
                    model = new DefaultMenuModel();
                    
                    //First submenu
                    DefaultSubMenu firstSubmenu = new DefaultSubMenu("Dynamic Submenu");
                     
                    DefaultMenuItem item = new DefaultMenuItem("External");
                    item.setUrl("http://www.primefaces.org");
                    item.setIcon("ui-icon-home");
                    firstSubmenu.addElement(item);
                     
                    model.addElement(firstSubmenu);
                     
                    //Second submenu
                    DefaultSubMenu secondSubmenu = new DefaultSubMenu("Dynamic Actions");
             
                    item = new DefaultMenuItem("Save");
                    item.setIcon("ui-icon-disk");
                    item.setCommand("#{dataRecorderBean.save}");
                    //item.setUpdate("messages");
                    secondSubmenu.addElement(item);
                     
             
                    model.addElement(secondSubmenu);
                    
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
                columnDescriptor.setValue("carNum");         
                columnDescriptor.setHeader("CarNum");        
                columnDescriptor.setType("String");        
                columns.add(columnDescriptor);
                
                System.out.println("init car table start 2");
                columnDescriptor = new ColumnModel();
                columnDescriptor.setValue("carOwner");         
                columnDescriptor.setHeader("CarOwner");        
                columnDescriptor.setType("String");        
                columns.add(columnDescriptor);
        		
                System.out.println("init car table start 3");
                columnDescriptor = new ColumnModel();
                columnDescriptor.setKey("carNum");
                columnDescriptor.setValue("priceMap");         
                columnDescriptor.setHeader("PriceMap");        
                columnDescriptor.setType("Map");        
                columns.add(columnDescriptor);
                
                model = new DefaultMenuModel();
                
                //First submenu
                DefaultSubMenu firstSubmenu = new DefaultSubMenu("Dynamic Submenu");
                 
                DefaultMenuItem item = new DefaultMenuItem("External");
                item.setUrl("http://www.primefaces.org");
                item.setIcon("ui-icon-home");
                firstSubmenu.addElement(item);
                 
                model.addElement(firstSubmenu);
                 
                //Second submenu
                DefaultSubMenu secondSubmenu = new DefaultSubMenu("Dynamic Actions");
                
                item = new DefaultMenuItem("Delete");
                item.setIcon("ui-icon-close");
                item.setCommand("#{dataRecorderBean.delete}");
                item.setAjax(false);
                secondSubmenu.addElement(item);
                 
                item = new DefaultMenuItem("Redirect");
                item.setIcon("ui-icon-search");
                item.setCommand("#{dataRecorderBean.redirect}");
                secondSubmenu.addElement(item);
                
                model.addElement(secondSubmenu);
                
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
    
    public MenuModel getModel() {
        return model;
    }   
     
    public void save() {
        //addMessage("Success", "Data saved");
    	System.out.println("Data saved");
    }
     
    public void update() {
        //addMessage("Success", "Data updated");
    	System.out.println("Data updated");
    }
     
    public void delete() {
        //addMessage("Success", "Data deleted");
    	System.out.println("Data deleted");
    }
     
//    public void addMessage(String summary, String detail) {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
//        FacesContext.getCurrentInstance().addMessage(null, message);
//    }
    
    public void onCellEdit(CellEditEvent event) {
    	
//    	int alteredRow = event.getRowIndex();
//    	int alteredCol = ((DynamicColumn)event.getColumn()).getIndex();
//    	
//    	System.out.println("onCellEdit " + alteredRow +  ", " + alteredCol);
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        //System.out.println("Cell Changed" + " Old: " + oldValue + ", New:" + newValue);
        if(newValue != null && !newValue.equals(oldValue)) {
        	System.out.println("Cell Changed" + " Old: " + oldValue + ", New:" + newValue);
            //FacesContext.getCurrentInstance().addMessage(null, msg);
        	System.out.println("DataList " + data.size());
        }
    }
    
    public void onMapCellEdit(CellEditEvent event) {
    	
//    	int alteredRow = event.getRowIndex();
//    	int alteredCol = ((DynamicColumn)event.getColumn()).getIndex();
//    	
//    	System.out.println("onCellEdit " + alteredRow +  ", " + alteredCol);
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        //System.out.println("Cell Changed" + " Old: " + oldValue + ", New:" + newValue);
        if(newValue != null && !newValue.equals(oldValue)) {
        	System.out.println("Cell Changed" + " Old: " + oldValue + ", New:" + newValue);
            //FacesContext.getCurrentInstance().addMessage(null, msg);
        	System.out.println("DataList " + data.size());
        	for(int i=0; i< data.size(); i++) {
        		Object dataObj = data.get(i);
        		System.out.println(dataObj.toString());
        	}
        }
    }

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Object> getSelectedCars() {
        return selectedCars;
    }
 
    public void setSelectedCars(List<Object> aSelectedCars) {
    	System.out.println("set selected cars");
    	if(aSelectedCars != null) {
    		for(int i=0; i<aSelectedCars.size();i++) {
    			Object obj = aSelectedCars.get(i);
    			System.out.println(obj.toString());
    		}
    	}
        this.selectedCars = aSelectedCars;
    }
	
	public Object getSelectedCar() {
		return selectedCar;
	}
	public Object getSelectedCarKey() {
		return selectedCarKey;
	}
	public void setSelectedCarKey(Object selectedCarKey) {
		System.out.println("set selected carKey " + selectedCarKey);
		this.selectedCarKey = selectedCarKey;
	}
	public void setSelectedCar(Object selectedCar) {
		System.out.println("set selected car " + selectedCar.toString());
		
		selColumns = new ArrayList<ColumnModel>();
        ColumnModel columnDescriptor = new ColumnModel();
        columnDescriptor.setValue("model");         
        columnDescriptor.setHeader("Model");        
        columnDescriptor.setType("String");        
        selColumns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();
        columnDescriptor.setValue("manuYear");         
        columnDescriptor.setHeader("ManuYear");        
        columnDescriptor.setType("String");        
        selColumns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();
        columnDescriptor.setValue("price");         
        columnDescriptor.setHeader("Price");        
        columnDescriptor.setType("Long");        
        selColumns.add(columnDescriptor);
        
		this.selectedCar = selectedCar;
	}
	
	public List<ColumnModel> getSelColumns() {
		return selColumns;
	}
	public void setSelColumns(List<ColumnModel> selColumns) {
		this.selColumns = selColumns;
	}
	
	public void login(ActionEvent event) {
		System.out.println("username is " + username + ", password " + password);
		if(selectedCar != null) {
			System.out.println("selectedCar " + selectedCar.toString());
		} else System.out.println("selectedCar is null");
		
		if(username != null && username.equals("admin") && password != null && password.equals("admin")) {
            loggedIn = true;
        } else {
            loggedIn = false;
        }

		selColumns = new ArrayList<ColumnModel>();
        ColumnModel columnDescriptor = new ColumnModel();
        columnDescriptor.setValue("model");         
        columnDescriptor.setHeader("Model");        
        columnDescriptor.setType("String");        
        selColumns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();
        columnDescriptor.setValue("manuYear");         
        columnDescriptor.setHeader("ManuYear");        
        columnDescriptor.setType("String");        
        selColumns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();
        columnDescriptor.setValue("price");         
        columnDescriptor.setHeader("Price");        
        columnDescriptor.setType("Long");        
        selColumns.add(columnDescriptor);
         
        PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
	}
    
	public void buttonNewAction(ActionEvent actionEvent) {
		Class obj;
		try {
			obj = Class.forName("com.mwi.aws.dynamodb.model.Car");
			Field[] crunchifyFields = obj.getDeclaredFields();
			
			PodamFactory factory = new PodamFactoryImpl();

			// This will use constructor with minimum arguments and
			// then setters to populate POJO
			Object car = factory.manufacturePojo(Car.class);
			System.out.println("new car " + car.toString());
			
			for(int i=0; i< data.size(); i++) {
	    		Object dataObj = data.get(i);
	    		System.out.println(dataObj.toString());
	    	}
			
			this.getData().add(car);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void buttonDeleteAction(ActionEvent actionEvent) {
		System.out.println("delete selected cars " + selectedCars.size());
    	if(selectedCars != null) {
    		for(int i=0; i<selectedCars.size();i++) {
    			Object obj = selectedCars.get(i);
    			System.out.println(obj.toString());
    			this.data.remove(obj);
    		}
    	}
		
		System.out.println("DataList " + data.size());
    	for(int i=0; i< data.size(); i++) {
    		Object dataObj = data.get(i);
    		System.out.println(dataObj.toString());
    	}
	}
    
    
}