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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.R;
import com.dap.fooneeds.adapter.AddressAdapter;
import com.dap.fooneeds.databinding.CartFragmentBinding;
import com.dap.fooneeds.databinding.EditaddressFragmentBinding;
import com.dap.fooneeds.entity.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    private CartFragmentBinding binding;
    private static CheckoutFragment checkoutFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference dbreference;
    private ArrayList<Address> addresses;
    private AddressAdapter addressAdapter;

    private CheckoutFragment(){}

    public static CheckoutFragment newInstance(){
        if(checkoutFragment == null){
            checkoutFragment = new CheckoutFragment();
        }
        return checkoutFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        fetchAddress();
        if(getArguments() != null){
            binding.tvSubtotal.setText(getArguments().getString("subtotal"));
            binding.tvShipping.setText("Rp.10.000");
            binding.tvTotal.setText(getArguments().getString("total"));
        }
        dbreference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/pay");
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && !snapshot.getValue(String.class).equals("cod")){
                    binding.btnCOD.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cod), null, null, null);
                    binding.btnBankTransfer.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.atm), null, getResources().getDrawable(R.drawable.checked), null);
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
        addresses = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater, container, false);
        addressAdapter = new AddressAdapter(addresses, new AddressAdapter.AddressItemListener() {
            @Override
            public void itemClicked(int position) {
                reference.child("address").setValue(addresses.get(position).getStreet());
                reference.child("addresses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("street").getValue(String.class).equals(addresses.get(position).getStreet())){
                                reference.child("addresses").child(dataSnapshot.getKey()).child("selected").setValue("Yes");
                                addresses.get(position).setSelected("Yes");
                                addressAdapter.notifyDataSetChanged();
                            }else{
                                reference.child("addresses").child(dataSnapshot.getKey()).child("selected").setValue("No");
                                addresses.get(position).setSelected("No");
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                        fetchAddress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getContext(), "Address Changed", Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvShipping.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvShipping.setAdapter(addressAdapter);
        binding.btnBankTransfer.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.checkoutContainer, BankFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        binding.btnCOD.setOnClickListener(v -> {
            dbreference.setValue("cod");
            binding.btnCOD.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cod), null, getResources().getDrawable(R.drawable.checked), null);
            binding.btnBankTransfer.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.atm), null, getResources().getDrawable(R.drawable.arrow_right2), null);
        });
        binding.btnCheckout.setOnClickListener(v -> {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.child("cart").getRef().removeValue();
                    snapshot.child("pay").getRef().removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.checkoutContainer, AfterCheckoutFragment.newInstance());
            transaction.commit();
        });
        return binding.getRoot();
    }

    private void fetchAddress(){
        addresses.clear();
        reference.child("addresses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    addresses.add(dataSnapshot.getValue(Address.class));
                }
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
