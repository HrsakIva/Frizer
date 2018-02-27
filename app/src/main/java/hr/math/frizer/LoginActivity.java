package hr.math.frizer;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // u onCreate

        DBAdapter db = new DBAdapter(this);
        
        //--get all users---
        db.open();
        Cursor c = db.getAllUsers();
        if (c.moveToFirst())
        {
            do {
                Toast.makeText(this, c.getString(1).toString(), Toast.LENGTH_SHORT).show();
                //DisplayUser(c);
            } while (c.moveToNext());
        }
        db.close();



        //---get a contact---
        /*db.open();
        Cursor cu = db.getUser(2);
        if (cu.moveToFirst())
            DisplayUser(cu);
        else
            Toast.makeText(this, "No user found", Toast.LENGTH_LONG).show();
        db.close();*/



        //---update contact---
        /*db.open();
        if (db.updateUser(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();*/



        //---delete a contact---
        /*db.open();
        if (db.deleteUser(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();*/
    }


// nakon onCreate

    //funkcija za ispis
    public void DisplayUser(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Phone number:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }

    public void goToRegistr(View view)
    {
        Intent intent = new Intent(this, RegistrActivity.class);
        startActivity(intent);
    }

    public boolean checkUserData(Cursor c, String userName, String Password) {
        if (c.moveToFirst()) {
            do {
                if (c.getString(1).equals(userName) && c.getString(2).equals(Password))
                    return true;
                //Toast.makeText(this, c.getString(i) + " " + toBeChecked, Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
        return false;
    }

    public void login(View v){
        DBAdapter db = new DBAdapter(this);

        EditText eText = (EditText) findViewById(R.id.txtUserName);
        String userName = eText.getText().toString();

        eText = (EditText) findViewById(R.id.txtPass);
        String password = eText.getText().toString();

        db.open();
        Cursor user = db.getAllUsers();
        if(checkUserData(user, userName, password)){
            Toast.makeText(this, "Pozdrav, " + userName + "!", Toast.LENGTH_SHORT).show();
            db.close();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, R.string.loginFailed , Toast.LENGTH_SHORT).show();
            db.close();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }


}
