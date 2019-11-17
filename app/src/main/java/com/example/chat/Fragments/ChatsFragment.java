package com.example.chat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.icu.text.Edits;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chat.Adapter.GroupsAdapter;
import com.example.chat.Adapter.UserAdapter;
import com.example.chat.GroupChatActivity;

import com.example.chat.Model.User;
import com.example.chat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ChatsFragment extends Fragment {


    private View groupFragmentView;
    private  ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_group= new ArrayList<>();
    private DatabaseReference reference;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        groupFragmentView= inflater.inflate(R.layout.fragment_chats, container, false);

        reference= FirebaseDatabase.getInstance().getReference().child("Groups");
        initializeFields();

        displayGroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameGroupName= parent.getItemAtPosition(position).toString();
                Intent intent=  new Intent(getContext(), GroupChatActivity.class);
                intent.putExtra("groupName",nameGroupName);
                startActivity(intent);
            }
        });
        return groupFragmentView;


    }

    private void displayGroups() {

    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            list_of_group.clear();
            Set<String> set= new HashSet<>();
            Iterator iterator= dataSnapshot.getChildren().iterator();
            while (iterator.hasNext()){

                set.add(((DataSnapshot) iterator.next()).getKey());
            }

           list_of_group.addAll(set);
            arrayAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


    }

    private void initializeFields() {

        listView= (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list_of_group);
        listView.setAdapter(arrayAdapter);

    }


}
