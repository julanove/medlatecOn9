package medlatec.listview.connect;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.OAEPParameterSpec;

import medlatec.listview.object.Doctor;
import medlatec.listview.object.PID;
import medlatec.listview.object.TestCode;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

public class DatabaseHandlerTestCode extends SQLiteOpenHelper {
 

    private static String DB_PATH = "/data/data/medlatec.listview/databases/";
    
    private static String DB_NAME = "tprofile.db";

   // private SQLiteDatabase myDataBase; 
    private SQLiteDatabase database;
    private static SQLiteDatabase myDataBase;
    private static DatabaseHandlerTestCode mInstance;
    
    private final Context myContext;
    
    
 
    public DatabaseHandlerTestCode(Context context) {
    	
     
         super(context, DB_NAME, null, 1);
         this.myContext = context;
    }
    
    ///
    public void initialise() 
    {
    	try
    	{
	        if (mInstance == null) {
	            if (!checkDatabase()) 
	            {
	            	this.getReadableDatabase();
	                copyDataBase();
	            }
	            
	            mInstance = new DatabaseHandlerTestCode(myContext);
	            myDataBase = mInstance.getWritableDatabase();
;
	        }
    	}
    	catch (IOException ex)
    	{
    		Log.d("ERROR", "" + ex.getMessage());
    	}
    }
    
    public DatabaseHandlerTestCode getInstance(){
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return myDataBase;
    }
    
    private static boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try 
        {
            try 
            {
            	String myPath = DB_PATH + DB_NAME;
            	checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            	
            	myDataBase.close();
            } catch (Exception e) 
            { }
        } catch 
        (Throwable ex) 
        {
        }
        
        Log.d("CHECKDB", "" +  checkDB != null ? "TRUE" : "FALSE");
        
