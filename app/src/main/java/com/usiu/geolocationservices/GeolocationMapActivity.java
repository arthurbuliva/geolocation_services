package com.usiu.geolocationservices;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class GeolocationMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener
{
    private GoogleMap mMap;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geolocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        GPSTracker gps = new GPSTracker(this);
        double gpsLat = gps.getLatitude();
        double gpsLng = gps.getLongitude();

        gpsLat = -1.3138;
        gpsLng = 36.7941;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gpsLat, gpsLng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));


        String category = getIntent().getExtras().getString("category");

        category = category.toLowerCase().replaceAll("nearby ", "");

        Toast.makeText(getApplicationContext(), category, Toast.LENGTH_LONG).show();
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        String coordinates = String.format("%s,%s", new Object[]{gpsLat, gpsLng});

        String link =
//                "https://maps.googleapis.com/maps/api/place/search/json?";
                "https://maps.googleapis.com/maps/api/place/search/json?location=" +
                        coordinates +
                        "&radius=2000&types=" +
                        category +
                        "&sensor=true&key=AIzaSyBu7Bb7aEC0fzWr_7QXHqiHuZluUnkKgiU";

//        System.out.println(link);
//

        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                //   loading.dismiss();

//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("results");

                    for (int i = 0; i < result.length(); i++)
                    {

                        JSONObject nameOfPlace = result.getJSONObject(i);

                        String name = (nameOfPlace.getString("name"));

                        JSONObject geometry = nameOfPlace.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");

                        String latitude = location.getString("lat");
                        String longitude = location.getString("lng");

                        String message = String.format("%s at (%s, %s)", new Object[]{name, latitude, longitude});

                        mMap.addMarker(new MarkerOptions().
                                position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                                .title(name)).showInfoWindow();

//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                }
                catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }

        );


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override
    public void onLocationChanged(Location location)
    {
        String latLng = ("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        Toast.makeText(getApplicationContext(), latLng, Toast.LENGTH_LONG).show();
    }


}
