package com.hugues.taskfarmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hugues.taskfarmy.DataModel.User;
import com.hugues.taskfarmy.DataModel.UserDAO;
import com.hugues.taskfarmy.DataModel.UserDatabase;

import java.util.List;

public class ShowUsersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UserDAO userDAO;
    User user = new User();
    List<User> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        recyclerView = findViewById(R.id.userRecyclearView);
        userDAO = UserDatabase.getDBInstence(this).userDAO();
        UserRecycler userRecycler = new UserRecycler(userDAO.getAllUsers());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userRecycler);
    }

}