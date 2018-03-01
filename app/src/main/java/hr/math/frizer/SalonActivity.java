package hr.math.frizer;

import android.app.LauncherActivity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SalonActivity extends AppCompatActivity {

    private ArrayList<String> names;
    private ArrayList<String> dates;
    private ArrayList<String> comments;
    private String id_salon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(currentTime);

        //Toast.makeText(this, currentDateandTime , Toast.LENGTH_SHORT).show();

        Bundle bundle=getIntent().getExtras();

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getSalon(Integer.parseInt(bundle.getString("_id")));
        db.close();

        id_salon = c.getString(0);

        TextView salonName = (TextView) findViewById(R.id.tvSalonName);
        salonName.setText(c.getString(1));
        salonName.setGravity(Gravity.CENTER);

        TextView adresaSalona = (TextView) findViewById(R.id.txtAdresaSalon);
        adresaSalona.setText(c.getString(2));

        TextView telNumber = (TextView) findViewById(R.id.txtTelNumberSalon);
        telNumber.setText(c.getString(4));

        TextView mailSalona = (TextView) findViewById(R.id.txtMailSalon);
        mailSalona.setText(c.getString(3));

        TextView radnoVrijeme = (TextView) findViewById(R.id.txtWorkhoursSalon);
        radnoVrijeme.setText(c.getString(5));

        TextView komentari = (TextView) findViewById(R.id.txtKomentari);

        db.open();
        long id= db.insertKomentar("2", currentDateandTime ,"Iva", "Odlicno!.");
        id= db.insertKomentar("2",currentDateandTime,"AnaM", "Jako sam zadovoljna.");
        db.close();
        //Toast.makeText(this, c1.getString(1)+" "+c1.getString(2)+" "+c1.getString(3)+" "+c1.getString(4),Toast.LENGTH_LONG).show();

        db.open();
        Cursor c1 = db.getAllKomentarForSalon(c.getString(0));
        ListView lv = (ListView) findViewById(R.id.listViewSalon);

        names = new ArrayList<String>();
        dates = new ArrayList<String>();
        comments = new ArrayList<String>();
        if(c1.moveToFirst())
        {
            do{

                names.add(c1.getString(3));
                dates.add(c1.getString(2));
                comments.add(c1.getString(4));

            }
            while(c1.moveToNext());
        }
        db.close();

        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);

        Button btnComment = (Button)findViewById(R.id.btnComment);


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText etComment =(EditText)findViewById(R.id.etComment);
                String commentText  = etComment.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(currentTime);

                DBAdapter db = new DBAdapter(SalonActivity.this);
                db.open();
                long id = db.insertKomentar(id_salon, currentDateandTime,"ZasadNepoznat",commentText);
                db.close();


            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount(){
            return names.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_row, null);

            TextView tvName = (TextView)view.findViewById(R.id.tvNameLV);
            TextView tvDate = (TextView)view.findViewById(R.id.tvDateLV);
            TextView tvComment = (TextView)view.findViewById(R.id.tvKomentarLV);

            tvName.setText(names.get(i));
            tvDate.setText(dates.get(i));
            tvComment.setText(comments.get(i));

            return view;
        }

    }
}
