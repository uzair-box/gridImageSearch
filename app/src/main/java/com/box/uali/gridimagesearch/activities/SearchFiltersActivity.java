package com.box.uali.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.box.uali.gridimagesearch.R;
import com.box.uali.gridimagesearch.models.ImageFilter;

public class SearchFiltersActivity extends ActionBarActivity {

    private EditText etImageSize;
    private EditText etColorFilter;
    private EditText etImageType;
    private EditText etSiteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        setViews();

        // Set up click handler for filtering
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageSize = etImageSize.getText().toString();
                String colorFilter= etColorFilter.getText().toString();
                String imageType = etImageType.getText().toString();
                String siteFilter = etSiteFilter.getText().toString();

                ImageFilter imageFilter = new ImageFilter(imageSize, colorFilter, imageType, siteFilter);

                Intent i = new Intent();
                i.putExtra("filter", imageFilter);
                setResult(RESULT_OK, i);

                finish();
            }
        });

    }

    private void setViews() {
        etImageSize = (EditText) findViewById(R.id.etImageSize);
        etColorFilter = (EditText) findViewById(R.id.etColorFilter);
        etImageType = (EditText) findViewById(R.id.etImageType);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);

        Intent i = getIntent();
        ImageFilter imageFilter = (ImageFilter) i.getSerializableExtra("filter");
        etImageSize.setText(imageFilter.imageSize);
        etColorFilter.setText(imageFilter.colorFilter);
        etImageType.setText(imageFilter.imageType);
        etSiteFilter.setText(imageFilter.siteFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
