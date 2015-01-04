package com.ratio.connectedcommuter.domain;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.ratio.connectedcommuter.application.CCApp;

public class DBManager extends Activity {
	
	private static String TAG = DBManager.class.getSimpleName();
	private static String DB_NAME = "connected_car";
	
	private Context mContext;
	private Manager mManager;
	private Database mDatabase;
	
	public void init(Context context) {

		if (mManager == null) {
			// create a manager
			try {
			    mManager = new Manager(new AndroidContext(mContext), Manager.DEFAULT_OPTIONS);
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
	}
	
	public void update(String docID, Map<String, Object> data) {
		Document retrievedDocument = this.select(docID);
		
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
	
	public void insert(Map<String, Object> data) {
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
		String docID = document.getId(); 
	}
	
	public Document select(String docID) {
		// retrieve the document from the database
		return mDatabase.getDocument(docID);
	}
	
	public void delete(String docID, Map<String, Object> data) {
		Document retrievedDocument = this.select(docID);
		// delete the document
		try {
		    retrievedDocument.delete();
		    Log.d (TAG, "Deleted document, deletion status = " + retrievedDocument.isDeleted());
		} catch (CouchbaseLiteException e) {
		    Log.e (TAG, "Cannot delete document", e);
		}
	}
	
	private String getCurrentDateTime(){
		// get the current date and time
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return dateFormatter.format(GregorianCalendar.getInstance().getTime());
	}
}
