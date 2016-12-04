package com.example.duedatemanager;

import android.provider.BaseColumns;

public class DueDateDBContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DueDateDBContract() {}
    
    /* Inner class that defines the table contents */
    public static abstract class DueItems implements BaseColumns {
        public static final String TABLE_NAME = "due_items";
        public static final String COLUMN_NAME_ITEM = "item";
        public static final String COLUMN_NAME_DESC = "desc";
        public static final String COLUMN_NAME_DATE = "date";

    }
    
}
