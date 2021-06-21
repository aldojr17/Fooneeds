package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dap.fooneeds.databinding.ActivityLoginBinding;
import com.dap.fooneeds.databinding.ActivityProfileBinding;
import com.dap.fooneeds.entity.User;
import com.dap.fooneeds.fragment.CategoryFragment;
import com.dap.fooneeds.fragment.EditAddressFragment;
import com.dap.fooneeds.fragment.EditProfileFragment;
import com.dap.fooneeds.fragment.EditSpesificProfileFragment;
import com.dap.fooneeds.fragment.LoginFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(getIntent().getStringExtra(User.EDIT_USER) == null){
            fragmentTransaction.add(binding.profileContainer.getId(), EditAddressFragment.newInstance());
            fragmentTransaction.commit();
        }else {
            EditSpesificProfileFragment editSpesificProfileFragment = EditSpesificProfileFragment.newInstance();
            editSpesificProfileFragment.setArguments(getIntent().getExtras());

            fragmentTransaction.add(binding.profileContainer.getId(), editSpesificProfileFragment);
            fragmentTransaction.commit();
        }
    }
}