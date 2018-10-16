package com.example.taras.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MonthStat extends AppCompatActivity {


    private Bundle b;
    private String value;
    private static JSONArray jsonArray;
    private int month;
    private ArrayList<String> categories = new ArrayList<String>();
    private ArrayList<Double> categorySpentMoney = new ArrayList<Double>();
    int width = 0;
    int height = 0;


    int[] nameOfColor = new int[12];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_stat);

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();


        try {
            b = getIntent().getExtras();
            month = b.getInt("month");
            jsonArray = new JSONArray(getIntent().getStringExtra("monthstat"));
            jsonArray.remove(0);//
            jsonArray.remove(0);//
            jsonArray.remove(0);//remove trash

            getNeededMonth();

            getCategories();



        } catch (JSONException e) {
            e.printStackTrace();
        }


        drawStat();



    }
    protected void getNeededMonth()
    {
        String date = null;
        JSONObject obj = null;
        int monthFromData = 0;
        int counter = 0;


        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                obj = jsonArray.getJSONObject(i);
                date = obj.getString("FIELD1");
                monthFromData = Character.getNumericValue(date.charAt(3)) + Character.getNumericValue(date.charAt(4));
                if (monthFromData - 1 != month) {
                    jsonArray.remove(i);
                    i--;
                }
                else{
                    counter++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }




        Toast.makeText(MonthStat.this,Integer.toString(counter),Toast.LENGTH_SHORT).show();
    }

    protected void getCategories() {
        TextView txv = (TextView)findViewById(R.id.textView3);
        String tmp;
        int size = 0;
        double priceOfOperation = 0.0;

        boolean alreadyAdded = false;
        try {
            categories.add(jsonArray.getJSONObject(0).getString("FIELD3"));
            categorySpentMoney.add(0.0);

            for(int i = 0; i < jsonArray.length(); i++){

                tmp = jsonArray.getJSONObject(i).getString("FIELD3");
                priceOfOperation = jsonArray.getJSONObject(i).getDouble("FIELD6");

                for(int j = 0; j < categories.size(); j++){
                    if(categories.get(j).equals(tmp)){
                        alreadyAdded = true;
                        categorySpentMoney.set(j,categorySpentMoney.get(j)+priceOfOperation);
                        break;
                    }
                }
                if(!alreadyAdded){
                    categories.add(tmp);
                    categorySpentMoney.add(priceOfOperation);
                }
                alreadyAdded = false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tmp2 = "";
        for(int i = 0; i < categories.size(); i++){
            categorySpentMoney.set(i,(double) (Math.floor(categorySpentMoney.get(i) * 100) / 100));
            tmp2 +=categories.get(i).toString()+" : "+Double.toString(categorySpentMoney.get(i)*(-1.0))+"\n";
        }
        txv.setText(tmp2);
    }



    @SuppressLint("ResourceAsColor")
    protected void drawStat()
    {
        nameOfColor[0] = getColor(R.color.January);
        nameOfColor[1] = getColor(R.color.February);
        nameOfColor[2] = getColor(R.color.March);
        nameOfColor[3] = getColor(R.color.April);
        nameOfColor[4] = getColor(R.color.May);
        nameOfColor[5] = getColor(R.color.June);
        nameOfColor[6] = getColor(R.color.July);
        nameOfColor[7] = getColor(R.color.August);
        nameOfColor[8] = getColor(R.color.September);
        nameOfColor[9] = getColor(R.color.October);
        nameOfColor[10] = getColor(R.color.November);
        nameOfColor[11] = getColor(R.color.December);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        TextView txt = (TextView)findViewById(R.id.textView3);
        Paint paint = new Paint();
        double angle = -90;
        double angle2 = 0;
        float oX = width/2;
        float oY = height/2;
        float R = 400;
        float x = getX(angle,R);
        float y = getY(angle,R);
        double sum = 0;
        double cof = 0;

        for(int i = 0; i < categorySpentMoney.size(); i++){
            sum -= categorySpentMoney.get(i);
        }
        cof = sum/360;



        Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);



        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(oX,oY,R,paint);
        canvas.drawLine(oX,oY,x,y,paint);

        paint.setStyle(Paint.Style.FILL);

        paint.setStrokeWidth(3);
        paint.setTextSize(25);
        for(int i = 0; i < categorySpentMoney.size(); i++) {
            angle2 = categorySpentMoney.get(i) / cof * (-1);
            x = getX(angle,R);
            y = getY(angle,R);


            canvas.drawLine(oX, oY, x, y, paint);
            paint.setColor(nameOfColor[i]);
            canvas.drawArc(width/2-R,height/2-R,width/2+R,height/2+R, (float) angle, (float) angle2,true,paint);


            x = getX(angle+angle2/2,R+100);
            y = getY(angle+angle2/2,R+100);



            canvas.drawLine(oX,oY,x,y,paint);
            x = getX(angle+angle2/2,R+110);
            y = getY(angle+angle2/2,R+110);


            paint.setColor(Color.BLACK);
            canvas.drawText(angle2/3.6 + "%",x,y,paint);
            angle += angle2;
        }




        layout.setBackgroundDrawable(new BitmapDrawable(bg));
    }

    protected float getX(double angle,double R)
    {
        float x = 0;
        float oX = width/2;
        //float R = 400;
        angle = Math.toRadians(angle);

        x = (float)(oX+R*Math.cos(angle));

        return x;
    }

    protected float getY(double angle,double R)
    {
        float y = 0;
        float oY = height/2;
        //float R = 400;
        angle = Math.toRadians(angle);

        y = (float)(oY+R*Math.sin(angle));

        return y;
    }



}
