package com.home.dbykovskyy.imagesearch.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.home.dbykovskyy.imagesearch.adapters.ImageResultArrayAdapter;
import com.home.dbykovskyy.imagesearch.fragments.FilterDialogFragment;
import com.home.dbykovskyy.imagesearch.models.FilterFragment;
import com.home.dbykovskyy.imagesearch.models.ImageResult;
import com.home.dbykovskyy.imagesearch.R;
import com.home.dbykovskyy.imagesearch.utils.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;


public class SearchActivity extends AppCompatActivity {
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ImageResultArrayAdapter imageAdapter;
    ArrayList<ImageResult> imageResults;
    FilterFragment filters;
    final String [] pages = {"8", "16", "24", "32", "40", "48", "56"};
    private final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();

    }

    //this is to set up views of the main activity
    public void setUpViews(){
        gvResults = (GridView) findViewById(R.id.gv_resultView);
        imageResults = new ArrayList<>();
        imageAdapter = new ImageResultArrayAdapter(this,imageResults);
        gvResults.setAdapter(imageAdapter);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult imageRes = imageResults.get(position);
                intent.putExtra("url", imageRes);
                startActivity(intent);
            }
        });

    }

    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding", "identity");
        client.get(constructUrl("cat"), null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageJsonresults;
                try {

                    imageJsonresults = response.getJSONObject("responseData").getJSONArray("results");

                    //imageAdapter.clear();
                    imageAdapter.addAll(ImageResult.getFromJsonArray(imageJsonresults));
                    Log.d("DEBUG", imageJsonresults.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Toast.makeText(this,"recuest wasn't sucessful ", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //setting up action bar
        getMenuInflater().inflate(R.menu.main_search_menu, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_app_launcher);
        //setting up search bar text view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                //getinng result on first search
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("Accept-Encoding", "identity");
                client.get(constructUrl(query), null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray imageJsonresults;
                        try {
                            //listener
                            imageJsonresults = response.getJSONObject("responseData").getJSONArray("results");

                            imageAdapter.clear();
                            imageAdapter.addAll(ImageResult.getFromJsonArray(imageJsonresults));
                            Log.d("DEBUG", imageJsonresults.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());
                    }
                });

                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return true;
    }

    public String constructUrl(String query){
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(query);
        if(filters!=null){
            if(!filters.getImageColor().equals("not selected"))
                builder.append("&imgcolor="+filters.getImageColor());
            //populate filter image size
            if(!filters.getImageSize().equals("not selected"))
                builder.append("&imgsz="+filters.getImageSize());

            if(!filters.getImageType().equals("not selected"))
                builder.append("&imgtype="+filters.getImageType());
        }
        return builder.toString();
    }

    private void showAlertDialog() {
        //this is to show dialog with filters
        final FilterDialogFragment alertDialog = FilterDialogFragment.newInstance("Advanced Filters");
        alertDialog.show(getSupportFragmentManager(), "sd");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this is just to to test
        int id = item.getItemId();
        if(id==R.id.miFiltersMenu) {
            showAlertDialog();
            Toast.makeText(this, "My filters clicked", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.action_search){
            Toast.makeText(this, "My search is clicked", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }


    //gening data back for the Fragment dialog through this method
    public void onUserSelectValue(List<String> allFilterValues) {
        filters = new FilterFragment(allFilterValues);
        Toast.makeText(this,"I received that shit ", Toast.LENGTH_SHORT).show();
    }

}
