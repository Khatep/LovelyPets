package com.example.lovelypets.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.exitalertdialog.ExitDialogActivity;
import com.example.lovelypets.fragments.orderhistory.OrderHistoryDetailFragment;
import com.example.lovelypets.fragments.orderhistory.OrderHistoryFragment;
import com.example.lovelypets.models.User;

public class ProfileFragment extends Fragment implements OnBackPressedListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PROFILE_IMAGE_URI_KEY = "profile_image_uri";
    private User currentUser;
    private ImageView profileImageView;
    private TextView profileNameTextView, profileEmailTextView;
    private Uri profileImageUri;
    private Button historyButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        requireActivity().setTitle("Profile");
        profileImageView = view.findViewById(R.id.profile_image);
        profileNameTextView = view.findViewById(R.id.profile_name);
        profileEmailTextView = view.findViewById(R.id.profile_email);
        historyButton = view.findViewById(R.id.history_button);

        if (getArguments() != null) {
            currentUser = getArguments().getParcelable("currentUser");
            updateUI();
        }

        profileImageView.setOnClickListener(v -> selectImage());

        // Restore saved image URI
        Context context = getContext();
        if (context != null) {
            String savedUriString = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
                    .getString(PROFILE_IMAGE_URI_KEY, null);
            if (savedUriString != null) {
                profileImageUri = Uri.parse(savedUriString);
                loadImage(profileImageUri);
            }
        }

        historyButton.setOnClickListener(v -> {
            OrderHistoryFragment orderHistoryFragment = OrderHistoryFragment.newInstance();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, orderHistoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void updateUI() {
        if (currentUser != null) {
            profileNameTextView.setText(currentUser.getName());
            profileEmailTextView.setText(currentUser.getEmail());
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            saveImageUri(profileImageUri);
            loadImage(profileImageUri);
        }
    }

    private void loadImage(Uri imageUri) {
        Context context = getContext();
        if (context != null) {
            Glide.with(context)
                        .load(imageUri)
                        .placeholder(R.drawable.ic_baseline_woman) // Укажите изображение-заполнитель
                        .error(R.drawable.ic_baseline_woman) // Укажите изображение ошибки
                        .into(profileImageView);
        }
    }

    private void saveImageUri(Uri uri) {
        Context context = getContext();
        if (context != null) {
            context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString(PROFILE_IMAGE_URI_KEY, uri.toString())
                    .apply();
        }
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
