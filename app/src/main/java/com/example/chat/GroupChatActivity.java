package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.Adapter.GroupsAdapter;
import com.example.chat.Adapter.MessageAdapter;
import com.example.chat.Model.Chat;
import com.example.chat.Model.GroupMessage;
import com.example.chat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    TextView groupName;
    private String currentGroupName;
    ImageButton sendButton;
    EditText editMessageText;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    List<GroupMessage> mGroupMessage;

    GroupsAdapter groupsAdapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        sendButton= findViewById(R.id.btn_send_group_message);
        editMessageText= findViewById(R.id.group_text_send);
        currentGroupName= getIntent().getExtras().get("groupName").toString();

        recyclerView= findViewById(R.id.group_recycler_view);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupName= findViewById(R.id.chat_name);
        groupName.setText(currentGroupName);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= editMessageText.getText().toString();
                if(!message.equals("")){
                    sendGroupMessage(firebaseUser.getUid(),  currentGroupName, message);
                }
                editMessageText.setText("");
            }
        });

      readMessage(currentGroupName);


    }


    public void sendGroupMessage(String sender, String groupName, String message){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        HashMap<String, String> hashMap= new HashMap<>();
        hashMap.put("groupName", groupName);
        hashMap.put("sender", sender);
        hashMap.put("message", message);




        reference.child("Groups_Messages").push().setValue(hashMap);

    }


    private void readMessage( final String groupName ){
        mGroupMessage= new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups_Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroupMessage.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    GroupMessage groupMessage=  new GroupMessage();
                    groupMessage= snapshot.getValue(GroupMessage.class);


                    if(groupMessage.getGroupName().equals(groupName)) {
                        mGroupMessage.add(groupMessage);
                    }

                }

                groupsAdapter= new GroupsAdapter(GroupChatActivity.this, mGroupMessage);
                recyclerView.setAdapter(groupsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}




