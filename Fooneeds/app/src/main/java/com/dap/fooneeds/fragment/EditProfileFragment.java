package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dap.fooneeds.CategoryActivity;
import com.dap.fooneeds.LoginActivity;
import com.dap.fooneeds.MainActivity;
import com.dap.fooneeds.ProfileActivity;
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
    public static final int PICK_IMAGE = 1;

    private EditProfileFragment() {

    }

    public static EditProfileFragment newInstance() {
        if (editProfileFragment == null) {
            editProfileFragment = new EditProfileFragment();
        }
        return editProfileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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
        fragmentBinding.btnEditAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        fragmentBinding.btnEditGender.setOnClickListener(v -> {
            updateUI("gender");
        });

        fragmentBinding.btnChangePass.setOnClickListener(v -> {
            updateUI("password");
        });

        fragmentBinding.btnChangeProfilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PICK_IMAGE){
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                fragmentBinding.profilePhoto.setImageURI(selectedImageUri);
            }
        }
    }

    private void updateUI(String type){
        Bundle bundle = new Bundle();
        bundle.putString(User.EDIT_USER, type);
        bundle.putParcelable(User.USER_DATA, u);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        u = new User();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        reference.child("fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fragmentBinding.txtCountSaved.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
