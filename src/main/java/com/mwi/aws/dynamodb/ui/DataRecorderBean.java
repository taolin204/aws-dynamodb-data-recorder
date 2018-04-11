package com.mwi.aws.dynamodb.ui;

import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.mfg.AwsDataInterface;
import com.mwi.aws.dynamodb.datamanager.AwsDataTypeManager;
import com.mwi.aws.dynamodb.model.AwsDataType;
import com.mwi.aws.dynamodb.service.AwsService;
import com.mwi.aws.dynamodb.service.OwnerConfigService;
import com.mwi.aws.dynamodb.util.DebugUtil;

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
	
	
	private AwsDataTypeManager dataTypeManager;
	private AwsService service;
    private MenuModel MenuModel;
    
    
   
    private Long dataTypeId;
    private AwsDataType tableDataType;
    private List<Object> tableDatas;
    private List<ColumnModel> tableDataColumns = new ArrayList<ColumnModel>(0);
    private List<Object> selTableDatas;
    
    private Long mapDataTypeId;
    private AwsDataType tableMapDataType;
    private Map selTableMapDataObj;
    private Object selTableDataObjKey;
    private List<ColumnModel> selTableDataMapColumns = new ArrayList<ColumnModel>(0);
    
    private List<Object> selDialogMapObjs;
    
    private String[] selTableListDataObj;
    
    private String menuSensor;
    
    @PostConstruct
    private void postConstruct() {
    	initDataRecorderBean();
    }
    
    public void initDataRecorderBean() {
    	dataTypeManager = AwsDataTypeManager.getInstance();
    	service  = AwsService.getInstance();
    	createRootMenu();
    }
    
    public void createRootMenu() {
    	
    	MenuModel = new DefaultMenuModel();
        
        //First submenu
        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Customer");
         
        DefaultMenuItem item = new DefaultMenuItem("Customer");
        item.setIcon("ui-icon-disk");
        item.setCommand("#{dataRecorderBean.loadOwnerData}");
        item.setValue("Customer");
        item.setParam("Customer", "Customer");
        item.setParam("dataTypeId", 1L);
        item.setUpdate("myTable");
        firstSubmenu.addElement(item);
         
        MenuModel.addElement(firstSubmenu);
         
        //Second submenu
        DefaultSubMenu secondSubmenu = new DefaultSubMenu("Printers");
        DebugUtil.log(service.getLoggedInUser().toString());
        List<String> ownerMenus = service.getUserOwnerStrngs(service.getLoggedInUser());
        DebugUtil.log("ownerMenus " + ownerMenus);
        
        if(ownerMenus != null) {
	        for(String ownerMenu : ownerMenus) {
	        	item = new DefaultMenuItem(ownerMenu);
	            item.setIcon("ui-icon-disk");
	            item.setCommand("#{dataRecorderBean.loadOwnerSensorData}");
	            item.setValue(ownerMenu);
	            item.setParam("owner", ownerMenu);
	            item.setParam("dataTypeId", 3L);
	            item.setUpdate("myTable");
	            secondSubmenu.addElement(item);
	        }
        }
        
        MenuModel.addElement(secondSubmenu);
        
    }
    
    private void createCustomerMenu() {
    	
    }
    
    public void createPrinterMenu(List customerList) {
    	if(customerList == null) {
    		
    	}
    }
    
    
    public void loadOwnerData() {
    	FacesContext fc = FacesContext.getCurrentInstance();
    	Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
    	for(String key : params.keySet()) {
    		System.out.println("key " + key + ", value " + params.get(key));
    	}
    	
    	dataTypeId =  Long.parseLong(params.get("dataTypeId"));
    	if(dataTypeId != null) {
    		tableDataType = dataTypeManager.getAwsDataMap().get(dataTypeId);
	    	System.out.println("loadContactData : " + tableDataType);
	    	
	    	tableDatas = service.getUserOwnerLists(service.getLoggedInUser());
	    	tableDataColumns = tableDataType.getColumnModels();
    	} else {
    		System.out.println("dataTypeId is null");
    	}
    	
    }
    
    
    public void loadOwnerSensorData() {
    	FacesContext fc = FacesContext.getCurrentInstance();
    	Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
    	for(String key : params.keySet()) {
    		System.out.println("key " + key + ", value " + params.get(key));
    	}
    	
    	dataTypeId =  Long.parseLong(params.get("dataTypeId"));
    	DebugUtil.log("dataTypeId " + dataTypeId);
    	String owner = params.get("owner");
    	
    	if(dataTypeId != null) {
    		tableDataType = dataTypeManager.getAwsDataMap().get(dataTypeId);
	    	System.out.println("loadOwnerSensorData : " + tableDataType);
	    	
	    	if(dataTypeId.equals(3L)) {
	    		menuSensor = owner;
				selTableListDataObj = service.getContactStringsForOwner(owner);
				DebugUtil.log("selTableListDataObj " + selTableListDataObj.toString());
			}
  	
	    	tableDatas = service.getSensorListsByOwner(owner);
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
    	if(this.isValidTableDataKey()) {
    		
    		List<String> owners = new ArrayList();
    		for(Object obj : this.tableDatas) {
    			
    			if(this.getDataTypeId().equals(1L)) {
    				AwsDataInterface awsData = (AwsDataInterface) obj;
    				String owner = (String) awsData.getKey();
    				owners.add(owner);
    			}
    			DebugUtil.log(obj.toString());
    			service.saveAwsData(obj);
    		}
    		
    		if(this.getDataTypeId().equals(1L)) {
    			DebugUtil.log("save managed owners " + service.getLoggedInUser().getManagedOwners());
    			service.getLoggedInUser().setManagedOwners(owners);
    			service.saveAwsData(service.getLoggedInUser());
    			
    		}
    		createRootMenu();
    		
    	} else {
    		DebugUtil.error("buttonSaveAction table data key is not valid");
    	}
    	
    }
    
    public void buttonSaveMapObjAction() {
    	if(this.isValidTableDataKey()) {
    		DebugUtil.log("buttonSaveMapObjAction table data key is valid, can save");
    	} else {
    		DebugUtil.error("buttonSaveMapObjAction table data key is not valid");
    	}
    	
    	if(this.isValidTableMapDataKey()) {
    		DebugUtil.log("buttonSaveMapObjAction table data key is valid, can save");
    	} else {
    		DebugUtil.error("buttonSaveMapObjAction table data key is not valid");
    	}
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
	
	public Map getSelTableMapDataObj() {
		return selTableMapDataObj;
	}
	
	public void setSelTableMapDataObj(Map selObj) {
		System.out.println("setSelTableMapDataObj " + selObj.toString());
		if(mapDataTypeId != null) {
			AwsDataType awsDataType = dataTypeManager.getAwsDataMap().get(mapDataTypeId);
			
			this.selTableDataMapColumns = awsDataType.getColumnModels();
			this.selTableMapDataObj = selObj;
		} else {
			System.out.println("mapDataTypeId is null");
		}
	}
	
	
	public String[] getSelTableListDataObj() {
		return selTableListDataObj;
	}

	public void setSelTableListDataObj(String[] selObj) {
		DebugUtil.log("setSelTableMapDataObj " + selObj.toString());
		this.selTableListDataObj = selTableListDataObj;
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
	
	public List<Object> getSelDialogMapObjs() {
		return selDialogMapObjs;
	}
	public void setSelDialogMapObjs(List<Object> selectedMapObjs) {
		this.selDialogMapObjs = selectedMapObjs;
	}
    
	public void buttonNewAction(ActionEvent actionEvent) {
		
		if(this.getTableDatas() != null) {
			Object obj;
			try {

				DebugUtil.log("tableDataType.getDataTypeId() " + tableDataType.getDataTypeId());
				if(tableDataType.getDataTypeId() != null && tableDataType.getDataTypeId().equals(1L)) {
					obj = service.getOwnerConfigService().createOwnerConfigDummy();
				} else if(tableDataType.getDataTypeId() != null && tableDataType.getDataTypeId().equals(3L)) {
					obj = service.getSensorConfigService().createSensorConfigDummy(menuSensor);
				} else {
					String className = tableDataType.getDataClass();
					obj = Class.forName(className).newInstance();
					
					PodamFactory factory = new PodamFactoryImpl();
		
					// This will use constructor with minimum arguments and
					// then setters to populate POJO
					factory.populatePojo(obj);
					System.out.println("new object using Podam " + obj.toString());
				}
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
		
	}
	
	public void buttonDeleteAction(ActionEvent actionEvent) {
		DebugUtil.log("delete selected size " + selTableDatas.size());
		if(this.isValidTableDataKey()) {
	    	if(selTableDatas != null) {
	    		for(int i=0; i<selTableDatas.size();i++) {
	    			AwsDataInterface obj = (AwsDataInterface) selTableDatas.get(i);
	    			System.out.println(obj.toString());
	    			for(Object object : this.getSelTableDatas()) {
	    				AwsDataInterface awsData = (AwsDataInterface) object;
	    				if(obj.getKey().equals(awsData.getKey())) {
	    					this.tableDatas.remove(object);
	    					service.removeAwsData(awsData);
	    				}
	    			}
	    			
	    		}
	    	} 
	    	
	    	createRootMenu();
		} else {
    		FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "not able to delete record."));
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
			if(tableDataType.getDataTypeId().equals(2L)) {
				obj = service.getOwnerConfigService().createContactDummy();
			} 
			String className = tableMapDataType.getDataClass();
			obj = (AwsDataInterface) Class.forName(className).newInstance();
			
			PodamFactory factory = new PodamFactoryImpl();

			// This will use constructor with minimum arguments and
			// then setters to populate POJO
			factory.populatePojo(obj);
			System.out.println("new map object : " + obj.toString());
			
			this.getSelTableMapDataObj().put(obj.getKey(), obj);
			
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
		System.out.println("delete selected map data size  " + selDialogMapObjs.size());
    	if(selDialogMapObjs != null) {
    		for(int i=0; i<selDialogMapObjs.size();i++) {
    			AwsDataInterface obj = (AwsDataInterface) selDialogMapObjs.get(i);
    			System.out.println(obj.toString());
    			if(selTableMapDataObj.containsKey(obj.getKey())) {
    				System.out.println("object key is in the map, remove the object.");
    				this.selTableMapDataObj.remove(obj.getKey());
    			} else {
    				System.out.println("===== object key not in the map."); 
    			}
    			
    		}
    	}
		
		System.out.println("selTableDataObj " + selTableMapDataObj.size());
    	Iterator itr = selTableMapDataObj.values().iterator();
    	while(itr.hasNext()) {
    		AwsDataInterface obj = (AwsDataInterface) itr.next();
    		System.out.println(obj.toString());
    	}
	}
	
	public boolean isValidTableDataKey() {
		boolean result = true;
		List<Object> tableDataKeys = new ArrayList();
		for(Object object : this.getTableDatas()) {
			AwsDataInterface awsData = (AwsDataInterface) object;
			if(awsData.getKey() == null || awsData.getKey().equals("")) {
				result = false;
			} else if(tableDataKeys.contains(awsData.getKey())) {
				result = false;
			} else {
				tableDataKeys.add(awsData.getKey());
			}
		}
		
		return result;
	}
	
	public boolean isValidTableMapDataKey() {
		boolean result = true;
		List<Object> tableDataKeys = new ArrayList();
		for(Object object : this.getSelTableMapDataObj().values()) {
			AwsDataInterface awsData = (AwsDataInterface) object;
			if(awsData.getKey() == null || awsData.getKey().equals("")) {
				result = false;
			} else if(tableDataKeys.contains(awsData.getKey())) {
				result = false;
			} else {
				tableDataKeys.add(awsData.getKey());
			}
		}
		
		return result;
	}
    
}