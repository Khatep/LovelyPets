package com.example.lovelypets.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovelypets.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView msgCountLbl = view.findViewById(R.id.profile_fragment_textView);
        msgCountLbl.setText("Profile Fragment");

        // Inflate the layout for this fragment
        return view;
    }
}