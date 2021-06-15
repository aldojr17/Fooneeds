package com.dap.fooneeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dap.fooneeds.databinding.ActivityCheckoutBinding;
import com.dap.fooneeds.fragment.CartCheckoutFragment;
import com.dap.fooneeds.fragment.EmptyCartFragment;
import com.dap.fooneeds.fragment.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users/" + mAuth.getCurrentUser().getUid());


        reference.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    fragment = CartCheckoutFragment.newInstance();
                }else{
                    fragment = EmptyCartFragment.newInstance();
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(binding.checkoutContainer.getId(), fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}