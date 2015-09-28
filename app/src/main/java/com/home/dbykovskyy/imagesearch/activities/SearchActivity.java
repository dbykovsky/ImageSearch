package com.home.dbykovskyy.imagesearch.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.Toast;

import com.home.dbykovskyy.imagesearch.adapters.ImageResultArrayAdapter;
import com.home.dbykovskyy.imagesearch.fragments.FilterDialogFragment;
import com.home.dbykovskyy.imagesearch.fragments.InternetConnectivityFragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity extends AppCompatActivity {
    GridView gvResults;
    ImageResultArrayAdapter imageAdapter;
    ArrayList<ImageResult> imageResults;
    FilterFragment filters;
    String currentSearchQuery;
    final Map<Integer, String> paginationMap = new HashMap<>();
    private final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //setting up all views
        setUpViews();
        //populating the pagination map
        paginationMap.put(1, "8");
        paginationMap.put(2,"16");
        paginationMap.put(3,"24");
        paginationMap.put(4,"32");
        paginationMap.put(5,"40");
        paginationMap.put(6, "48");
        paginationMap.put(7, "56");
        //check connection
        checkInternetConnection();
    }

    //this is to set up views of the main activity
    public void setUpViews(){
        gvResults = (GridView) findViewById(R.id.gv_resultView);
        imageResults = new ArrayList<>();
        imageAdapter = new ImageResultArrayAdapter(this,imageResults);
        gvResults.setAdapter(imageAdapter);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //paginate up to 7 pages
                if (page <= 7) {
                    customLoadMoreDataFromApi(page);
                    return true;
                } else {
                    return false;
                }
            }
        });


        //set up image click listener
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
        //
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Accept-Encoding", "identity");
            //adding offset to each
            String query = currentSearchQuery+ "&start=" + paginationMap.get(offset);
            Log.d("DEBUG", "Search query 2222 is >>>>>>>>>>>>>>>>>>> " + query);
            client.get(query, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray imageJsonresults;
                    try {
                        imageJsonresults = response.getJSONObject("responseData").getJSONArray("results");
                        imageAdapter.addAll(ImageResult.getFromJsonArray(imageJsonresults));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
                currentSearchQuery = constructUrl(query);
                client.addHeader("Accept-Encoding", "identity");
                Log.d("DEBUG", "Search query 11111 is >>>>>>>>>>>>>>>>>>> " + currentSearchQuery);
                client.get(currentSearchQuery, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray imageJsonresults;
                        try {
                            imageJsonresults = response.getJSONObject("responseData").getJSONArray("results");
                            //since it's a new search, clearing adapter form previous results
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

    //this is to append filters to the base URL form Filters object
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

            if(!filters.getWebsite().equals(""))
                builder.append("&as_sitesearch="+filters.getWebsite());

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
        }
        return super.onOptionsItemSelected(item);
    }


    //gening data back for the Fragment dialog through this method
    public void onUserSelectValue(List<String> allFilterValues) {
        filters = new FilterFragment(allFilterValues);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else return false;
    }


    private void checkInternetConnection(){
        if(!isConnected(SearchActivity.this)){
            final InternetConnectivityFragment connectivity = InternetConnectivityFragment.newInstance("Internet connection");
            connectivity.show(getSupportFragmentManager(), "internet_con");
        }
    }

}
