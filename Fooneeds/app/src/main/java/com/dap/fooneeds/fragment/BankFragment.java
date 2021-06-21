package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.BanktransferFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BankFragment extends Fragment {

    private BanktransferFragmentBinding binding;
    private static BankFragment bankFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String bank;
    private DatabaseReference reference;

    private BankFragment (){}

    public static BankFragment newInstance(){
        if(bankFragment == null){
            bankFragment = new BankFragment();
        }
        return bankFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BanktransferFragmentBinding.inflate(inflater, container, false);
        binding.btnBCA.setOnClickListener(v -> {
            binding.btnBCA.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
            binding.btnBNI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBRI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnMandiri.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnOCBC.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            bank = "bca";
        });
        binding.btnBNI.setOnClickListener(v -> {
            binding.btnBNI.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
            binding.btnBCA.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBRI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnMandiri.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnOCBC.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            bank = "bni";
        });
        binding.btnBRI.setOnClickListener(v -> {
            binding.btnBRI.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
            binding.btnBNI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBCA.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnMandiri.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnOCBC.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            bank = "bri";
        });
        binding.btnMandiri.setOnClickListener(v -> {
            binding.btnMandiri.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
            binding.btnBNI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBRI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBCA.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnOCBC.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            bank = "mandiri";
        });
        binding.btnOCBC.setOnClickListener(v -> {
            binding.btnOCBC.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
            binding.btnBNI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBRI.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnMandiri.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.btnBCA.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            bank = "ocbc";
        });
        binding.btnConfirmBank.setOnClickListener(v -> {
            reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/pay");
            reference.setValue(bank);
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/pay");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    switch (snapshot.getValue(String.class)){
                        case "bca":
                            binding.btnBCA.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
                            break;
                        case "bni":
                            binding.btnBNI.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
                            break;
                        case "bri":
                            binding.btnBRI.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
                            break;
                        case "mandiri":
                            binding.btnMandiri.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
                            break;
                        case "ocbc":
                            binding.btnOCBC.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.checked), null);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
