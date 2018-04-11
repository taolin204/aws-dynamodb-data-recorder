package com.mwi.aws.dynamodb.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.mfg.SensorConfig;


/**
 * Class for containing the formatting strings and DateFormat for making human
 * readable event messages.
 */
public class FormattingControl {
    /** DateFormat to format a human readable date time in year, month, day, hour, minute. */
    private DateFormat df;
    /** sensorId/ assocId of the sensor/ assoc that this object formats*/
    private String id;
    
    public static final String sensorCriticalHighAlarmMsg = "CRITICAL HIGH";
    public static final String sensorCriticalLowAlarmMsg = "CRITICAL LOW";
    public static final String sensorHighAlarmMsg = "ALARM HIGH";
    public static final String sensorLowAlarmMsg = "ALARM LOW";
    public static final String sensorHighRestoreMsg = "NOMINAL";
    public static final String sensorLowRestoreMsg = "NOMINAL";

    public static final String matchedAssocHighAlarmMsg = "ALARM HIGH";
    public static final String matchedAssocLowAlarmMsg = "ALARM LOW";
    public static final String unmatchedAssocAlarmMsgPref = "Two Or More Sensors Have Too High A Variation (>";
    public static final String unmatchedAssocAlarmMsgSuf = ")";
    public static final String matchedAssocHighRestoreMsg = "No Longer All Giving High Readings";
    public static final String matchedAssocLowRestoreMsg = "No Longer All Giving Low Readings";
    public static final String unmatchedAssocRestoreMsg = "No Longer Giving Readings Too Far Apart";

    //========== PMS change start ==========
    private static final String sensorOfflineMsg = "OFFLINE: no data received";
    private static final String sensorOnlineMsg = "ONLINE: data received";
    private static final String systemOfflineMsg = "ERROR: IoT system is down";
    private static final String systemOnlineMsg = "NORMINAL: IoT system is OK";
    private static final String sensorMaitMsg = "due for maintenance on ";
    //========== PMS change end ==========
    
    public static final String since = "Since";
    public static final String previousStatus = "Previously";
    public static final String fromString = "From";
    public static final String toString = "-";
    public static final String readingsString = "Readings:";

    /**
     * Constructor.
     * @param id sensorId/ assocId
     * @param sensorConfig
     */
    public FormattingControl(String id, SensorConfig sensorConfig) {
        df = new SimpleDateFormat("dd MMM hh:mmaa");
        //========== PMS change start ==========
        if (sensorConfig != null) {
        //========== PMS change end ==========
	        String timezone = sensorConfig.getTimezone() == null
	                ? SensorConfig.DF_TIME_ZONE : sensorConfig.getTimezone();
	        df.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        this.id = id;
    }

    /* FIELD GETTERS AND SETTERS */
    // note that Drools requires field getters in the specific format get{FieldName}()
    // to access them even if field is public
    public DateFormat getDf() {
        return df;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSensorCriticalHighAlarmMsg() {
        return sensorCriticalHighAlarmMsg;
    }

    public String getSensorCriticalLowAlarmMsg() {
        return sensorCriticalLowAlarmMsg;
    }

    public String getSensorHighAlarmMsg() {
        return sensorHighAlarmMsg;
    }

    public String getSensorLowAlarmMsg() {
        return sensorLowAlarmMsg;
    }

    public String getSensorHighRestoreMsg() {
        return sensorHighRestoreMsg;
    }

    public String getSensorLowRestoreMsg() {
        return sensorLowRestoreMsg;
    }

    public String getMatchedAssocHighAlarmMsg() {
        return matchedAssocHighAlarmMsg;
    }

    public String getMatchedAssocLowAlarmMsg() {
        return matchedAssocLowAlarmMsg;
    }

    public String getUnmatchedAssocAlarmMsgPref() {
        return unmatchedAssocAlarmMsgPref;
    }

    public String getUnmatchedAssocAlarmMsgSuf() {
        return unmatchedAssocAlarmMsgSuf;
    }

    public String getMatchedAssocHighRestoreMsg() {
        return matchedAssocHighRestoreMsg;
    }

    public String getMatchedAssocLowRestoreMsg() {
        return matchedAssocLowRestoreMsg;
    }

    public String getUnmatchedAssocRestoreMsg() {
        return unmatchedAssocRestoreMsg;
    }

    public String getSince() {
        return since;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getFromString() {
        return fromString;
    }

    public String getToString() {
        return toString;
    }

    public String getReadingsString() {
        return readingsString;
    }
    /* /FIELD GETTERS AND SETTERS */

    //========== PMS change start ==========
    public String getSensorOfflineMsg() {
		return sensorOfflineMsg;
	}

    public String getSensorOnlineMsg() {
		return sensorOnlineMsg;
	}

    public String getSystemOfflineMsg() {
		return systemOfflineMsg;
	}

    public String getSystemOnlineMsg() {
		return systemOnlineMsg;
	}

    public String getSensorMaitMsg() {
		return sensorMaitMsg;
	}
    //========== PMS change end ==========
}
