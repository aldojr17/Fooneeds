package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dap.fooneeds.LoginActivity;
import com.dap.fooneeds.MainActivity;
import com.dap.fooneeds.ProfileActivity;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.ProfileFragmentBinding;
import com.dap.fooneeds.databinding.SaveFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileFragmentBinding fragmentBinding;
    private static ProfileFragment profileFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = ProfileFragmentBinding.inflate(inflater, container, false);
        fragmentBinding.editProfile.setOnClickListener(v -> {
            updateUI(user);
        });
        fragmentBinding.btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        return fragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users/"+user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    System.out.println(snapshot.getValue());
                    fragmentBinding.txtName.setText(snapshot.child("name").getValue(String.class));
                    fragmentBinding.txtEmail.setText(snapshot.child("email").getValue(String.class));
                    fragmentBinding.txtPhoneNumber.setText(snapshot.child("phoneNumber").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
    }

//    private void collectPhoneNumbers(Map<String,Object> users) {
//
//        ArrayList<String> phoneNumbers = new ArrayList<>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : users.entrySet()){
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            System.out.println(singleUser.toString());
//        }
//    }
}
