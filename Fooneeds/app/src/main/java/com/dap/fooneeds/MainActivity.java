package com.dap.fooneeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.dap.fooneeds.databinding.ActivityMainBinding;
import com.dap.fooneeds.databinding.HomeFragmentBinding;
import com.dap.fooneeds.fragment.ArticleFragment;
import com.dap.fooneeds.fragment.ForgotPasswordFragment;
import com.dap.fooneeds.fragment.HomeFragment;
import com.dap.fooneeds.fragment.LoginFragment;
import com.dap.fooneeds.fragment.ProfileFragment;
import com.dap.fooneeds.fragment.SavedFragment;
import com.dap.fooneeds.fragment.SignUpFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(binding.container.getId(), HomeFragment.newInstance()).commit();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.article:
                        fragment = ArticleFragment.newInstance();
                        break;
                    case R.id.favourites:
                        fragment = SavedFragment.newInstance();
                        break;
                    case R.id.profile:
                        fragment = ProfileFragment.newInstance();
                        break;
                    case R.id.homee:
                        fragment = HomeFragment.newInstance();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.container.getId(), fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Use Back Button On Top", Toast.LENGTH_SHORT).show();
    }
}