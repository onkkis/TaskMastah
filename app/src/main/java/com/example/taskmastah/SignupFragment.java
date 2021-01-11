package com.example.taskmastah;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText email,passwd;
    private AppCompatButton signup_btn;
    private TextView link_login;

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        email = v.findViewById(R.id.signup_input_email);
        passwd = v.findViewById(R.id.signup_input_password);
        signup_btn = v.findViewById(R.id.btn_signup);
        link_login = v.findViewById(R.id.link_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup_btn.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter email!",
                        Toast.LENGTH_SHORT).show();
            }else if(passwd.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please enter password!",
                        Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(getActivity(), "All good :)",
                //        Toast.LENGTH_SHORT).show();
                signUp(email.getText().toString(),passwd.getText().toString());
            }
        });

        link_login.setOnClickListener(v -> {
            NavHostFragment.findNavController(SignupFragment.this)
                    .navigate(R.id.action_signupFragment_to_LoginFragment);
        });
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);

                            Toast.makeText(getActivity(), "Registration successful!.",
                                    Toast.LENGTH_SHORT).show();

                            User usr = new User(user.getEmail());
                            mDatabase.child("users").child(usr.getEncodedEmail()).setValue(usr);

                            NavHostFragment.findNavController(SignupFragment.this)
                                    .navigate(R.id.action_signupFragment_to_LoginFragment);

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Registration failed",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }
}