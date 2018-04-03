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

import com.mwi.aws.dynamodb.datamanager.AwsDataTypeManager;
import com.mwi.aws.dynamodb.model.AwsDataInterface;
import com.mwi.aws.dynamodb.model.AwsDataType;
import com.mwi.aws.dynamodb.service.OwnerConfigService;

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
	
    private MenuModel MenuModel;
    
    private AwsDataTypeManager dataTypeManager;
   
    private Long dataTypeId;
    private AwsDataType tableDataType;
    private List<Object> tableDatas;
    private List<ColumnModel> tableDataColumns = new ArrayList<ColumnModel>(0);
    private List<Object> selTableDatas;
    
    
    private Long mapDataTypeId;
    private AwsDataType tableMapDataType;
    private Map selTableDataObj;
    private Object selTableDataObjKey;
    private List<ColumnModel> selTableDataMapColumns = new ArrayList<ColumnModel>(0);
    
    private List<Object> selTableDataMapObjs;
    
    @PostConstruct
    private void postConstruct() {
    	initDataRecorderBean();
    }
    
    public void initDataRecorderBean() {
    	dataTypeManager = AwsDataTypeManager.getInstance();
    	createRootMenu();
    }
    
    public void createRootMenu() {
    	
    	MenuModel = new DefaultMenuModel();
        
        //First submenu
        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Customer");
         
        DefaultMenuItem item = new DefaultMenuItem("Customer");
        item.setIcon("ui-icon-disk");
        item.setCommand("#{dataRecorderBean.loadContactData}");
        item.setValue("Customer");
        item.setParam("Customer", "Customer");
        item.setParam("dataTypeId", 1L);
        item.setUpdate("myTable");
        firstSubmenu.addElement(item);
         
        MenuModel.addElement(firstSubmenu);
         
        //Second submenu
        DefaultSubMenu secondSubmenu = new DefaultSubMenu("Printers");
 
        item = new DefaultMenuItem("Printer");
        item.setIcon("ui-icon-disk");
        item.setCommand("#{dataRecorderBean.save}");
        //item.setUpdate("messages");
        secondSubmenu.addElement(item);
         
 
        MenuModel.addElement(secondSubmenu);
    }
    
    private void createCustomerMenu() {
    	
    }
    
    public void createPrinterMenu(List customerList) {
    	if(customerList == null) {
    		
    	}
    }
    
    
    public void loadContactData() {
    	FacesContext fc = FacesContext.getCurrentInstance();
    	Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
    	for(String key : params.keySet()) {
    		System.out.println("key " + key + ", value " + params.get(key));
    	}
    	
    	dataTypeId =  Long.parseLong(params.get("dataTypeId"));
    	if(dataTypeId != null) {
    		tableDataType = dataTypeManager.getAwsDataMap().get(dataTypeId);
	    	System.out.println("loadContactData : " + tableDataType);
	    	
	    	tableDatas = OwnerConfigService.getInstance().getOwnList();
	    	tableDataColumns = tableDataType.getColumnModels();
    	} else {
    		System.out.println("dataTypeId is null");
    	}
    	
    }
    
    public List<ColumnModel> getTableDataColumns() {
		return tableDataColumns;
	}
    
    public void setTableDataColumns(List<ColumnModel> tableDataColumns) {
		this.tableDataColumns = tableDataColumns;
	}

	public List<Object> getTableDatas() {
    	return tableDatas;
//    	UserService service = new UserService();
//    	return service.getUsers();
    }

    public Long getDataTypeId() {
		return dataTypeId;
	}

	public void setDataTypeId(Long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	public Long getMapDataTypeId() {
		return mapDataTypeId;
	}

	public void setMapDataTypeId(Long aMapDataTypeId) {
		System.out.println("setMapDataTypeId mapDataTypeId " + aMapDataTypeId);
		mapDataTypeId = aMapDataTypeId;
		if(mapDataTypeId != null) {
			tableMapDataType = dataTypeManager.getAwsDataMap().get(mapDataTypeId);
		}
	}

	public MenuModel getMenuModel() {
        return MenuModel;
    }   
     
    public void buttonSaveAction() {
        //addMessage("Success", "Data saved");
    	System.out.println("develop service to save current table object");
    }
     
    public void save() {
        //addMessage("Success", "Data updated");
    	System.out.println("Data saved");
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
        	System.out.println("DataList " + tableDatas.size());
        }
    }
    
    public void onMapDataCellEdit(CellEditEvent event) {
    	
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
        	System.out.println("DataList " + tableDatas.size());
        	for(int i=0; i< tableDatas.size(); i++) {
        		Object dataObj = tableDatas.get(i);
        		System.out.println(dataObj.toString());
        	}
        }
    }

    public List<Object> getSelTableDatas() {
        return selTableDatas;
    }
 
    public void setSelTableDatas(List<Object> aSelectedCars) {
    	System.out.println("setSelectedTableDatas ");
    	if(aSelectedCars != null) {
    		for(int i=0; i<aSelectedCars.size();i++) {
    			Object obj = aSelectedCars.get(i);
    			System.out.println(obj.toString());
    		}
    	}
        this.selTableDatas = aSelectedCars;
    }
	
	public Map getSelTableDataObj() {
		return selTableDataObj;
	}
	
	public void setSelTableDataObj(Map selObj) {
		System.out.println("setSelTableDataObj " + selObj.toString());
		if(mapDataTypeId != null) {
			AwsDataType awsDataType = dataTypeManager.getAwsDataMap().get(mapDataTypeId);
			
			this.selTableDataMapColumns = awsDataType.getColumnModels();
			this.selTableDataObj = selObj;
		} else {
			System.out.println("mapDataTypeId is null");
		}
	}
	
	
	public Object getSelTableDataObjKey() {
		return selTableDataObjKey;
	}
	public void setSelTableDataObjKey(Object aSelectedKey) {
		System.out.println("setSelTableDataObjKey " + aSelectedKey);
		this.selTableDataObjKey = aSelectedKey;
	}
	
	
	public List<ColumnModel> getSelTableDataMapColumns() {
		return selTableDataMapColumns;
	}
	public void setSelTableDataMapColumns(List<ColumnModel> selColumns) {
		this.selTableDataMapColumns = selColumns;
	}
	
	public List<Object> getSelTableDataMapObjs() {
		return selTableDataMapObjs;
	}
	public void setSelTableDataMapObjs(List<Object> selectedMapObjs) {
		this.selTableDataMapObjs = selectedMapObjs;
	}
    
	public void buttonNewAction(ActionEvent actionEvent) {
		Object obj;
		try {
			
			String className = tableDataType.getDataClass();
			obj = Class.forName(className).newInstance();
			
			PodamFactory factory = new PodamFactoryImpl();

			// This will use constructor with minimum arguments and
			// then setters to populate POJO
			factory.populatePojo(obj);
			System.out.println("new car " + obj.toString());
			
			this.getTableDatas().add(obj);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void buttonDeleteAction(ActionEvent actionEvent) {
//		System.out.println("delete selected cars " + selTableDatas.size());
    	if(selTableDatas != null) {
    		for(int i=0; i<selTableDatas.size();i++) {
    			Object obj = selTableDatas.get(i);
//    			System.out.println(obj.toString());
    			this.tableDatas.remove(obj);
    		}
    	}
		
//		System.out.println("DataList " + tableDatas.size());
//    	for(int i=0; i< tableDatas.size(); i++) {
//    		Object dataObj = tableDatas.get(i);
//    		System.out.println(dataObj.toString());
//    	}
	}
    
	
	public void buttonNewMapObjAction(ActionEvent actionEvent) {
		
		
		AwsDataInterface obj;
		try {
			
			String className = tableMapDataType.getDataClass();
			obj = (AwsDataInterface) Class.forName(className).newInstance();
			
			PodamFactory factory = new PodamFactoryImpl();

			// This will use constructor with minimum arguments and
			// then setters to populate POJO
			factory.populatePojo(obj);
			System.out.println("new map object : " + obj.toString());
			
			this.getSelTableDataObj().put(obj.getKey(), obj);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void buttonDeleteMapObjAction(ActionEvent actionEvent) {
		System.out.println("delete selected cars " + selTableDataMapObjs.size());
    	if(selTableDataMapObjs != null) {
    		for(int i=0; i<selTableDataMapObjs.size();i++) {
    			AwsDataInterface obj = (AwsDataInterface) selTableDataMapObjs.get(i);
    			System.out.println(obj.toString());
    			if(selTableDataObj.containsKey(obj.getKey())) {
    				System.out.println("object key is in the map, remove the object.");
    				this.selTableDataObj.remove(obj.getKey());
    			} else {
    				System.out.println("===== object key not in the map."); 
    			}
    			
    		}
    	}
		
		System.out.println("selTableDataObj " + selTableDataObj.size());
    	Iterator itr = selTableDataObj.values().iterator();
    	while(itr.hasNext()) {
    		AwsDataInterface obj = (AwsDataInterface) itr.next();
    		System.out.println(obj.toString());
    	}
	}
	
    
}