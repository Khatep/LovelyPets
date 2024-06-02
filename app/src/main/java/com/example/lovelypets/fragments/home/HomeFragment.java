package com.example.lovelypets.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.PartnersImageAdapter;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.exitalertdialog.ExitDialogActivity;

public class HomeFragment extends Fragment implements OnBackPressedListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        int[] images = {
                R.drawable.samsung,
                R.drawable.life_planet,
                R.drawable.doctor_vet,
                R.drawable.kaspi,
                R.drawable.magnum,
                R.drawable.bonnyville,
                R.drawable.my_pets_kz,
                R.drawable.iitu,
                R.drawable.murkel,
                R.drawable.astana_hub
        };

        RecyclerView recyclerView = view.findViewById(R.id.partners_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PartnersImageAdapter adapter = new PartnersImageAdapter(requireContext(), images);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10)); // Adjust the spacing value as needed
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