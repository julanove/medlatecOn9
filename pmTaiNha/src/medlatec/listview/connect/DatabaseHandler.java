package medlatec.listview.connect;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;

import medlatec.listview.DumbInfo;
import medlatec.listview.object.Doctor;
import medlatec.listview.object.Idk_Patient;
import medlatec.listview.object.NhanMau;
import medlatec.listview.object.PID;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.object.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
    //private static final String FILE_DIR = "contactsManager";
    
    // Contacts table name

    ////////////////////////////
    
 // Contacts table name
    private static final String TABLE_SELECTED = "tbl_SelectedTestCode";
    private static final String TABLE_PATIENT = "tbl_Patient";
    private static final String TABLE_RESULT = "tbl_Result";
    private static final String SID = "SID";

    
    private static String DB_PATH = "/data/data/medlatec.listview/databases/";
    
    private static String DB_NAME = "hml3.db";
    private static String DB_NAME2 = "pids.db";
    private static String DB_NAME3 = "doctors.db";
    private static String DB_NAME4 = "tprofile.db";

    private SQLiteDatabase database;
    private static SQLiteDatabase myDataBase;
    private static DatabaseHandler mInstance;
    
    private final Context myContext;
    
    public DatabaseHandler(Context context) {
    	
    	
    	
       // super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, Environment.getExternalStorageDirectory() + "/data/data/hml.db", null, DATABASE_VERSION);
         
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
	            mInstance = new DatabaseHandler(myContext);
	            myDataBase = mInstance.getWritableDatabase();
	          //  myDataBase = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() +  "hml.db",null, SQLiteDatabase.OPEN_READWRITE);
	        }
    	}
    	catch (IOException ex)
    	{
    		Log.d("ERROR", "" + ex.getMessage());
    	}
    }
    
    public DatabaseHandler getInstance(){
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
            	
            	//checkDB = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/data/data/hml.db",null, SQLiteDatabase.OPEN_READWRITE);
            	
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
   
	public void openDataBase() throws SQLException{
    	 
    	//Open the database
       // String myPath = DB_PATH + DB_NAME;
    	//myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	
    	//real device
    	myDataBase = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/data/data/hml.db",null, SQLiteDatabase.OPEN_READWRITE);
    	
    	
    	// virtual device
    	/*try
    	{
    		createDataBase();
    	}
    	catch(Exception ex)
    	{
    		
    	}*/
    }
 
    @Override
	public synchronized void close() 
    {
    	    if(database != null)
    	    	database.close();
 
    	    super.close();
	}
    
    public void deleteALLDB()
    {
    	Log.d("DELETE","" +	SQLiteDatabase.deleteDatabase(new File(DB_PATH + DB_NAME)));
    	Log.d("DELETE","" +	SQLiteDatabase.deleteDatabase(new File(DB_PATH + DB_NAME2)));
    	Log.d("DELETE","" +	SQLiteDatabase.deleteDatabase(new File(DB_PATH + DB_NAME3)));
    	Log.d("DELETE","" +	SQLiteDatabase.deleteDatabase(new File(DB_PATH + DB_NAME4)));
    }
    
    /*public void createDataBase() throws IOException{
   	 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist)
    	{
    		//do nothing - database already exist
    	}
    	else
    	{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try 
        	{
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }*/
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    /*private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try
    	{
    		//String myPath = DB_PATH + DB_NAME;
    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		checkDB = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/data/data/hml.db",null, SQLiteDatabase.OPEN_READONLY);
 
    	}
    	catch(SQLiteException e)
    	{
    		//database does't exist yet.
    		Log.d("xxxxx", "database doesnt exist");
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
    		
    		// want to refresh
    	    //Log.d("DELETE","" +	SQLiteDatabase.deleteDatabase(new File("/data/data/medlatec.listview/databases/hml.db")));
    		
    		
    		Log.d("xxxxx", "database does exist");
    	}
 
    	return checkDB != null ? true : false;
    }*/
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
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

	// Creating Tables
 
 
    // Upgrading database

    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new  testcode
    
	public void deleteOneTestCode(String sid, String testCode) {
	       // SQLiteDatabase db = this.getWritableDatabase();
		
	        myDataBase.delete(TABLE_RESULT, "SID" + " = ? and TestCode" + " = ?",
	                new String[] { sid, testCode });
	      
	    }
	
	public void deleteAllResult(String sid) {
	       // SQLiteDatabase db = this.getWritableDatabase();

			if (sid.equals("1"))
			{
		        myDataBase.delete(TABLE_RESULT, SID + " != ?",
		                new String[] { "1" });
			}
			else
			{
				myDataBase.delete(TABLE_RESULT, SID + " = ?",
		                new String[] { sid });
			}
	      
	    }
	    
	// Deleting single contact
    public void deleteAllPatient(String sid) {
       // SQLiteDatabase db = this.getWritableDatabase();

    	if (sid.equals("1"))
    	{
    		myDataBase.delete(TABLE_PATIENT, SID + " != ?",
    				new String[] { "1" });
    	}
    	else
    	{
    		myDataBase.delete(TABLE_PATIENT, SID + " = ?",
    				new String[] { sid });
    	}
      
    }
    
    // Adding new contact
    
    public void deleteResult(String testcode, String sid) {
	       // SQLiteDatabase db = this.getWritableDatabase();

	        myDataBase.delete(TABLE_RESULT, "TestCode = ? and SID = ?",
	                new String[] { testcode, sid });
	      
	    }
    
    public List<TestCode> getAllContacts(String sid, int page) {
    	
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        String selectQuery = "";

        if (sid.equals(""))
         selectQuery = "select a.* from tbl_TestCode a join tbl_Order b on a.TestCOde = b.TestCode where b.Page = "+page+" order by b.[Order]";
        else 
    	 selectQuery = "SELECT * FROM " + "tbl_Result where SID = '"+sid+"' and Price != 0";
        
        if(page == 2)
        {
        	selectQuery = "SELECT * FROM " + "tbl_Result where SID = '"+sid+"'";
        }
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {

	            	TestCode contact = new TestCode();
	            	if (sid.equals(""))
	            	{
	            		contact.setTestCode(cursor.getString(0));
	            	}
	            	else 
	            	{
	            		contact.setTestCode(cursor.getString(1));
	            	}
	                
	            	
	            	/////////////
	                
	                
	            	if (sid.equals(""))
	            	{
		                if(cursor.getString(2) != null)
		                {
		                	contact.setTestName(cursor.getString(2));
		                }
		                else
		                {continue;}
	            	}
	            	else
	            	{
	            		if(cursor.getString(2) != null)
		                {
		                	contact.setTestName(cursor.getString(2));
		                }
		                else
		                {continue;}
	            	}
	            	
	            	///
	                
	            	if (sid.equals(""))
	            	{
		                if(cursor.getString(3) != null)
		                {
		                	contact.setCategory(cursor.getString(3));
		                }
		                else
		                {continue;}
	            	}
	            	else
	            	{
	            		if(cursor.getString(5) != null)
		                {
		                	contact.setPrice(cursor.getString(5));
		                	//Log.d("Price", cursor.getString(6));
		                }
		                else
		                {continue;}
	            	}
	            	
	            	////
	            	
	            	if (sid.equals(""))
	            	{
		                if(cursor.getString(4) != null)
		                {
		                	contact.setPrice(cursor.getString(4));
		                }
		                else
		                {continue;}
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
 
    public List<TestCode> getAllContract4Print(String sid, int status) {
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        String selectQuery = "";

        if (status == -1)
        {
        	selectQuery = "SELECT * FROM " + "tbl_Result where (SID = '"+sid+"' and Price != 0) or (SID = '"+sid+"' and TestCode = '783Mea-M')";
        }
        else
        {
        	//selectQuery = "SELECT * FROM " + "tbl_Result where SID = '"+sid+"' and Price != 0  and trangthai = " + status;
        	selectQuery = "SELECT * FROM " + "tbl_Result where (SID = '"+sid+"' and Price != 0  and trangthai != 2) or (SID = '"+sid+"' and  TestCode = '783Mea-M' and trangthai != 2)";
        }
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	
	            	TestCode contact = new TestCode();
	            	if (sid.equals(""))
	            	{
	            		contact.setTestCode(cursor.getString(0));
	            	}
	            	else 
            		{
            			contact.setTestCode(cursor.getString(1)); 
            		}
	                
	            	if (sid.equals(""))
	            	{
		                if(cursor.getString(2) != null)
		                {
		                	contact.setTestName(cursor.getString(2));
		                }
		                else
		                {continue;}
	            	}
	            	else
	            	{
	            		if(cursor.getString(2) != null)
		                {
		                	contact.setTestName(cursor.getString(2));
		                }
		                else
		                {continue;}
	            	}
	            	
	            	///
	                
	            	if (sid.equals(""))
	            	{
		                if(cursor.getString(3) != null)
		                {
		                	contact.setCategory(cursor.getString(3));
		                }
		                else
		                {continue;}
	            	}
	            	else
	            	{
	            		if(cursor.getString(5) != null)
		                {
		                	contact.setPrice(cursor.getString(5));
		                }
		                else
		                {continue;}
	            	}
	            	
	            	////
	            	
	            	if (sid.equals(""))
	            	{
		                if(cursor.getString(4) != null)
		                {
		                	contact.setPrice(cursor.getString(4));
		                }
		                else
		                {continue;}
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
 
    public double getSumResult(String sid) {
    	
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        
        String selectQuery = "";

    	selectQuery = "SELECT * FROM " + "tbl_Result where SID = '"+sid+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        double total = 0;
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	
	            	TestCode contact = new TestCode();
	            	
	                if(cursor.getString(5) != null)
	                {
	                	total += Double.parseDouble(cursor.getString(5));
	                }
	                else
	                {continue;}
	            	
	                
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
        return total;
    }
    
    public int getSumMoney(String sid) {
    	
        
        String selectQuery = "";

    	selectQuery = "SELECT sum(Cost) as TOTAL FROM " + "tbl_Result where SID = '"+sid+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        int total = 0;
        
        try
        {

        	if(cursor.moveToFirst())
        	{
        	    total = cursor.getInt(0);
        	}
 
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
        return total;
    }
 
    public int checkTestCodeExists(String SID, String TestCode) {
        
    	int result = 0;
        String selectQuery = "";

    	selectQuery = "select count(*) from tbl_Result where SID = '"+SID+"' and TestCode = '"+TestCode+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
	        if (cursor.moveToFirst()) {
	            do {
		                if(cursor.getString(0) != null)
		                {result = Integer.parseInt(cursor.getString(0));}
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
        return result;
    }
   
    public int checkParentBehaviour(String sid) {
        
    	int result = 0;
        String selectQuery = "";

    	selectQuery = "select trangthai from tbl_Patient where SID = '"+sid+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
	        if (cursor.moveToFirst()) {
	            do {
		                if(cursor.getString(0) != null)
		                {result = Integer.parseInt(cursor.getString(0));}
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
        return result;
    }
    
    public void showSumPatient() {
    	
        String selectQuery = "";

    	selectQuery = "SELECT * FROM " + "tbl_Patient";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
	            	
            		contact.setSid((cursor.getString(0)));
            	
            		for (int i = 0; i < 20; i ++)
            		{
            			Log.d("INFO", "" + i + "-" + cursor.getString(i));
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
        }
    }
    
    public void showResultSum() {
    	
        String selectQuery = "";
        
        int total = 0;

    	selectQuery = "SELECT * FROM " + "tbl_Result";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	TestCode contact = new TestCode();
	            	
            		contact.setSid((cursor.getString(0)));
            	
            		for (int i = 0; i < 20; i ++)
            		{
            			Log.d("RESULT", "" + i + "-" + cursor.getString(i));
            			//total += Integer.parseInt(cursor.getString(6));
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
        	
        }
        
    }
    
    
    // Getting contacts Count
    
    public List<TestCode> getAllResult(String sid) {
    	
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        String selectQuery = "";

        if(sid.equals(""))
        {
        	selectQuery = "SELECT * FROM " + "tbl_Result";
        }
        else
        {
        	selectQuery = "SELECT * FROM " + "tbl_Result where SID = '"+sid+"'";
        }
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	TestCode contact = new TestCode();
	            	
	            		contact.setSid(cursor.getString(1));
	            	
		                if(cursor.getString(1) != null)
		                {
		                	contact.setTestCode(cursor.getString(1));
		                }
	            	
		                //
		                
		                
	            	
		                //
	            	
		                if(cursor.getString(2) != null)
		                {
		                	contact.setTestName(cursor.getString(2));
		                }
		                
		                if(cursor.getString(4) != null)
		                {
		                	contact.setCategory(cursor.getString(4));
		                }
		                
		                if(cursor.getString(5) != null)
		                {
		                	contact.setPrice(cursor.getString(5));
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

    public List<Patient> getAllPatient(String sid, int type) {
    	
    	
        List<Patient> contactList = new ArrayList<Patient>();
        
        String selectQuery = "";

        if (type == 0)
        {
	        if(sid.equals(""))
	        {
	        	selectQuery = "SELECT * FROM " + "tbl_Patient";
	        }
	        else
	        {
	        	selectQuery = "SELECT * FROM " + "tbl_Patient where SID = '"+sid+"'";
	        }
        }
        else
        {
        	selectQuery = "SELECT * FROM " + "tbl_Patient where DoctorID = '"+sid+"'";
        }
        
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
	            	
            		contact.setSid((cursor.getString(0)));
            	
            		if(cursor.getString(1) != null)
	                {
	                	contact.setPatientname(cursor.getString(1));
	                }
            		
            		if(cursor.getString(3) != null)
	                {contact.setPhone((cursor.getString(3)));}
            	
	                if(cursor.getString(4) != null)
	                {
	                	contact.setAge((Integer.parseInt(cursor.getString(4))));
	                }
	                
	                if(cursor.getString(5) != null)
	                {
	                	contact.setSex((cursor.getString(5)));
	                }
	            	
	                if(cursor.getString(6) != null)
	                {
	                	contact.setAddress((cursor.getString(6)));
	                }
	                
	                if(cursor.getString(7) != null)
	                {
	                	contact.setDoctorID((cursor.getString(7)));
	                }
	                
	                if(cursor.getString(8) != null)
	                {
	                	contact.setSumpertage(Double.parseDouble(cursor.getString(8)));
	                }
	                
	                if(cursor.getString(9) != null)
	                {
	                	contact.setDiagnostic((cursor.getString(9)));
	                }
	                
	                if(cursor.getString(11) != null)
	                {contact.setIntime(cursor.getString(11));}
	                
	                if(cursor.getString(12) != null)
	                {
	                	contact.setTiendilai(Integer.parseInt(cursor.getString(12)));
	                }
	                
	                if(cursor.getString(13) != null)
	                {
	                	contact.setEmail(cursor.getString(13));
	                }
	                
	                if(cursor.getString(14) != null)
	                {
	                	contact.setQuan(cursor.getString(14));
                	}
	                
	                if(cursor.getString(15) != null)
	                {
	                	contact.setCommend(cursor.getString(15));
                	}

	                if(cursor.getString(16) != null)
	                {
	                	contact.setPid(cursor.getString(16));
	                }
	                
	                if(cursor.getString(18) != null)
	                {
	                	contact.setTrangthai(Integer.parseInt(cursor.getString(18)));
	                }
	                
	                if(cursor.getString(19) != null)
	                {
	                	contact.setRandom(cursor.getString(19));
	                }
	                
	                if(cursor.getString(22) != null)
	                {
	                	contact.setGG(Integer.parseInt(cursor.getString(22)));
	                }
	                
	                if(cursor.getString(23) != null)
	                {contact.setTuvan(Integer.parseInt(cursor.getString(23)));} 
	                
	                contact.setPos(getPos(contact.getSid()));
	                
	                if(cursor.getString(26) != null)
	                {contact.setObjectID(cursor.getString(26));} 
	                
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
 
    public List<Patient> getAllSyncedPatient() {
    	
    	
        List<Patient> contactList = new ArrayList<Patient>();
        
        String selectQuery = "";
   
		
		/////
        
    	selectQuery = "SELECT * FROM " + "tbl_Patient order by Intime";
                	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
	            	
            		contact.setSid(cursor.getString(0));
            	
            		if(cursor.getString(1) != null)
	                {
	                	contact.setPatientname((cursor.getString(1)));
	                }

	                if(cursor.getString(4) != null)
	                {
	                	contact.setAge(Integer.parseInt(cursor.getString(4)));
                	}
	                
	                if(cursor.getString(5) != null)
	                {
	                	contact.setSex(cursor.getString(5));
                	}
	                
	                if(cursor.getString(6) != null)
	                {
	                	contact.setAddress((cursor.getString(6)));
	                }
	                else
	                {contact.setAddress("");}
	                
	                if(cursor.getString(18) != null)
	                {
	                	contact.setTrangthai(Integer.parseInt(cursor.getString(18)));
                	}
	                
	                if(cursor.getString(24) != null)
	                {
	                	contact.setReason(Integer.parseInt(cursor.getString(24)));
                	}
	                else
	                {
	                	contact.setReason(0);
	                }
	                
	                if(cursor.getString(25) != null)
	                {
	                	contact.setsReason(cursor.getString(25));
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
 
    
    public List<Patient> getAllSyncedPatient(String sid) {
    	
    	
        List<Patient> contactList = new ArrayList<Patient>();
        
        String selectQuery = "";
        
    	selectQuery = "SELECT * FROM tbl_Patient where SID = '"+sid+"' ";
                	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
	            	
            		contact.setSid(cursor.getString(0));
	                
	                if(cursor.getString(24) != null)
	                {contact.setReason(Integer.parseInt(cursor.getString(24)));}
	                else
	                {contact.setReason(0); }
	                
	                if(cursor.getString(25) != null)
	                {contact.setsReason(cursor.getString(25));}
	                
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
        }
        // return contact list
        return contactList;
    }
    
    ////////////////////////////////// PID async
    
    public void addResult(TestCode testCode)
    {
    	if(checkTestCodeExists(testCode.getSid(), testCode.getTestCode()) != 0)
		{
    		return;
		}
    	
    	SQLiteDatabase db = null;
    	try
    	{
 
        ContentValues values = new ContentValues();
        values.put("SID", testCode.getSid()); // Contact Name
        values.put("TestCode", testCode.getTestCode()); // Contact Name
        values.put("TestName", testCode.getTestName()); // Contact Name
        values.put("Price", testCode.getPrice());
        values.put("Category", testCode.getCategory());
        values.put("Cost", testCode.getPrice());
        values.put("trangthai", "2"); 


        // Inserting Row
        
        myDataBase.insert(TABLE_RESULT, null, values);
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
    
    public void addPatient(Patient testCode) {
        
    	SQLiteDatabase db = null;
    	try
    	{
    	//db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        Calendar now = Calendar.getInstance();
        
        values.put("PID", testCode.getPid());
        values.put("SID", testCode.getSid()); // Contact Name
        values.put("PatientName", testCode.getPatientname()); // Contact Name
        values.put("Seq", testCode.getSeq());
        values.put("Phone", testCode.getPhone());
        values.put("Age", testCode.getAge()); // Contact Name
        values.put("Sex", testCode.getSex()); // Contact Name
        values.put("Address", testCode.getAddress()); // Contact Name
        values.put("DoctorID", testCode.getDoctorID()); // Contact Name
        values.put("Sumpertage", testCode.getSumpertage()); // Contact Name
        values.put("Diagnostic", testCode.getDiagnostic() ); // Contact Name
        values.put("DateIN", (now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE)); // Contact Name
        values.put("SumMoney", "0"); // Contact Name
        values.put("SumTransport", testCode.getTiendilai());
        values.put("Email", testCode.getEmail());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        values.put("Intime",  dateFormat.format(now.getTime())); 
        values.put("District", testCode.getQuan()); // Contact Name
        values.put("EmailResult", "0"); // Contact Name
        values.put("trangthai", "0"); // Contact Name
        values.put("random", testCode.getRandom()); // Contact Name
        values.put("SubComment", testCode.getCommend()); // Contact Name
        values.put("GG", testCode.getGG()); // Contact Name
        values.put("TV", testCode.getTuvan()); // Contact Name
        values.put("ObjectID", testCode.getObjectID()); // Contact Name
        
        Log.d("TUVAN", "TUVAN:" + testCode.getTuvan());

        // Inserting Row
        
        myDataBase.insert(TABLE_PATIENT, null, values);
    	}
    	catch(Exception ex)
    	{}
        finally {
            if (db != null && db.isOpen()) {
              //  db.close();
                
            }
        }
    }
    
    public int updatePatient(Patient patient) 
    {
        ContentValues values = new ContentValues();
        values.put("Phone", patient.getPhone());
        values.put("Address", patient.getAddress());
        values.put("PatientName", patient.getPatientname());
        values.put("Age", patient.getAge());
        values.put("Diagnostic", patient.getDiagnostic());
        values.put("DoctorID", patient.getDoctorID());
        values.put("Sex", patient.getSex());
        values.put("Sumpertage", patient.getSumpertage());
        //values.put("trangthai", patient.isTiendilai());
        values.put("PID", patient.getPid());

        return myDataBase.update(TABLE_PATIENT, values, SID + " = ?",
                new String[] { patient.getSid() });
        
    }
   
    ///////////////////
    
    public int updateReason(String sid, int reason, String strReason) 
    {
        ContentValues values = new ContentValues();
        //values.put("SID", sid);
        values.put("ReturnResult", reason);
        values.put("ReturnDescription", strReason);

        return myDataBase.update(TABLE_PATIENT, values, SID + " = ?",
                new String[] { sid });
        
    }
    
    public int updateReason_HoTro(String sid, int reason, String strReason) 
    {
        ContentValues values = new ContentValues();
        values.put("ReturnResult", reason);
        values.put("ReturnDescription", strReason);

        return myDataBase.update("tbl_Return", values, SID + " = ?",
                new String[] { sid });
        
    }
    
    public void addReason_HoTro(Patient patient, int reason, String strReason) {
        
    	SQLiteDatabase db = null;
    	try
    	{
	        ContentValues values = new ContentValues();
	        values.put("SID", patient.getSid()); // 
	        values.put("PatientName", patient.getPatientname()); //
	        values.put("Phone", patient.getPhone()); //
	        values.put("Address", patient.getAddress()); //
	        values.put("ReturnResult", reason); //
	        values.put("ReturnDescription", strReason); //
        
        myDataBase.insert("tbl_Return", null, values);
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
    
    public List<Patient> getAllReason_HoTro() {
    	
    	
        List<Patient> contactList = new ArrayList<Patient>();
        
        String selectQuery = "";
        
    	selectQuery = "SELECT * FROM " + "tbl_Return where ReturnResult != - 1";
                	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
	            	
            		contact.setSid(cursor.getString(0));
            	
            		if(cursor.getString(1) != null)
	                {
	                	contact.setPatientname((cursor.getString(1)));
	                }

	                if(cursor.getString(2) != null)
	                {
	                	contact.setPhone(cursor.getString(2));
                	}
	                
	                if(cursor.getString(3) != null)
	                {
	                	contact.setAddress(cursor.getString(3));
                	}
	                
	                if(cursor.getString(4) != null)
	                {
	                	contact.setReason(Integer.parseInt(cursor.getString(4)));
	                }
	                
	                if(cursor.getString(5) != null)
	                {
	                	contact.setsReason(cursor.getString(5));
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

	////////////////////////////////////////////
    
    
    public int updateBLStatus(String SID) 
    {
        ContentValues values = new ContentValues();
        values.put("Invoice", SID);

        return myDataBase.update(TABLE_PATIENT, values, "SID = ?",
                new String[] { SID });
    }
    
    public List<Patient> getAllPatientForSync() {
    	
    	
        List<Patient> contactList = new ArrayList<Patient>();
        
        //////
        
        Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
		String formattedDate = df.format(c.getTime());
		
		//////
        
        String selectQuery = "";

    	//selectQuery = "SELECT * FROM " + "tbl_Patient where trangthai = 0";
        
        selectQuery = "SELECT * FROM " + "tbl_Patient where trangthai = 0 ";

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        ///////////
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
            		contact.setSid((cursor.getString(0)));
            		
            		
            		if(contact.getSid().split("-")[0].equals(formattedDate) == false)
            		{
            			Log.d("_FAIL_" + formattedDate + "_" + contact.getSid().split("-")[0], "" + contact.getSid() + "_");
            			// delete un-needed one(s)
            			//deleteOneWhole(contact.getSid());
            			//
            			continue;
            		}
            		else
            		{
            			Log.d("_SUCCESS_" + formattedDate + "_" + contact.getSid().split("-")[0], "" + contact.getSid() + "_");
            		}
            		
            	
            		///patient
	                if(cursor.getString(1) != null)
	                {contact.setPatientname(cursor.getString(1));}
	                
	              //phone
	                if(cursor.getString(3) != null)
	                {contact.setPhone((cursor.getString(3)));}
	                
	              //age
	                if(cursor.getString(4) != null)
	                {contact.setAge((Integer.parseInt(cursor.getString(4)))); }
	                
	                if(cursor.getString(5) != null)
	                {contact.setSex((cursor.getString(5)));}
	                
	              //adress
	                if(cursor.getString(6) != null)
	                {contact.setAddress((cursor.getString(6)));}
	                
	              //doctor
	                if(cursor.getString(7) != null)
	                {contact.setDoctorID((cursor.getString(7)));}
	                
	              ///sum
	                if(cursor.getString(8) != null)
	                {contact.setSumpertage(Double.parseDouble(cursor.getString(8)));}
	                
	              //diag
	                if(cursor.getString(9) != null)
	                {contact.setDiagnostic((cursor.getString(9)));}
	                
	                if(cursor.getString(11) != null)
	                {contact.setIntime(cursor.getString(11));}
	                
	              ///sum
	                if(cursor.getString(12) != null)
	                {
	                	contact.setTiendilai(Integer.parseInt(cursor.getString(12)));
	                }
	                if(cursor.getString(13) != null)
	                {
	                	contact.setEmail(cursor.getString(13));
	                }
	                
	              //quan
	                if(cursor.getString(14) != null)
	                {contact.setQuan(cursor.getString(14));}
	                
	                if(cursor.getString(15) != null)
	                {contact.setCommend(cursor.getString(15));}
	                
	                //pid
	                if(cursor.getString(16) != null)
	                {contact.setPid(cursor.getString(16));}
	                
	                if(cursor.getString(19) != null)
	                {contact.setRandom(cursor.getString(19));}  
	                
	                if(cursor.getString(23) != null)
	                {contact.setTuvan(Integer.parseInt(cursor.getString(23)));} 
	                
	                contact.setPos(getPos(contact.getSid()));
	                
	                if(cursor.getString(26) != null)
	                {contact.setObjectID(cursor.getString(26));} 
	                
	                /*//userI
	                if(cursor.getString(16) != null)
	                {contact.setUserI(cursor.getString(16));}*/
	                
	                // Adding contact to list
	                contactList.add(contact);
	            } while (cursor.moveToNext());
	        }
	        
	        cursor.close();
 
        }
        catch (SQLiteException e) 
        {
			// TODO: handle exception
        	e.printStackTrace();
		}
        finally
        {
        	
        	//if(db != null && db.isOpen())
             //   db.close();
        }
        // return contact list
        return contactList;
    }
    
    public String getAllResultForSyncFinal(String SID) {
    	
        String testCodes = "";
        String selectQuery = "";

    	selectQuery = "select distinct TestCode from tbl_Result where trangthai = 0 and SID = '"+SID+"'";
       
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            		//testcode
		                if(cursor.getString(0) != null)
		                {
		                	testCodes += cursor.getString(0) + "&";
	                	}
		                
	                // Adding contact to list
	            } while (cursor.moveToNext());
	        }
	        
	        cursor.close();
 
        }
        catch (SQLiteException e) 
        {
        	Log.d("ERRORRRR HERRRRRRRRRRREEEEEEE", "ERRORRRR HERRRRRRRRRRREEEEE" );
		}
        finally
        {
        	
        	//if(db != null && db.isOpen())
             //   db.close();
        }
        // return contact list
        if(testCodes.equals("") == false)
        {
        	return testCodes.substring(0, testCodes.length() - 1);
        }
        else
        {
        	return "";
        }
    }
    
    public void deleteAllForSync() {
	       // SQLiteDatabase db = this.getWritableDatabase();

	        myDataBase.delete(TABLE_RESULT, SID + " != ?",
	                new String[] { "1" });
	        
	        myDataBase.delete(TABLE_PATIENT, SID + " != ?",
	                new String[] { "1" });
	      
	    }
    
    public void deleteOneWhole(String sid) {
	       // SQLiteDatabase db = this.getWritableDatabase();

	        myDataBase.delete(TABLE_RESULT, SID + " = ?",
	                new String[] { sid });
	        
	        myDataBase.delete(TABLE_PATIENT, SID + " = ?",
	                new String[] { sid });
	      
	    }
    
    public int updatePatientForSync(String sid, int status) 
    {
        ContentValues values = new ContentValues();
        
        if (status == 0)
        {
        	values.put("trangthai", 0);
        }
        if (status == 1)
        {
        	values.put("trangthai", 1);
        }
        if (status == 9)
        {
        	values.put("trangthai", 9);
        }
        //1 DA XONG
        //0 OK
        //
 
        // updating row
        return myDataBase.update(TABLE_PATIENT, values, SID + " = ?",
                new String[] { sid });
        
    }
    
    public int updateResultForSync(String sid, String testCode, int status) 
    {
        ContentValues values = new ContentValues();
        values.put("trangthai", status);
        //1 DAXONG
        //0 CHUAN BI
        //7 PATIENT OK
 
        // updating row
        return myDataBase.update(TABLE_RESULT, values, " SID = ? and TestCode = ?",
                new String[] { sid, testCode });
    }
    
    public int updateResultStatusForSyncFinal(String sid, String testCode) 
    {
        ContentValues values = new ContentValues();
        values.put("trangthai", 5);
        // 0 OK CHO UPDATE
        // 1 DA XONG
 
        // updating row
        return myDataBase.update(TABLE_RESULT, values, " SID = ? and TestCode = ?",
                new String[] { sid , testCode });
    }
    
    public int updateResultStatusForSync(String sid, int status) 
    {
        ContentValues values = new ContentValues();
        values.put("trangthai", status);
        // 0 OK CHO UPDATE
        // 1 DA XONG
 
        // updating row
        return myDataBase.update(TABLE_RESULT, values, " SID = ? and trangthai = 2",
                new String[] { sid });
    }
    
    public int updateResultReSync(String sid, int status) 
    {
        ContentValues values = new ContentValues();
        values.put("trangthai", status);
        // 0 OK CHO UPDATE
        // 1 DA XONG
 
        // updating row
        return myDataBase.update(TABLE_RESULT, values, " SID = ?",
                new String[] { sid });
    }
    
    public int updatePatientStatusForSync(String sid) 
    {
        ContentValues values = new ContentValues();
        
        values.put("trangthai", 0);
 
        // updating row
        return myDataBase.update(TABLE_PATIENT, values, " SID = ?",
                new String[] { sid });
    }
    
    //////////////////////////////////// PID progress
    
	/////////////////////////////////// Dumb progress
    
    public List<PersonalInfo> getAllDumb(int type) {
    	
    	
        List<PersonalInfo> contactList = new ArrayList<PersonalInfo>();
        String selectQuery = "";

        if (type == 0)
        {
        	selectQuery = "select * from tbl_Dumb where tinhtrang != 3";
        }
        else
        {
        	selectQuery = "select * from tbl_Dumb where tinhtrang = 3";
        }
       
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        PersonalInfo contact;
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	 contact = new PersonalInfo();
	            	
	            		contact.SetName(cursor.getString(1));
	            	
	            		//unit
		                if(cursor.getString(2) != null)
		                {contact.SetMobile(cursor.getString(2));}
	            		
	            		//phone
		                if(cursor.getString(3) != null)
		                {contact.setPhone(cursor.getString(3));}
		                
		              //phone
		                if(cursor.getString(4) != null)
		                {contact.setMaLH(cursor.getString(4));}
		                
		                if(cursor.getString(5) != null)
		                {contact.setTinhtrang(cursor.getString(5));}
		                
		                /////
		                
		                if(cursor.getString(6) != null)
		                {contact.setGiohen1(cursor.getString(6));}
		                
		                if(cursor.getString(7) != null)
		                {contact.setGiohen2(cursor.getString(7));}
		                
		                if(cursor.getString(8) != null)
		                {contact.setGioitinh(cursor.getString(8));}
		                
		                if(cursor.getString(9) != null)
		                {contact.setNamsinh(cursor.getString(9));}
	            	
		                if(cursor.getString(13) != null)
		                {contact.setYeucauXN(cursor.getString(13));}
		                
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
    
    public int checkExist(String maLH) {
        
    	int result = 0;
        String selectQuery = "";

    	selectQuery = "select count(*) from tbl_Dumb where MaLH = '"+maLH+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
	        if (cursor.moveToFirst()) {
	            do {
		                if(cursor.getString(0) != null)
		                {result = Integer.parseInt(cursor.getString(0));}
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
        return result;
    }
    
    public int getNonReadDumb() {
        
    	int result = 0;
        String selectQuery = "";

    	selectQuery = "select count(*) from tbl_Dumb where tinhtrang = 0";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
        	
	        if (cursor.moveToFirst()) {
	            do {
		                if(cursor.getString(0) != null)
		                {result = Integer.parseInt(cursor.getString(0));}
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
        return result;
    }
    
    public void deleteAllDumb(String name) {
	       // SQLiteDatabase db = this.getWritableDatabase();
		
	        myDataBase.delete("tbl_Dumb", "Name" + " != ?",
	                new String[] { "-1" });
	      
	    }
    
    public void deleteOneDumb(String name) {
	       // SQLiteDatabase db = this.getWritableDatabase();
		
	        myDataBase.delete("tbl_Dumb", "Phone" + " = ?",
	                new String[] { name });
	      
	    }
    
    public int addDumb(PersonalInfo testCode) {
        
    	int result = 0;
    	
    	if(checkExist(testCode.getMaLH()) != 0)
		{
    		result = 0;
    		return result;
		}
    	
    	SQLiteDatabase db = null;
    	try
    	{
    		
        ContentValues values = new ContentValues();
        values.put("Name", testCode.GetName()); // Contact Name
        values.put("Address", testCode.GetMobile()); // Contact Name
        values.put("Phone", testCode.getPhone()); // Contact Name
        values.put("MaLH", testCode.getMaLH()); // Contact Name
        values.put("tinhtrang", "0"); // Contact Name
        values.put("giohen1", testCode.getGiohen1()); // Contact Name
        values.put("giohen2", testCode.getGiohen2()); // Contact Name
        values.put("gioitinh", testCode.getGioitinh()); // Contact Name
        values.put("namsinh", testCode.getNamsinh()); // Contact Name
        values.put("ttLayMau", 0); // Contact Name
        values.put("ttDocTin", 0); // Contact Name
        //values.put("yeucauXN", testCode.getYeucauXN()); // Contact Name
        

        // Inserting Row
       // db = this.getWritableDatabase();
        	myDataBase.insert("tbl_Dumb", null, values);
        	result = 1;
    	}
    	catch(Exception ex)
    	{
    		
    	}
        finally {
           // if (db != null && db.isOpen()) {
              //  db.close();
                
         //   }
        }
    	return result;
    }
    
    public int addDumbClone(String maLH, String SID) {
        
    	int result = 0;
    	try
    	{
	        ContentValues values = new ContentValues();
	        values.put("Name", SID); // Contact Name
	        values.put("MaLH", maLH); // Contact Name
	        values.put("tinhtrang", "3");
	        values.put("ttDocTin", 0); // Contact Name
	        values.put("SID", SID); // Contact Name
	        
	        
        	myDataBase.insert("tbl_Dumb", null, values);
        	result = 1;
    	}
    	catch(Exception ex)
    	{
    		
    	}
        finally {
           // if (db != null && db.isOpen()) {
           //  db.close();
           //   }
        }
    	return result;
    }
    
    public int updateDumbAss(String malh, int type) 
    {
        ContentValues values = new ContentValues();
        
        if (type == 2)
        {
        	values.put("ttLayMau", 1);
        }
        else if(type == 1)
        {
        	values.put("ttDocTin", 1);
        }
        else if(type == 3)
        {
        	values.put("ttDocTin", 1);
        }
 
        // updating row
        return myDataBase.update("tbl_Dumb", values, " maLH = ?",
                new String[] { malh });
    }
    
    public int updateMaCanBo(String maCanBo) 
    {
        ContentValues values = new ContentValues();
        values.put("Category_Name", maCanBo);
 
        // updating row
        return myDataBase.update("tbl_Category", values, " CategoryID = ?",
                new String[] { "1" });
    }
    
    public String getCurrentCanBo() 
	{
		String selectQuery = "";
		String tenCanBo = "";
		
		selectQuery = "select * from tbl_Category where CategoryID = 1";
		
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	
		try
		{
			if (cursor.moveToFirst()) 
			{
				do 
				{
					if(cursor.getString(1) != null)
					{
						tenCanBo = cursor.getString(1);
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
    
    public List<PersonalInfo> getAllDumbForUpdate() {
    	
    	
        List<PersonalInfo> contactList = new ArrayList<PersonalInfo>();
        String selectQuery = "";

    	selectQuery = "select maLH, tinhtrang, SID from tbl_Dumb where (tinhtrang = 1 and TTDocTin = 0) or (tinhtrang = 2 and TTLayMau = 0) or (tinhtrang = 3 and TTDocTin = 0)";
       
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
        	PersonalInfo contact;
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	
	            	contact = new PersonalInfo(); 
	            	
	            	contact.setMaLH(cursor.getString(0));
	                
	                if(cursor.getString(1) != null)
	                {
	                	contact.setTinhtrang(cursor.getString(1));
	                }
	                //
	                if(cursor.getString(2) != null)
	                {
	                	contact.setSID(cursor.getString(2));
	                }
	                
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

    public int updateDumb(String malh, String tinhtrang) 
    {
        ContentValues values = new ContentValues();
        values.put("tinhtrang", tinhtrang);
 
        // updating row
        return myDataBase.update("tbl_Dumb", values, " maLH = ? and tinhtrang != 3",
                new String[] { malh });
    }
    
	/////////////////////////////////////////// DELETE FRAG
	
    public void clearFrag() {
    	
        Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
		String formattedDate = df.format(c.getTime());
		
		//////
        
        String selectQuery = "";
        
        selectQuery = "SELECT * FROM " + "tbl_Patient";

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
   
        ///////////
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	Patient contact = new Patient();
	            	
            		contact.setSid((cursor.getString(0)));
            		
            		if(contact.getSid().split("-")[0].equals(formattedDate) == false)
            		{
            			Log.d("_DELETE FAIL_" + formattedDate + "_" + contact.getSid().split("-")[0], "" + contact.getSid() + "_");
            			// delete un-needed one(s)
        				deleteOneWhole(contact.getSid());
            		}
            		
	            } while (cursor.moveToNext());
	        }
	        
	        cursor.close();
 
        }
        catch (SQLiteException e) 
        {
			// TODO: handle exception
        	e.printStackTrace();
		}
        finally
        {
        	
        	//if(db != null && db.isOpen())
             //   db.close();
        }
    }
    
	public void deleteFrag(String SID) {
    	
        myDataBase.delete(TABLE_RESULT, "SID = ?",
                new String[] { SID });	      
    }

	public List<String> getAllFraq() {
	    	
	    	
	        List<String> contactList = new ArrayList<String>();
	        String selectQuery = "";

	    	selectQuery = "select distinct SID from tbl_Result";
	       
	        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	        
	        try
	        {
		        // looping through all rows and adding to list
		        if (cursor.moveToFirst()) {
		            do {
		                // Adding contact to list
		                contactList.add(cursor.getString(0));
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

	public int checkValidSID(String SID) {
	        
	    	int result = 0;
	        String selectQuery = "";

	    	selectQuery = "select count(*) from tbl_Patient where SID = '"+SID+"'";
	        	
	        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	        
	        try
	        {
		        // looping through all rows and adding to list
	        	
		        if (cursor.moveToFirst()) {
		            do {
			                if(cursor.getString(0) != null)
			                {result = Integer.parseInt(cursor.getString(0));}
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
	        return result;
	    }
		
	///////////////////////////////////////////SYNCE STATUS
	
	public List<TestCode> getAllResultSync(String sid) {
    	
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        String selectQuery = "";
       
    	selectQuery = "SELECT TestCode, SID, trangthai FROM " + "tbl_Result where trangthai != 2 and SID = '"+sid+"'";
        
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	TestCode contact = new TestCode();
	            	
	            		contact.setSid(cursor.getString(1));
	            	
		                if(cursor.getString(0) != null)
		                {
		                	contact.setTestCode(cursor.getString(0));
		                }
	            	
		                //
		                
		                if(cursor.getString(2) != null)
		                {
		                	contact.setTrangthai(Integer.parseInt(cursor.getString(2)));
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
	
	public List<TestCode> getAllResultSync() {
    	
    	
        List<TestCode> contactList = new ArrayList<TestCode>();
        String selectQuery = "";
       
    	selectQuery = "SELECT SID, TestCode, trangthai FROM " + "tbl_Result";
        
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	TestCode contact = new TestCode();
	            	
	            		contact.setSid(cursor.getString(0));
	            	
		                if(cursor.getString(1) != null)
		                {
		                	contact.setTestCode(cursor.getString(1));
		                }
	            	
		                //
		                
		                if(cursor.getString(2) != null)
		                {
		                	contact.setTrangthai(Integer.parseInt(cursor.getString(2)));
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

    ////////////////////////////////////////// HANDLE ERROR
	
	public String[] getError() 
	{
		String[] result = new String[2];
		String selectQuery = "";
		
		selectQuery = "select * from " +TABLE_SELECTED+ " limit 1";
		
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	
		try
		{
			if (cursor.moveToFirst()) 
			{
				do 
				{
			
					if(cursor.getString(0) != null)
					{
						result[0] = cursor.getString(0);
					}
					
					if(cursor.getString(1) != null)
					{
						result[1] = cursor.getString(0);
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
		return result;
	}

	public void deleteError() {
	       // SQLiteDatabase db = this.getWritableDatabase();

	        myDataBase.delete(TABLE_SELECTED, "TestCode" + " != ?",
	                new String[] { "1" });
	    }

	//////////////////////////////////////////HANDLE POS
	
	public void addPos(String SID, double pos) {
        
    	SQLiteDatabase db = null;
    	try
    	{
	        ContentValues values = new ContentValues();
	        values.put("SID", SID); // 
	        values.put("Pos", pos); // 
        //  Inserting Row
        
        myDataBase.insert("tbl_Pos", null, values);
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
	
	public double getPos(String sid) {
    	
        
        String selectQuery = "";

    	selectQuery = "SELECT Pos FROM tbl_Pos where SID = '"+sid+"'";
        	
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        int total = 0;
        
        try
        {

        	if(cursor.moveToFirst())
        	{
        	    total = cursor.getInt(0);
        	}
 
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
        return total;
    }

	//////////////////////////////////////////HANDLE POS
	
	public List<NhanMau> getAllNhanMau(String sid) {
    	
    	
        List<NhanMau> contactList = new ArrayList<NhanMau>();
        String selectQuery = "";

        
    	selectQuery = "select * from tbl_NhanMau where SID = '"+sid+"'";
        
       
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        
        NhanMau contact;
        
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	 contact = new NhanMau();
	            	
	            	 	contact.setSid(cursor.getString(0));
	            	 
	            		contact.setLoai(Integer.parseInt(cursor.getString(1)));
	            	
	            		//unit
		                if(cursor.getString(2) != null)
		                {contact.setSoluong(Integer.parseInt(cursor.getString(2)));}
	            		
	            		//phone
		                if(cursor.getString(4) != null)
		                {contact.setGhichu(Integer.parseInt(cursor.getString(4)));}
		                
		                /////
		                
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

	public void addNhanMau(NhanMau testCode) {
        
    	SQLiteDatabase db = null;
    	try
    	{
    		
	        ContentValues values = new ContentValues();
	        values.put("SID", testCode.getSid()); // Contact Name
	        values.put("Loai", testCode.getLoai()); // Contact Name
	        values.put("SoLuong", testCode.getSoluong()); // Contact Name
	        values.put("GhiChu", testCode.getGhichu()); // Contact Name
	        values.put("Tinhtrang", 0); // Contact Name
	        
	    	myDataBase.insert("tbl_NhanMau", null, values);
    	}
    	catch(Exception ex)
    	{
    		
    	}
        finally {
           // if (db != null && db.isOpen()) {
              //  db.close();
                
         //   }
        }
    }
	
	 public int updateNhanMau(NhanMau patient) 
     {
        ContentValues values = new ContentValues();
        //values.put("Loai", patient.getLoai());
        values.put("SoLuong", patient.getSoluong());
        values.put("GhiChu", patient.getGhichu());
        values.put("Tinhtrang", 0);

        return myDataBase.update("tbl_NhanMau", values, SID + " = ? and Loai = ?",
                new String[] { patient.getSid(), "" + patient.getLoai() });
         
     }
	 
	 public int doneNhanMau(String SID) 
     {
        ContentValues values = new ContentValues();
        values.put("Tinhtrang", 1);
        return myDataBase.update("tbl_NhanMau", values,  "SID = ?",
                new String[] { SID });
         
     }
	 
	 public void deleteNhanMau(String sid, String loai) {
	       // SQLiteDatabase db = this.getWritableDatabase();
		
	        myDataBase.delete("tbl_NhanMau", SID + " = ? and Loai = ?",
	                new String[] { sid, loai });
	      
	    }

	 
	 //////////////////////////////// DIAG
	 
	 public String[] getAllDiag() {
	    	
	        String[] diagArr = new String[100];
	        String selectQuery = "";

	    	selectQuery = "select DiagID from tbl_Diag";
	        
	        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	        
	        try
	        {
		        int i = 0;
	        	
		        if (cursor.moveToFirst()) {
		            do {
		            	
	            		diagArr[i] = cursor.getString(0);
	            		i++;
	            		
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
	        return diagArr;
	    }

	 public String getOneDiag(String id) 
		{
			String selectQuery = "";
			String tenCanBo = "";
			
			selectQuery = "select * from tbl_Diag where DiagID = '"+id+"'";
			
			Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		
			try
			{
				if (cursor.moveToFirst()) 
				{
					do 
					{
						if(cursor.getString(1) != null)
						{
							tenCanBo = cursor.getString(1);
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

}
