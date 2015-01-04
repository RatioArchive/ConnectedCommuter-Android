package com.ratio.connectedcommuter.application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;
import com.ratio.common.application.RatioApplication;
import com.ratio.common.interfaces.ILogger;
import com.ratio.common.utils.FontUtils;
import com.ratio.common.utils.Logger;
import com.ratio.connectedcommuter.R;

public class CCApp extends RatioApplication implements ILogger {

	private static final boolean LOGGING_ENABLED = true;
	private static final String TAG = CCApp.class.getSimpleName();
	private static final String SYNC_URL_HTTP = "http://ec2-54-226-153-12.compute-1.amazonaws.com:4984/hack3/";
	private String mDeviceId;
	private String mPoolId;
	
	private String mPersonId;
	private String mPoolDocId;
	
	private static String DB_NAME = "connected_car";
	
	private Manager mManager;
	private Database mDatabase;
	
	public static CCApp mInstance;

	public static CCApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mDeviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        mPoolId = mDeviceId + "_pool";
        dbInit();
    }    
       
    // Fonts need to be initialized in static block to prevent memory leaks
    static {
        initFonts();
    }

    // Protected static methods //
    protected static void initFonts() {
        Map<String, Integer> fontMap = new HashMap<String, Integer>();
        fontMap.put("HelveticaNeueBoldItalic", R.raw.helvetica_neue_bold_italic);
        fontMap.put("HelveticaNeueBold", R.raw.helvetica_neue_bold);
        fontMap.put("HelveticaNeueLightItalic", R.raw.helvetica_neue_light_italic);
        fontMap.put("HelveticaNeueLight", R.raw.helvetica_neue_light);
        fontMap.put("HelveticaNeueMedium", R.raw.helvetica_neue_medium);
        fontMap.put("HelveticaNeueUltraLight", R.raw.helvetica_neue_ultra_light);
        fontMap.put("HelveticaNeue", R.raw.helvetica_neue);
        fontMap.put("RobotoBlack", R.raw.roboto_black);
        fontMap.put("RobotoBold", R.raw.roboto_bold);
        fontMap.put("RobotoLight", R.raw.roboto_light);
        fontMap.put("RobotoRegular", R.raw.roboto_regular);
        FontUtils.setFontMap(fontMap);
    }

    @Override
    protected Logger createLogger() {
        return Logger.createInstance(this);
    }

    @Override
    public boolean getLoggingEnabled() {
        return LOGGING_ENABLED;
    }

    @Override
    public String getAppTag() {
        return TAG;
    }
    
	public void dbInit() {

		if (mManager == null) {
			// create a manager
			try {
			    mManager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
			    Log.d (TAG, "Manager created");
			    initDB();
			} catch (IOException e) {
			    Log.e(TAG, "Cannot create manager object");
			}
		}
	}
	
	private void initDB() {
		if (mDatabase == null) {
			// create a name for the database and make sure the name is legal
			if (!Manager.isValidDatabaseName(DB_NAME)) {
			    Log.e(TAG, "Bad database name");
			    return;
			}
			// create a new database
			try {
			    mDatabase = mManager.getDatabase(DB_NAME);
			    Log.d (TAG, "Database created");
			} catch (CouchbaseLiteException e) {
			    Log.e(TAG, "Cannot get database");
			    return;
			}
		}
		//This is where we synchronize with the cloud server
//		try {
//			Replication pullReplication = mDatabase.createPullReplication(new URL(SYNC_URL_HTTP));
//			pullReplication.setContinuous(true);
//			pullReplication.start();
//			Replication pushReplication = mDatabase.createPushReplication(new URL(SYNC_URL_HTTP));
//			pushReplication.setContinuous(true);
//	        pushReplication.start();
//		} catch (MalformedURLException e) {
//			// TODO Show this pretty like later
//			e.printStackTrace();
//		}
        
		//This is where we should be querying to see if our basic documents are created locally - these won't be sent to the server.
		
//		Document personDoc = selectDoc(mDeviceId);
//		if (personDoc.getProperties() == null) {
			// create an object that contains data for a person document
			Map<String, Object> personProperties = new HashMap<String, Object>();
			personProperties.put("_id", mDeviceId);
			personProperties.put("type", "Person");
			personProperties.put(Constants.PROFILE_IMG, R.drawable.avatars_14);
			personProperties.put(Constants.TOTAL_PTS, 694);
			mPersonId = insertDoc(personProperties);
//		}
//		
//		Document poolDoc = selectDoc(mPoolId);
//		if (poolDoc.getProperties() == null) {
			// create an object that contains data for a pool document
			List<Integer> riders = new ArrayList<Integer>();
			riders.add(R.id.btn);
			riders.add(R.drawable.avatars_14);
			riders.add(R.drawable.avatars_15);
			riders.add(R.drawable.avatars_16);
			Map<String, Object> poolProperties = new HashMap<String, Object>();
			//figure out a better id
			poolProperties.put("_id", mPoolId);
			poolProperties.put("type", "Pool");
			poolProperties.put("start_datetime", "");
			poolProperties.put("end_datetime", "");
			poolProperties.put(Constants.RIDERS, riders);
			mPoolDocId = insertDoc(poolProperties);
//		}		
	}
	
	public String getPersonId() {
		return mPersonId;
	}
	
	public String getPoolId() {
		return mPoolDocId;
	}
	
	//Update a document - with the replication on this will sync to the server automatically
	public void updateDoc(String docID, Map<String, Object> data) {
		Document retrievedDocument = this.selectDoc(docID);
		
		Map<String, Object> updatedProperties = new HashMap<String, Object>();
		updatedProperties.putAll(retrievedDocument.getProperties());
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			updatedProperties.put(entry.getKey(), entry.getValue());
		}
		try {
		    retrievedDocument.putProperties(updatedProperties);
		    Log.d(TAG, "updated retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
		} catch (CouchbaseLiteException e) {
		    Log.e (TAG, "Cannot update document", e);
		}
	}

	//Insert a document - with the replication on this will sync to the server automatically
	public String insertDoc(Map<String, Object> data) {
		// create an empty document
		Document document = mDatabase.createDocument();
		// add content to document and write the document to the database
		try {
		    document.putProperties(data);
		    Log.d (TAG, "Document written to database named " + mDatabase.getName() + " with ID = " + document.getId());
		} catch (CouchbaseLiteException e) {
		    Log.e(TAG, "Cannot write document to database", e);
		}
		// save the ID of the new document
		return document.getId(); 
	}

	//Select a document
	public Document selectDoc(String docID) {
		// retrieve the document from the database
		return mDatabase.getDocument(docID);
	}

	//Delete a document - with the replication on this will sync to the server automatically
	public void deleteDoc(String docID, Map<String, Object> data) {
		Document retrievedDocument = this.selectDoc(docID);
		// delete the document
		try {
		    retrievedDocument.delete();
		    Log.d (TAG, "Deleted document, deletion status = " + retrievedDocument.isDeleted());
		} catch (CouchbaseLiteException e) {
		    Log.e (TAG, "Cannot delete document", e);
		}
	}
	
	public String getCurrentDateTime(){
		// get the current date and time
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return dateFormatter.format(GregorianCalendar.getInstance().getTime());
	}
}
