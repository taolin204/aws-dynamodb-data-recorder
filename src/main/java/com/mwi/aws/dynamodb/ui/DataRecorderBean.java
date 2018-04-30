package com.mwi.aws.dynamodb.ui;

import org.fluttercode.datafactory.impl.DataFactory;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.expression.impl.NextExpressionResolver;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.S3LinkToStringMarshaller;
import com.mfg.AwsDataInterface;
import com.mfg.OwnerConfig;
import com.mfg.SensorConfig;
import com.mwi.aws.dynamodb.SessionBean;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.SystemEvent;
import javax.xml.crypto.Data;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

@ManagedBean
@ViewScoped
public class DataRecorderBean {
	
	@ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
	
	private AwsDataTypeManager dataTypeManager;
	private AwsService service;
    private MenuModel MenuModel;
    
    private Long panelSize;
   
    private Long dataTypeId;
    private AwsDataType tableDataType;
    private List<Object> tableDatas;
    private List<ColumnModel> tableDataColumns = new ArrayList<ColumnModel>(0);
    private List<Object> selTableDatas;
    
    private Long mapDataTypeId;
    private AwsDataType tableMapDataType;
    private Map selTableMapDataObj;
    private Map selTableTempMapDataObj;
    private Object selTableDataObjKey;
    private List<ColumnModel> selTableDataMapColumns = new ArrayList<ColumnModel>(0);
    
    private List<Object> selDialogMapObjs;
    
    private String[] selTableListDataObj;
    
    private TreeNode root;
    private TreeNode selectedNode;
    
    private String loginUserName;
    private String selectedMenuData;
    private String selectedOwner;
    private String menuSensor;
    
    private String errorMessage;
    private String dialogErrorMessage;
    private boolean isTableEditing;
    private boolean isDialogEditing;
    
    @PostConstruct
    private void postConstruct() {
    	initDataRecorderBean();
    }
    
    public void initDataRecorderBean() {
    	
    	System.out.println("initDataRecorderBean ======================");
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:myTable");
        if(table != null) {
        	table.setValueExpression("sortBy", null);
        }
        table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:detailTable");
        if(table != null) {
        	table.setValueExpression("sortBy", null);
        }
    	dataTypeManager = AwsDataTypeManager.getInstance();
    	service  = AwsService.getInstance();
    	createRootMenu();
    }
    
    public void createRootMenu() {
    	
    	Map params = new HashMap();
    	root = new DefaultTreeNode(new MenuDocument("Folder", params), null);
    	TreeNode menu = new DefaultTreeNode(new MenuDocument("Menu", params), root);
    	params.put("menuData", "Customer");
    	params.put("dataTypeId", 1L);
    	root.setExpanded(true);
    	menu.setExpanded(true);
    	
    	TreeNode customers = new DefaultTreeNode(new MenuDocument("Customers", params), menu);
    	customers.setExpanded(true);
    	
    	params = new HashMap();
    	TreeNode printers = new DefaultTreeNode(new MenuDocument("Printers", params), menu);
    	printers.setExpanded(true);
    	
    	List<String> ownerMenus = service.getUserOwnerStrngs(service.getLoggedInUser());
        DebugUtil.log("ownerMenus " + ownerMenus);
        
        if(ownerMenus != null) {
	        for(String ownerMenu : ownerMenus) {
	        	params = new HashMap();
	        	params.put("menuData", ownerMenu);
	        	params.put("dataTypeId", 3L);
	        	params.put("owner", ownerMenu);
	        	TreeNode treeNode = new DefaultTreeNode(new MenuDocument(ownerMenu, params), printers);
	        	treeNode.setExpanded(true);
	        }
        }
    	
    	
//    	MenuModel = new DefaultMenuModel();
//        
//        //First submenu
//        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Customer");
//         
//        DefaultMenuItem item = new DefaultMenuItem("Customer");
//        item.setIcon("ui-icon-disk");
//        item.setCommand("#{dataRecorderBean.loadOwnerData}");
//        item.setValue("Customer");
//        item.setParam("menuData", "Customer");
//        item.setParam("dataTypeId", 1L);
//        item.setUpdate("myTable selMenu errorMessage");
//        //item.setStyle("class=\"active\"");
//        firstSubmenu.addElement(item);
//         
//        MenuModel.addElement(firstSubmenu);
//         
//        //Second submenu
//        DefaultSubMenu secondSubmenu = new DefaultSubMenu("Printers");
//        DebugUtil.log(service.getLoggedInUser().toString());
//        List<String> ownerMenus = service.getUserOwnerStrngs(service.getLoggedInUser());
//        DebugUtil.log("ownerMenus " + ownerMenus);
//        
//        if(ownerMenus != null) {
//	        for(String ownerMenu : ownerMenus) {
//	        	item = new DefaultMenuItem(ownerMenu);
//	            item.setIcon("ui-icon-disk");
//	            item.setCommand("#{dataRecorderBean.loadOwnerSensorData}");
//	            item.setValue(ownerMenu);
//	            item.setParam("menuData", ownerMenu);
//	            item.setParam("owner", ownerMenu);
//	            item.setParam("dataTypeId", 3L);
//	            item.setUpdate("myTable selMenu errorMessage");
//	            secondSubmenu.addElement(item);
//	        }
//        }
//        
//        MenuModel.addElement(secondSubmenu);
        
    }
    
