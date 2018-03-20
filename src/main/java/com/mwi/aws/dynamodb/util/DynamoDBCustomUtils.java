package com.mwi.aws.dynamodb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * a collection of static general methods to aid in DynamoDB operations.
 */
public class DynamoDBCustomUtils {
    /**
     * generate an UpdateItemSpec for a DynamoDB object with primary key name,
     * primary key value, and the attributes to be updated (set).
     * @param primaryKeyName primary key name
     * @param primaryKey primary key value
     * @param setAttr attributes to be updated in the form of a String to Object map
     *      the map key is attribute name, the map value is attribute value.
     * @return UpdateItemSpec
     */
    public static UpdateItemSpec genUpdateItemSpecSetAttr(
            String primaryKeyName,
            Object primaryKey,
            Map<String, Object> setAttr) {
        UpdateItemSpec us = new UpdateItemSpec();
        String uex = "set ";
        NameMap nameMap = new NameMap();
        ValueMap valMap = new ValueMap();
        int index = 0;
        // for each attribute in the map
        for (Entry<String, Object> entry : setAttr.entrySet()) {
            if (entry.getValue() != null) {
                String nameEx = "#a" + index;
                String valEx = ":v" + index;
                uex += nameEx + "=" + valEx + ", ";
                nameMap.put(nameEx, entry.getKey());
                valMap.put(valEx, entry.getValue());
                index++;
            }
        }
        // uex example: "set #a0=:v0, #a1=:v1"
        // remove last ", "
        uex = uex.replaceAll(", $", "");
        us.withPrimaryKey(primaryKeyName, primaryKey)
                .withUpdateExpression(uex)
                .withNameMap(nameMap)
                .withValueMap(valMap);
        return us;
    }

    /**
     * generate an UpdateItemSpec for a DynamoDB object with primary key name,
     * primary key value, update (set) an object on a list attribute with attribute name,
     * index, and new value at the index.
     * @param primaryKeyName primary key name
     * @param primaryKey primary key value
     * @param listAttrName attribute name of the list attribute
     * @param index index of the list
     * @param indexObject attribute values to set at the index in the list
     * @return UpdateItemSpec
     */
    public static UpdateItemSpec genUpdateItemSpecSetListAttr(
            String primaryKeyName,
            Object primaryKey,
            String listAttrName,
            int index,
            Object indexObject) {
        UpdateItemSpec us = new UpdateItemSpec();
        String uex = "set " + listAttrName + "[" + index + "] = :v1";
        ValueMap valMap = new ValueMap();
        valMap.put(":v1", indexObject);
        us.withPrimaryKey(primaryKeyName, primaryKey)
            .withUpdateExpression(uex)
            .withValueMap(valMap);
        return us;
    }

    /**
     * filter out items (DB rows) from an Item Collection where value of a specific attribute
     * in the item does not equal to comparison string (case insensitive).
     * @param itemCollection a collection of items
     * @param attrName attribute name to check
     * @param comp comparison string for filtering
     * @return filtered item list
     */
    public static List<Item> filterItemsByStringCaseInsens(
            Iterable<Item> itemCollection, String attrName, String comp) {
        List<Item> res = new ArrayList<Item>();
        // remove space and "."
        String check = comp.replaceAll("\\s|\\.", "");
        Iterator<Item> itr = itemCollection.iterator();
        while (itr.hasNext()) {
            Item item = itr.next();
            String itemCheck = item.getString(attrName).replaceAll("\\s|\\.", "");
            if (itemCheck.equalsIgnoreCase(check)) {
                res.add(item);
            }
        }
        return res;
    }

