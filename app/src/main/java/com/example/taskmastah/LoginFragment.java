package com.example.taskmastah;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText email,passwd;
    private AppCompatButton login_btn;
    private TextView link_signup,forgot_passwd;

    private UserViewModel model;

    private AppCompatButton test_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login_fragment, container, false);
        email = v.findViewById(R.id.login_input_email);
        passwd = v.findViewById(R.id.login_input_password);
        login_btn = v.findViewById(R.id.btn_login);
        link_signup = v.findViewById(R.id.link_signup);
        forgot_passwd = v.findViewById(R.id.forgot_passwd);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        test_btn = v.findViewById(R.id.login_testbtn);
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FOR LAYOUT TESTING
        test_btn.setOnClickListener(v -> {
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_LoginFragment_to_HomeFragment);
        });
        /////////////////////////////////////////////////////////////////////

        login_btn.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter email!",
                        Toast.LENGTH_SHORT).show();
            }else if(passwd.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please enter password!",
                        Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(getActivity(), "All good :)",
                //        Toast.LENGTH_SHORT).show();
                signIn(email.getText().toString(),passwd.getText().toString());
            }
        });

        link_signup.setOnClickListener(v -> {
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_LoginFragment_to_SignupFragment);
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            model.setUser(mAuth.getCurrentUser());
                            Toast.makeText(getActivity(), "Authentication Success!.",
                                    Toast.LENGTH_SHORT).show();

                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_LoginFragment_to_HomeFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });

    }
}