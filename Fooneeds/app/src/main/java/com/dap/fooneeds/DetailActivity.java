package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && !bundle.isEmpty()){
            Glide.with(binding.getRoot()).load(bundle.getString("cover")).into(binding.imgFood);
            binding.tvFoodName.setText(bundle.getString("name"));
            binding.tvFoodType.setText(bundle.getString("type"));
            binding.tvDesc.setText(bundle.getString("desc"));
            binding.tvStock.setText("Stock : " + bundle.getString("stock") + " pcs");
            binding.tvPrice.setText("Rp." + bundle.getString("price"));
        }
    }
}