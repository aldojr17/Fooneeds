package com.dap.fooneeds.fragment;

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
import com.dap.fooneeds.databinding.EditaddressFragmentBinding;
import com.dap.fooneeds.entity.Address;
import com.dap.fooneeds.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditAddressFragment extends Fragment {

    private EditaddressFragmentBinding fragmentBinding;
    private static EditAddressFragment addressFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference databaseReference;
    private ArrayList<Address> addresses;
    private AddressAdapter addressAdapter;
    private User u;

    private EditAddressFragment(){}

    public static EditAddressFragment newInstance(){
        if(addressFragment == null){
            addressFragment = new EditAddressFragment();
        }
        return addressFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        fetchAddress();
        u = new User();
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    u.setName(snapshot.child("name").getValue(String.class));
                    u.setEmail(snapshot.child("email").getValue(String.class));
                    u.setPassword(snapshot.child("password").getValue(String.class));
                    u.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
                    u.setGender(snapshot.child("gender").getValue(String.class));
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
        fragmentBinding = EditaddressFragmentBinding.inflate(inflater, container, false);
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
        fragmentBinding.rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvAddress.setAdapter(addressAdapter);
        fragmentBinding.btnAddAddress.setOnClickListener(v -> {
            updateUI("address");
        });
        return fragmentBinding.getRoot();
    }

    private void updateUI(String type){
        Bundle bundle = new Bundle();
        bundle.putString(User.EDIT_USER, type);
        bundle.putParcelable(User.USER_DATA, u);
        EditSpesificProfileFragment spesificProfileFragment = EditSpesificProfileFragment.newInstance();
        spesificProfileFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profileContainer, spesificProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
