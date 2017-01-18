package ay3524.com.wallpapertime.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by Ashish on 19-01-2017.
 */

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient{
    private MediaScannerConnection mMs;
    private File mFile;
    //Context context1;
    public SingleMediaScanner(Context context, File f) {
        mFile = f;
        //context1 = context;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        //Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setData(uri);
        //context1.startActivity(intent);
        mMs.disconnect();
    }
}
