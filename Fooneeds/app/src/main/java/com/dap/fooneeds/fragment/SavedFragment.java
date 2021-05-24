package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dap.fooneeds.databinding.HomeFragmentBinding;
import com.dap.fooneeds.databinding.SaveFragmentBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SavedFragment extends Fragment {

    private SaveFragmentBinding fragmentBinding;
    private static SavedFragment savedFragment;

    private SavedFragment() {

    }

    public static SavedFragment newInstance() {
        if (savedFragment == null) {
            savedFragment = new SavedFragment();
        }
        return savedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = SaveFragmentBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

}
