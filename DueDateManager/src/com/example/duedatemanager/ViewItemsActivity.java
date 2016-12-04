package com.example.duedatemanager;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewItemsActivity extends ActionBarActivity {
	TableLayout table_layout;
	EditText itemName;
	EditText fromDate;
	EditText toDate;
	TextView message;
	private Calendar cal;
	 private int day;
	 private int month;
	 private int year;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_items);
		  table_layout = (TableLayout) findViewById(R.id.tableLayout1);
		  itemName= (EditText)findViewById(R.id.edtItemName);
		  fromDate= (EditText)findViewById(R.id.edtFromDate);
		  toDate= (EditText)findViewById(R.id.edtToDate);
		  message= (TextView)findViewById(R.id.txtMessage);
		  
		  cal = Calendar.getInstance();
	         day = cal.get(Calendar.DAY_OF_MONTH);
	         month = cal.get(Calendar.MONTH);
	         year = cal.get(Calendar.YEAR);
	         
	         
		  fromDate.setOnClickListener(new OnClickListener() {
  	        @Override
  	        public void onClick(View v) {
  	            DateDialog(v); 
  	        }
  	    });
		  
		  toDate.setOnClickListener(new OnClickListener() {
  	        @Override
  	        public void onClick(View v) {
  	        	 DateDialog(v); 
  	        }
  	    });
		  //buildTable(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_items, menu);
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
	 public void findItems(View view){
		 table_layout.removeAllViews();
		 buildTable(view.getContext());
	 }
	public void buildTable(Context cont){
		//System.out.println("inside build table");
		DueDateDbHelper mDbHelper =new DueDateDbHelper(cont);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			DueDateDBContract.DueItems._ID,
			DueDateDBContract.DueItems.COLUMN_NAME_ITEM,
			DueDateDBContract.DueItems.COLUMN_NAME_DATE
		    };

		//selection logic
		
		String sItemName=itemName.getText().toString();
		String sFromdate=fromDate.getText().toString();
		String sToDate=toDate.getText().toString();
		String selection="";
		String selectionArg="";
		
		if(sItemName!=null && !sItemName.isEmpty()){
			if(selection==""){
				selection+= (DueDateDBContract.DueItems.COLUMN_NAME_ITEM +" like ? ");
				selectionArg+="%"+sItemName+"%";
			}else{
				selection+= (" AND "+DueDateDBContract.DueItems.COLUMN_NAME_ITEM +" like ? ");
				selectionArg+= (",%"+sItemName+"%");
			}
		}
		
		if(sFromdate!=null && !sFromdate.isEmpty()){
			String sItemdate="";
			String[] sDateArr=sFromdate.split("-");
			  
			  if(sDateArr!=null && sDateArr.length==3 ){
				  sItemdate=sDateArr[2]+"-"+sDateArr[1]+"-"+sDateArr[0];
			  }
			  
			  if(selection==""){
					selection+= (DueDateDBContract.DueItems.COLUMN_NAME_DATE +" >= ? ");
					selectionArg+=sItemdate;
				}else{
					selection+= (" AND "+DueDateDBContract.DueItems.COLUMN_NAME_DATE +" >= ? ");
					selectionArg+= (","+sItemdate);
				}
			  
		}
		
		if(sToDate!=null && !sToDate.isEmpty()){
			String sItemdate="";
			String[] sDateArr=sToDate.split("-");
			  
			  if(sDateArr!=null && sDateArr.length==3 ){
				  sItemdate=sDateArr[2]+"-"+sDateArr[1]+"-"+sDateArr[0];
			  }
			  
			  if(selection==""){
					selection+= (DueDateDBContract.DueItems.COLUMN_NAME_DATE +" <= ? ");
					selectionArg+=sItemdate;
				}else{
					selection+= (" AND "+DueDateDBContract.DueItems.COLUMN_NAME_DATE +" <= ? ");
					selectionArg+= (","+sItemdate);
				}
			  
		}
		
		String qWhereClause=null;
		String[] qWhereValue=null;
		if(selection!=""){
			qWhereClause=selection;
			qWhereValue=selectionArg.split(",");
		}
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
			DueDateDBContract.DueItems.COLUMN_NAME_DATE + " DESC";

		Cursor c = db.query(
			DueDateDBContract.DueItems.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    qWhereClause,                                // The columns for the WHERE clause
		    qWhereValue,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		//System.out.println("after cursor query");
		int rows = c.getCount();
		 int cols = c.getColumnCount();
		if(rows>0){
			message.setText("**Tap an item for more info**");
		}else{
			message.setText("**No results found**");
		}
		  c.moveToFirst();

		  // outer for loop
		  for (int i = 0; i < rows; i++) {

		   TableRow row = new TableRow(this);
		 //  row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		  //   LayoutParams.WRAP_CONTENT));

		   // inner for loop
		   for (int j = 0; j < cols; j++) {

		    TextView tv = new TextView(this);
		  //.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		  //    LayoutParams.WRAP_CONTENT));
		   tv.setBackgroundResource(R.drawable.abc_ab_bottom_solid_light_holo);
		    
		    tv.setGravity(Gravity.CENTER);
		    tv.setTextSize(18);
		    if(j!=0){
		    tv.setPadding(0, 5, 0, 5);
		    }
		    if(j==cols-1){//date format
		    	String sDate=c.getString(j);
				 String sDateArr[]=sDate.split("-");//YYYY-MM-DD
				 if(sDateArr!=null&& sDateArr.length==3)
					 tv.setText(sDateArr[2]+"-"+sDateArr[1]+"-"+sDateArr[0]);
				 else
					 tv.setText(sDate);
		    }else{
		    tv.setText(c.getString(j));
		    }
		    row.addView(tv);

		   }

		   c.moveToNext();
		   row.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View view) {
				      TableRow t = (TableRow) view;
				      TextView firstTextView = (TextView) t.getChildAt(0);
				      String firstText = firstTextView.getText().toString();
				      Intent intent = new Intent(t.getContext(), MoreInfoActivity.class);
				      intent.putExtra("ItemId", firstText);
				      startActivity(intent);
				   }
		});
		   table_layout.addView(row);

		  }
		 // table_layout.setColumnCollapsed(0, true);
	}
	 public void DateDialog(final View dateView){

		  DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

		    				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
		    					monthOfYear++;
		    		String mth=(monthOfYear<10)? ("0"+monthOfYear):(""+monthOfYear);
		    		 String dy=(dayOfMonth<10)? ("0"+dayOfMonth):(""+dayOfMonth);
		    		 ((EditText)dateView).setText(dy+"-"+mth+"-"+year);
				}};

		    DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
		    dpDialog.show();

		}
	 
}