    /**
     * Convert a <String, Object> map to a <String, AttributeValue> map.
     * Opposite of convertToObjectMap(Map<String, AttributeValue> map)
     * Supported Object Classes:
     *   Long, Double, Float, Integer, BigDecimal, String, Boolean,
     *   and Map (recursive call), List of any of the above classes.
     * @param map input map. map key is attribute name, map value is attribute value in Object
     * @return output map. map key is attribute name, map value is attribute value in AttributeValue
     */
    public static Map<String, AttributeValue> convertToAttributeValueMap(Map<String, Object> map) {
        Map<String, AttributeValue> res = new HashMap<String, AttributeValue>();
        // for each attribute in the map
        for (Entry<String, Object> entry : map.entrySet()) {
            AttributeValue attr;
            String attrName = entry.getKey();
            Object attrVal = entry.getValue();
            
            if (attrVal instanceof java.lang.Double
                    || attrVal instanceof java.lang.Long
                    || attrVal instanceof java.lang.Integer
                    || attrVal instanceof java.lang.Float
                    || attrVal instanceof java.math.BigDecimal) {
                attr = new AttributeValue().withN(String.valueOf(attrVal));

            } else if (attrVal instanceof java.lang.String) {
                attr = new AttributeValue().withS((String) attrVal);
            
            } else if (attrVal instanceof java.lang.Boolean) {
                attr = new AttributeValue().withBOOL((Boolean) attrVal);
            
            } else if (attrVal instanceof java.util.Map) {
                Map<String, AttributeValue> attrMap =
                        convertToAttributeValueMap((Map<String, Object>) attrVal);
                attr = new AttributeValue().withM(attrMap);
            
            } else if (attrVal instanceof java.util.List) {
                List<Object> attrList = (List<Object>) attrVal;
                List<AttributeValue> convList = new ArrayList<AttributeValue>();
                if(attrList.size() > 0){
                    Object attrTypeCheck = attrList.get(0);
                    if (attrTypeCheck instanceof java.lang.Double
                            || attrTypeCheck instanceof java.lang.Long
                            || attrTypeCheck instanceof java.lang.Integer
                            || attrTypeCheck instanceof java.lang.Float
                            || attrTypeCheck instanceof java.math.BigDecimal) {
                        // for each obj in the list
                        for (Object attrObj : attrList) {
                            convList.add(new AttributeValue().withN(String.valueOf(attrObj)));
                        }
                    } else if (attrTypeCheck instanceof java.lang.String) {
                        for (Object attrObj : attrList) {
                            convList.add(new AttributeValue().withS((String) attrObj));
                        }
                    } else if (attrTypeCheck instanceof java.lang.Boolean) {
                        for (Object attrObj : attrList) {
                            convList.add(new AttributeValue().withBOOL((Boolean) attrObj));
                        }
                    } else if (attrTypeCheck instanceof java.util.Map) {
                        for (Object attrObj : attrList) {
                            convList.add(new AttributeValue().withM(
                                    convertToAttributeValueMap((Map<String, Object>) attrObj)));
                        }
                    }
                }
                attr = new AttributeValue().withL(convList);

            } else { // null value default
                attr = new AttributeValue().withNULL(true);
            }
            res.put(attrName, attr);
        }
        return res;
    }

    /**
     * Convert a <String, AttributeValue> map to a <String, Object> map.
     * Opposite of convertToAttributeValueMap(Map<String, Object> map) Right
     * now only handles AttributeValues of type
     *   BOOL (boolean), N (number), NS (number set), SS (String set), S (String),
     *    M (Map - recursive call), L (Lists - only String lists or Number lists)
     * unsupported AttributeValue types: 
     *    non-string or non-number lists
     * @param map input map. map key is attribute name, map value is attribute value in AttributeValue
     * @return output map. map key is attribute name, map value is attribute value in Object
     */
    public static Map<String, Object> convertToObjectMap(Map<String, AttributeValue> map) {
        Map<String, Object> res = new HashMap<String, Object>();
        // for each attribute in the map
        for (String attrName : map.keySet()) {
            AttributeValue val = map.get(attrName);

            if (val.getBOOL() != null) {
                res.put(attrName, val.getBOOL());
            
            } else if (val.getN() != null) {
                String valString = val.getN();
                if (valString.contains(".")) {
                    res.put(attrName, Double.parseDouble(valString));
                } else {
                    res.put(attrName, Integer.parseInt(valString));
                }
            
            } else if (val.getNS() != null) {
                List<String> ns = val.getNS();
                List<Double> doubleList = new ArrayList<Double>();
                for (String n : ns) {
                    doubleList.add(Double.parseDouble(n));
                }
                res.put(attrName, doubleList);

            } else if (val.getS() != null) {
                res.put(attrName, val.getS());

            } else if (val.getSS() != null) {
                res.put(attrName, val.getSS());
            
            } else if (val.getM() != null) {
                res.put(attrName, convertToObjectMap(val.getM()));
            
            } else if (val.getL() != null) {
                List subRes;
                List<AttributeValue> attrList = val.getL();
                if(attrList == null || attrList.size() == 0){
                    subRes = null;
                } else if (attrList.get(0).getS() != null) {
                    subRes = new ArrayList<String>();
                    for (AttributeValue attr : attrList) {
                        subRes.add(attr.getS());
                    }
                } else if (attrList.get(0).getN() != null) {
                    subRes = new ArrayList<Double>();
                    for (AttributeValue attr : attrList) {
                        subRes.add(Double.parseDouble(attr.getN()));
                    }
                } else {
                    subRes = null;
                }

                res.put(attrName, subRes);
            }
        }
        return res;
    }
    
    /**
     * Convert an object to JSONObject
     * @param obj object
     * @return JSONObject
     */
    public static JSONObject toJSON(Object obj) {
        JSONObject jsonObj = new JSONObject();
        
    	Gson gson = new GsonBuilder().create();
        String jsonStr = gson.toJson(obj, obj.getClass());
        
        JSONParser parser = new JSONParser();
        try {
        	jsonObj = (JSONObject) parser.parse(jsonStr);
        } catch (Exception e) {
        	System.out.println("Convert to JSONObject exception: " + e
        			+ ", jsonStr = " + jsonStr);
        }

        return jsonObj;
    }
}
