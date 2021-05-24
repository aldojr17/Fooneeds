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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.R;
import com.dap.fooneeds.adapter.ArticleAdapter;
import com.dap.fooneeds.databinding.ArticleFragmentBinding;
import com.dap.fooneeds.databinding.EditSpesificProfileFragmentBinding;
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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        if(getArguments() != null){
            User u = getArguments().getParcelable(User.USER_DATA);
            switch (getArguments().getString(User.EDIT_USER)){
                case "name":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.edit_name));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_edit_name));
                    fragmentBinding.etEditSpesific1.setText(u.getName());
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
                        }
                    });
                    break;
                case "email":
                    fragmentBinding.tvEditSpesific.setText(getResources().getString(R.string.edit_email));
                    fragmentBinding.tvEdit.setText(getResources().getString(R.string.text_edit_email));
                    fragmentBinding.etEditSpesific1.setText(u.getEmail());
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
                    fragmentBinding.etEditSpesific1.setText(u.getPhoneNumber());
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
                    if(u.getGender() != null){
                        fragmentBinding.spinGender.setSelection(u.getGender().equals("Male") ? 0 : 1);
                    }
                    fragmentBinding.btnConfirm.setOnClickListener(v -> {
                        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        reference.child(user.getUid()).child("gender").setValue(gen);
                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        fragmentBinding.progressBar.setVisibility(View.GONE);
                        fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                    });
                    break;
                case "password":
                    fragmentBinding.editLayout.setVisibility(View.GONE);
                    fragmentBinding.changePasswordLayout.setVisibility(View.VISIBLE);
                    fragmentBinding.btnChangePassword.setOnClickListener(v -> {
                        fragmentBinding.progressBar2.setVisibility(View.VISIBLE);
                        fragmentBinding.btnConfirm.setVisibility(View.GONE);
                        if(fragmentBinding.etCurrentPassword.getText().toString().isEmpty() || fragmentBinding.etNewPassword.getText().toString().isEmpty() || fragmentBinding.etConfirmPassword.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                            fragmentBinding.progressBar2.setVisibility(View.GONE);
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
                                    user.updatePassword(fragmentBinding.etNewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                reference.child(user.getUid()).child("password").setValue(fragmentBinding.etNewPassword.getText().toString());
                                                Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                            fragmentBinding.progressBar2.setVisibility(View.GONE);
                            fragmentBinding.btnConfirm.setVisibility(View.VISIBLE);
                        }
                    });
            }
        }
        fragmentBinding.btnBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });
    }
}
