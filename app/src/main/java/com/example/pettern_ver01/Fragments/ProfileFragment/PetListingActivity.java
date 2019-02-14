package com.example.pettern_ver01.Fragments.ProfileFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pettern_ver01.R;
import com.example.pettern_ver01.helper.CheckNetworkStatus;
import com.example.pettern_ver01.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PetListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PET_ID = "pet_id";
    private static final String KEY_PET_NAME = "pet_name";


    private static final String BASE_URL = "http://211.206.115.80/apptest1/pet";
    private ArrayList<HashMap<String, String>> petList;
    private ListView petListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_listing);
        petListView = (ListView) findViewById(R.id.petList);
        new FetchPetsAsyncTask().execute();

    }

    /**
     * Fetches the list of pets from the server
     */
    private class FetchPetsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PetListingActivity.this);
            pDialog.setMessage("Loading pets. Please wait...");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%DIALOG");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%in doInBackground");
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%httpjsonparser Object made");

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "pet_fetch_all.php", "GET", null);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Json Object made");

            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray pets;
                if (success == 1) {
                    petList = new ArrayList<>();
                    pets = jsonObject.getJSONArray(KEY_DATA);
                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%pets Object made");
                    //Iterate through the response and populate pets list
                    for (int i = 0; i < pets.length(); i++) {
                        JSONObject pet = pets.getJSONObject(i);
                        System.out.println(pet);
                        Integer petId = pet.getInt(KEY_PET_ID);
                        String petName = pet.getString(KEY_PET_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_PET_ID, petId.toString());
                        map.put(KEY_PET_NAME, petName);
                        petList.add(map);
                    }
                }
            } catch (JSONException e) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Error made");
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    populatePetList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     */
    private void populatePetList() {
        ListAdapter adapter = new SimpleAdapter(
                PetListingActivity.this, petList,
                R.layout.list_item, new String[]{KEY_PET_ID,
                KEY_PET_NAME},
                new int[]{R.id.petId, R.id.petName});
        // updating listview
        petListView.setAdapter(adapter);
        //Call PetUpdateDeleteActivity when a pet is clicked
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String petId = ((TextView) view.findViewById(R.id.petId))
                            .getText().toString();
                    Intent intent = new Intent(getApplicationContext(),
                            PetUpdateDeleteActivity.class);
                    intent.putExtra(KEY_PET_ID, petId);
                    startActivityForResult(intent, 20);

                } else {
                    Toast.makeText(PetListingActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            // If the result code is 20 that means that
            // the user has deleted/updated the pet.
            // So refresh the pet listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}