        return checkDB != null ? true : false;
    }
    
   ////////////////////////////////////////////////////////////////////////////////////////
   
 
    @Override
	public synchronized void close() 
    {
    	    if(database != null)
    	    	database.close();
 
    	    super.close();
	}
    
    private void copyDataBase() throws IOException
    {
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    	
    	Log.d("COPY", "DONE");
 
    }
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
	public List<TestCode> searchTestCode(String keyword) {
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        TestCode contact;
        String selectQuery = "";

    	selectQuery = "SELECT * FROM " + "tbl_TestCode where TestName like '%"+keyword+"%' and Price != 0 and QuickCode IS NOT NULL order by TestName";
        
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
        	// PID
        	// 1 Name
        	
	        if (cursor.moveToFirst()) {
	            do {
	            	contact = new TestCode();
	            	
	                if(cursor.getString(0) != null)
	                {contact.setTestCode(cursor.getString(0));}
	                
	                if(cursor.getString(2) != null)
	                {contact.setTestName(cursor.getString(2));}

	                if(cursor.getString(4) != null)
	                {contact.setPrice(cursor.getString(4));}
            	
	                if(cursor.getString(3) != null)
	                {contact.setCategory(cursor.getString(3)); }
	               
	                contactList.add(contact);
	            } while (cursor.moveToNext());
	        }
	        
	        cursor.close();
 
        }
        catch (SQLiteException e) 
        {
			// TODO: handle exception
		}
        finally
        {
        	
        	//if(db != null && db.isOpen())
             //   db.close();
        }
        // return contact list
        return contactList;
    }
	

	public TestCode selectOneTestCode(String testcode) {
	    
        TestCode contact = new TestCode();
        String selectQuery = "";

        
    	selectQuery = "SELECT * FROM " + "tbl_TestCode where TestCode = '"+testcode+"'";
        
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
        	
	        if (cursor.moveToFirst()) {
	            do {
	            	
	            		contact.setSid(cursor.getString(1));
	            	
		                if(cursor.getString(0) != null)
		                {contact.setTestCode(cursor.getString(0));}
		                
		                if(cursor.getString(2) != null)
		                {contact.setTestName(cursor.getString(2));}

		                if(cursor.getString(4) != null)
		                {contact.setPrice(cursor.getString(4));}
	            	
		                if(cursor.getString(3) != null)
		                {contact.setCategory(cursor.getString(3)); }
	                
	                // Adding contact to list
	            } while (cursor.moveToNext());
	        }
	        
	        cursor.close();
 
        }
        catch (SQLiteException e) 
        {
			// TODO: handle exception
		}
        finally
        {
        	
        	//if(db != null && db.isOpen())
             //   db.close();
        }
        // return contact list
        return contact;
    }
	
	public void deleteAllRealTestCode() {
	       // SQLiteDatabase db = this.getWritableDatabase();

	        myDataBase.delete("tbl_TestCode", "TestCode" + " != ?",
	                new String[] { "1" });
	    }
	
    public void insertTestCode(TestCode testCode) {
        
    	SQLiteDatabase db = null;
    	try
    	{
	        ContentValues values = new ContentValues();
	
	         // Contact Name
	        values.put("TestCode", testCode.getTestCode()); 
	        values.put("QuickCode", testCode.getPrice());
	        values.put("TestName", testCode.getTestName()); 
	        values.put("Category", testCode.getCategory());
	        values.put("Price", testCode.getPrice());
	        
	        values.put("TestHead", 0);
	        values.put("Profile", 0);
	        values.put("Bold", 0);
	        values.put("Stat", 1);
	        values.put("LowHigh", 0);
	        values.put("Type", "P");
	        values.put("Optional", 0);
	        values.put("ShowTestResult", 0);
	        values.put("ResultType", 0);
	        values.put("rowguid", testCode.getRowguid());
	        
/*	        
	        values.put("NormalRange", testCode.getRange());
	        values.put("NormalRangeF", testCode.getRangeF());
	        values.put("PrintOrder", testCode.getPrintOrder());
	        values.put("Unit", testCode.getUnit());
	        values.put("LowerLimit", testCode.getLower());
	        values.put("HigherLimit", testCode.getHigher());
	        
	        ///
	        
	        values.put("BLLower", testCode.getBllLower());
	        values.put("BLHigher", testCode.getBllHigher());
	        values.put("TestNameE", testCode.getTestNameE());
	        values.put("BLLowerE", testCode.getBllLowerE());
	        values.put("BLHigherE", testCode.getBllHigherE());
	        values.put("Criteria", testCode.getCriteria());
	        
	        */

        // Inserting Row
        
	        myDataBase.insert("tbl_TestCode", null, values);
        	//Log.d("tbl_DichVu", "tbl_DichVu");
    	}
    	catch(Exception ex)
    	{
    		
    	}
        finally {
            if (db != null && db.isOpen()) {
              //  db.close();
                
            }
        }
    }

	public List<TestCode> getAllTestCode() {
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        TestCode contact;
        String selectQuery = "";

    	selectQuery = "SELECT * FROM " + "tbl_TestCode";
        
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
        	// PID
        	// 1 Name
        	
	        if (cursor.moveToFirst()) {
	            do {
	            	contact = new TestCode();
	            	
	                if(cursor.getString(0) != null)
	                {contact.setTestCode(cursor.getString(0));}
	                
	                if(cursor.getString(2) != null)
	                {contact.setTestName(cursor.getString(2));}

	                if(cursor.getString(4) != null)
	                {contact.setPrice(cursor.getString(4));}
            	
	                if(cursor.getString(3) != null)
	                {contact.setCategory(cursor.getString(3)); }
	                
	                
	                // Adding contact to list
	                contactList.add(contact);
	            } while (cursor.moveToNext());
	        }
	        
	        cursor.close();
 
        }
        catch (SQLiteException e) 
        {
			// TODO: handle exception
		}
        finally
        {
        	
        	//if(db != null && db.isOpen())
             //   db.close();
        }
        // return contact list
        return contactList;
    }

}