package hr.math.frizer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(getUserName() != null){
            Toast.makeText(this, R.string.hello + ", " + getUserName(), Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(this, "No one logged in!", Toast.LENGTH_SHORT).show();
        }*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();

        if(getUserName() != null) {
            navigationView.inflateMenu(R.menu.navigation_menu_loggedin);
        }
        else{
            navigationView.inflateMenu(R.menu.navigation_menu);
        }
        navigationView.setNavigationItemSelectedListener(this);

        //--- Creating database ---
        DBAdapter db = new DBAdapter(this);

        //---add a salon---
        /*db.open();
        long id = db.insertSalon("Fluid", "Malešnica 54, Zagreb", "fsfluid@gmail.com", "0912345555", "8-20","0",
                                "45.807559", "15.897518","fluidic","12345678");
        id = db.insertSalon("Frizerski salon W", "Taborska 31, Zagreb", "frizerskisalonw@yahoo.com", "0912345444", "8-20","0",
                            "45.801492", "15.952285","wich","12345678");
        id = db.insertSalon("Frizerski salon ona & on", "Domitrovićeva ulica 21-1, Zagreb", "fsoni@gmail.com", "012345678",
                "9 - 20:30", "0", "45.820408", "15.888669", "onaion", "12345678");
        id = db.insertSalon("ONA I ON", "Ul. Grada Mainza 19, Zagreb", "onaion@gmail.com", "013589765",
                "8 - 22", "0", "45.812512", "15.952699", "fsonaion", "12345678");
        id = db.insertSalon("Frizerski salon Smart", "A.T. Mimare 14", "fssmart@gmail.com", "013735438",
                "8-20", "0", "45.809451", "15.891741", "smart", "12345678");
        id = db.insertSalon("FRIZERSKI SALON ANDREA", "Ul. kneza Branimira, Zagreb", "fsandrea@yahoo.com", "0953988453",
                "8 - 15", "0", "45.806156", "15.989840", "andrea", "12345678");
        id = db.insertSalon("Frizerski studio Alter", "Ribnjak 18, Zagreb", "alter.studio@gmail.com", "0916765839", "8 - 22",
                "0", "45.816751", "15.981491", "alter", "12345678");
        id = db.insertSalon("Evelin", "Nova ves 72, Zagreb", "evelin.salon@gmail.com", "013675482", "8 - 20",
                "0", "45.826501", "15.978745", "evelin", "12345678");

        db.close();*/


        //--get all salons---
        db.open();
        Cursor c = db.getAllSalons();
        DisplaySalon(c);
        db.close();
    }

    public void onRestart(){
        super.onRestart();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void DisplaySalon(Cursor c)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayoutMain);

        if (c.moveToFirst()) {
            do {

                TextView tvName = new TextView(this);
                tvName.setText(c.getString(1));
                tvName.setTextSize(20);
                ll.addView(tvName);

                RatingBar rating = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rating.setLayoutParams(layoutParams);
                rating.setNumStars(5);
                rating.setStepSize((float) 0.1);
                rating.setIsIndicator(true);
                rating.setRating(Float.parseFloat(c.getString(6)));
                ll.addView(rating);

                TextView tvAddress = new TextView(this);
                tvAddress.setText(c.getString(2));
                ll.addView(tvAddress);

                TextView tvTelNumber = new TextView(this);
                tvTelNumber.setText(c.getString(4));
                ll.addView(tvTelNumber);

                Button btOpen = new Button(this);
                btOpen.setText(R.string.btOpen);
                btOpen.setId(Integer.parseInt(c.getString(0)));
                ll.addView(btOpen);

                btOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, SalonActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("_id", Integer.toString(view.getId()) );
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });

                /*Toast.makeText(this,
                        "id: " + c.getString(0) + "\n" +
                                "Name: " + c.getString(1) + "\n" +
                                "Email:  " + c.getString(2),
                        Toast.LENGTH_LONG).show();*/
            } while (c.moveToNext());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);

   }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pretrazi) {
            //Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, NearestSalons.class);
            startActivity(i);
        }
        else if (id == R.id.nav_login) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else if(id == R.id.nav_logout){
            SharedPreferences userPref = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editUserInfo = userPref.edit();
            editUserInfo.remove("username");
            editUserInfo.apply();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getUserName(){
        SharedPreferences userPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String currentUser = userPref.getString("username", null);
        return currentUser;
    }
}
