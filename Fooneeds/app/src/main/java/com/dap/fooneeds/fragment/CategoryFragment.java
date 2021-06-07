package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.DetailActivity;
import com.dap.fooneeds.adapter.FoodAdapter;
import com.dap.fooneeds.databinding.PopularSearchFragmentBinding;
import com.dap.fooneeds.databinding.TabFragmentBinding;
import com.dap.fooneeds.entity.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private TabFragmentBinding fragmentBinding;
    private static CategoryFragment categoryFragment;
    private ArrayList<Food> foods;
    private FoodAdapter foodAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private CategoryFragment() {

    }

    public static CategoryFragment newInstance() {
        if (categoryFragment == null) {
            categoryFragment = new CategoryFragment();
        }
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foods = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("foods");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = TabFragmentBinding.inflate(inflater, container, false);

        fragmentBinding.btnLatest.setOnClickListener(v -> {
            fragmentBinding.btnLatest.setTextColor(Color.parseColor("#000000"));
            fragmentBinding.btnTopSales.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnPrice.setTextColor(Color.parseColor("#808080"));
        });
        fragmentBinding.btnTopSales.setOnClickListener(v -> {
            fragmentBinding.btnLatest.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnTopSales.setTextColor(Color.parseColor("#000000"));
            fragmentBinding.btnPrice.setTextColor(Color.parseColor("#808080"));
        });
        fragmentBinding.btnPrice.setOnClickListener(v -> {
            fragmentBinding.btnLatest.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnTopSales.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnPrice.setTextColor(Color.parseColor("#000000"));
        });

        foodAdapter = new FoodAdapter(foods, food -> {
            Bundle bundle = new Bundle();
            bundle.putString("cover", food.getCover());
            bundle.putString("name", food.getName());
            bundle.putString("type", food.getType());
            bundle.putString("desc", food.getDescription());
            bundle.putString("stock", String.valueOf(food.getStock()));
            bundle.putString("price", String.valueOf(food.getPrice()));
            bundle.putString("id", mAuth.getUid());
            bundle.putString("foodId", String.valueOf(food.getId()));
            bundle.putString("category", food.getCategory());
            bundle.putString("age", food.getAge());

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        fragmentBinding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvData2.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvData.setAdapter(foodAdapter);
        fragmentBinding.rvData2.setAdapter(foodAdapter);
        fragmentBinding.rvData.setNestedScrollingEnabled(false);
        fragmentBinding.rvData2.setNestedScrollingEnabled(false);
        return fragmentBinding.getRoot();
    }

    public void fetchDataFood(String cat){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("category").getValue().equals(cat)){
                        DatabaseReference keyReference = databaseReference.child(ds.getKey()).child("products");
                        keyReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Food food = new Food();
                                    food.setId(dataSnapshot.child("id").getValue(Integer.class));
                                    food.setAge(dataSnapshot.child("age").getValue(String.class));
                                    food.setCover(dataSnapshot.child("cover").getValue(String.class));
                                    food.setDescription(dataSnapshot.child("description").getValue(String.class));
                                    food.setName(dataSnapshot.child("name").getValue(String.class));
                                    food.setPrice(dataSnapshot.child("price").getValue(Integer.class));
                                    food.setStock(dataSnapshot.child("stock").getValue(Integer.class));
                                    food.setType(ds.child("category").getValue(String.class));

                                    foods.add(food);
                                }
                                refreshData(foods);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        if(getArguments() != null){
            String cat = getArguments().getString("cat");
            fetchDataFood(cat);
        }
    }

    private void refreshData(ArrayList<Food> foodArrayList){
        foodAdapter = new FoodAdapter(foodArrayList, food -> {
            Bundle bundle = new Bundle();
            bundle.putString("cover", food.getCover());
            bundle.putString("name", food.getName());
            bundle.putString("type", food.getType());
            bundle.putString("desc", food.getDescription());
            bundle.putString("stock", String.valueOf(food.getStock()));
            bundle.putString("price", String.valueOf(food.getPrice()));
            bundle.putString("id", mAuth.getUid());
            bundle.putString("foodId", String.valueOf(food.getId()));
            bundle.putString("age", food.getAge());

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        fragmentBinding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvData2.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvData.setAdapter(foodAdapter);
        fragmentBinding.rvData2.setAdapter(foodAdapter);
        fragmentBinding.rvData.setNestedScrollingEnabled(false);
        fragmentBinding.rvData2.setNestedScrollingEnabled(false);
    }
}