    private void createCustomerMenu() {
    	
    }
    
    public void createPrinterMenu(List customerList) {
    	if(customerList == null) {
    		
    	}
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
    	MenuDocument menuDoc = (MenuDocument) event.getTreeNode().getData();
        //DebugUtil.log(event.getTreeNode().getData().toString());
        if(menuDoc.getParams().get("dataTypeId") != null) {
        	
        	this.isTableEditing = false; 
        	
	        dataTypeId = (Long) menuDoc.getParams().get("dataTypeId");
	        String selMenu = (String) menuDoc.getParams().get("menuData");
	        this.setSelectedMenuData(selMenu);
	        
	        if(dataTypeId.equals(1L)) {
	        	loadOwnerData();
	        } else if(dataTypeId.equals(3L)) {
	        	String owner = (String) menuDoc.getParams().get("owner");
	        	selectedOwner = owner;
	        	loadOwnerSensorData(owner);
	        }
        }
    }
    
    public void loadOwnerData() {
//    	FacesContext fc = FacesContext.getCurrentInstance();
//    	Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
////    	for(String key : params.keySet()) {
////    		System.out.println("key " + key + ", value " + params.get(key));
////    	}
//    	
//    	dataTypeId =  Long.parseLong(params.get("dataTypeId"));
//    	String selMenu = params.get("menuData");
//    	this.setSelectedMenuData(selMenu);
    	DebugUtil.log("dataTypeId " + dataTypeId + ", selMenu " +  selectedMenuData);
    	tableDatas = new ArrayList<>();
    	tableDataColumns = new ArrayList<ColumnModel>(0);
    	panelSize = 0L;
    	clearTableState();
    	
    	if(dataTypeId != null) {
    		tableDataType = dataTypeManager.getAwsDataMap().get(dataTypeId);
	    	//System.out.println("loadContactData : " + tableDataType);
	    	
    		errorMessage = "";
    		
	    	tableDatas = service.getUserOwnerLists(service.getLoggedInUser());
	    	tableDataColumns = tableDataType.getColumnModels();
	    	panelSize = tableDataType.getPanelSize();
    	} else {
    		System.out.println("dataTypeId is null");
    	}
    	
    }
    
    
    public void loadOwnerSensorData(String owner) {
//    	FacesContext fc = FacesContext.getCurrentInstance();
//    	Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
//    	for(String key : params.keySet()) {
//    		System.out.println("key " + key + ", value " + params.get(key));
//    	}
    	
//    	String selMenu = params.get("menuData");
//    	this.setSelectedMenuData(selMenu);
//    	String owner ="owner";
//    	dataTypeId =  Long.parseLong(params.get("dataTypeId"));
    	DebugUtil.log("dataTypeId " + dataTypeId + ", selMenu " +  selectedMenuData);
    	tableDatas = new ArrayList<>();
    	tableDataColumns = new ArrayList<ColumnModel>(0);
    	panelSize = 0L;
    	clearTableState();
    	
    	
    	if(dataTypeId != null) {
    		tableDataType = dataTypeManager.getAwsDataMap().get(dataTypeId);
	    	//System.out.println("loadOwnerSensorData : " + tableDataType);
	    	
	    	if(dataTypeId.equals(3L)) {
	    		menuSensor = owner;
				selTableListDataObj = service.getContactStringsForOwner(owner);
				//DebugUtil.log("selTableListDataObj " + selTableListDataObj.toString());
			}
  	
	    	errorMessage = "";
	    	tableDatas = service.getSensorListsByOwner(owner);
	    	tableDataColumns = tableDataType.getColumnModels();
	    	panelSize = tableDataType.getPanelSize();
    	} else {
    		System.out.println("dataTypeId is null");
    	}
    }
    
