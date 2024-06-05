package com.example.lovelypets.fragments.aboutus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.exitalertdialog.ExitDialogActivity;

/**
 * {@link Fragment} subclass representing the "About Us" section.
 * Implements the {@link OnBackPressedListener} to handle back button presses.
 */
public class AboutUsFragment extends Fragment implements OnBackPressedListener {

    /**
     * Default constructor. Required for fragment subclasses.
     */
    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of AboutUsFragment.
     *
     * @return A new instance of fragment AboutUsFragment.
     */
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log creation of fragment
        Log.d("AboutUsFragment", "Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    /**
     * Displays an exit dialog when the back button is pressed.
     */
    public void showExitDialog() {
        ExitDialogActivity dialog = new ExitDialogActivity(requireContext());
        dialog.show();
        // Log the showing of exit dialog
        Log.d("AboutUsFragment", "Exit dialog shown");
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}
