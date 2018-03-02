package hr.math.frizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NarudzbaActivity extends AppCompatActivity {

    private String id_salon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_narudzba);

        Bundle bundle=getIntent().getExtras();

        id_salon = bundle.getString("_id");



    }
    public void NaruciSe(View v)
    {
        if(getUserName()!=null)
        {
            v.setEnabled(true);
            EditText etDatum = (EditText) findViewById(R.id.etDatum);
            EditText etVrijeme = (EditText) findViewById(R.id.etVrijeme);
            EditText etUsluga = (EditText) findViewById(R.id.etUsluga);

            String datum = etDatum.getText().toString();
            String vrijeme = etVrijeme.getText().toString();
            String usluga = etUsluga.getText().toString();

            datum = datum + " " + vrijeme;

            DBAdapter db = new DBAdapter(this);
            db.open();
            long id = db.insertNarudzba(id_salon, usluga, datum, getUserName() );
            db.close();

            Toast.makeText(this,R.string.uspjesnoNarucivanje, Toast.LENGTH_LONG).show();
            v.setEnabled(false);

        }
        else{

            v.setEnabled(false);
            Toast.makeText(this, R.string.neuspjesnoNarucivanje, Toast.LENGTH_LONG).show();
        }
    }

    public String getUserName(){
        SharedPreferences userPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String currentUser = userPref.getString("username", null);
        return currentUser;
    }
}