    public void  clearTableState() {
    	DataTable dataTable = (DataTable) FacesContext.getCurrentInstance()
    								.getViewRoot().findComponent(":form:myTable");
    	//dataTable = null;
    	System.out.println("clearTableState ");
    	dataTable.setValueExpression("sortBy", null);
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
     
	public void buttonRefreshAction() {
		if(dataTypeId.equals(1L)) {
        	loadOwnerData();
        } else if(dataTypeId.equals(3L)) {
        	if(selectedOwner != null) {
        		loadOwnerSensorData(this.selectedOwner);
        	}
        }
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
    			//DebugUtil.log(obj.toString());
    			service.saveAwsData(obj);
    		}
    		
    		if(this.getDataTypeId().equals(1L)) {
    			DebugUtil.log("save managed owners " + service.getLoggedInUser().getManagedOwners());
    			service.getLoggedInUser().setManagedOwners(owners);
    			service.saveAwsData(service.getLoggedInUser());
    			
    		}
    		createRootMenu();
    		
    		this.isTableEditing = false;
    		errorMessage = "";
    		
    	} else {
    		DebugUtil.error("buttonSaveAction table data key is not valid");
    	}
    	
    }
    
    public void buttonLogoutAction() {
    	sessionBean.logout();
    }
    
    public void buttonSaveMapObjAction() {
//    	if(this.isValidTableDataKey()) {
//    		DebugUtil.log("buttonSaveMapObjAction table data key is valid, can save");
//    	} else {
//    		DebugUtil.error("buttonSaveMapObjAction table data key is not valid");
//    	}
    	
    	if(this.isValidTableMapDataKey()) {
    		
    		selTableMapDataObj = new HashMap();
            
            Iterator itr = selTableTempMapDataObj.values().iterator();
            while(itr.hasNext()) {
            	AwsDataInterface awsDataInterface = (AwsDataInterface) itr.next();
            	selTableMapDataObj.put(awsDataInterface.getKey(), awsDataInterface);
            }
            //selTableTempMapDataObj = null;
            System.out.println("selTableMapDataObj size " + selTableMapDataObj.size());
            
            
            for(int i=0; i<tableDatas.size(); i++) {
            	OwnerConfig ownerConfig = (OwnerConfig) tableDatas.get(i);
            	if(ownerConfig.getKey().equals(selTableDataObjKey)) {
                	ownerConfig.setContacts(selTableMapDataObj);
                }
            }
            
    		isDialogEditing = false;
    		this.dialogErrorMessage = "";
    		
//            OwnerConfig ownerConfig = service.loadOwnerConfig(selTableDataObjKey.toString());
//            if(ownerConfig != null) {
//            	ownerConfig.setContacts(selTableMapDataObj);
//            }
            
//            System.out.println("ownerConfig size " + ownerConfig.toString());
            
//    		Map newDataMap = new HashMap();
//    		Iterator entrySet = selTableMapDataObj.entrySet().iterator();
//    		while(entrySet.hasNext()) {
//    			Entry entry =  (Entry) entrySet.next();
//    			AwsDataInterface awsData = (AwsDataInterface) entry.getValue();
//    			newDataMap.put(awsData.getKey(), awsData);
//    		}
//    		
//    		
//    		selTableMapDataObj.clear();
//    		entrySet = newDataMap.entrySet().iterator();
//    		while(entrySet.hasNext()) {
//    			Entry entry =  (Entry) entrySet.next();
//    			AwsDataInterface awsData = (AwsDataInterface) entry.getValue();
//    			selTableMapDataObj.put(awsData.getKey(), awsData);
//    		}
//    		
//    		isDialogEditing = false;
//    		this.dialogErrorMessage = "";
//    		DebugUtil.log("selTableMapDataObj size " + selTableMapDataObj.size());
    		
    		//DebugUtil.log("buttonSaveMapObjAction table data key is valid, can save");
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
        System.out.println("Cell Changed" + " Old: " + oldValue + ", New:" + newValue);
        //event.get
        //if(selTableMapDataObj != null) {
        //	selTableMapDataObj.clear();
        //}
        
        
//        buttonSaveMapObjAction();
//        if(newValue != null && !newValue.equals(oldValue)) {
//        	System.out.println("Cell Changed" + " Old: " + oldValue + ", New:" + newValue);
//            //FacesContext.getCurrentInstance().addMessage(null, msg);
//        	System.out.println("DataList " + tableDatas.size());
//        	for(int i=0; i< tableDatas.size(); i++) {
//        		Object dataObj = tableDatas.get(i);
//        		//System.out.println(dataObj.toString());
//        	}
//        }
    }

    public List<Object> getSelTableDatas() {
        return selTableDatas;
    }
 
    public void setSelTableDatas(List<Object> aSelectedCars) {
    	System.out.println("setSelectedTableDatas ");
    	if(aSelectedCars != null) {
    		for(int i=0; i<aSelectedCars.size();i++) {
    			Object obj = aSelectedCars.get(i);
    			//System.out.println(obj.toString());
    		}
    	}
        this.selTableDatas = aSelectedCars;
    }
	
	public Map getSelTableMapDataObj() {
		return selTableMapDataObj;
	}
	
	public void setSelTableMapDataObj(Map selObj) {
		selTableMapDataObj = selObj;
	}
	
	
	public String[] getSelTableListDataObj() {
		return selTableListDataObj;
	}

	public void setSelTableListDataObj(String[] selObj) {
		//DebugUtil.log("setSelTableMapDataObj " + selObj.toString());
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
    
	public Long getPanelSize() {
		return panelSize;
	}

	public void setPanelSize(Long panelSize) {
		this.panelSize = panelSize;
	}
	

	public String getSelectedMenuData() {
		return selectedMenuData;
	}

	public void setSelectedMenuData(String selectedMenuData) {
		this.selectedMenuData = selectedMenuData;
	}

	public String getLoginUserName() {
		loginUserName = service.getLoggedInUser().getUserName();
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public void buttonNewAction(ActionEvent actionEvent) {
		
		if(this.getTableDatas() != null) {
			Object obj;
			try {
				if(isValidTableDataKey()) {
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
						//System.out.println("new object using Podam " + obj.toString());
					}
					
					this.getTableDatas().add(obj);
					
					this.isTableEditing = true;
				} else {
					//errorMessage = "ERROR: not able to create new record";
					FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "ERROR", errorMessage));
				}
				
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
		if(this.isValidTableDeletedDataKey()) {
	    	if(selTableDatas != null) {
	    		
	    		//Map<String, String> removedOwners = new HashMap<>();
	    			
    			for(Object object : this.getSelTableDatas()) {
    				AwsDataInterface awsData = (AwsDataInterface) object;
    				
    				for(int i=0; i<tableDatas.size();i++) {
    	    			AwsDataInterface obj = (AwsDataInterface) tableDatas.get(i);
	    				if(obj.getKey().toString().equals(awsData.getKey().toString())) {
	    					this.tableDatas.remove(awsData);
	    					service.removeAwsData(awsData);
	    					
//	    					if(this.getDataTypeId().equals(1L)) {
//	    	    				String owner = (String) awsData.getKey();
//	    	    				service.getLoggedInUser().getManagedOwners().remove(owner);
//	    	    				
//	    	    				List sensorList = service.getSensorListsByOwner(owner);
//	    	    				for(int j=0; j<sensorList.size(); j++) {
//	    	    					AwsDataInterface sensor = (AwsDataInterface) sensorList.get(j);
//	    	    					service.removeAwsData(sensor);
//	    	    				}
//	    	    				//removedOwners.put(owner, owner);
//	    	    			}
	    					
	    					break;
	    				}
    				}
    			}

	    		
	    		if(this.getDataTypeId().equals(1L)) {
	    			DebugUtil.log("save managed owners " + service.getLoggedInUser().getManagedOwners());	
	    			service.saveAwsData(service.getLoggedInUser());
	    		}
	    		
	    		errorMessage = "";
	    	} 
	    	
	    	createRootMenu();
		} else {
			//errorMessage = "ERROR: not able to create new record, save the current first";
    		FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", errorMessage));
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
			
			if(isValidTableMapDataKey()) {
				if(mapDataTypeId.equals(2L)) {
					obj = service.getOwnerConfigService().createContactDummy();
				}  else {
					String className = tableMapDataType.getDataClass();
					obj = (AwsDataInterface) Class.forName(className).newInstance();
					
					PodamFactory factory = new PodamFactoryImpl();
		
					// This will use constructor with minimum arguments and
					// then setters to populate POJO
					factory.populatePojo(obj);
					//System.out.println("new map object : " + obj.toString());
				}
				
				System.out.println("new map object : " + obj.toString());
				this.getSelTableTempMapDataObj().put(obj.getKey(), obj);
				this.isDialogEditing = true;
				
				dialogErrorMessage = "";
			} else {
				
				//dialogErrorMessage = "not able to create new record"; 
				System.out.println("dialogErrorMessage " + dialogErrorMessage);
				FacesContext.getCurrentInstance()
				.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"ERROR", dialogErrorMessage));
			}
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
		System.out.println("======================================");
		//System.out.println("selDialogMapObjs  " + selDialogMapObjs.toString());
		//System.out.println("selTableTempMapDataObj  " + selTableTempMapDataObj.toString());
		Map<String, String> removedContacts = new HashMap();
		
    	if(selDialogMapObjs != null) {
    		for(int i=0; i<selDialogMapObjs.size();i++) {
    			AwsDataInterface obj = (AwsDataInterface) selDialogMapObjs.get(i);
    			//System.out.println(obj.toString());
    			Iterator itr = selTableTempMapDataObj.values().iterator();
    			while(itr.hasNext()) {
    				AwsDataInterface selObj = (AwsDataInterface) itr.next();
    				if(selObj.getKey().toString().equals(obj.getKey().toString())) {
    					itr.remove();
    				}
    			}
    			
    			
//    			if(selTableTempMapDataObj.containsKey(obj.getKey().toString())) {
//    				System.out.println("object key is in the map, remove the object." + obj.getKey());
//    				this.selTableTempMapDataObj.remove(obj.getKey());
//    				removedContacts.put(obj.getKey().toString(), obj.getKey().toString());
//    			} else {
//    				System.out.println("===== object key not in the map." + obj.getKey()); 
//    			}
    			
    		}
    	}
    	
    	selTableMapDataObj =  new HashMap();
    	Iterator itr = selTableTempMapDataObj.values().iterator();
        while(itr.hasNext()) {
        	AwsDataInterface awsDataInterface = (AwsDataInterface) itr.next();
        	selTableMapDataObj.put(awsDataInterface.getKey(), awsDataInterface);
        }
        //System.out.println("selTableMapDataObj " + selTableMapDataObj.toString());
       
    	
    	for(int i=0; i<tableDatas.size(); i++) {
        	OwnerConfig ownerConfig = (OwnerConfig) tableDatas.get(i);
        	if(ownerConfig.getKey().equals(selTableDataObjKey)) {
            	ownerConfig.setContacts(selTableMapDataObj);
            	//System.out.println("update contacts " + ownerConfig.getContacts());
            }
        }
		
		System.out.println("selTableDataObjKey " + selTableDataObjKey);
		String owner = (String) selTableDataObjKey;
		
		List sensorList = service.getSensorListsByOwner(owner);
		for(int j=0; j<sensorList.size(); j++) {
			SensorConfig sensorConfig = (SensorConfig) sensorList.get(j);
			sensorConfig.getContactNames().removeAll(removedContacts.keySet());	
			service.saveAwsData(sensorConfig);
		}
    	
	}
	
	public boolean isValidTableDataKey() {
		boolean result = true;
		List<Object> tableDataKeys = new ArrayList();
		for(Object object : this.getTableDatas()) {
			AwsDataInterface awsData = (AwsDataInterface) object;
			if(awsData.getKey() == null || awsData.getKey().equals("")) {
				result = false;
				if(this.dataTypeId.equals(1L)) {
					errorMessage = "Owner should not be empty";
				} else if(this.dataTypeId.equals(3L)) {
					errorMessage = "SensorId should not be empty";
				}
			} else if(this.dataTypeId.equals(1L) && awsData.getKey().equals("Owner")) {
				result = false;
				errorMessage = "Owner name should not be \"Owner\"";
			}  else if(this.dataTypeId.equals(3L) && awsData.getKey().equals("SensorId")) {
				result = false;
				errorMessage = "Please change the Sensor Id.";
			} else if(tableDataKeys.contains(awsData.getKey())) {
				result = false;
				if(this.dataTypeId.equals(1L)) {
					errorMessage = "Owner should not be the same.";
				} else if(this.dataTypeId.equals(3L)) {
					errorMessage = "SensorId should not be the same.";
				}
			} else {
				tableDataKeys.add(awsData.getKey());
			}
		}
		
		return result;
	}
	
	public boolean isValidTableDeletedDataKey() {
		boolean result = true;
		List<Object> tableDataKeys = new ArrayList();
		for(Object object : this.getTableDatas()) {
			AwsDataInterface awsData = (AwsDataInterface) object;
			if(awsData.getKey() == null || awsData.getKey().equals("")) {
				result = false;
				if(this.dataTypeId.equals(1L)) {
					errorMessage = "Owner should not be empty";
				} else if(this.dataTypeId.equals(3L)) {
					errorMessage = "SensorId should not be empty";
				}
			} else {
				if(tableDataKeys.contains(awsData.getKey())) {
			
					result = false;
					if(this.dataTypeId.equals(1L)) {
						errorMessage = "Owner should not be the same.";
					} else if(this.dataTypeId.equals(3L)) {
						errorMessage = "SensorId should not be the same.";
					}
				} else {
					
					if(this.dataTypeId.equals(1L)) {
						String owner = (String) awsData.getKey();
						List sensorList = service.getSensorListsByOwner(owner);
						if(sensorList != null && sensorList.size() > 0) {
							result = false;
							errorMessage = "Owner sensor list is not empty. not able to delete it.";
						}
					} else {
						tableDataKeys.add(awsData.getKey());
					}
				}
			}
		}
		
		return result;
	}
	
	public boolean isValidTableMapDataKey() {
		boolean result = true;
		List<Object> tableDataKeys = new ArrayList();
		for(Object object : this.getSelTableTempMapDataObj().values()) {
			AwsDataInterface awsData = (AwsDataInterface) object;
			System.out.println("awsData.getKey() " + awsData.getKey());
			if(awsData.getKey() == null || awsData.getKey().equals("")) {
				result = false;
				if(this.mapDataTypeId.equals(2L)) {
					dialogErrorMessage = "Contact name should not be empty.";
				} 
			} else if(this.mapDataTypeId.equals(2L) && awsData.getKey().equals("Contact")) {
				result = false;
				dialogErrorMessage = "Please change the contact name.";
			} else if(tableDataKeys.contains(awsData.getKey().toString())) {
				result = false;
				dialogErrorMessage = "Contact name should not be the same.";
			} else {
				tableDataKeys.add(awsData.getKey().toString());
			}
		}
		
		return result;
	}
	
	
	public String isWarrantyExpired(String warrrantyEndDateStr) {
		
		String result = "1";
		try {
			Date warrrantyEndDate = new Date(warrrantyEndDateStr);
			Date dateNow = new Date();
			DebugUtil.log("*********  warrantyEndDate " + warrrantyEndDate + ", DateNow " + dateNow) ;
			if(dateNow.before(warrrantyEndDate)) {
				result = "0";
			} else {
				result = "1";
			}
		} catch(Exception ex) {
			result = "0";
		}
		
		return result;
	}
	   /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * @param sessionBean the sessionBean to set
     */
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getDialogErrorMessage() {
		return dialogErrorMessage;
	}

	public void setDialogErrorMessage(String dialogErrorMessage) {
		this.dialogErrorMessage = dialogErrorMessage;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Map getSelTableTempMapDataObj() {
		return selTableTempMapDataObj;
	}

	public void setSelTableTempMapDataObj(Map selObj) {
		//System.out.println("setSelTableMapDataObj " + selObj.toString());
		if(mapDataTypeId != null) {
			AwsDataType awsDataType = dataTypeManager.getAwsDataMap().get(mapDataTypeId);
			
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:detailTable");
	    	dataTable = null;
			
			
			this.selTableDataMapColumns = awsDataType.getColumnModels();
			//this.selTableTempMapDataObj = selObj;
			
			selTableTempMapDataObj = new HashMap<>();
			Iterator itr = selObj.values().iterator();
			while(itr.hasNext()) {
				AwsDataInterface awsData = (AwsDataInterface) itr.next();
				selTableTempMapDataObj.put(awsData.getKey(), awsData);
			}
			System.out.println("selTableTempMapDataObj size " + selTableTempMapDataObj.size());
			
		} else {
			System.out.println("mapDataTypeId is null");
		}
	}
    
	public void handleDialogClose() {
		this.dialogErrorMessage = "";
		this.isDialogEditing = false;
	}
    
    
}