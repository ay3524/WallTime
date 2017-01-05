package ay3524.com.wallpapertime.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.utils.Constants;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    /**
     * The dummy content this fragment is presenting.
     */
    private String previewHeight;
    private String previewWidth;
    private String webformatHeight;
    private String webformatWidth;
    private String imageHeight;
    private String imageWidth;

    private String largeImageURL;
    private String fullHDURL;
    private String previewURL;
    private String imageURL;
    private String userImageURL;
    private String webformatURL;

    private String user_id;
    private String id_hash;
    private String type;
    private String user;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.PREVIEW_HEIGHT)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            previewHeight = getArguments().getString(Constants.PREVIEW_HEIGHT);
            previewWidth = getArguments().getString(Constants.PREVIEW_WIDTH);
            webformatHeight = getArguments().getString(Constants.WEB_FORMAT_HEIGHT);
            webformatWidth = getArguments().getString(Constants.WEB_FORMAT_WIDTH);
            imageHeight = getArguments().getString(Constants.IMAGE_HEIGHT);
            imageWidth = getArguments().getString(Constants.IMAGE_WIDTH);

            largeImageURL = getArguments().getString(Constants.LARGE_IMAGE_URL);
            fullHDURL = getArguments().getString(Constants.FULL_HD_URL);
            previewURL = getArguments().getString(Constants.PREVIEW_URL);
            imageURL = getArguments().getString(Constants.IMAGE_URL);
            userImageURL = getArguments().getString(Constants.USER_IMAGE_URL);
            webformatURL = getArguments().getString(Constants.WEB_FORMAT_URL);

            user_id = getArguments().getString(Constants.USER_ID);
            id_hash = getArguments().getString(Constants.ID_HASH);
            type = getArguments().getString(Constants.TYPE);
            user = getArguments().getString(Constants.USER);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(previewHeight);
            }
            //final ImageView image = (ImageView)activity.findViewById(R.id.image);
            //Picasso.with(getActivity()).load(webformatURL).into(image);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (previewWidth != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(previewWidth);
        }
        return rootView;
    }
}