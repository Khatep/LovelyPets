package com.example.lovelypets.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovelypets.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        TextView msgCountLbl = view.findViewById(R.id.category_fragment_textView);
        msgCountLbl.setText("Category Fragment");

        // Inflate the layout for this fragment
        return view;
    }
}