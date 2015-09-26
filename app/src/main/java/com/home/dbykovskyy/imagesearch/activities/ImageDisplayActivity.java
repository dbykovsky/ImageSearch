package com.home.dbykovskyy.imagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.dbykovskyy.imagesearch.R;
import com.home.dbykovskyy.imagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);



        ImageResult imageRes = (ImageResult) getIntent().getParcelableExtra("url");

        ImageView iv_fullScreenPhoto = (ImageView) findViewById(R.id.iv_full_screen);

/*
        int displayWidth = DeviceDimensionsHelper.getDisplayWidth(this);
        int picWidth = Integer.parseInt(i.getImageWidth());
        int picHeight = Integer.parseInt(photo.getImageHeight());
        int aspectRatio =picWidth/picHeight;
        int newHeight = displayWidth/aspectRatio;


*/

        Picasso.with(this).load(imageRes.getFullUrl()).fit().into(iv_fullScreenPhoto);
        if(iv_fullScreenPhoto.getDrawable()==null){
            //shoe placeholder
            TextView textView = new TextView(this);
            textView.setText("Image can't be found");

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
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
}
