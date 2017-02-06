package ay3524.com.wallpapertime.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ashish on 01-01-2017.
 */

public class ImageDownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog pDialog;
    private String fileName;

    public ImageDownloadTask(Context cxt, String file_name) {
        context = cxt;
        fileName = file_name;
        pDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog.setMessage("Downloading Image. Please wait...");
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);

        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel(true);
            }
        });
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        pDialog.show();
    }

    @Override
    protected void onCancelled() {
        String dir_path = Environment.getExternalStorageDirectory() + "/WallTime";
        File file = new File(dir_path + "/" + fileName);
        if(file.delete()){
            Toast.makeText(context, "Download Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(String... sUrl) {
        //File extStore = Environment.getExternalStorageDirectory();

            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                String dir_path = Environment.getExternalStorageDirectory() + "/WallTime";

                if (!dir_exists(dir_path)) {
                    File directory = new File(dir_path);
                    directory.mkdirs();
                }
                output = new FileOutputStream(dir_path + "/" + fileName);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }

        return null;
    }
    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        // dismiss the dialog after the file was downloaded
        mWakeLock.release();
        pDialog.dismiss();
        if (result != null) {
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/WallTime");
            File allFiles[] = folder.listFiles();
            for (File allFile : allFiles) {
                new SingleMediaScanner(context, allFile);
            }
        }
    }

    private boolean dir_exists(String dir_path) {
        boolean ret = false;
        File dir = new File(dir_path);
        if (dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }
}
