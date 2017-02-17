package ay3524.com.wallpapertime.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.ViewPagerAdapter;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.model.WallpaperCollection;
import ay3524.com.wallpapertime.sync.WallpaperIntentService;
import ay3524.com.wallpapertime.sync.WallpaperSyncUtils;
import ay3524.com.wallpapertime.utils.CircleTransform;
import ay3524.com.wallpapertime.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ay3524.com.wallpapertime.utils.Constants.API_KEY;
import static ay3524.com.wallpapertime.utils.Constants.CLIENT_ID;

public class ItemListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, SearchView.OnQueryTextListener {

    public static int navItemIndex = 0;

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    TextView txtName;
    TextView txtWebsite;
    TextView txtSignOut;

    private DrawerLayout mDrawerLayout;
    private ImageView imgProfile;

    ArrayList<WallpaperCollection> collections_list = new ArrayList<>();
    ArrayList<String> durations = new ArrayList<>();
    ArrayList<String> spinner_collection_list = new ArrayList<>();
    ArrayAdapter<String> spinner_collection_adapter;
    private Spinner collections_spinner;
    String collection_string, duration_string;
    private TextView total_photos;
    private String total_photos_value;
    int position_of_collection_spinner;
    private static final int REQ_CODE = 9001;

    GoogleApiClient googleApiClient;
    private int position_of_duration_spinner;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        // Navigation view header
        View navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        txtSignOut = (TextView) navHeader.findViewById(R.id.signout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        loadNavHeader();

        setUpNavigationView();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
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
                        mDrawerLayout.closeDrawers();
                        showAutomationDialog();
                        break;
                    case R.id.nav_photo_of_the_day:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), PhotoOfTheDayActivity.class));
                        break;
                    case R.id.nav_downloads:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), MyDownloadsActivity.class));
                        break;
                    case R.id.nav_settings:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), LogActivity.class));
                        break;
                    case R.id.nav_about_us:
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        mDrawerLayout.closeDrawers();
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
        //txtName.setText("Ashish Yadav");
        txtWebsite.setText(getString(R.string.sign_in));
        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        txtName.setVisibility(View.INVISIBLE);
                        //txtWebsite.setVisibility(View.GONE);
                        imgProfile.setVisibility(View.INVISIBLE);
                        txtWebsite.setText(getString(R.string.sign_in));
                        txtWebsite.setClickable(true);
                        txtSignOut.setVisibility(View.INVISIBLE);
                        Toast.makeText(ItemListActivity.this, getString(R.string.logged_out), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
        adapter.addFragment(new FragmentCollections(), getString(R.string.collection));
        adapter.addFragment(new FragmentDailyNew(), getString(R.string.daily_new));
        adapter.addFragment(new FragmentPopular(), getString(R.string.popular));
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


        final ArrayAdapter<String> duration_adapter;
        if (spinner_collection_list.size() == 0) {
            getListOfCollections();

            durations.add(getString(R.string.one_hour));
            durations.add(getString(R.string.two_hour));
            durations.add(getString(R.string.three_hour));
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

         dialogBuilder.setTitle(getString(R.string.add_automation));

        dialogBuilder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (spinner_collection_list.isEmpty()) {
                    Toast.makeText(ItemListActivity.this, getString(R.string.data_not_fetched), Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences(getPackageName(), MODE_PRIVATE);
                    sharedPreferences.edit().putString(Constants.ID, collections_list.get(position_of_collection_spinner).getId()).apply();
                    sharedPreferences.edit().putInt(Constants.TIME, position_of_duration_spinner + 1).apply();
                    sharedPreferences.edit().putBoolean(Constants.AUTOMATION, true).apply();

                    WallpaperSyncUtils.scheduleWallpaperChange(getApplicationContext(), position_of_duration_spinner);
                    Intent intentToSyncImmediately = new Intent(getApplicationContext(), WallpaperIntentService.class);
                    startService(intentToSyncImmediately);
                }

            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void getListOfCollections() {

        JsonArrayRequest req = new JsonArrayRequest(Constants.UNSPLASH_BASE_COLLECTION_CURATED + CLIENT_ID + API_KEY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperCollection wallpaperUnsplash = new WallpaperCollection();

                                JSONObject jsonObject = response.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));
                                wallpaperUnsplash.setTitle(jsonObject.getString(Constants.TITLE));
                                wallpaperUnsplash.setTotal_photos(jsonObject.getString(Constants.TOTAL_PHOTOS));

                                spinner_collection_list.add(jsonObject.getString(Constants.TITLE));

                                total_photos_value = jsonObject.getString(Constants.TOTAL_PHOTOS);

                                collections_list.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        spinner_collection_adapter = new ArrayAdapter<>(ItemListActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinner_collection_list);
                        collections_spinner.setAdapter(spinner_collection_adapter);
                        total_photos.setText(total_photos_value);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getInstance().addToRequestQueue(req,
                Constants.TAG_JSON_ARRAY);
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
            position_of_duration_spinner = position;
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

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void handleResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = null, email = null, img_url = null;
            if (account != null) {
                name = account.getDisplayName();
                email = account.getEmail();
                img_url = account.getPhotoUrl().toString();
            }

            txtName.setText(name);
            txtWebsite.setText(email);
            txtWebsite.setClickable(false);

            txtName.setVisibility(View.VISIBLE);
            imgProfile.setVisibility(View.VISIBLE);
            txtSignOut.setVisibility(View.VISIBLE);

            Glide.with(this).load(img_url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        //searchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
        i.putExtra(Constants.QUERY, query.trim());
        startActivity(i);

        invalidateOptionsMenu();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}