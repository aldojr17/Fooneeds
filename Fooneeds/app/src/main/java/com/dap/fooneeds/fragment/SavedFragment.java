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
import com.dap.fooneeds.adapter.SavedAdapter;
import com.dap.fooneeds.databinding.HomeFragmentBinding;
import com.dap.fooneeds.databinding.SaveFragmentBinding;
import com.dap.fooneeds.entity.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private SaveFragmentBinding fragmentBinding;
    private static SavedFragment savedFragment;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<Food> foods;
    private SavedAdapter savedAdapter;

    private SavedFragment() {

    }

    public static SavedFragment newInstance() {
        if (savedFragment == null) {
            savedFragment = new SavedFragment();
        }
        return savedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foods = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/fav");
        fetchSavedData();
    }

    private void fetchSavedData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Food item = new Food();
                    item.setName(childSnapshot.child("name").getValue(String.class));
                    item.setCover(childSnapshot.child("cover").getValue(String.class));
                    item.setCategory(childSnapshot.child("category").getValue(String.class));
                    item.setType(childSnapshot.child("type").getValue(String.class));
                    item.setAge(childSnapshot.child("age").getValue(String.class));
                    item.setId(childSnapshot.child("id").getValue(Integer.class));
                    item.setPrice(childSnapshot.child("price").getValue(Integer.class));
                    foods.add(item);
                }
                savedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = SaveFragmentBinding.inflate(inflater, container, false);
        savedAdapter = new SavedAdapter(foods);
        fragmentBinding.rvSave.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvSave.setAdapter(savedAdapter);
        fragmentBinding.btnAllItems.setOnClickListener(v -> {
            fragmentBinding.btnAllItems.setTextColor(Color.parseColor("#000000"));
            fragmentBinding.btnCatsTab.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnDogsTab.setTextColor(Color.parseColor("#808080"));
            foods.clear();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childSnapshot : snapshot.getChildren()){
                        Food item = new Food();
                        item.setName(childSnapshot.child("name").getValue(String.class));
                        item.setCover(childSnapshot.child("cover").getValue(String.class));
                        item.setCategory(childSnapshot.child("category").getValue(String.class));
                        item.setType(childSnapshot.child("type").getValue(String.class));
                        item.setAge(childSnapshot.child("age").getValue(String.class));
                        item.setId(childSnapshot.child("id").getValue(Integer.class));
                        item.setPrice(childSnapshot.child("price").getValue(Integer.class));
                        foods.add(item);
                    }
                    savedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        fragmentBinding.btnCatsTab.setOnClickListener(v -> {
            fragmentBinding.btnAllItems.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnCatsTab.setTextColor(Color.parseColor("#000000"));
            fragmentBinding.btnDogsTab.setTextColor(Color.parseColor("#808080"));
            foods.clear();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childSnapshot : snapshot.getChildren()){
                        if(childSnapshot.child("category").getValue(String.class).equals("Cat")){
                            Food item = new Food();
                            item.setName(childSnapshot.child("name").getValue(String.class));
                            item.setCover(childSnapshot.child("cover").getValue(String.class));
                            item.setCategory(childSnapshot.child("category").getValue(String.class));
                            item.setType(childSnapshot.child("type").getValue(String.class));
                            item.setAge(childSnapshot.child("age").getValue(String.class));
                            item.setId(childSnapshot.child("id").getValue(Integer.class));
                            item.setPrice(childSnapshot.child("price").getValue(Integer.class));
                            foods.add(item);
                        }
                    }
                    savedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        fragmentBinding.btnDogsTab.setOnClickListener(v -> {
            fragmentBinding.btnAllItems.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnCatsTab.setTextColor(Color.parseColor("#808080"));
            fragmentBinding.btnDogsTab.setTextColor(Color.parseColor("#000000"));
            foods.clear();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childSnapshot : snapshot.getChildren()){
                        if(childSnapshot.child("category").getValue(String.class).equals("Dog")){
                            Food item = new Food();
                            item.setName(childSnapshot.child("name").getValue(String.class));
                            item.setCover(childSnapshot.child("cover").getValue(String.class));
                            item.setCategory(childSnapshot.child("category").getValue(String.class));
                            item.setType(childSnapshot.child("type").getValue(String.class));
                            item.setAge(childSnapshot.child("age").getValue(String.class));
                            item.setId(childSnapshot.child("id").getValue(Integer.class));
                            item.setPrice(childSnapshot.child("price").getValue(Integer.class));
                            foods.add(item);
                        }
                    }
                    savedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        return fragmentBinding.getRoot();
    }

}
