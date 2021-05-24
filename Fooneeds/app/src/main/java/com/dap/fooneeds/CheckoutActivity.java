package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dap.fooneeds.databinding.ActivityCheckoutBinding;
import com.dap.fooneeds.fragment.EmptyCartFragment;
import com.dap.fooneeds.fragment.LoginFragment;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(binding.checkoutContainer.getId(), EmptyCartFragment.newInstance());
        fragmentTransaction.commit();
    }
}