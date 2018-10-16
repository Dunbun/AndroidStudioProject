package com.example.taras.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Start extends AppCompatActivity {

    private Button clickButton;
    private EditText email;
    private EditText pass;
    SQLite sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        clickButton = (Button) findViewById(R.id.reg);
        clickButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.taras.myapplication.Registration");
                        startActivity(intent);

                    }
                }
        );

        sqLite = new SQLite(Start.this);

    }


    public void Login(View view) {
        String emailString = email.getText().toString();
        String passString = pass.getText().toString();

        SQLiteDatabase database = sqLite.getReadableDatabase();

        String selection = SQLite.KEY_EMAIL + " = ?" + SQLite.KEY_PASSWORD + " = ?";
        String [] selectionArgs = {emailString,passString};

        String q = "SELECT * FROM " + SQLite.TABLE_NAME + " WHERE email = " + "'" + emailString + "' AND" + " password = '" + passString.hashCode() + "'";


        Cursor cursor = database.rawQuery(q,null);

        if(cursor.moveToFirst()){
            Toast.makeText(Start.this,"Welcome",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("com.example.taras.myapplication.GetData");
            startActivity(intent);
        }
        else{
            Toast.makeText(Start.this,"Wrong email or password",Toast.LENGTH_SHORT).show();
        }


    }
}