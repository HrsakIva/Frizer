package hr.math.frizer;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RatingBar;
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

       /* db.open();
        long id= db.insertKomentar("2",currentDateandTime,"Iva", "Odlicno!.");
        id= db.insertKomentar("2",currentDateandTime,"AnaM", "Jako sam zadovoljna.");
        db.close();*/
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

                if(getUserName()!= null)
                {
                    view.setEnabled(true);
                    EditText etComment =(EditText)findViewById(R.id.etComment);
                    String commentText  = etComment.getText().toString();

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    String currentDateandTime = sdf.format(currentTime);
                    String autor = getUserName();

                    DBAdapter db = new DBAdapter(SalonActivity.this);
                    db.open();
                    long id = db.insertKomentar(id_salon, currentDateandTime, autor, commentText);
                    db.close();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                else{
                    view.setEnabled(false);
                    Toast.makeText(SalonActivity.this, R.string.btnKomentiranjeLog, Toast.LENGTH_LONG).show();
                }

            }
        });

        RatingBar rbStars =(RatingBar) findViewById(R.id.rtStars);

        if(getUserName()!=null)
        {
            rbStars.setIsIndicator(false);

            rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                    DBAdapter db = new DBAdapter(SalonActivity.this);
                    db.open();
                    Cursor c = db.getSalon(Integer.parseInt(id_salon));
                    db.close();

                    String rating = c.getString(6);
                    float floatRating = Float.parseFloat(rating);
                    float no_votes = Float.parseFloat(c.getString(11));
                    floatRating = (floatRating+v);
                    no_votes = no_votes + 1;
                    float rating_final = floatRating/no_votes;

                    long id = Integer.parseInt(c.getString(0));

                    db.open();
                    db.updateSalon(id,c.getString(1),c.getString(2), c.getString(3),c.getString(4),c.getString(5),Float.toString(floatRating),c.getString(7),c.getString(8),c.getString(9),c.getString(10), Float.toString(no_votes));

                    ratingBar.setRating(v);
                    ratingBar.setIsIndicator(true);


                }
            });
        }
        else
        {
            rbStars.setIsIndicator(true);
            float rating_number ;
            if(Float.parseFloat(c.getString(11))==0)
            {
                rating_number=Float.parseFloat(c.getString(6))/ (Float.parseFloat(c.getString(11))+1);
            }
            else{
                rating_number =Float.parseFloat(c.getString(6))/ (Float.parseFloat(c.getString(11)));
            }
            rbStars.setRating(rating_number);

        }

        /*Button btnPonuda = (Button) findViewById(R.id.btnPonuda);
        btnPonuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent i = new Intent(SalonActivity.this, PonudaActivity.class);
                Bundle extras = new Bundle();
                extras.putString("_id", id_salon );
                i.putExtras(extras);
                startActivity(i);
            }
        });

        Button btnOrder = (Button) findViewById(R.id.btnOrderSalon);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SalonActivity.this, NarudzbaActivity.class);
                Bundle extras = new Bundle();
                extras.putString("_id", id_salon );
                i.putExtras(extras);
                startActivity(i);
            }
        });*/

    }

    public void Order(View v)
    {
        Intent i = new Intent(this, NarudzbaActivity.class);
        Bundle extras = new Bundle();
        extras.putString("_id", id_salon );
        i.putExtras(extras);
        startActivity(i);
    }

    public void Ponuda(View v)
    {
        Intent i = new Intent(this, PonudaActivity.class);
        Bundle extras = new Bundle();
        extras.putString("_id", id_salon );
        i.putExtras(extras);
        startActivity(i);
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

    public String getUserName(){
        SharedPreferences userPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String currentUser = userPref.getString("username", null);
        return currentUser;
    }
}
