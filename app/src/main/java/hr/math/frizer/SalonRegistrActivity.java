package hr.math.frizer;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SalonRegistrActivity extends AppCompatActivity {

    private ArrayList<String> vrijeme;
    private ArrayList<String> usluga;
    private ArrayList<String> ime;
    private ArrayList<String> broj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_registr);

        DBAdapter db = new DBAdapter(this);

        Bundle bundle=getIntent().getExtras();

        db.open();
        Cursor c = db.getSalon(Integer.parseInt(bundle.getString("_id")));
        db.close();

        db.open();
        Cursor c1 = db.getAllNarudzbaForSalon(bundle.getString("_id"));
        //Toast.makeText(this, c1.getString(4), Toast.LENGTH_LONG).show();
        db.close();


        vrijeme= new ArrayList<String>();
        usluga = new ArrayList<String>();
        ime = new ArrayList<String>();
        broj = new ArrayList<String>();

        if(c1!=null)
        {
            ListView lv = (ListView) findViewById(R.id.lvNarudzbe);
            if(c1.moveToFirst())
            {
                do{
                    db.open();
                    Cursor c2 = db.getUserByUsername(c1.getString(4));
                    if(c2.moveToFirst())
                    {
                        ime.add(c2.getString(3)+ " "+c2.getString(4));
                        broj.add(c2.getString(5));
                    }
                    db.close();


                    vrijeme.add(c1.getString(3));
                    usluga.add(c1.getString(2));
                }
                while(c1.moveToNext());
            }
            CustomAdapter customAdapter = new CustomAdapter();
            lv.setAdapter(customAdapter);
        }



    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return usluga.size();
        }

        @Override
        public Object getItem(int i){
            return null;
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){
            view = getLayoutInflater().inflate(R.layout.custom_row_narudzbe, null);

            TextView tvUsluga = (TextView)view.findViewById(R.id.tvOpisUsluge);
            TextView tvVrijeme = (TextView)view.findViewById(R.id.tvVrijeme);
            TextView tvIme = (TextView)view.findViewById(R.id.tvImeKorisnika);
            TextView tvBroj = (TextView)view.findViewById(R.id.tvBrojKorisnika);
            //TextView tvTrajanje = (TextView)view.findViewById(R.id.tvTrajanjePonuda);

            tvUsluga.setText(usluga.get(i));
            tvVrijeme.setText(vrijeme.get(i));
            tvIme.setText(ime.get(i));
            tvBroj.setText(broj.get(i));
            //tvTrajanje.setText(trajanje.get(i));

            return view;
        }

    }
}
