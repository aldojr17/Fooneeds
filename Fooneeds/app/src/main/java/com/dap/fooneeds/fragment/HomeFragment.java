package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.CategoryActivity;
import com.dap.fooneeds.CheckoutActivity;
import com.dap.fooneeds.DetailActivity;
import com.dap.fooneeds.PopularActivity;
import com.dap.fooneeds.adapter.FoodAdapter;
import com.dap.fooneeds.databinding.HomeFragmentBinding;
import com.dap.fooneeds.entity.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeFragmentBinding fragmentBinding;
    private static HomeFragment homeFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Food> foods;
    private FoodAdapter foodAdapter;

    private HomeFragment() {

    }

    public static HomeFragment newInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foods = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("foods");
        fetchDataFood();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = HomeFragmentBinding.inflate(inflater, container, false);
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

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        fragmentBinding.rvMostPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        fragmentBinding.rvDailyDiscover.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvDailyDiscover2.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvMostPopular.setAdapter(foodAdapter);
        fragmentBinding.rvDailyDiscover.setAdapter(foodAdapter);
        fragmentBinding.rvDailyDiscover2.setAdapter(foodAdapter);
        fragmentBinding.rvDailyDiscover.setNestedScrollingEnabled(false);
        fragmentBinding.rvDailyDiscover2.setNestedScrollingEnabled(false);
        fragmentBinding.swipeRefresh.setOnRefreshListener(() -> {
            fetchDataFood();
            foodAdapter.notifyDataSetChanged();
            fragmentBinding.swipeRefresh.setRefreshing(false);
        });

        fragmentBinding.btnCart.setOnClickListener(v -> {
            updateUI(user);
        });

        fragmentBinding.btnSeeMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PopularActivity.class);
            startActivity(intent);
        });

        fragmentBinding.btnDogs.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CategoryActivity.class);
            intent.putExtra("cat", "Dog");
            startActivity(intent);
        });

        fragmentBinding.btnCats.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CategoryActivity.class);
            intent.putExtra("cat", "Cat");
            startActivity(intent);
        });
        return fragmentBinding.getRoot();
    }

    public void fetchDataFood(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
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
                                food.setCategory(ds.child("category").getValue(String.class));
                                food.setType(dataSnapshot.child("type").getValue(String.class));

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            startActivity(intent);
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
            bundle.putString("category", food.getCategory());
            bundle.putString("age", food.getAge());

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        fragmentBinding.rvMostPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        fragmentBinding.rvDailyDiscover.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvDailyDiscover2.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvMostPopular.setAdapter(foodAdapter);
        fragmentBinding.rvDailyDiscover.setAdapter(foodAdapter);
        fragmentBinding.rvDailyDiscover2.setAdapter(foodAdapter);
        fragmentBinding.rvDailyDiscover.setNestedScrollingEnabled(false);
        fragmentBinding.rvDailyDiscover2.setNestedScrollingEnabled(false);
    }
}
