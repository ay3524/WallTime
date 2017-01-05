package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.utils.Constants;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    String hash;
    //private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        //window = getWindow();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(Constants.PREVIEW_HEIGHT, getIntent().getStringExtra(Constants.PREVIEW_HEIGHT));
            arguments.putString(Constants.PREVIEW_WIDTH, getIntent().getStringExtra(Constants.PREVIEW_WIDTH));
            arguments.putString(Constants.WEB_FORMAT_HEIGHT, getIntent().getStringExtra(Constants.WEB_FORMAT_HEIGHT));
            arguments.putString(Constants.WEB_FORMAT_WIDTH, getIntent().getStringExtra(Constants.WEB_FORMAT_WIDTH));
            arguments.putString(Constants.IMAGE_HEIGHT, getIntent().getStringExtra(Constants.IMAGE_HEIGHT));
            arguments.putString(Constants.IMAGE_WIDTH, getIntent().getStringExtra(Constants.IMAGE_WIDTH));

            arguments.putString(Constants.LARGE_IMAGE_URL, getIntent().getStringExtra(Constants.LARGE_IMAGE_URL));
            arguments.putString(Constants.FULL_HD_URL, getIntent().getStringExtra(Constants.FULL_HD_URL));
            arguments.putString(Constants.PREVIEW_URL, getIntent().getStringExtra(Constants.PREVIEW_URL));
            arguments.putString(Constants.IMAGE_URL, getIntent().getStringExtra(Constants.IMAGE_URL));
            arguments.putString(Constants.USER_IMAGE_URL, getIntent().getStringExtra(Constants.USER_IMAGE_URL));
            arguments.putString(Constants.WEB_FORMAT_URL, getIntent().getStringExtra(Constants.WEB_FORMAT_URL));

            arguments.putString(Constants.USER_ID, getIntent().getStringExtra(Constants.USER_ID));
            arguments.putString(Constants.ID_HASH, getIntent().getStringExtra(Constants.ID_HASH));
            String hash = getIntent().getStringExtra(Constants.ID_HASH);
            arguments.putString(Constants.TYPE, getIntent().getStringExtra(Constants.TYPE));
            arguments.putString(Constants.USER, getIntent().getStringExtra(Constants.USER));

            //boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
        }


        final ImageView image = (ImageView)findViewById(R.id.image);
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra(Constants.WEB_FORMAT_URL)).into(image, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                /*Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(
                        new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrant =
                                        palette.getVibrantSwatch();
                                try{
                                int mutedColor = palette.getVibrantSwatch().getRgb();
                                if (vibrant != null) {

                                    collapsingToolbarLayout.setBackgroundColor(mutedColor);
                                    collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedColor));

                                }
                                }catch (NullPointerException ignored){

                                }
                            }
                        });*/
            }

            @Override
            public void onError() {

            }
        });

        getDetailsOfCurrentWallpaper(hash);
    }

    private void getDetailsOfCurrentWallpaper(String hash) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
