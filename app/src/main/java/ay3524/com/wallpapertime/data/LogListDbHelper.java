package ay3524.com.wallpapertime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ashish on 15-02-2017.
 */

class LogListDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "logslist.db";
    private static final int DATABASE_VERSION = 1;

    LogListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_LOG_TABLE = "CREATE TABLE " +
                LogDbContract.LogDbContractEntry.TABLE_NAME + " (" +
                LogDbContract.LogDbContractEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LogDbContract.LogDbContractEntry.COLUMN_NAME_TASK + " TEXT NOT NULL," +
                LogDbContract.LogDbContractEntry.COLUMN_NAME_TIME + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_LOG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LogDbContract.LogDbContractEntry.TABLE_NAME);
        onCreate(db);
    }
}
