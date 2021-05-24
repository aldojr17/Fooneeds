package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dap.fooneeds.databinding.ActivityLoginBinding;
import com.dap.fooneeds.databinding.ActivityProfileBinding;
import com.dap.fooneeds.fragment.EditProfileFragment;
import com.dap.fooneeds.fragment.LoginFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(binding.profileContainer.getId(), EditProfileFragment.newInstance());
        fragmentTransaction.commit();
    }
}