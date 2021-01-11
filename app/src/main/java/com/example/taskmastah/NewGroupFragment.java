package com.example.taskmastah;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGroupFragment extends Fragment {

    private AppCompatButton btn_showall,btn_add,btn_create;
    private EditText group_name,email;

    private UserViewModel model;

    private FirebaseDatabase database; //Realtime database
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;   //Firestore
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.new_group_fragment, container, false);

        btn_showall = v.findViewById(R.id.new_group_btn_showall);
        btn_add = v.findViewById(R.id.new_group_btn_add);
        btn_create = v.findViewById(R.id.new_group_btn_create);

        group_name = v.findViewById(R.id.new_group_name);
        email = v.findViewById(R.id.new_group_input_email);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        //db = FirebaseFirestore.getInstance();

        //database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_showall.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "Show All clicked",
                    Toast.LENGTH_SHORT).show();
        });

        btn_add.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "Add clicked.",
                    Toast.LENGTH_SHORT).show();
        });

        btn_create.setOnClickListener(view1 -> {
            if(group_name.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please enter a name for group.",
                        Toast.LENGTH_SHORT).show();
            }else {

                //MAKES NEW GROUP AND PUTS IT INSIDE GROUPS NODE
                //Group grp = new Group(group_name.getText().toString());
                //mDatabase.child("groups").child(grp.getName()).setValue(grp);

                mDatabase.child("groups").child(group_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(getActivity(), "Name has already been taken please change name.",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            model.getUser().observe(getViewLifecycleOwner(), user -> {
                                Group grp = new Group(group_name.getText().toString(), user.getEmail());
                                mDatabase.child("groups").child(grp.getName()).setValue(grp);

                                mDatabase.child("users").child(user.getEmail().replace(".", ",")).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User usr = snapshot.getValue(User.class);

                                        Log.d("Usr email:", usr.getEmail());
                                        if(usr.getGroups() == null){
                                            ArrayList<Group> groups = new ArrayList<>();
                                            groups.add(grp);
                                            usr.setGroups(groups);
                                            mDatabase.child("users").child(usr.getEncodedEmail()).setValue(usr);
                                        }else {
                                            ArrayList<Group> groups = usr.getGroups();
                                            groups.add(grp);
                                            usr.setGroups(groups);
                                            mDatabase.child("users").child(usr.getEncodedEmail()).setValue(usr);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            });
                            NavHostFragment.findNavController(NewGroupFragment.this)
                                    .navigate(R.id.action_newGroupFragment_to_HomeFragment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });

    }

}