package com.example.duedatemanager;

import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddItemActivity extends ActionBarActivity {
	EditText itemName;
	EditText itemDesc;
	EditText itemDate;
	private Calendar cal;
	 private int day;
	 private int month;
	 private int year;
	 long newRowId;
	 String alertMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		 itemName= (EditText)findViewById(R.id.editItemName);
    	 itemDesc= (EditText)findViewById(R.id.editDesc);
    	 itemDate= (EditText)findViewById(R.id.editDueDate);
    	 cal = Calendar.getInstance();
         day = cal.get(Calendar.DAY_OF_MONTH);
         month = cal.get(Calendar.MONTH);
         year = cal.get(Calendar.YEAR);
         int m=month+1;
         String mth=(month<10)? ("0"+m):(""+m);
         String dy=(day<10)? ("0"+day):(""+day);
         itemDate.setText(dy+"-"+ mth +"-"+year);
        
    	 itemDate.setOnClickListener(new OnClickListener() {

    	        @Override
    	        public void onClick(View v) {

    	            DateDialog(); 

    	        }
    	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  public void addNewDueItem(View view){
	    	/*EditText itemName= (EditText)findViewById(R.id.editItemName);
	    	EditText itemDesc= (EditText)findViewById(R.id.editDesc);
	    	EditText itemDate= (EditText)findViewById(R.id.editDueDate);*/
	    if(itemName.getText().toString()==null || itemName.getText().toString().isEmpty()){
	    	itemName.setError("Fill in an Item Name");
	    	return;
	    }
	    if(itemDate.getText().toString()==null || itemDate.getText().toString().isEmpty()){
	    	itemDate.setError("Fill in a Date");
	    	return;
	    }
		  String[] sDateArr=itemDate.getText().toString().split("-");
		  String sItemdate="";
		  if(sDateArr!=null && sDateArr.length==3 
				  && sDateArr[0].length()==2 && sDateArr[1].length()==2
				  && sDateArr[2].length()==4){
			  sItemdate=sDateArr[2]+"-"+sDateArr[1]+"-"+sDateArr[0];
		  }else{
			  itemDate.setError("Invalid Date. Valid format : DD-MM-YYYY");
		      return;
		  }
	    	DueDateDbHelper mDbHelper =new DueDateDbHelper(view.getContext());
	    	// Gets the data repository in write mode
	    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

	    	// Create a new map of values, where column names are the keys
	    	ContentValues values = new ContentValues();
	    	values.put(DueDateDBContract.DueItems.COLUMN_NAME_ITEM, itemName.getText().toString());
	    	values.put(DueDateDBContract.DueItems.COLUMN_NAME_DESC, itemDesc.getText().toString());
	    	values.put(DueDateDBContract.DueItems.COLUMN_NAME_DATE, sItemdate);

	    	// Insert the new row, returning the primary key value of the new row
	    	
	    	newRowId = db.insert(
	    			DueDateDBContract.DueItems.TABLE_NAME,
	    			null,
	    	         values);
	    	
	    	if(newRowId!=-1){
	    		alertMsg="Due Item Added successfully!!!";
	    	}else{
	    		alertMsg="Transaction Failure!!!";
	    	}
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
	 
				// set title
				alertDialogBuilder.setTitle("Info");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage(alertMsg)
					.setCancelable(false)
					.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							if(newRowId!=-1){//reset values in case of success
								itemName.setText("");
								itemDesc.setText("");
								 int m=month+1;
						         String mth=(month<10)? ("0"+m):(""+m);
						         String dy=(day<10)? ("0"+day):(""+day);
						         itemDate.setText(dy+"-"+ mth +"-"+year);
							}
							dialog.cancel();
						}
					  });
					/*.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});*/
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show itx`x
					alertDialog.show();
	    }
	  public void DateDialog(){

		  DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

		    				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
		    					monthOfYear++;
		    		String mth=(monthOfYear<10)? ("0"+monthOfYear):(""+monthOfYear);
		    		 String dy=(dayOfMonth<10)? ("0"+dayOfMonth):(""+dayOfMonth);
		    		itemDate.setText(dy+"-"+mth+"-"+year);
				}};

		    DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
		    dpDialog.show();

		}

}
