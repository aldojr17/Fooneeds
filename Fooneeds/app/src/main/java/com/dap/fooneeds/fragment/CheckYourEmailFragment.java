package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dap.fooneeds.databinding.EmailsentFragmentBinding;
import com.dap.fooneeds.entity.User;

public class CheckYourEmailFragment extends Fragment {

    private EmailsentFragmentBinding emailsentFragmentBinding;
    private static CheckYourEmailFragment checkYourEmailFragment;

    private CheckYourEmailFragment() {

    }

    public static CheckYourEmailFragment newInstance() {
        if (checkYourEmailFragment == null) {
            checkYourEmailFragment = new CheckYourEmailFragment();
        }
        return checkYourEmailFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null && getArguments().containsKey(User.USER_EMAIL)){
            String email = getArguments().getString(User.USER_EMAIL);
            emailsentFragmentBinding.tvEmailSent.setText("Reset link has been sent to " + email);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        emailsentFragmentBinding = EmailsentFragmentBinding.inflate(inflater, container, false);
        return emailsentFragmentBinding.getRoot();
    }
}
