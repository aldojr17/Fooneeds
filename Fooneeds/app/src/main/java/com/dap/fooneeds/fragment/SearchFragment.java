package com.dap.fooneeds.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.R;
import com.dap.fooneeds.adapter.ProductAdapter;
import com.dap.fooneeds.databinding.SearchFragmentBinding;
import com.dap.fooneeds.entity.Food;
import com.dap.fooneeds.entity.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchFragmentBinding binding;
    private static SearchFragment searchFragment;
    private ArrayList<Product> products;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProductAdapter productAdapter;
    private DatabaseReference databaseReference;

    private SearchFragment(){}

    public static SearchFragment newInstance(){
        if(searchFragment == null){
            searchFragment = new SearchFragment();
        }
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");
        fetchPopularData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragmentBinding.inflate(inflater, container, false);
        productAdapter = new ProductAdapter(products, product -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", product.getId());
            MostPopularFragment mostPopularFragment = MostPopularFragment.newInstance();
            mostPopularFragment.setArguments(bundle);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.searchContainer, mostPopularFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        binding.rvPopProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvPopProduct.setAdapter(productAdapter);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.etSearch.getText().toString().isEmpty()){
                    binding.btnCancel.setText("Cancel");
                    binding.btnCancel.setTextColor(Color.parseColor("#ff0000"));
                }else{
                    binding.btnCancel.setText("Search");
                    binding.btnCancel.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.etSearch.addTextChangedListener(textWatcher);
        binding.btnCancel.setOnClickListener(v -> {
            if(binding.btnCancel.getText().toString().equals("Cancel")){
                getActivity().onBackPressed();
            }else if(binding.btnCancel.getText().toString().equals("Search")){
                Bundle bundle = new Bundle();
                bundle.putString("name", binding.etSearch.getText().toString().trim());
                MostPopularFragment mostPopularFragment = MostPopularFragment.newInstance();
                mostPopularFragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.searchContainer, mostPopularFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.etSearch.setText("");
        user = mAuth.getCurrentUser();
    }

    private void fetchPopularData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Product product = new Product();
                    product.setId(childSnapshot.getKey());
                    product.setName(childSnapshot.child("name").getValue(String.class));
                    products.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
