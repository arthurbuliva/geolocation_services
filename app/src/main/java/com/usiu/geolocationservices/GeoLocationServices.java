package com.usiu.geolocationservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoLocationServices extends AppCompatActivity
{

    private RadioGroup radioCategoryGroup;
    private RadioButton radioCategoryButton;
    private Button displayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location_services);
    }


    /**
     * Called when the user touches the button
     */
    public void displayMap(View view)
    {
        radioCategoryGroup = (RadioGroup) findViewById(R.id.radioCategory);
        displayButton = (Button) findViewById(R.id.bttnFindLocation);

        // get selected radio button from radioGroup
        int selectedId = radioCategoryGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioCategoryButton = (RadioButton) findViewById(selectedId);

        Intent intent = new Intent(GeoLocationServices.this, GeolocationMapActivity.class);
        intent.putExtra("category", radioCategoryButton.getText());
        startActivity(intent);
    }
}
