package com.allen.test.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.allen.test.R;

public class DbOpenHelper extends SQLiteOpenHelper {

	Context mContext = null;
	
	public DbOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " +SelectorData.DataColumns.TBL_NAME+"(" +
				SelectorData.DataColumns._ID + " INTEGER PRIMARY KEY," +
				SelectorData.DataColumns.TYPE+" INTEGER," +
				SelectorData.DataColumns.GROUP+" INTEGER," +
				SelectorData.DataColumns.NAME+" TEXT," +
				SelectorData.DataColumns.PICTURE+" BLOB DEFAULT NULL," +
				SelectorData.DataColumns.DETAIL+" TEXT" +
                ");");

        final CharSequence[] selections = mContext.getResources()
                .getTextArray(R.array.default_menu);
        int size = selections.length;
        try {
            for (int i = 0; i < size; i = i + 2) {
                db.execSQL("INSERT INTO "+SelectorData.DataColumns.TBL_NAME+" (" +
                		SelectorData.DataColumns.TYPE+", " +
                		SelectorData.DataColumns.GROUP+", " +
                		SelectorData.DataColumns.NAME+
                		")"+" VALUES("+
                		"1, "+
                		"1, "+
                		"'"+selections[i]+"',"+
                		");");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + SelectorData.DataColumns.TBL_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
