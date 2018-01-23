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

public class DatabaseHandlerPID extends SQLiteOpenHelper {
 

    private static String DB_PATH = "/data/data/medlatec.listview/databases/";
    
    private static String DB_NAME = "pids.db";

   // private SQLiteDatabase myDataBase; 
    private SQLiteDatabase database;
    private static SQLiteDatabase myDataBase;
    private static DatabaseHandlerPID mInstance;
    private final Context myContext;
    
    
    public DatabaseHandlerPID(Context context) {
    	
     
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
	            mInstance = new DatabaseHandlerPID(myContext);
	            myDataBase = mInstance.getWritableDatabase();
;
	        }
    	}
    	catch (IOException ex)
    	{
    		Log.d("ERROR", "" + ex.getMessage());
    	}
    }
    
    public DatabaseHandlerPID getInstance(){
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
	
	public String getMaxTime() {
    	
        String testCodes = "";
        String selectQuery = "";

    	selectQuery = "select max(Intime) from tbl_Inter";
       
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
		                if(cursor.getString(0) != null)
		                {
		                	testCodes = testCodes + cursor.getString(0);
	                	}
	                // Adding contact to list
	            } while (cursor.moveToNext());
	        }
	        
	        
 
        }
        catch (SQLiteException e) 
        {
        	Log.d("ERRORRRR HERRRRRRRRRRREEEEEEE", "ERRORRRR HERRRRRRRRRRREEEEEEE" );
		}
        finally
        {
        	cursor.close();
        }
       
    	return testCodes;
    }

	public List<PID> getAllDoc(String PID) {
    	
        List<PID> contactList = new ArrayList<PID>();
        PID contact;
        String selectQuery = "";

        selectQuery = "SELECT * FROM " + "tbl_Inter where rtrim(PID) = '"+PID+"'";
    	//selectQuery = "SELECT * FROM tbl_Inter where PID like '%"+PID+"%' order by PID ";
    	//
        
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
    	
	        if (cursor.moveToFirst()) {
	            do {
	            	contact = new PID();
	            	
            		contact.setpID((cursor.getString(0)));
            	
	                if(cursor.getString(1) != null)
	                {
	                	contact.setName(cursor.getString(1));
	                }
	                
	                if(cursor.getString(2) != null)
	                {
	                	contact.setSex(cursor.getString(2));
	                }
	                
	                if(cursor.getString(3) != null)
	                {
	                	contact.setAge(Integer.parseInt((cursor.getString(3))));
	                }
	                
	                ////////
	                
	                if(cursor.getString(7) != null)
	                {
	                	contact.setAddress(cursor.getString(7));
	                }
	                
	                if(cursor.getString(14) != null)
	                {
	                	contact.setPhone(cursor.getString(14));
	                }

	                
	                // Adding contact to list
	                contactList.add(contact);
	            } while (cursor.moveToNext());
	        }
	        
	        
 
        }
        catch (SQLiteException e) 
        {
			// TODO: handle exception
		}
        finally
        {
        	cursor.close();
        	//if(db != null && db.isOpen())
             //   db.close();
        }
        // return contact list
        return contactList;
    }

}