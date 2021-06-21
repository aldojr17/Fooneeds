package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.R;
import com.dap.fooneeds.adapter.ArticleAdapter;
import com.dap.fooneeds.databinding.ArticleFragmentBinding;
import com.dap.fooneeds.databinding.EditSpesificProfileFragmentBinding;
import com.dap.fooneeds.entity.Address;
import com.dap.fooneeds.entity.Article;
import com.dap.fooneeds.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditSpesificProfileFragment extends Fragment {

    private EditSpesificProfileFragmentBinding fragmentBinding;
    private static EditSpesificProfileFragment spesificProfileFragment;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String gen;

    private EditSpesificProfileFragment() {

    }

    public static EditSpesificProfileFragment newInstance() {
        if (spesificProfileFragment == null) {
            spesificProfileFragment = new EditSpesificProfileFragment();
        }
        return spesificProfileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = EditSpesificProfileFragmentBinding.inflate(inflater, container, false);
        fragmentBinding.spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gen = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return fragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        clear();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    switch (getArguments().getString(User.EDIT_USER)){
                        case "phone":
                            fragmentBinding.etEditSpesific1.setText(snapshot.child("phoneNumber").getValue(String.class));
                            break;
                        case "name":
                            fragmentBinding.etEditSpesific1.setText(snapshot.child("name").getValue(String.class));
                            break;
                        case "email":
                            fragmentBinding.etEditSpesific1.setText(snapshot.child("email").getValue(String.class));
                            break;
                        case "gender":
                            fragmentBinding.spinGender.setSelection(snapshot.child("gender").equals("Male") ? 0 : 1);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("users");
        if(getArguments() != null){
            User u = getArguments().getParcelable(User.USER_DATA);
            switch (getArguments().getString(User.EDIT_USER)){
                case "address":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.new_address));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_street));
                    fragmentBinding.spinGender.setVisibility(View.GONE);
                    fragmentBinding.btnConfirm.setOnClickListener(v -> {
                        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        if(fragmentBinding.etEditSpesific1.getText().toString().isEmpty() || fragmentBinding.etEditSpesific2.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }else{
                            Address address = new Address();
                            address.setStreet(fragmentBinding.etEditSpesific1.getText().toString());
                            address.setCity(fragmentBinding.etEditSpesific2.getText().toString());
                            address.setSelected("No");
                            reference.child(user.getUid()).child("addresses").push().setValue(address);
                            Toast.makeText(getContext(), "Address Added", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                            getActivity().onBackPressed();
                        }
                    });
                    break;
                case "name":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.edit_name));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_edit_name));
                    fragmentBinding.tvEdit2.setVisibility(View.GONE);
                    fragmentBinding.etEditSpesific2.setVisibility(View.GONE);
                    fragmentBinding.spinGender.setVisibility(View.GONE);
                    fragmentBinding.btnConfirm.setOnClickListener(v -> {
                        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        if(fragmentBinding.etEditSpesific1.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }else{
                            reference.child(user.getUid()).child("name").setValue(fragmentBinding.etEditSpesific1.getText().toString());
                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                            getActivity().onBackPressed();

                        }
                    });
                    break;
                case "email":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.edit_email));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_edit_email));
                    fragmentBinding.tvEdit2.setVisibility(View.GONE);
                    fragmentBinding.etEditSpesific2.setVisibility(View.GONE);
                    fragmentBinding.spinGender.setVisibility(View.GONE);
                    fragmentBinding.btnConfirm.setOnClickListener(v -> {
                        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        if(fragmentBinding.etEditSpesific1.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }else {
                            AuthCredential credential = EmailAuthProvider.getCredential(u.getEmail(), u.getPassword()); // Current Login Credentials

                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.updateEmail(fragmentBinding.etEditSpesific1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                reference.child(user.getUid()).child("email").setValue(fragmentBinding.etEditSpesific1.getText().toString());
                                                Toast.makeText(getContext(), "Email Changed" + ", Current Email is " + fragmentBinding.etEditSpesific1.getText().toString(), Toast.LENGTH_LONG).show();
                                                getActivity().onBackPressed();
                                            }
                                        }
                                    });
                                }
                            });
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }
                    });
                    break;
                case "phone":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.edit_phone_number));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_edit_phone_number));
                    fragmentBinding.tvEdit2.setVisibility(View.GONE);
                    fragmentBinding.etEditSpesific2.setVisibility(View.GONE);
                    fragmentBinding.spinGender.setVisibility(View.GONE);
                    fragmentBinding.btnConfirm.setOnClickListener(v -> {
                        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        if(fragmentBinding.etEditSpesific1.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }else{
                            reference.child(user.getUid()).child("phoneNumber").setValue(fragmentBinding.etEditSpesific1.getText().toString());
                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                            getActivity().onBackPressed();
                        }
                    });
                    break;
                case "gender":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.edit_gender));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_edit_gender));
                    fragmentBinding.etEditSpesific1.setVisibility(View.GONE);
                    fragmentBinding.tvEdit2.setVisibility(View.GONE);
                    fragmentBinding.etEditSpesific2.setVisibility(View.GONE);
                    fragmentBinding.spinGender.setVisibility(View.VISIBLE);
                    fragmentBinding.btnConfirm.setOnClickListener(v -> {
                        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        reference.child(user.getUid()).child("gender").setValue(gen);
                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        fragmentBinding.progressBar.setVisibility(View.GONE);
                        fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        getActivity().onBackPressed();
                    });
                    break;
                case "password":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.change_password));
                    fragmentBinding.editLayout.setVisibility(View.GONE);
                    fragmentBinding.changePasswordLayout.setVisibility(View.VISIBLE);
                    fragmentBinding.btnForgotPassword.setOnClickListener(v -> {
                        ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.profileContainer, forgotPasswordFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
                    fragmentBinding.btnChangePassword.setOnClickListener(v -> {
                        fragmentBinding.progressBar2.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        if(fragmentBinding.etCurrentPassword.getText().toString().isEmpty() || fragmentBinding.etNewPassword.getText().toString().isEmpty() || fragmentBinding.etConfirmPassword.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar2.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }else {
                            if(!fragmentBinding.etCurrentPassword.getText().toString().trim().equals(u.getPassword())){
                                Toast.makeText(getContext(), "Wrong Current Password", Toast.LENGTH_SHORT).show();
                                fragmentBinding.progressBar2.setVisibility(View.GONE);
                                fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                            }else if(!fragmentBinding.etNewPassword.getText().toString().trim().equals(fragmentBinding.etConfirmPassword.getText().toString().trim())){
                                Toast.makeText(getContext(), "Password and Confirm Password Don't Match", Toast.LENGTH_SHORT).show();
                                fragmentBinding.progressBar2.setVisibility(View.GONE);
                                fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                            }else{
                                AuthCredential credential = EmailAuthProvider.getCredential(u.getEmail(), u.getPassword()); // Current Login Credentials

                                // Prompt the user to re-provide their sign-in credentials
                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Now change your email address \\
                                        //----------------Code for Changing Email Address----------\\
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user.updatePassword(fragmentBinding.etNewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    reference.child(user.getUid()).child("password").setValue(fragmentBinding.etNewPassword.getText().toString());
                                                    Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_LONG).show();
                                                    getActivity().onBackPressed();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                            fragmentBinding.progressBar2.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }
                    });
            }
        }
    }

    private void clear() {
        fragmentBinding.etNewPassword.setText("");
        fragmentBinding.etCurrentPassword.setText("");
        fragmentBinding.etConfirmPassword.setText("");
        fragmentBinding.etEditSpesific1.setText("");
        fragmentBinding.etEditSpesific2.setText("");
    }
}
