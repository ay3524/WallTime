package ay3524.com.wallpapertime.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.WallpaperDownloadsAdapter;

public class MyDownloadsActivity extends AppCompatActivity implements WallpaperDownloadsAdapter.ListItemClickListener {

    RecyclerView recyclerView;
    WallpaperDownloadsAdapter wallpaperDownloadsAdapter;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloads);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_downloads);
        emptyView = (RelativeLayout) findViewById(R.id.empty_view);

        recyclerView.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        checkPermissionForMarshmallowAndAbove();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File folder = new File(Environment.getExternalStorageDirectory() + "/WallTime");
        if (folder.exists()) {
            File allFiles[] = folder.listFiles();

            Arrays.sort( allFiles, new Comparator()
            {
                public int compare(Object o1, Object o2) {

                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });
            List<File> listOfFiles = Arrays.asList(allFiles);
            if (allFiles.length != 0) {
                wallpaperDownloadsAdapter = new WallpaperDownloadsAdapter(listOfFiles,MyDownloadsActivity.this);
                recyclerView.setAdapter(wallpaperDownloadsAdapter);
            }else{
                emptyView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "No Files", Toast.LENGTH_SHORT).show();
            }
        }else{
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Folder not created", Toast.LENGTH_SHORT).show();
        }
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
        return super.onOptionsItemSelected(item);
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
                        builder.setMessage("To get storage access you have to allow us access to your sd card content.");
                        builder.setTitle("Storage");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

    }
}
