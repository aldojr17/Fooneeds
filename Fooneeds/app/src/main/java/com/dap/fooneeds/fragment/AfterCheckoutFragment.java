package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dap.fooneeds.MainActivity;
import com.dap.fooneeds.databinding.AfterCheckoutFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AfterCheckoutFragment extends Fragment {

    private AfterCheckoutFragmentBinding binding;
    private static AfterCheckoutFragment afterCheckoutFragment;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private AfterCheckoutFragment(){}

    public static AfterCheckoutFragment newInstance(){
        if(afterCheckoutFragment == null){
            afterCheckoutFragment = new AfterCheckoutFragment();
        }
        return afterCheckoutFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/pay");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getValue(String.class).equals("cod")){
                    binding.thankYouLayout.setVisibility(View.VISIBLE);
                    binding.paymentLayout.setVisibility(View.INVISIBLE);
                }else{
                    binding.thankYouLayout.setVisibility(View.INVISIBLE);
                    binding.paymentLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AfterCheckoutFragmentBinding.inflate(inflater, container, false);
        binding.closeButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        return binding.getRoot();
    }
}
