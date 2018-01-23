package medlatec.listview.testcode;

import medlatec.listview.Front_Activity;
import medlatec.listview.MainActivity;
import medlatec.listview.PatientInformation;
import medlatec.listview.R;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.R.menu;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerTestCode;
import medlatec.listview.object.TestCode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class TestCode_ViKhuan extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vi_khuan);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_code__vi_khuan, menu);
		return true;
	}
	
	public void onClick (View view)
	{
		switch(view.getId()) {
        case R.id.tv11:
        	
        	tvAll = (TextView) findViewById(R.id.tv11);
        	chooseTestCode(tvAll, new String[] {"629LAU"});
        	break;
        	
        case R.id.tv12: 
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "630LAU" });
        	break;
        
        	
        	
        	/// P 2
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] { "224Ch" });
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "226Chlam IgG" });
        	break;
        	
        case R.id.tv23:
        	tvAll = (TextView) findViewById(R.id.tv23);
        	chooseTestCode(tvAll, new String[] { "227Chlam IgM" });
        	break;
        	
        case R.id.tv24:
        	tvAll = (TextView) findViewById(R.id.tv24);
        	chooseTestCode(tvAll, new String[] { "798PCR" });
        	break;
        
/// P 3
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] { "208RPR" });
        	break;
        	
        case R.id.tv32:
        	tvAll = (TextView) findViewById(R.id.tv32);
        	chooseTestCode(tvAll, new String[] { "209Syphi" });
        	break;
        	
        case R.id.tv33:
        	tvAll = (TextView) findViewById(R.id.tv33);
        	chooseTestCode(tvAll, new String[] { "209TPHA" });
        	break;
        	
        case R.id.tv34:
        	tvAll = (TextView) findViewById(R.id.tv34);
        	chooseTestCode(tvAll, new String[] { "760TPHA" });
        	break;
        	
/// P 5
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] { "600DichAD", "602NAMS","604CHO","606TAP","608TBB" });
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] {"601DichND","602Trich", "603SBC","603SHC","603SN","606TAP","608TBB" });
        	break;
        	
        case R.id.tv53:
        	tvAll = (TextView) findViewById(R.id.tv53);
        	chooseTestCode(tvAll, new String[] { "630SOITUOI" });
        	break;
        	
        
/// P 6
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] { "764UTSL" });
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "1720" });
        	break;
        	
        	
        	
/// P 7
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] { "610RTA" });
        	break;
        	
        case R.id.tv72:
        	tvAll = (TextView) findViewById(R.id.tv72);
        	chooseTestCode(tvAll, new String[] { "611KST" });
        	break;
        	
        case R.id.tv73:
        	tvAll = (TextView) findViewById(R.id.tv73);
        	chooseTestCode(tvAll, new String[] { "617SOIP", "618NAMTP","619CANTP","619HATMO","620HBCF","621VKCDR","622vkc","622vkcc","622vkl" });
        	break;
        	
        	/// 8
        	
        case R.id.tv81:
        	tvAll = (TextView) findViewById(R.id.tv81);
        	chooseTestCode(tvAll, new String[] { "065KST" });
        	break;
        	
        case R.id.tv82:
        	tvAll = (TextView) findViewById(R.id.tv82);
        	chooseTestCode(tvAll, new String[] { "067KST" });
        	break;
        	
        	/// 9
        	
        case R.id.tv91:
        	tvAll = (TextView) findViewById(R.id.tv91);
        	chooseTestCode(tvAll, new String[] { "602BK" });
        	break;
        	
      /*  case R.id.tv92:
        	tvAll = (TextView) findViewById(R.id.tv92);
        	chooseTestCode(tvAll, new String[] { "067KST" });
        	break;*/
        	
        case R.id.tv93:
        	tvAll = (TextView) findViewById(R.id.tv93);
        	chooseTestCode(tvAll, new String[] { "621TBtest" });
        	break;
        	
        	
        	//10
        	
        case R.id.tv101:
        	tvAll = (TextView) findViewById(R.id.tv101);
        	chooseTestCode(tvAll, new String[] { "622PCRL " });
        	break;
        	
	//11
        	
        case R.id.tv111:
        	tvAll = (TextView) findViewById(R.id.tv111);
        	chooseTestCode(tvAll, new String[] { "790HpAntigen" });
        	break;
        	

//12
        	
        case R.id.tv121:
        	tvAll = (TextView) findViewById(R.id.tv121);
        	chooseTestCode(tvAll, new String[] { "623H.pylori IgM" });
        	break;
        	
        case R.id.tv122:
        	tvAll = (TextView) findViewById(R.id.tv122);
        	chooseTestCode(tvAll, new String[] { "623H.pylori DL" });
        	break;
        	
        case R.id.tv123:
        	tvAll = (TextView) findViewById(R.id.tv123);
        	chooseTestCode(tvAll, new String[] { "623H.pylori DT" });
        	break;
        	
