package hr.math.frizer;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // u onCreate

        DBAdapter db = new DBAdapter(this);


        //---add a contact---
        /*db.open();
        long id = db.insertUser("Wei-Meng Lee", "weimenglee@learn2develop.net");
        id = db.insertUser("Mary Jackson", "mary@jackson.com");
        db.close();*/



        //--get all contacts---
        db.open();
        Cursor c = db.getAllUsers();
        if (c.moveToFirst())
        {
            do {
                DisplayUser(c);
            } while (c.moveToNext());
        }
        db.close();



        //---get a contact---
        db.open();
        Cursor cu = db.getUser(2);
        if (cu.moveToFirst())
            DisplayUser(cu);
        else
            Toast.makeText(this, "No user found", Toast.LENGTH_LONG).show();
        db.close();



        //---update contact---
        db.open();
        if (db.updateUser(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();



        //---delete a contact---
        db.open();
        if (db.deleteUser(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();
    }


// nakon onCreate

    //funkcija za ispis
    public void DisplayUser(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Email:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }

    public void goToRegistr(View view)
    {
        Intent intent = new Intent(this, RegistrActivity.class);
        startActivity(intent);
    }


}
