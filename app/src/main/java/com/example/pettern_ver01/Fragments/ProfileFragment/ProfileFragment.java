package com.example.pettern_ver01.Fragments.ProfileFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pettern_ver01.LoginRegister.MainActivity;
import com.example.pettern_ver01.LoginRegister.SessionHandler;
import com.example.pettern_ver01.R;
import com.example.pettern_ver01.User;

public class ProfileFragment extends Fragment {
    private SessionHandler session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = view.findViewById(R.id.welcomeText);

        welcomeText.setText("Welcome " + user.getFullName() + ", your session will expire on " + user.getSessionExpiryDate());

        Button logoutBtn = view.findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        Button viewPetBtn = view.findViewById(R.id.btnViewPet);
        viewPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PetListingActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        Button addPetBtn = view.findViewById(R.id.btnAddPet);
        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddPetActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


        return view;


    }
}
