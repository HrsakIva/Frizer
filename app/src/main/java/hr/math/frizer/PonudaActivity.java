package hr.math.frizer;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PonudaActivity extends AppCompatActivity {

    private ArrayList<String> ponuda;
    private ArrayList<String> cijena;
    private ArrayList<String> trajanje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponuda);

        Bundle bundle=getIntent().getExtras();

        DBAdapter db = new DBAdapter(this);

        db.open();
        Cursor c = db.getSalon(Integer.parseInt(bundle.getString("_id")));
        db.close();

        /*db.open();
        long id = db.insertPonuda("2","Žensko šišanje kratka kosa","50kn","30 min");
        id = db.insertPonuda("2","Žensko šišanje duga kosa","80kn","1h 15min");
        db.close();*/

        db.open();
        Cursor c1 = db.getAllPonudaForSalon(c.getString(0));
        ListView lv = (ListView) findViewById(R.id.lvPonude);

        ponuda = new ArrayList<String>();
        cijena = new ArrayList<String>();
        trajanje = new ArrayList<String>();

        if(c1.moveToFirst())
        {
            do{

                ponuda.add(c1.getString(2));
                cijena.add(c1.getString(3));
                trajanje.add(c1.getString(4));

            }
            while(c1.moveToNext());
        }
        db.close();

        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return ponuda.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_row_ponuda, null);

            TextView tvPonuda = (TextView)view.findViewById(R.id.tvPonuda);
            TextView tvCijena = (TextView)view.findViewById(R.id.tvCijenaPonuda);
            TextView tvTrajanje = (TextView)view.findViewById(R.id.tvTrajanjePonuda);

            tvPonuda.setText(ponuda.get(i));
            tvCijena.setText(cijena.get(i));
            tvTrajanje.setText(trajanje.get(i));

            return view;
        }

    }
}
