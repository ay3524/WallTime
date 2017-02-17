package ay3524.com.wallpapertime.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.WallpaperDownloadsAdapter;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.data.LogDbContract;
import ay3524.com.wallpapertime.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDownloadsActivity extends AppCompatActivity implements WallpaperDownloadsAdapter.ListItemClickListener {

    @BindView(R.id.recyclerview_downloads)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    List<File> listOfFiles;
    WallpaperDownloadsAdapter wallpaperDownloadsAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloads);

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);

        Constants.setGridLayoutManager(getApplicationContext(),recyclerView);

        checkPermissionForMarshmallowAndAbove();

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        File folder = new File(Environment.getExternalStorageDirectory() + Constants.WALLTIME_PATH);
        if (folder.exists()) {
            File allFiles[] = folder.listFiles();

            Constants.sortAllFilesNewFirst(allFiles);

            listOfFiles = new LinkedList<>(Arrays.asList(allFiles));
            if (allFiles.length != 0) {
                wallpaperDownloadsAdapter = new WallpaperDownloadsAdapter(listOfFiles, MyDownloadsActivity.this);
                recyclerView.setAdapter(wallpaperDownloadsAdapter);
            } else {
                emptyView.setVisibility(View.VISIBLE);
                Toast.makeText(this, getString(R.string.no_files), Toast.LENGTH_SHORT).show();
            }
        } else {
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.folder_not_created), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            navigateUpTo(intent);
            return true;
        }
        if (id == R.id.action_delete) {
            File folder = new File(Environment.getExternalStorageDirectory() + Constants.WALLTIME_PATH);

            if (folder.exists()) {
                File allFiles[] = folder.listFiles();
                if (allFiles.length > 0) {
                    showDeleteDialog();
                } else {
                    Toast.makeText(this, getString(R.string.no_wall_del), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.no_wall_del), Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyDownloadsActivity.this);
        builder.setMessage(getString(R.string.action_del_msg));
        builder.setPositiveButton(getString(R.string.del), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //deletePet();x

                        File folder = new File(Environment.getExternalStorageDirectory() + Constants.WALLTIME_PATH);
                        File allFiles[] = folder.listFiles();
                        for (File allFile : allFiles) {
                            allFile.delete();
                        }

                        listOfFiles.clear();
                        recyclerView.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.VISIBLE);

                        addThisDownloadedFileInfoToLog(getString(R.string.yes_wall_del));

                        Constants.refreshSystemMediaScanDataBase(getApplicationContext(), folder.getAbsolutePath());

                        Toast.makeText(MyDownloadsActivity.this, getString(R.string.yes_wall_del), Toast.LENGTH_SHORT).show();

                    }


                /*long rows = deleteAllMovies();
                if (rows != 0) {
                    Toast.makeText(getActivity(), rows + " Movies Deleted :)", Toast.LENGTH_SHORT).show();
                }
                itemsModels.clear();
                adapter.notifyDataSetChanged();
                if (adapter == null) {
                    emptyView.setVisibility(View.VISIBLE);
                }*/
                }

        );
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }

        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addThisDownloadedFileInfoToLog(String message) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LogDbContract.LogDbContractEntry.COLUMN_NAME_TASK, message);
        contentValues.put(LogDbContract.LogDbContractEntry.COLUMN_NAME_TIME, DateFormat.getDateTimeInstance().format(new Date()));

        Uri uri = getContentResolver().insert(LogDbContract.LogDbContractEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkPermissionForMarshmallowAndAbove() {
        final int MY_PERMISSIONS_REQUEST_STORAGE = 1;
        final String[] storage_permissions =
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(getString(R.string.permission_message));
                        builder.setTitle(getString(R.string.storage));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MyDownloadsActivity.this, storage_permissions, 0);
                            }
                        });

                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(this, storage_permissions, 0);
                    }
                } else {
                    ActivityCompat.requestPermissions(MyDownloadsActivity.this,
                            storage_permissions,
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }

            }
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent i = new Intent(getApplicationContext(), DownloadDetailActivity.class);
        i.putExtra(Constants.URL_FILE, listOfFiles.get(clickedItemIndex).getAbsolutePath());
        startActivity(i);
    }
}
