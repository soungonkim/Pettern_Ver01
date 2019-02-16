package com.example.pettern_ver01.Fragments.ProfileFragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import com.example.pettern_ver01.LoginRegister.SessionHandler;
import com.example.pettern_ver01.R;
import com.example.pettern_ver01.TabActivity;
import com.example.pettern_ver01.User;
import com.example.pettern_ver01.helper.CheckNetworkStatus;
import com.example.pettern_ver01.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private SessionHandler session;

    private static final String KEY_USER_EMAIL = "user_email";

    private static String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_GENDER = "user_gender";
    private static final String KEY_USER_ADDRESS = "user_address";
    private static final String KEY_USER_JOB = "user_job";
    private static final String KEY_USER_SPENT = "user_spent";
    private static final String KEY_USER_YEAR = "user_year";
    private static final String KEY_USER_MONTH = "user_month";
    private static final String KEY_USER_DAY = "user_day";
    private static final String BASE_URL = "http://211.206.115.80/apptest1/user/";

    private String userId;
    private EditText userNameEditText;
    private EditText userGenderEditText;
    private EditText userAddressEditText;
    private EditText userJobEditText;
    private EditText userSpentEditText;
    private EditText userYearEditText;
    private EditText userMonthEditText;
    private EditText userDayEditText;

    private String userName;
    private String userGender;
    private String userAddress;
    private String userJob;
    private String userSpent;
    private String userYear;
    private String userMonth;
    private String userDay;

    private Button updateButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Intent intent = getIntent();
        userNameEditText = (EditText) findViewById(R.id.txtUserNameAdd);
        userGenderEditText = (EditText) findViewById(R.id.txtUserGenderAdd);
        userAddressEditText = (EditText) findViewById(R.id.txtUserAddressAdd);
        userJobEditText = (EditText) findViewById(R.id.txtUserJobAdd);
        userSpentEditText = (EditText) findViewById(R.id.txtUserSpentAdd);
        userYearEditText = (EditText) findViewById(R.id.txtUserYearAdd);
        userMonthEditText = (EditText) findViewById(R.id.txtUserMonthAdd);
        userDayEditText = (EditText) findViewById(R.id.txtUserDayAdd);

        userId = intent.getStringExtra(KEY_USER_ID);
        new FetchUserDetailsAsyncTask().execute();

        updateButton = (Button) findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    if (userNameEditText.getText().toString().matches(""))
                        addUser();
                    else
                        updateUser();

                } else {
                    Toast.makeText(UserProfile.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, TabActivity.class);
        startActivity(intent);
    }

    /**
     * Fetches single user details from the server
     */
    private class FetchUserDetailsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(UserProfile.this);
            pDialog.setMessage("Loading User Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_USER_ID, userId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "user_get_details.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject user;
                if (success == 1) {
                    //Parse the JSON response
                    user = jsonObject.getJSONObject(KEY_DATA);
                    userName = user.getString(KEY_USER_NAME);
                    userGender = user.getString(KEY_USER_GENDER);
                    userAddress = user.getString(KEY_USER_ADDRESS);
                    userJob = user.getString(KEY_USER_JOB);
                    userSpent = user.getString(KEY_USER_SPENT);
                    userYear = user.getString(KEY_USER_YEAR);
                    userMonth = user.getString(KEY_USER_MONTH);
                    userDay = user.getString(KEY_USER_DAY);

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
                    userNameEditText.setText(userName);
                    userGenderEditText.setText(userGender);
                    userAddressEditText.setText(userAddress);
                    userJobEditText.setText(userJob);
                    userSpentEditText.setText(userSpent);
                    userYearEditText.setText(userYear);
                    userMonthEditText.setText(userMonth);
                    userDayEditText.setText(userDay);
                }
            });
        }
    }


    /**
     * Checks whether all files are filled. If so then calls UpdateUserAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void updateUser() {
        if (!STRING_EMPTY.equals(userNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userGenderEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userAddressEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userJobEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userSpentEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userYearEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userMonthEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userDayEditText.getText().toString())) {
            userName = userNameEditText.getText().toString();
            userGender = userGenderEditText.getText().toString();
            userAddress = userAddressEditText.getText().toString();
            userJob = userJobEditText.getText().toString();
            userSpent = userSpentEditText.getText().toString();
            userYear = userYearEditText.getText().toString();
            userMonth = userMonthEditText.getText().toString();
            userDay = userDayEditText.getText().toString();
            new UpdateUserAsyncTask().execute();

        } else {
            Toast.makeText(UserProfile.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * AsyncTask for updating a user details
     */

    private class UpdateUserAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(UserProfile.this);
            pDialog.setMessage("Updating User. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            session = new SessionHandler(getApplicationContext());
            User user = session.getUserDetails();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters

            httpParams.put(KEY_USER_ID, userId);
            httpParams.put(KEY_USER_EMAIL, user.getUsername());
            httpParams.put(KEY_USER_NAME, userName);
            httpParams.put(KEY_USER_GENDER, userGender);
            httpParams.put(KEY_USER_ADDRESS, userAddress);
            httpParams.put(KEY_USER_JOB, userJob);
            httpParams.put(KEY_USER_SPENT, userSpent);
            httpParams.put(KEY_USER_YEAR, userYear);
            httpParams.put(KEY_USER_MONTH, userMonth);
            httpParams.put(KEY_USER_DAY, userDay);

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "user_update.php", "POST", httpParams);
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
                        Toast.makeText(UserProfile.this,
                                "User Updated", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about user update
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(UserProfile.this,
                                "Some error occurred while updating user",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void addUser() {
        if (!STRING_EMPTY.equals(userNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userGenderEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userAddressEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userJobEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userSpentEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userYearEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userMonthEditText.getText().toString()) &&
                !STRING_EMPTY.equals(userDayEditText.getText().toString())) {
            userName = userNameEditText.getText().toString();
            userGender = userGenderEditText.getText().toString();
            userAddress = userAddressEditText.getText().toString();
            userJob = userJobEditText.getText().toString();
            userSpent = userSpentEditText.getText().toString();
            userYear = userYearEditText.getText().toString();
            userMonth = userMonthEditText.getText().toString();
            userDay = userDayEditText.getText().toString();
            new UserProfile.AddUserAsyncTask().execute();
        } else {
            Toast.makeText(UserProfile.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * AsyncTask for adding a use
     */
    private class AddUserAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(UserProfile.this);
            pDialog.setMessage("Adding User. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            session = new SessionHandler(getApplicationContext());
            User user = session.getUserDetails();
            System.out.println(user.getUsername());

            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_USER_EMAIL, user.getUsername());
            httpParams.put(KEY_USER_NAME, userName);
            httpParams.put(KEY_USER_GENDER, userGender);
            httpParams.put(KEY_USER_ADDRESS, userAddress);
            httpParams.put(KEY_USER_JOB, userJob);
            httpParams.put(KEY_USER_SPENT, userSpent);
            httpParams.put(KEY_USER_YEAR, userYear);
            httpParams.put(KEY_USER_MONTH, userMonth);
            httpParams.put(KEY_USER_DAY, userDay);

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "user_add.php", "POST", httpParams);
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
                        Toast.makeText(UserProfile.this,
                                "User Added", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(UserProfile.this, TabActivity.class);
                        //send result code 20 to notify about user update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        startActivity(i);

                    } else {
                        Toast.makeText(UserProfile.this,
                                "Some error occurred while adding user",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


}