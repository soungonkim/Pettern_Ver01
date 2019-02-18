package com.example.pettern_ver01.Fragments.ProfileFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pettern_ver01.LoginRegister.SessionHandler;
import com.example.pettern_ver01.TabActivity;
import com.example.pettern_ver01.User;
import com.example.pettern_ver01.helper.CheckNetworkStatus;
import com.example.pettern_ver01.R;
import com.example.pettern_ver01.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPetActivity extends AppCompatActivity {
    private SessionHandler session;

    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_PET_NAME = "pet_name";
    private static final String KEY_PET_CHAR = "pet_char";
    private static final String KEY_PET_BREED = "pet_breed";
    private static final String KEY_PET_GENDER = "pet_gender";
    private static final String KEY_PET_AGE = "pet_age";
    private static final String KEY_PET_WEIGHT = "pet_weight";

    private static final String BASE_URL = "http://101.101.163.224/apptest1/pet/";
    private static String STRING_EMPTY = "";
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

    private Button addButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        petNameEditText = (EditText) findViewById(R.id.txtPetNameAdd);
        petCharEditText = (EditText) findViewById(R.id.txtPetCharAdd);
        petBreedEditText = (EditText) findViewById(R.id.txtPetBreedAdd);
        petGenderEditText = (EditText) findViewById(R.id.txtPetGenderAdd);
        petAgeEditText = (EditText) findViewById(R.id.txtPetAgeAdd);
        petWeightEditText = (EditText) findViewById(R.id.txtPetWeightAdd);

        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addPet();
                } else {
                    Toast.makeText(AddPetActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddPetActivity.this, TabActivity.class);
        startActivity(intent);
    }


    /**
     * Checks whether all files are filled. If so then calls AddPetAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void addPet() {
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
            new AddPetAsyncTask().execute();
        } else {
            Toast.makeText(AddPetActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();
        }


    }

    /**
     * AsyncTask for adding a pet
     */
    private class AddPetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(AddPetActivity.this);
            pDialog.setMessage("Adding Pet. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%httpjsonparser Object made");
            session = new SessionHandler(getApplicationContext());
            User user = session.getUserDetails();
            System.out.println(user.getUsername());

            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_USER_EMAIL, user.getUsername());
            httpParams.put(KEY_PET_NAME, petName);
            httpParams.put(KEY_PET_CHAR, petChar);
            httpParams.put(KEY_PET_BREED, petBreed);
            httpParams.put(KEY_PET_GENDER, petGender);
            httpParams.put(KEY_PET_AGE, petAge);
            httpParams.put(KEY_PET_WEIGHT, petWeight);


            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%  PUT");

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "pet_add.php", "POST", httpParams);
            try {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Json Object made");

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
                        Toast.makeText(AddPetActivity.this,
                                "Pet Added", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddPetActivity.this, TabActivity.class);
                        //send result code 20 to notify about pet update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        startActivity(i);

                    } else {
                        Toast.makeText(AddPetActivity.this,
                                "Some error occurred while adding pet",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}