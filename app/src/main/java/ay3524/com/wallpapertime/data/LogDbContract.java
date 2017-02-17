package ay3524.com.wallpapertime.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ashish on 15-02-2017.
 */

public class LogDbContract {
    private LogDbContract(){}

    static final String AUTHORITY = "ay3524.com.wallpapertime";

    // The base content URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    static final String PATH_LOGS = "logs";

    public static class LogDbContractEntry implements BaseColumns{


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOGS).build();

        static final String TABLE_NAME = "logs";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_TIME = "time";


    }
}
