package com.example.lovelypets.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.exit_alert_dialog.ExitDialogActivity;

public class HomeFragment extends Fragment implements OnBackPressedListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView msgCountLbl = view.findViewById(R.id.home_fragment_textView);
        msgCountLbl.setText("Home Fragment");

        // Inflate the layout for this fragment
        return view;
    }


    public void showExitDialog() {
        ExitDialogActivity dialog = new ExitDialogActivity(requireContext());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}