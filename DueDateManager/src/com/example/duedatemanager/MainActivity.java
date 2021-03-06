package com.example.duedatemanager;

import java.util.Calendar;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;



public class MainActivity extends ActionBarActivity {
	private Calendar cal;
	 private int day;
	 private int month;
	 private int year;
	 ListView tdyListView;
	 SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tdyListView = (ListView) findViewById(R.id.listView1);
        //tdyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        tdyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        populateTodaysList(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void goToAddDueItem(View view){
    	Intent intent = new Intent(this, AddItemActivity.class);
    	startActivity(intent);
    }
    public void goToViewItems(View view){
    	Intent intent = new Intent(this, ViewItemsActivity.class);
    	startActivity(intent);
    }
    public void populateTodaysList(Context cont){
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
		
		String selection=DueDateDBContract.DueItems.COLUMN_NAME_DATE +" = ? ";
		
		
		
		 cal = Calendar.getInstance();
         day = cal.get(Calendar.DAY_OF_MONTH);
         month = cal.get(Calendar.MONTH);
         year = cal.get(Calendar.YEAR);		
         int m=month+1;
         String mth=(month<10)? ("0"+m):(""+m);
         String dy=(day<10)? ("0"+day):(""+day);
         String[] selectionArg={(year+"-"+ mth +"-"+dy)};
		
		
		// How you want the results sorted in the resulting Cursor
		//String sortOrder =
		//	DueDateDBContract.DueItems.COLUMN_NAME_DATE + " DESC";

		Cursor c = db.query(
			DueDateDBContract.DueItems.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    selection,                                // The columns for the WHERE clause
		    selectionArg,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		  adapter = new SimpleCursorAdapter(cont,
	               android.R.layout.simple_list_item_2, 
	                c,
	                        new String[] {
				 DueDateDBContract.DueItems.COLUMN_NAME_ITEM,
				 DueDateDBContract.DueItems.COLUMN_NAME_DATE
	                        },
	                        new int[] { android.R.id.text1, android.R.id.text2 },0);
		 adapter.notifyDataSetChanged();
		tdyListView.setAdapter(adapter);
		tdyListView.setOnItemClickListener(mMessageClickedHandler);
		
    }
    private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
        	Intent intent = new Intent(v.getContext(), MoreInfoActivity.class);
		      intent.putExtra("ItemId", id+"");
		      startActivity(intent);
        }
    };
}
