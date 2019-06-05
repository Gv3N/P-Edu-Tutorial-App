package com.example.tutor;
//main activity
//display tutorials
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView lvrest;
    ArrayList<HashMap<String, String>> restlist;
    Spinner spinCou;
    String userid, name, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvrest = findViewById(R.id.listviewRest);
        spinCou = findViewById(R.id.spinCou);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("userid");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        loadCourse(spinCou.getSelectedItem().toString());
        lvrest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("courseID", restlist.get(position).get("courseid"));
                bundle.putString("name", restlist.get(position).get("name"));
                bundle.putString("phone", restlist.get(position).get("phone"));
                bundle.putString("address", restlist.get(position).get("address"));
                bundle.putString("location", restlist.get(position).get("location"));
                bundle.putString("userid", userid);
                bundle.putString("userphone", phone);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        spinCou.setSelection(0, false);
        spinCou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadCourse(spinCou.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void loadCourse(final String loc) {
        class LoadCourse extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("course", loc);
                RequestHandler rh = new RequestHandler();
                restlist = new ArrayList<>();
                String s = rh.sendPostRequest("http://githubbers.com/jivan/load_course.php", hashMap);
                return s;
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                restlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("rest");
                    Log.e("HANIS", jsonObject.toString());
                    for (int i = 0; i < restarray.length(); i++) {
                        JSONObject c = restarray.getJSONObject(i);
                        String rid = c.getString("courseID");
                        String rname = c.getString("name");
                        String rphone = c.getString("phone");
                        String raddress = c.getString("tutor");
                        String rlocation = c.getString("location");
                        HashMap<String, String> restlisthash = new HashMap<>();
                        restlisthash.put("courseID", rid);
                        restlisthash.put("name", rname);
                        restlisthash.put("phone", rphone);
                        restlisthash.put("tutor", raddress);
                        restlisthash.put("location", rlocation);
                        restlist.add(restlisthash);
                    }
                } catch (final JSONException e) {
                    Log.e("JSONERROR", e.toString());
                }
                ListAdapter adapter = new CustomAdapter(
                        MainActivity.this, restlist,
                        R.layout.cust_list_tutorial, new String[]
                        {"name", "phone", "address", "location"}, new int[]
                        {R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4});
                lvrest.setAdapter(adapter);

            }
        }
        LoadCourse loadCourse = new LoadCourse();
        loadCourse.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.myclass:
                //loadCartData();
                return true;
            case R.id.myprofile:
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",userid);
                bundle.putString("username",name);
                bundle.putString("phone",phone);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            //case R.id.myhistory:
                //loadHistoryOrderData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


