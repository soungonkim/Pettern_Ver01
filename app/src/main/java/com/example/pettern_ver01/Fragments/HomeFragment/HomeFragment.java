package com.example.pettern_ver01.Fragments.HomeFragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.pettern_ver01.helper.HttpJsonParser;

import com.example.pettern_ver01.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String BASE_URL = "http://101.101.163.224/";
    private static final String KEY_CMD = "t";

    private String cold = "cold_cmd";
    private String stop = "stop_cmd";
    private String hot = "hot_cmd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton coldBtn = view.findViewById(R.id.btnCold);
        coldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Toast toast = Toast.makeText(getActivity(), "Cold Mode", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 400);
                toast.show();
                ColdMode();
            }
        });
        ImageButton stopBtn = view.findViewById(R.id.btnStop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Toast toast = Toast.makeText(getActivity(), "Stop Mode", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 400);
                toast.show();
                StopMode();
            }
        });
        ImageButton hotBtn = view.findViewById(R.id.btnHot);
        hotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Toast toast = Toast.makeText(getActivity(), "Hot Mode", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 400);
                toast.show();
                HotMode();
            }
        });

        return view;
    }

    void ColdMode(){
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        httpParams.put(KEY_CMD, cold);
        System.out.println(httpParams);
        httpJsonParser.makeHttpRequest(BASE_URL + "cmd.php", "GET", httpParams);
    }
    void StopMode(){
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        httpParams.put(KEY_CMD, stop);
        httpJsonParser.makeHttpRequest(BASE_URL + "cmd.php", "GET", httpParams);
    }
    void HotMode(){
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        Map<String, String> httpParams = new HashMap<>();
        //Populating request parameters
        httpParams.put(KEY_CMD, hot);
        httpJsonParser.makeHttpRequest(BASE_URL + "cmd.php", "GET", httpParams);
    }

}
