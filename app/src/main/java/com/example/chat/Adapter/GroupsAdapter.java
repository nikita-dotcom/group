package com.example.chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.GroupChatActivity;
import com.example.chat.MainActivity;

import com.example.chat.Model.GroupMessage;
import com.example.chat.Model.User;
import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private  int VIEW_TYPE_LEFT=0;
    private  int VIEW_TYPE_RIGHT=1;
    private List<GroupMessage> groupMessagesList;
    private Context mContext;
    private FirebaseUser firebaseUser;
    public GroupsAdapter(Context mContext, List<GroupMessage> groupMessagesList) {
        this.mContext= mContext;
        this.groupMessagesList= groupMessagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_item_right, parent, false);
            return new GroupsAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_item_left, parent, false);
            return new GroupsAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

       final GroupMessage groupMessage= groupMessagesList.get(position);

        holder.groupName.setText(groupMessage.getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, GroupChatActivity.class);
                intent.putExtra(groupMessage.getGroupName(),position);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return groupMessagesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView groupName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupName= itemView.findViewById(R.id.show_group_message);
        }
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(groupMessagesList.get(position).getSender().equals(firebaseUser.getUid())){
            return VIEW_TYPE_RIGHT;
        }
        else {
            return VIEW_TYPE_LEFT;
        }

    }



}
