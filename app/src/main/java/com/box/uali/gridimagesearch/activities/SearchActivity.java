package com.box.uali.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.box.uali.gridimagesearch.R;
import com.box.uali.gridimagesearch.adapters.EndlessScrollListener;
import com.box.uali.gridimagesearch.adapters.ImageResultsAdapter;
import com.box.uali.gridimagesearch.models.ImageFilter;
import com.box.uali.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private int pageSize = 8;

    private ImageFilter imageFilter;

    private static final int FILTERS_INTENT_CODE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        // Create the data source
        imageResults = new ArrayList<ImageResult>();

        // Attach the data source to the adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);

        // Link the adapter to the view
        gvResults.setAdapter(aImageResults);

        imageFilter = new ImageFilter();
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch item display activity
                // Create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);

                // Get the image result to display
                ImageResult result = imageResults.get(position);

                // Pass the image result to the intent
                i.putExtra("url", result.fullUrl);

                // Launch the new activity
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener(5, 0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG", "onLoadMore page: " + Integer.toString(page - 1));
                doGoogleImageSearch(page - 1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("DEBUG", "onCreateOptionsMenu on create: ");
        Toast.makeText(this,"Sup World", Toast.LENGTH_SHORT).show();
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    /**
     * Handles the click when the search button is pressed
     *
     * @param View v - The btn that was clicked
     */
    public void onImageSearch(View v) {
        imageResults.clear();
        doGoogleImageSearch(0);
    }

    private String getQueryUrl(String query, int offset) {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + Integer.toString(this.pageSize) +
                "&q=" + query;

        if (offset > 0) {
            url += "&start=" + Integer.toString(offset);
        }

        String filterParams = imageFilter.getAsReqeustParams();

        if (filterParams != "") {
            url += filterParams;
        }

        return url;
    }

    private void doGoogleImageSearch(int pageOffset) {
        int offset = pageOffset * this.pageSize;
        String query = etQuery.getText().toString();
        String searchUrl = this.getQueryUrl(query, offset);

        Log.d("DEBUG", "doGoogleImageSearch URL: " + searchUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "doGoogleImageSearch " + response.toString());

                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", "doGoogleImageSearch " + imageResults.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.miFilters) {
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
            // Launch a second activity (age form)

            Intent i = new Intent(this, SearchFiltersActivity.class);
            i.putExtra("filter", imageFilter);
            startActivityForResult(i, FILTERS_INTENT_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTERS_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "You set new filters!", Toast.LENGTH_SHORT).show();
                imageFilter = (ImageFilter) data.getSerializableExtra("filter");
                imageResults.clear();
                doGoogleImageSearch(0);
            }
        }
    }
}
