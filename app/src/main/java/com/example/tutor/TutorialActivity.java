package com.example.tutor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TutorialActivity extends AppCompatActivity {
    TextView tvrname,tvrphone,tvraddress,tvrloc;
    ImageView imgRest;
    Button btnloc,btnpay;

    Dialog myDialogWindow;
    ArrayList<HashMap<String, String>> foodlist;
    String userid,restid,userphone;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnloc = findViewById(R.id.btnloc);
        btnpay = findViewById(R.id.btnpay);
        setContentView(R.layout.activity_tutorial);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        restid = bundle.getString("courseID");
        String rname = bundle.getString("name");
        String rphone = bundle.getString("phone");
        String raddress = bundle.getString("tutor");
        String rlocation = bundle.getString("location");
        userid = bundle.getString("userid");
        userphone = bundle.getString("userphone");
        initView();
        tvrname.setText(rname);
        tvraddress.setText(raddress);
        tvrphone.setText(rphone);
        tvrloc.setText(rlocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).load("http://githubbers.com/jivan/courseimg/"+restid+".png")
                .fit().into(imgRest);
        //  .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)

        loadFoods(restid);

    }

    private void loadFoods(final String restid) {
        class LoadFood extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("restid",restid);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://githubbers.com/jivan/load_class.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                foodlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray foodarray = jsonObject.getJSONArray("food");
                    for (int i = 0; i < foodarray.length(); i++) {
                        JSONObject c = foodarray.getJSONObject(i);
                        String jsid = c.getString("foodid");
                        String jsfname = c.getString("foodname");
                        String jsfprice = c.getString("foodprice");
                        String jsquan = c.getString("quantity");
                        HashMap<String,String> foodlisthash = new HashMap<>();
                        foodlisthash.put("foodid",jsid);
                        foodlisthash.put("foodname",jsfname);
                        foodlisthash.put("foodprice",jsfprice);
                        foodlisthash.put("foodquantity",jsquan);
                        foodlist.add(foodlisthash);
                    }
                }catch(JSONException e){}

            }
        }
        LoadFood loadFood = new LoadFood();
        loadFood.execute();
    }

    private void initView() {
        imgRest = findViewById(R.id.imageView3);
        tvrname = findViewById(R.id.textView6);
        tvrphone = findViewById(R.id.textView7);
        tvraddress = findViewById(R.id.textView8);
        tvrloc = findViewById(R.id.textView9);
        foodlist = new ArrayList<>();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TutorialActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",userid);
                bundle.putString("phone",userphone);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
