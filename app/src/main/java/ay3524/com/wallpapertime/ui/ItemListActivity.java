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
import android.support.v7.app.ActionBar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.ViewPagerAdapter;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.sync.WallpaperIntentService;
import ay3524.com.wallpapertime.sync.WallpaperSyncUtils;
import ay3524.com.wallpapertime.utils.CircleTransform;
import ay3524.com.wallpapertime.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ay3524.com.wallpapertime.R.id.collections;

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

    ArrayList<String> durations = new ArrayList<>();
    String collection_string, duration_string;
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
        //mToolbar.setTitle(getTitle());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setLogo(R.drawable.coollogo);
        }

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        // Navigation view header
        View navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        txtSignOut = (TextView) navHeader.findViewById(R.id.signout);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);


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
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
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

        final Spinner collections_spinner = (Spinner) dialogView.findViewById(collections);
        Spinner date_spinner = (Spinner) dialogView.findViewById(R.id.durations);
        final EditText editText = (EditText) dialogView.findViewById(R.id.time);

        collections_spinner.setOnItemSelectedListener(this);
        date_spinner.setOnItemSelectedListener(this);

        setCategoryAdapter(collections_spinner);
        setDurationAdapter(date_spinner);

        dialogBuilder.setTitle(getString(R.string.add_automation));

        dialogBuilder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String time = editText.getText().toString();
                if (!time.isEmpty() || time.equals("0")) {
                    int intTime = Integer.parseInt(time);
                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences(getPackageName(), MODE_PRIVATE);
                    sharedPreferences.edit().putInt(Constants.CATEGORY, position_of_collection_spinner).apply();
                    sharedPreferences.edit().putBoolean(Constants.AUTOMATION, true).apply();

                    if (Constants.isServiceRunning(WallpaperIntentService.class, getApplicationContext())) {
                        //stopService(new Intent(getApplicationContext(), WallpaperIntentService.class));
                        WallpaperSyncUtils.stopFirebaseJobDispatcher(getApplicationContext());
                    }
                    startWallpaperChangerService(intTime);

                } else {
                    Toast.makeText(ItemListActivity.this, "Time Not Set", Toast.LENGTH_SHORT).show();
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

    private void startWallpaperChangerService(int intTime) {
        WallpaperSyncUtils.scheduleWallpaperChange(getApplicationContext(), position_of_duration_spinner, intTime);
        Intent intentToSyncImmediately = new Intent(getApplicationContext(), WallpaperIntentService.class);
        startService(intentToSyncImmediately);
    }

    private void setDurationAdapter(Spinner date_spinner) {
        if (durations.isEmpty()) {
            durations.add(getString(R.string.hour));
            durations.add(getString(R.string.minutes));
            ArrayAdapter<String> duration_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, durations);
            date_spinner.setAdapter(duration_adapter);
        } else {
            ArrayAdapter<String> duration_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, durations);
            date_spinner.setAdapter(duration_adapter);
        }
    }

    private void setCategoryAdapter(Spinner collections_spinner) {

        if (Constants.getCategoryList().isEmpty()) {

            ArrayAdapter<String> spinner_collection_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.getCategoryList());
            collections_spinner.setAdapter(spinner_collection_adapter);
        } else {
            ArrayAdapter<String> spinner_collection_adapter = new ArrayAdapter<>(ItemListActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, Constants.getCategoryList());
            collections_spinner.setAdapter(spinner_collection_adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner duration_spinner = (Spinner) parent;
        Spinner collection_spinner = (Spinner) parent;

        if (duration_spinner.getId() == collections) {
            collection_string = collection_spinner.getItemAtPosition(position).toString();
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

        if (duration_spinner.getId() == collections) {
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