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

import com.dap.fooneeds.MainActivity;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.ForgotpasswordFragmentBinding;
import com.dap.fooneeds.databinding.SignupFragmentBinding;
import com.dap.fooneeds.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class SignUpFragment extends Fragment {

    private SignupFragmentBinding signupFragmentBinding;
    private static SignUpFragment signUpFragment;
    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SignUpFragment() {

    }

    public static SignUpFragment newInstance() {
        if (signUpFragment == null) {
            signUpFragment = new SignUpFragment();
        }
        return signUpFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        signupFragmentBinding.btnSignUp.setOnClickListener(v -> {
            signupFragmentBinding.btnSignUp.setVisibility(View.GONE);
            signupFragmentBinding.loadingBar2.setVisibility(View.VISIBLE);
            String name = signupFragmentBinding.etNameSignUp.getText().toString();
            String email = signupFragmentBinding.etEmailSignUp.getText().toString();
            String password = signupFragmentBinding.etPasswordSignUp.getText().toString();
            String confirmPassword = signupFragmentBinding.etConfirmPasswordSignUp.getText().toString();
            if(email.trim().isEmpty() || password.trim().isEmpty() || name.trim().isEmpty() || confirmPassword.trim().isEmpty()){
                signupFragmentBinding.btnSignUp.setVisibility(View.VISIBLE);
                signupFragmentBinding.loadingBar2.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            }else{
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(command -> {
                    if(command.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User user = new User();
                        user.setId(firebaseUser.getUid());
                        user.setName(name);
                        user.setEmail(email);
                        user.setPassword(password);
                        databaseReference.child(firebaseUser.getUid()).setValue(user);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(command2 -> {
                            if(command2.isSuccessful()){
                                FirebaseUser firebaseUser2 = mAuth.getCurrentUser();
                                updateUI(firebaseUser2);
                            }else{
                                signupFragmentBinding.btnSignUp.setVisibility(View.VISIBLE);
                                signupFragmentBinding.loadingBar2.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        signupFragmentBinding.btnSignUp.setVisibility(View.VISIBLE);
                        signupFragmentBinding.loadingBar2.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        signupFragmentBinding = SignupFragmentBinding.inflate(inflater, container, false);
        return signupFragmentBinding.getRoot();
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }
}
