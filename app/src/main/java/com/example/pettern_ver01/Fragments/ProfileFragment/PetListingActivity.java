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
    private static final String KEY_PET_NAME = "pet_name";
    private static final String KEY_PET_CHAR = "pet_char";
    private static final String KEY_PET_BREED = "pet_breed";
    private static final String KEY_PET_GENDER = "pet_gender";
    private static final String KEY_PET_AGE = "pet_age";
    private static final String KEY_PET_WEIGHT = "pet_weight";

    private static final String BASE_URL = "http://211.206.115.80/apptest1/pet";
    private ArrayList<HashMap<String, String>> petList;
    private ListView petListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_listing);
        petListView = (ListView) findViewById(R.id.petList);
        new FetchMoviesAsyncTask().execute();

    }

    /**
     * Fetches the list of movies from the server
     */
    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(MovieListingActivity.this);
            pDialog.setMessage("Loading movies. Please wait...");
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
                    BASE_URL + "fetch_all_movies.php", "GET", null);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%Json Object made");

            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray movies;
                if (success == 1) {
                    movieList = new ArrayList<>();
                    movies = jsonObject.getJSONArray(KEY_DATA);
                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%movies Object made");
                    //Iterate through the response and populate movies list
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject movie = movies.getJSONObject(i);
                        System.out.println(movie);
                        Integer movieId = movie.getInt(KEY_MOVIE_ID);
                        String movieName = movie.getString(KEY_MOVIE_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_MOVIE_ID, movieId.toString());
                        map.put(KEY_MOVIE_NAME, movieName);
                        movieList.add(map);
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
                    populateMovieList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateMovieList() {
        ListAdapter adapter = new SimpleAdapter(
                MovieListingActivity.this, movieList,
                R.layout.list_item, new String[]{KEY_MOVIE_ID,
                KEY_MOVIE_NAME},
                new int[]{R.id.movieId, R.id.movieName});
        // updating listview
        movieListView.setAdapter(adapter);
        //Call MovieUpdateDeleteActivity when a movie is clicked
        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String movieId = ((TextView) view.findViewById(R.id.movieId))
                            .getText().toString();
                    Intent intent = new Intent(getApplicationContext(),
                            MovieUpdateDeleteActivity.class);
                    intent.putExtra(KEY_MOVIE_ID, movieId);
                    startActivityForResult(intent, 20);

                } else {
                    Toast.makeText(MovieListingActivity.this,
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
            // the user has deleted/updated the movie.
            // So refresh the movie listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}