package com.mwi.aws.dynamodb.ui;

import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.mwi.aws.dynamodb.model.Employee;
import com.mwi.aws.dynamodb.service.CarService;
import com.mwi.aws.dynamodb.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
    
    private MenuModel model;
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
}