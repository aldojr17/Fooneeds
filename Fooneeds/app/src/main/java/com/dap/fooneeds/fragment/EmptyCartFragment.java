package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dap.fooneeds.MainActivity;
import com.dap.fooneeds.databinding.EmptyCartFragmentBinding;
import com.dap.fooneeds.databinding.HomeFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmptyCartFragment extends Fragment {

    private EmptyCartFragmentBinding fragmentBinding;
    private static EmptyCartFragment emptyCartFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private EmptyCartFragment() {

    }

    public static EmptyCartFragment newInstance() {
        if (emptyCartFragment == null) {
            emptyCartFragment = new EmptyCartFragment();
        }
        return emptyCartFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = EmptyCartFragmentBinding.inflate(inflater, container, false);
        fragmentBinding.btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        return fragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }
}