//13
        	
        case R.id.tv131:
        	tvAll = (TextView) findViewById(R.id.tv131);
        	chooseTestCode(tvAll, new String[] { "609TGC" });
        	break;
        	
        case R.id.tv132:
        	tvAll = (TextView) findViewById(R.id.tv132);
        	chooseTestCode(tvAll, new String[] { "609UTGC" });
        	break;
        	
        case R.id.tv133:
        	tvAll = (TextView) findViewById(R.id.tv133);
        	chooseTestCode(tvAll, new String[] { "792Giun" });
        	break;
        	
//14
        	
        case R.id.tv141:
        	tvAll = (TextView) findViewById(R.id.tv141);
        	chooseTestCode(tvAll, new String[] { "793GiunL" });
        	break;
        	
        case R.id.tv142:
        	tvAll = (TextView) findViewById(R.id.tv142);
        	chooseTestCode(tvAll, new String[] { "794GiunDG" });
        	break;
        	
        case R.id.tv143:
        	tvAll = (TextView) findViewById(R.id.tv143);
        	chooseTestCode(tvAll, new String[] { "795Giun" });
        	break;
        	
//15
        	
        case R.id.tv151:
        	tvAll = (TextView) findViewById(R.id.tv151);
        	chooseTestCode(tvAll, new String[] { "1720" });
        	break;
        	
        case R.id.tv152:
        	tvAll = (TextView) findViewById(R.id.tv152);
        	chooseTestCode(tvAll, new String[] { "761SGL" });
        	break;
        	
        case R.id.tv153:
        	tvAll = (TextView) findViewById(R.id.tv153);
        	chooseTestCode(tvAll, new String[] { "762SLGB" });
        	break;       	
        	
//16
        	
        case R.id.tv161:
        	tvAll = (TextView) findViewById(R.id.tv161);
        	chooseTestCode(tvAll, new String[] { "764UTSL" });
        	break;
        	
        case R.id.tv162:
        	tvAll = (TextView) findViewById(R.id.tv162);
        	chooseTestCode(tvAll, new String[] { "768SM" });
        	break;

 //17
        case R.id.tv171:
        	tvAll = (TextView) findViewById(R.id.tv171);
        	chooseTestCode(tvAll, new String[] { "611Amip" });
        	break;
        	
        case R.id.tv172:
        	tvAll = (TextView) findViewById(R.id.tv172);
        	chooseTestCode(tvAll, new String[] { "613pH Phan" });
        	break;
        	
        default:
        	break;
		}
		

	}

	
	
	public void chooseTestCode(TextView tvChoose, String[] testCodes)
	{
		String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
		testCodeSader = testCodes;
		
		if (tvAll.getCurrentTextColor() == Color.RED)
    	{
			new SomeTask().execute(new String[] {sid, "red"});
    	}
		else
		{
			new SomeTask().execute(new String[] {sid, "blue"});
		}
		
		
	}
	
	private class SomeTask extends AsyncTask<String, Void, Integer> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(TestCode_ViKhuan.this);

	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

	    @Override
	    protected Integer doInBackground(String... params) 
	    {
	        TestCode item = new TestCode();
	        int resultType = 0;
	        
			if (params[1].equals("red"))
			{
				resultType = 1;
				for(int i = 0; i < testCodeSader.length; i++)
				{
					myDbHelperMain.deleteResult(testCodeSader[i], params[0]);
				}
	    	}
			else
	    	{
				resultType = 2;
	    		for(int i = 0; i < testCodeSader.length; i++)
				{
	    			item = myDbHelper.selectOneTestCode(testCodeSader[i]);
	    			if (PatientInformation.ObjectID.equals("") == false)
        			{
        				item.setPrice("" + (Double.parseDouble(item.getPrice()) * 0.95));
        			}
	    			item.setSid(params[0]);
					myDbHelperMain.addResult(item);
				}
	    	}
	        return resultType;
	    }

	    @Override
	    protected void onPostExecute(Integer result)
	    {
	        if(result==1)
	        {
	        	tvAll.setTextColor(Color.BLACK);
	        }
	        else
	        {
	        	tvAll.setTextColor(Color.RED);
	        }
	        //
	        String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
	        String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: " + myDbHelperMain.getSumResult(sid);
	        tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelperMain.getNonReadDumb();
	        MainActivity.txtSum.setText(tits);
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}



}
