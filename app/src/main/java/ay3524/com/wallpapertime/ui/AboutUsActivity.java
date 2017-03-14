package ay3524.com.wallpapertime.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.stream.IntStream;

import ay3524.com.wallpapertime.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Element adsElement = new Element();
        //adsElement.setTitle("Advertise with us");


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.description))
                .setImage(R.drawable.coollogo)
                .addItem(new Element().setTitle(getString(R.string.version)))
                //.addItem(adsElement)
                .addGroup(getString(R.string.connect_with_us))
                .addEmail(getString(R.string.email))
                //.addWebsite("http://medyo.github.io/")
                .addFacebook(getString(R.string.facebook))
                //.addTwitter("medyo80")
                //.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore(getString(R.string.package_name))
                .addInstagram(getString(R.string.instagram))
                .addGitHub(getString(R.string.git))
                //.addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);


    }

}
