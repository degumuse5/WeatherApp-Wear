package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    double degree_d;
    JSONObject object;
    private JSONArray weather;
    JSONObject Info;
    CheckBox f;
    CheckBox c;
    ImageView set;
    double temp_min;
    double temp_max;
    TextView min;
    TextView max;
    TextView stateN;
    TextView stateNameDis;
    TextView main;
    TextView degree_num;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        degree_num = findViewById(R.id.degree);
        f = findViewById(R.id.F);
        c = findViewById(R.id.C);
        set = findViewById(R.id.sett);
        min = findViewById(R.id.LOW);
        max = findViewById(R.id.HIGH);
        stateN = findViewById(R.id.stateN);
        main = findViewById(R.id.Description);
        search = findViewById(R.id.search);
        stateNameDis = findViewById(R.id.city);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_degree_now(degree_num, min, max, stateN, main, stateNameDis);
                choose_degree_type();
                setfahrniet();
            }
        });

    }


    private void setfahrniet() {
        f.post(() -> f.setChecked(true));
    }

    private void background_Optimizer(String main, String ID) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.back);
        ConstraintLayout card = (ConstraintLayout) findViewById(R.id.card);
        CardView dis = (CardView) findViewById(R.id.dis);
        if (ID.startsWith("7")) {
            layout.setBackgroundResource(R.color.red);
            card.setBackgroundResource(R.color.red);
        } else {
                if (main.equals("Thunderstorm")) {
                    layout.setBackgroundResource(R.color.thunder);
                    card.setBackground(getDrawable(R.drawable.thunder));
                    dis.setBackgroundResource(R.color.thunder);
                }
                if (main.equals("Clouds")) {
                    layout.setBackgroundResource(R.color.cloud);
                    card.setBackground(getDrawable(R.drawable.cloud));
                    dis.setBackgroundResource(R.color.cloud);
                }
                if (main.equals("Clear")) {
                    layout.setBackgroundResource(R.color.clear);
                    card.setBackground(getDrawable(R.drawable.clear));
                    dis.setBackgroundResource(R.color.clear);
                }
                if (main.equals("Snow")) {
                    layout.setBackgroundResource(R.color.snow);
                    card.setBackground(getDrawable(R.drawable.snow));
                    dis.setBackgroundResource(R.color.snow);
                }
                if (main.equals("Rainy")) {
                    layout.setBackgroundResource(R.color.rain);
                    card.setBackground(getDrawable(R.drawable.rainy));
                    dis.setBackgroundResource(R.color.rain);
                }

        }

    }

    private void choose_degree_type() {
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    double cel = (degree_d - 32) * 5 / 9;
                    double min1 = (temp_min - 32) * 5 / 9;
                    double max1 = (temp_max - 32) * 5 / 9;
                    String cel1 = cel + "";
                    String min2 = min1 + "";
                    String max2 = max1 + "";
                    degree_num.setText(cel1.substring(0, 2) + 'º');
                    min.setText(min2.substring(0, 2) + 'º');
                    max.setText(max2.substring(0, 2) + 'º');
                    f.post(new Runnable() {
                        @Override
                        public void run() {
                            f.setChecked(false);
                        }
                    });
                }
            }
        });
        f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String str = "" + degree_d;
                    String str2 = "" + temp_min;
                    String str3 = "" + temp_max;
                    degree_num.setText(str.substring(0, 2) + 'º');
                    min.setText(str2.substring(0, 2) + 'º');
                    max.setText(str3.substring(0, 2) + 'º');
                    c.post(new Runnable() {
                        @Override
                        public void run() {
                            c.setChecked(false);
                        }
                    });
                }

            }
        });
    }


    private void set_degree_now(TextView degree_num, TextView the_low, TextView the_high, TextView stateName, TextView Main, TextView stateNameDis) {

        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + stateName.getText() + "&appid=31bbb5abb1f4b9ac321b1a5712f638ff";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            object = response.getJSONObject("main");
                            weather = response.getJSONArray("weather");
                            int degree = object.getInt("temp");
                            int temp_min1 = object.getInt("temp_min");
                            int temp_max2 = object.getInt("temp_max");
                            Info = weather.getJSONObject(0);
                            int id = Info.getInt("id");
                            String main2 = Info.getString("main");
                            background_Optimizer(main2, String.valueOf(id));
                            temp_min = (temp_min1 - 273.15) * 9 / 5 + 32;
                            temp_max = (temp_max2 - 273.15) * 9 / 5 + 32;
                            degree_d = (degree - 273.15) * 9 / 5 + 32;

                            String str = "" + degree_d;
                            degree_num.setText(str.substring(0, 2) + 'º');
                            setWear(Integer.valueOf((int) degree_d));

                            String str2 = "" + temp_min;
                            the_low.setText(str2.substring(0, 2) + 'º');

                            String str3 = "" + temp_max;
                            the_high.setText(str3.substring(0, 2) + 'º');

                            Main.setText("• " + main2);

                            stateNameDis.setText(stateName.getText());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            degree_num.setText("Didn't Work");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        degree_num.setText("Error");
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void setWear(int degree) {
        ConstraintLayout lay = (ConstraintLayout) findViewById(R.id.wear);
        if(degree <= 50){
            lay.setBackgroundResource(R.drawable.bigjacket);

        }
        else if((degree > 50) & (degree < 70)){
            lay.setBackgroundResource(R.drawable.longsleeve);

        }
        else if(degree > 70){
            lay.setBackgroundResource(R.drawable.tshirt);
        }
    }
}