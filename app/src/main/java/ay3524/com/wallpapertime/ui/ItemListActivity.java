package ay3524.com.wallpapertime.ui;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.ViewPagerAdapter;
import ay3524.com.wallpapertime.utils.CircleTransform;


public class ItemListActivity extends AppCompatActivity {

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private static final String urlNavHeaderBg = "https://images.unsplash.com/photo-1484452330304-377cdeb05340?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&s=503f268f4cd99055517cc7ba13215db6";
    private static final String urlProfileImg = "https://images.unsplash.com/profile-1470357472607-48d8b4cba2cc?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=f2cb264bbb4c3550f3e795b1ed4ccde8";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;

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
                        startActivity(new Intent(getApplicationContext(), AutomationActivity.class));
                        break;
                    case R.id.nav_photo_of_the_day:
                        break;
                    case R.id.nav_downloads:
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
        adapter.addFragment(new FragmentCategories(), "Categories");
        adapter.addFragment(new FragmentDailyNew(), "Daily New");
        adapter.addFragment(new FragmentPopular(), "Popular");
        viewPager.setAdapter(adapter);
    }
}
