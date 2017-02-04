package ay3524.com.wallpapertime.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.ViewPagerAdapter;
import ay3524.com.wallpapertime.app.AppController;
import ay3524.com.wallpapertime.model.WallpaperCollection;
import ay3524.com.wallpapertime.utils.CircleTransform;

public class ItemListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static int navItemIndex = 0;
    private static final String urlNavHeaderBg = "https://images.unsplash.com/photo-1484452330304-377cdeb05340?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s=503f268f4cd99055517cc7ba13215db6";
    private static final String urlProfileImg = "https://images.unsplash.com/profile-1470357472607-48d8b4cba2cc?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=f2cb264bbb4c3550f3e795b1ed4ccde8";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private String tag_json_arry = "TAG_JSON_ARRAY";
    ArrayList<WallpaperCollection> collections_list = new ArrayList<>();
    ArrayList<String> durations = new ArrayList<>();
    ArrayList<String> spinner_collection_list = new ArrayList<>();
    ArrayAdapter<String> spinner_collection_adapter;
    private Spinner collections_spinner;
    String collection_string, duration_string;
    private TextView total_photos;
    private String total_photos_value;
    private ArrayAdapter<String> duration_adapter;
    int position_of_collection_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        View navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        loadNavHeader();

        setUpNavigationView();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpNavigationView() {

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_auto:
                        drawer.closeDrawers();
                        showAutomationDialog();
                        break;
                    case R.id.nav_photo_of_the_day:
                        break;
                    case R.id.nav_downloads:
                        drawer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), MyDownloadsActivity.class));
                        break;
                    case R.id.nav_settings:

                        break;
                    case R.id.nav_about_us:
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                return true;
            }
        });

    }

    // Sometimes, when fragment has huge data, screen seems hanging
    // when switching between navigation menus
    // So using runnable, the fragment is loaded with cross fade effect
    // This effect can be seen in GMail app

    private void loadNavHeader() {
        // name, website
        txtName.setText("Ashish Yadav");
        txtWebsite.setText("www.example.com");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentCategories(), "Collections");
        adapter.addFragment(new FragmentDailyNew(), "Daily New");
        adapter.addFragment(new FragmentPopular(), "Popular");
        viewPager.setAdapter(adapter);
    }

    private void showAutomationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemListActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ItemListActivity.this);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        collections_spinner = (Spinner) dialogView.findViewById(R.id.collections);
        total_photos = (TextView) dialogView.findViewById(R.id.total_photos);
        Spinner date_spinner = (Spinner) dialogView.findViewById(R.id.durations);

        collections_spinner.setOnItemSelectedListener(this);
        date_spinner.setOnItemSelectedListener(this);


        if (spinner_collection_list.size() == 0) {
            getListOfCollections();

            durations.add("1 Minutes");
            durations.add("2 Minutes");
            durations.add("3 Minutes");
            duration_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, durations);
            date_spinner.setAdapter(duration_adapter);
        } else {
            duration_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, durations);
            date_spinner.setAdapter(duration_adapter);

            spinner_collection_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, spinner_collection_list);
            collections_spinner.setAdapter(spinner_collection_adapter);
            total_photos.setText(total_photos_value);
        }

        //Log.d("SPINNER2", String.valueOf(sp.getSelectedItem()));
        dialogBuilder.setTitle("Add Automation");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /*SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences("ay3524.com.wallpapertime", MODE_PRIVATE);
                sharedPreferences.edit().putString("id", "136").apply();

                WallpaperSyncUtils.scheduleWallpaperChange(getApplicationContext());
                Intent intentToSyncImmediately = new Intent(getApplicationContext(), WallpaperIntentService.class);
                startService(intentToSyncImmediately);*/

                Toast.makeText(ItemListActivity.this, collection_string + "\n" + duration_string + "\n" + collections_list.get(position_of_collection_spinner).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void getListOfCollections() {

        JsonArrayRequest req = new JsonArrayRequest("https://api.unsplash.com/collections/curated?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperCollection wallpaperUnsplash = new WallpaperCollection();

                                JSONObject jsonObject = response.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString("id"));
                                wallpaperUnsplash.setTitle(jsonObject.getString("title"));
                                wallpaperUnsplash.setTotal_photos(jsonObject.getString("total_photos"));

                                spinner_collection_list.add(jsonObject.getString("title"));

                                total_photos_value = jsonObject.getString("total_photos");

                                collections_list.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        spinner_collection_adapter = new ArrayAdapter<>(ItemListActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinner_collection_list);
                        collections_spinner.setAdapter(spinner_collection_adapter);
                        total_photos.setText(total_photos_value);
                        //pb.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pb.setVisibility(View.GONE);
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner duration_spinner = (Spinner) parent;
        Spinner collection_spinner = (Spinner) parent;

        if (duration_spinner.getId() == R.id.collections) {
            collection_string = collection_spinner.getItemAtPosition(position).toString();
            total_photos.setText(collections_list.get(position).getTotal_photos());
            position_of_collection_spinner = position;
        }
        if (collection_spinner.getId() == R.id.durations) {
            duration_string = duration_spinner.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Spinner duration_spinner = (Spinner) parent;
        Spinner collection_spinner = (Spinner) parent;

        if (duration_spinner.getId() == R.id.collections) {
            collection_string = collection_spinner.getSelectedItem().toString();
        }
        if (collection_spinner.getId() == R.id.durations) {
            duration_string = duration_spinner.getSelectedItem().toString();
        }
    }
}