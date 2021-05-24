package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.EditprofileFragmentBinding;
import com.dap.fooneeds.databinding.ProfileFragmentBinding;
import com.dap.fooneeds.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {

    private EditprofileFragmentBinding fragmentBinding;
    private static EditProfileFragment editProfileFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User u;

    private EditProfileFragment() {

    }

    public static EditProfileFragment newInstance() {
        if (editProfileFragment == null) {
            editProfileFragment = new EditProfileFragment();
        }
        return editProfileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = EditprofileFragmentBinding.inflate(inflater, container, false);
        fragmentBinding.btnEditName.setOnClickListener(v -> {
            updateUI("name");
        });
        fragmentBinding.btnEditEmail.setOnClickListener(v -> {
            updateUI("email");
        });
        fragmentBinding.btnEditPhone.setOnClickListener(v -> {
            updateUI("phone");
        });
//        fragmentBinding.btnEditAddress.setOnClickListener(v -> {
//            updateUI("address");
//        });
        fragmentBinding.btnEditGender.setOnClickListener(v -> {
            updateUI("gender");
        });

        fragmentBinding.btnChangePass.setOnClickListener(v -> {
            updateUI("password");
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

    @Override
    public void onStart() {
        super.onStart();
        u = new User();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users/"+user.getUid());
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
}
