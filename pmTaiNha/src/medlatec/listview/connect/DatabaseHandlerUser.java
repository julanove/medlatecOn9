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
import medlatec.listview.object.User;
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

public class DatabaseHandlerUser extends SQLiteOpenHelper {
 

    private static String DB_PATH = "/data/data/medlatec.listview/databases/";
    
    private static String DB_NAME = "users.db";

   // private SQLiteDatabase myDataBase; 
    private SQLiteDatabase database;
    private static SQLiteDatabase myDataBase;
    private static DatabaseHandlerUser mInstance;
    private final Context myContext;
    
    public DatabaseHandlerUser(Context context) {
    	
         super(context, DB_NAME, null, 1);
         this.myContext = context;
    }
    
    ///
    public void initialise() 
    {
    	try
    	{
	        if (mInstance == null) {
	           // if (!checkDatabase()) 
	           // {
	            	this.getReadableDatabase();
	                copyDataBase();
	           // }
	            mInstance = new DatabaseHandlerUser(myContext);
	            myDataBase = mInstance.getWritableDatabase();
;
	        }
    	}
    	catch (IOException ex)
    	{
    		Log.d("ERROR", "" + ex.getMessage());
    	}
    }
    
    public DatabaseHandlerUser getInstance(){
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
	
	public User login(String id, String password) {
        
    	User contact = new User();
        String selectQuery = "";

    	selectQuery = "select * from tbl_User where rtrim(UserID) = '"+id+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
        	
	        if (cursor.moveToFirst()) {
	            do {
	            	
	            		contact.setId(cursor.getString(0));
	            	
		                if(cursor.getString(2) != null)
		                {contact.setName(cursor.getString(2));}
		                
		                if(cursor.getString(4) != null)
		                {contact.setRole(cursor.getString(4));}
		             
	                
	                // Adding contact to list
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
        }
        // return contact list
        return contact;
    }
	
	public String getUserforBL(String startBy) 
	{
		String selectQuery = "";
		String tenCanBo = "";
		
		selectQuery = "select (UserName || ' - ' || Phone) as UserName  from tbl_User where StartBy = '"+startBy+"'";
		
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	
		try
		{
			if (cursor.moveToFirst()) 
			{
				do 
				{
					if(cursor.getString(0) != null)
					{
						tenCanBo = cursor.getString(0);
					}
					
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
		return tenCanBo;
	}
	
	public void addUser(User testCode) {
		
		SQLiteDatabase db = null;
			try
			{
			
				/*this.getWritableDatabase().close();
				db = this.getWritableDatabase();*/
				
				ContentValues values = new ContentValues();
				values.put("UserID", testCode.getId().trim()); // Contact Name
				values.put("Password", testCode.getId().trim());
				values.put("UserName", testCode.getName());
				values.put("Role", "0");
				
				
				// Inserting Row
				
				myDataBase.insert("tbl_User", null, values);
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

	public List<User> getAllUser() {
    	
        List<User> contactList = new ArrayList<User>();
        User contact;
        String selectQuery = "";

    	selectQuery = "select  (StartBy || '_' || UserID) as UserID, UserName from tbl_User where StartBy is not null";
        
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
        	// PID
        	// 1 Name
        	
	        if (cursor.moveToFirst()) {
	            do {
	            	contact = new User();
	            	
            		contact.setId((cursor.getString(0)));
            	
	                if(cursor.getString(1) != null)
	                {
	                	contact.setName((cursor.getString(1)));
	                }
	                
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