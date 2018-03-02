package hr.math.frizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrActivity extends LoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);
    }

    public boolean checkUniqueness(Cursor c, String toBeChecked, int i) {
        if (c.moveToFirst()) {
            do {
                if (c.getString(i).equals(toBeChecked))
                    return false;
                //Toast.makeText(this, c.getString(i) + " " + toBeChecked, Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
        return true;
    }

    public void registerNewUser(View v){
        //Toast.makeText(this, "Okej", Toast.LENGTH_SHORT).show();
        DBAdapter db = new DBAdapter(this);
        boolean unique;
        boolean insert = true;

        EditText eText = (EditText) findViewById(R.id.txtUsername);

        // getting the username and checking if it is already taken
        String userName = eText.getText().toString();
        userName = userName.trim();
        if(userName.isEmpty()){
            Toast.makeText(this, R.string.userNameEmpty, Toast.LENGTH_LONG);
            insert = false;
        }
        db.open();
        Cursor users = db.getAllUsers();
        unique = checkUniqueness(users, userName, 1);
        if(insert) {
            if (!unique) {
                Toast.makeText(this, R.string.userNameTaken, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }

        // getting password and checking if it is at least 8 characters long
        eText = (EditText) findViewById(R.id.txtRegPass);
        String password = eText.getText().toString();
        if(insert) {
            if (password.length() < 8) {
                Toast.makeText(this, R.string.passwordShort, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }

        // checking if the password is confirmed
        eText = (EditText) findViewById(R.id.txtRegPassConfirm);
        String confirmPassword = eText.getText().toString();
        if(insert) {
            if (!confirmPassword.equals(password)) {
                Toast.makeText(this, R.string.confirmPasswordFalse, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }

        // getting name and surname
        eText = (EditText) findViewById(R.id.txtRegName);
        String name = eText.getText().toString();
        name = name.trim();
        if(insert) {
            if (name.isEmpty()) {
                Toast.makeText(this, R.string.nameEmpty, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }

        eText = (EditText) findViewById(R.id.txtRegSurname);
        String surname = eText.getText().toString();
        surname = surname.trim();
        if(insert) {
            if (surname.isEmpty()) {
                Toast.makeText(this, R.string.surnameEmpty, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }

        // getting contact number
        eText = (EditText) findViewById(R.id.txtRegTelnumber);
        String telNum = eText.getText().toString();
        if(insert) {
            if (telNum.isEmpty()) {
                Toast.makeText(this, R.string.telNumEmpty, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }
        unique = checkUniqueness(users, telNum, 5);
        if(insert) {
            if (!unique) {
                Toast.makeText(this, R.string.telNumTaken, Toast.LENGTH_LONG).show();
                insert = false;
            }
        }

        long success = 0;
        if(insert) {
            success = db.insertUser(userName, password, name, surname, telNum);
            if(success != -1){
                Toast.makeText(this, new StringBuilder().append(R.string.hello).append(", ").append(userName).append("!"), Toast.LENGTH_SHORT).show();
                SharedPreferences userPref = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editUserInfo = userPref.edit();
                editUserInfo.putString("username", userName);
                editUserInfo.apply();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }

            else{
                Toast.makeText(this, "Failed, " + userName + "!",  Toast.LENGTH_SHORT).show();
            }
        }
        db.close();


    }


}
