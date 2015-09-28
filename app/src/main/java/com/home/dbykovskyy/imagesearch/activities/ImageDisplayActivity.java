package com.home.dbykovskyy.imagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.dbykovskyy.imagesearch.R;
import com.home.dbykovskyy.imagesearch.fragments.InternetConnectivityFragment;
import com.home.dbykovskyy.imagesearch.models.ImageResult;
import com.home.dbykovskyy.imagesearch.utils.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDisplayActivity extends AppCompatActivity {
    private ShareActionProvider mShareAction;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        ImageResult imageRes = getIntent().getParcelableExtra("url");

        ImageView iv_fullScreenPhoto = (ImageView) findViewById(R.id.iv_full_screen);
        Picasso.with(this).load(imageRes.getFullUrl()).fit().centerInside().into(iv_fullScreenPhoto, new Callback() {
            @Override
            public void onSuccess() {
                setupShareIntent();
            }

            @Override
            public void onError() {

            }
        });

        //allow to zoom in and out
        mAttacher = new PhotoViewAttacher(iv_fullScreenPhoto);

        getSupportActionBar().setTitle(Html.fromHtml(imageRes.getTitle()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public Uri getLocalBitmapUri(ImageView imageView) {

        // Extract Bitmap from ImageView drawable

        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;

        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        } else {
            return null;
        }

        // Store image to default external storage directory
        Uri bmpUri = null;

        try {

            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);

        } catch (IOException e) {

            e.printStackTrace();

        }
        return bmpUri;
    }

    // Gets the image URI and setup the associated share intent to hook into the provider

    public void setupShareIntent() {

        // Fetch Bitmap Uri locally

        ImageView ivImage = (ImageView) findViewById(R.id.iv_full_screen);

        Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section

        // Create share intent as described above

        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

        shareIntent.setType("image/*");

        // Attach share event to the menu item provider

        mShareAction.setShareIntent(shareIntent);

    }


}
