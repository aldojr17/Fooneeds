package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dap.fooneeds.databinding.ActivityPopularBinding;
import com.dap.fooneeds.fragment.EditProfileFragment;
import com.dap.fooneeds.fragment.MostPopularFragment;

public class PopularActivity extends AppCompatActivity {

    private ActivityPopularBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopularBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(binding.popularContainer.getId(), MostPopularFragment.newInstance());
        fragmentTransaction.commit();
    }
}