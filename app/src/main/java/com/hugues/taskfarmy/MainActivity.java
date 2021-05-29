package com.hugues.taskfarmy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hugues.taskfarmy.DataModel.DataConverter;
import com.hugues.taskfarmy.DataModel.User;
import com.hugues.taskfarmy.DataModel.UserDAO;
import com.hugues.taskfarmy.DataModel.UserDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap bmpImage;
    EditText name, uName, pas, dod;
    private int mDate, mMonth, mYear;

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
        dod = findViewById(R.id.userDod);
        userDAO = UserDatabase.getDBInstence(this).userDAO();

        dod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, android.R.style.Widget_Material,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dod.setText(dayOfMonth+"/"+month+"/"+year);
                            }
                        },mYear, mMonth, mDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });


        Log.d("TAG1", "DATEDIALOG" );

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("bitmap", bmpImage);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bmpImage = outState.getParcelable("bitmap");
        imageView.setImageBitmap(bmpImage);

    }

    final  int CAMERA_INTENT = 51;

    @SuppressLint("QueryPermissionsNeeded")
    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_INTENT);

        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_INTENT) {
            assert data != null;
            bmpImage = (Bitmap) data.getExtras().get("data");
            if (bmpImage != null) {
                imageView.setImageBitmap(bmpImage);
            } else {
                Toast.makeText(this, "Bipmap is Null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void saveUser(View view) {

        if(name.getText().toString().isEmpty() || uName.getText().toString().isEmpty()
        || pas.getText().toString().isEmpty() || bmpImage == null){


            Toast.makeText(this,"User data is missing", Toast.LENGTH_LONG).show();

        }else {

            user.setFullName(name.getText().toString());
            user.setUserName(uName.getText().toString());
            user.setPassword(pas.getText().toString());
            user.setImage(DataConverter.convertImage2ByteArray(bmpImage));
            try {
                user.setDob(new SimpleDateFormat("dd/MM/yyyy").parse(dod.getText().toString()));
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