package com.example.pettern_ver01.Fragments.ProfileFragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import com.example.pettern_ver01.R;
import com.example.pettern_ver01.helper.CheckNetworkStatus;
import com.example.pettern_ver01.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PetUpdateDeleteActivity extends AppCompatActivity {
    private static String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PET_ID = "pet_id";
    private static final String KEY_PET_NAME = "pet_name";
    private static final String KEY_PET_CHAR = "pet_char";
    private static final String KEY_PET_BREED = "pet_breed";
    private static final String KEY_PET_GENDER = "pet_gender";
    private static final String KEY_PET_AGE = "pet_age";
    private static final String KEY_PET_WEIGHT = "pet_weight";

    private static final String BASE_URL = "http://211.206.115.80/apptest1/pet";

    private String petId;
    private EditText petNameEditText;
    private EditText petCharEditText;
    private EditText petBreedEditText;
    private EditText petGenderEditText;
    private EditText petAgeEditText;
    private EditText petWeightEditText;

    private String petName;
    private String petChar;
    private String petBreed;
    private String petGender;
    private String petAge;
    private String petWeight;

    private Button deleteButton;
    private Button updateButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_update_delete);
        Intent intent = getIntent();
        petNameEditText = (EditText) findViewById(R.id.txtPetNameUpdate);
        petCharEditText = (EditText) findViewById(R.id.txtPetCharUpdate);
        petBreedEditText = (EditText) findViewById(R.id.txtPetBreedUpdate);
        petGenderEditText = (EditText) findViewById(R.id.txtPetGenderUpdate);
        petAgeEditText = (EditText) findViewById(R.id.txtPetAgeUpdate);
        petWeightEditText = (EditText) findViewById(R.id.txtPetWeightUpdate);

        petId = intent.getStringExtra(KEY_PET_ID);
        new FetchPetDetailsAsyncTask().execute();
        deleteButton = (Button) findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });
        updateButton = (Button) findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    updatePet();

                } else {
                    Toast.makeText(PetUpdateDeleteActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Fetches single pet details from the server
     */
    private class FetchPetDetailsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PetUpdateDeleteActivity.this);
            pDialog.setMessage("Loading Pet Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PET_ID, petId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "pet_get_details.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject pet;
                if (success == 1) {
                    //Parse the JSON response
                    pet = jsonObject.getJSONObject(KEY_DATA);
                    petName = pet.getString(KEY_PET_NAME);
                    petChar = pet.getString(KEY_PET_CHAR);
                    petBreed = pet.getString(KEY_PET_BREED);
                    petGender = pet.getString(KEY_PET_GENDER);
                    petAge = pet.getString(KEY_PET_AGE);
                    petWeight = pet.getString(KEY_PET_WEIGHT);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    petNameEditText.setText(petName);
                    petCharEditText.setText(petChar);
                    petBreedEditText.setText(petBreed);
                    petGenderEditText.setText(petGender);
                    petAgeEditText.setText(petAge);
                    petWeightEditText.setText(petWeight);
                }
            });
        }
    }

    /**
     * Displays an alert dialogue to confirm the deletion
     */
    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PetUpdateDeleteActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to delete this pet?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            //If the user confirms deletion, execute DeletePetAsyncTask
                            new DeletePetAsyncTask().execute();
                        } else {
                            Toast.makeText(PetUpdateDeleteActivity.this,
                                    "Unable to connect to internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * AsyncTask to delete a pet
     */
    private class DeletePetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PetUpdateDeleteActivity.this);
            pDialog.setMessage("Deleting Pet. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Set pet_id parameter in request
            httpParams.put(KEY_PET_ID, petId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "pet_delete.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(PetUpdateDeleteActivity.this,
                                "Pet Deleted", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about pet deletion
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(PetUpdateDeleteActivity.this,
                                "Some error occurred while deleting pet",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    /**
     * Checks whether all files are filled. If so then calls UpdatePetAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void updatePet() {


        if (!STRING_EMPTY.equals(petNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(petCharEditText.getText().toString()) &&
                !STRING_EMPTY.equals(petBreedEditText.getText().toString()) &&
                !STRING_EMPTY.equals(petGenderEditText.getText().toString()) &&
                !STRING_EMPTY.equals(petAgeEditText.getText().toString()) &&
                !STRING_EMPTY.equals(petWeightEditText.getText().toString())) {

            petName = petNameEditText.getText().toString();
            petChar = petCharEditText.getText().toString();
            petBreed = petBreedEditText.getText().toString();
            petGender = petGenderEditText.getText().toString();
            petAge = petAgeEditText.getText().toString();
            petWeight = petWeightEditText.getText().toString();
            new UpdatePetAsyncTask().execute();
        } else {
            Toast.makeText(PetUpdateDeleteActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }


    }

    /**
     * AsyncTask for updating a pet details
     */

    private class UpdatePetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PetUpdateDeleteActivity.this);
            pDialog.setMessage("Updating Pet. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_PET_NAME, petName);
            httpParams.put(KEY_PET_CHAR, petChar);
            httpParams.put(KEY_PET_BREED, petBreed);
            httpParams.put(KEY_PET_GENDER, petGender);
            httpParams.put(KEY_PET_AGE, petAge);
            httpParams.put(KEY_PET_WEIGHT, petWeight);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "pet_update.php", "POST", httpParams);
            try {
                System.out.println(jsonObject);
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(PetUpdateDeleteActivity.this,
                                "Pet Updated", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about pet update
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(PetUpdateDeleteActivity.this,
                                "Some error occurred while updating pet",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}