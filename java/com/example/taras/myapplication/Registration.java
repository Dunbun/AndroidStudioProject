package com.example.taras.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    private boolean exist = false;
    private EditText email;
    private EditText pass;

    SQLite sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);


        sqLite = new SQLite(Registration.this);




    }

    public void Register(View view) {

        String emailString = email.getText().toString();
        String passString = pass.getText().toString();

        SQLiteDatabase database = sqLite.getWritableDatabase();


        String selection = SQLite.KEY_EMAIL + " = ?";
        String [] selectionArgs = {emailString};


        Cursor cursor = database.query(SQLite.TABLE_NAME, null, selection, selectionArgs, null, null, null);




        if(!cursor.moveToFirst()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLite.KEY_EMAIL, emailString);
            contentValues.put(SQLite.KEY_PASSWORD, passString.hashCode());

            database.insert(SQLite.TABLE_NAME, null, contentValues);
            Toast.makeText(Registration.this, "Registration completed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("com.example.taras.myapplication.Start");
            startActivity(intent);
        }
        else{
            Toast.makeText(Registration.this, "Such account already exists :(", Toast.LENGTH_LONG).show();
        }



       /* if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(SQLite.KEY_ID);
            int idPassword = cursor.getColumnIndex(SQLite.KEY_PASSWORD);
            int idEmail = cursor.getColumnIndex(SQLite.KEY_EMAIL);


            String arr = "ID = " + cursor.getInt(idIndex) + "\nPassword = " + cursor.getString(idPassword) + "\nEmail = " + cursor.getString(idEmail);
            Toast.makeText(Registration.this,arr, Toast.LENGTH_SHORT).show();
        }*/
    }



}
