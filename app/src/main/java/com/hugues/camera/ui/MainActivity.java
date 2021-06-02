package com.hugues.camera.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.hugues.camera.DataModel.DataConverter;
import com.hugues.camera.DataModel.User;
import com.hugues.camera.DataModel.UserDAO;
import com.hugues.camera.DataModel.UserDatabase;
import com.hugues.camera.R;

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

    @SuppressLint("SetTextI18n")
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

        dod.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance();
            mDate = cal.get(Calendar.DATE);
            mMonth = cal.get(Calendar.MONTH);
            mYear = cal.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                datePickerDialog = new DatePickerDialog(
                        MainActivity.this, android.R.style.Widget_Material,
                        (view, year, month, dayOfMonth) -> dod.setText(dayOfMonth+"/"+month+"/"+year),mYear, mMonth, mDate);
            }
            assert datePickerDialog != null;
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        });

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

    @SuppressLint("SimpleDateFormat")
    public void saveUser(View view) {

        if(name.getText().toString().isEmpty() || uName.getText().toString().isEmpty()
        || pas.getText().toString().isEmpty() || bmpImage == null){

            Toast.makeText(this,"Falta campo por rellenar", Toast.LENGTH_LONG).show();

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

            Toast.makeText(this, "Datos introducidos correctamente", Toast.LENGTH_SHORT).show();

        }

    }

    public void showUsers(View view) {

        Intent intent = new Intent(this, ShowUsersActivity.class);
        startActivity(intent);

    }

    public void deleteUsers(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Quieres borrar todas las fotos?")
                .setTitle("Ventana de alerta");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                userDAO.delete(userDAO.getAllUsers());
                Toast.makeText(MainActivity.this, "Has borrado todas las fotos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    }

