package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dap.fooneeds.databinding.ActivityCategoryBinding;
import com.dap.fooneeds.fragment.CategoryFragment;
import com.dap.fooneeds.fragment.MostPopularFragment;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = new Bundle();
        bundle.putString("cat", getIntent().getStringExtra("cat"));
        CategoryFragment categoryFragment = CategoryFragment.newInstance();
        categoryFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(binding.categoryContainer.getId(), categoryFragment);
        fragmentTransaction.commit();
    }
}