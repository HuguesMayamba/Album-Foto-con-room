package com.hugues.taskfarmy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hugues.taskfarmy.DataModel.DataConverter;
import com.hugues.taskfarmy.DataModel.User;
import com.hugues.taskfarmy.DataModel.UserDAO;
import com.hugues.taskfarmy.DataModel.UserDatabase;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap bmpImage;
    EditText name,uName, pas, dob;

    UserDAO userDAO;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.userImage);
        bmpImage = null;

        name = findViewById(R.id.fullName);
        uName = findViewById(R.id.userName);
        pas = findViewById(R.id.userPassword);
        dob = findViewById(R.id.userDob);

        userDAO = UserDatabase.getDBInstence(this).userDAO();
    }

    final  int CAMERA_INTENT = 51;

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_INTENT);

        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CAMERA_INTENT:

                    bmpImage = (Bitmap) data.getExtras().get("data");
                    if (bmpImage != null){
                        imageView.setImageBitmap(bmpImage);
                    }else{
                        Toast.makeText(this, "Bipmap is Null", Toast.LENGTH_SHORT).show();
                    }
                break;
        }
    }

    public void saveUser(View view) {

        if(name.getText().toString().isEmpty() || uName.getText().toString().isEmpty()
        || pas.getText().toString().isEmpty() || dob.getText().toString().isEmpty() || bmpImage == null){

            Toast.makeText(this,"User data is missing", Toast.LENGTH_LONG).show();

        }else {

            user.setFullName(name.getText().toString());
            user.setUserName(uName.getText().toString());
            user.setPassword(pas.getText().toString());
            user.setImage(DataConverter.convertImage2ByteArray(bmpImage));
            try {
                user.setDob(new SimpleDateFormat("dd/MM/yyyy").parse(dob.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Save particular user
            userDAO.insertUser(user);

            Toast.makeText(this, "Insertion successful", Toast.LENGTH_SHORT).show();

        }

    }

    public void showUsers(View view) {

        Intent intent = new Intent(this, ShowUsersActivity.class);
        startActivity(intent);

    }

    public void deleteUsers(View view) {

        userDAO.delete(userDAO.getAllUsers());

    }
}