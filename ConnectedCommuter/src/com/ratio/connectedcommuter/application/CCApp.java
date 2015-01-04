package com.ratio.connectedcommuter.application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.ratio.common.application.RatioApplication;
import com.ratio.common.interfaces.ILogger;
import com.ratio.common.utils.FontUtils;
import com.ratio.common.utils.Logger;
import com.ratio.connectedcommuter.R;
import com.ratio.connectedcommuter.domain.DBManager;

public class CCApp extends RatioApplication implements ILogger {

	private static final boolean LOGGING_ENABLED = true;
	private static final String TAG = CCApp.class.getSimpleName();
	
	private static String DB_NAME = "connected_car";
	
	private Manager mManager;
	private Database mDatabase;
	private String mPersonDocId;
	private String mPoolDocId;
	
	public static CCApp mInstance;

	public static CCApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
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
		
		// create an object that contains data for a person document
		Map<String, Object> personDoc = new HashMap<String, Object>();
		personDoc.put(Constants.PROFILE_IMG, R.id.btn);
		personDoc.put(Constants.TOTAL_PTS, 694);
		mPersonDocId = insertDoc(personDoc);

		// create an object that contains data for a person document
		List<Integer> riders = new ArrayList<Integer>();
		riders.add(R.id.btn);
		Map<String, Object> poolDoc = new HashMap<String, Object>();
		poolDoc.put("start_datetime", "");
		poolDoc.put("end_datetime", "");
		poolDoc.put(Constants.RIDERS, riders);
		mPoolDocId = insertDoc(poolDoc);
	}
	
	public String getPersonId() {
		return mPersonDocId;
	}
	
	public String getPoolId() {
		return mPoolDocId;
	}
	
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
	
	public Document selectDoc(String docID) {
		// retrieve the document from the database
		return mDatabase.getDocument(docID);
	}
	
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
