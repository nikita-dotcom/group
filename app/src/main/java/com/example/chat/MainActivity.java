package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.Fragments.ChatsFragment;
import com.example.chat.Fragments.UsersFragment;
import com.example.chat.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private DatabaseReference chatReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatReference= FirebaseDatabase.getInstance().getReference();

        username= findViewById(R.id.username);
     firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
     reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

     reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             User user= dataSnapshot.getValue(User.class);
             username.setText(user.getUsername());

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });

        TabLayout tabLayout= findViewById(R.id.tab_layout);
        ViewPager viewPager= findViewById(R.id.view_pager);

         ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());

         viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
         viewPagerAdapter.addFragment(new UsersFragment(), "Users");

         viewPager.setAdapter(viewPagerAdapter);

         tabLayout.setupWithViewPager(viewPager);
    }



    @Override
   public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;

            case R.id.create_new_chat:
                requestNewGroup();
                return true;
        }




        return false;
    }

    private void requestNewGroup() {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter group name");

        final EditText groupNameField= new EditText(MainActivity.this);
        groupNameField.setHint("Technopark");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName= groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this,"Input name of chat",Toast.LENGTH_SHORT).show();
                }

                else {
                    createNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
       private ArrayList<Fragment> fragments;
       private ArrayList<String> titles;

       ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments= new ArrayList<>();
            this.titles= new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }




        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return  titles.get(position);

        }
    }

    private void createNewGroup(String groupName) {

        chatReference.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(MainActivity.this,"Group is created", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}