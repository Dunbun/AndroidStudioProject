package com.example.taras.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetData extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat gd;
    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    File selected = null;
    File root;
    File curFolder;

    int width = 0;
    int height = 0;
    int heightOfWindow = 0;


    int[] nameOfMonthColor = new int[12];
    String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    final String[] JSON_STRING = {""};
    StringBuilder text = new StringBuilder();
    private List<String> fileList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);


        gd = new GestureDetectorCompat(this, this);
        gd.setOnDoubleTapListener(this);


        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        curFolder = root;


    }


    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(GetData.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);


                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selected = new File(fileList.get(position));
                        if (selected.isDirectory()) {
                            ListDir(selected);
                        } else {


                            File file = new File(selected.getParentFile(), selected.getName());


                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;

                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();
                            } catch (IOException e) {

                            }


                            drawFunc();



                            dismissDialog(CUSTOM_DIALOG_ID);
                        }
                    }

                });
                break;

        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }

    void ListDir(File f) {
        if (f.equals(root)) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        if (f.listFiles() != null) {
            File[] files = f.listFiles();
            fileList.clear();
            for (File file : files) {
                fileList.add(file.getAbsolutePath());
                //fileList.add(file.getName());
            }
        }


        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LinearLayout var = (LinearLayout) findViewById(R.id.draw);
        width = var.getWidth();
        height = var.getHeight();
        var = (LinearLayout) findViewById(R.id.Window);
        heightOfWindow = var.getHeight();

    }

    public void drawFunc() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.draw);
        Paint paint = new Paint();
        paint.setStrokeWidth(width / 12);
        Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);


        nameOfMonthColor[0] = getColor(R.color.January);
        nameOfMonthColor[1] = getColor(R.color.February);
        nameOfMonthColor[2] = getColor(R.color.March);
        nameOfMonthColor[3] = getColor(R.color.April);
        nameOfMonthColor[4] = getColor(R.color.May);
        nameOfMonthColor[5] = getColor(R.color.June);
        nameOfMonthColor[6] = getColor(R.color.July);
        nameOfMonthColor[7] = getColor(R.color.August);
        nameOfMonthColor[8] = getColor(R.color.September);
        nameOfMonthColor[9] = getColor(R.color.October);
        nameOfMonthColor[10] = getColor(R.color.November);
        nameOfMonthColor[11] = getColor(R.color.December);

        paint.setTextSize(40);

        float[] arr = new float[13];
        float max = 0;
        arr = getSpentMoney(12);
        float convert = 0;

        for (int i = 0; i < 12; i++) {
            arr[i] = (float) (Math.floor(arr[i] * 100) / 100);
            if (max < arr[i])
                max = arr[i];
        }
        convert = (height - paint.getTextSize() * 6) / max;


        for (int i = 0; i < 12; i++) {
            paint.setColor(nameOfMonthColor[i]);
            canvas.drawLine(i * width / 12 + 45, height, i * width / 12 + 45, height - convert * arr[i] - paint.getTextSize() * 6, paint);

            paint.setColor(getColor(R.color.Black));
            paint.setShadowLayer(1, 0, 1, Color.parseColor("#000000"));
            canvas.rotate(-90, (i * width / 12 + 55), height - 10);
            canvas.drawText(monthName[i], (i * width / 12 + 55), height - 10, paint);
            canvas.rotate(90, (i * width / 12 + 55), height - 10);

            canvas.rotate(-90, (i * width / 12 + 55), height - paint.getTextSize() * 6 - 10);
            canvas.drawText(Float.toString(arr[i]), (i * width / 12 + 55), height - paint.getTextSize() * 6 - 10, paint);
            canvas.rotate(90, (i * width / 12 + 55), height - paint.getTextSize() * 6 - 10);


        }

        paint.setStrokeWidth(2);
        canvas.drawLine(0, height - paint.getTextSize() * 6, width, height - paint.getTextSize() * 6, paint);


        layout.setBackgroundDrawable(new BitmapDrawable(bg));


    }

    public float[] getSpentMoney(int month) {


        float[] sum = new float[13];
        JSON_STRING[0] = text.toString();
        int monthFromData = 0;
        String date;

        if (month < 0 || month > 12)
            Toast.makeText(GetData.this, "Error!", Toast.LENGTH_SHORT).show();
        else {

            if (month == 12) {
                try {

                    JSONArray payment = new JSONArray(JSON_STRING[0]);
                    payment.remove(0);//
                    payment.remove(0);//
                    payment.remove(0);//remove trash
                    JSONObject obj = payment.getJSONObject(0);


                    for (int i = 0; i < payment.length(); i++) {
                        obj = payment.getJSONObject(i);
                        date = obj.getString("FIELD1");
                        monthFromData = Character.getNumericValue(date.charAt(3)) + Character.getNumericValue(date.charAt(4)) - 1;
                        sum[monthFromData] -= obj.getDouble("FIELD6");
                        sum[month] -= obj.getDouble("FIELD6");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                try {

                    JSONArray payment = new JSONArray(JSON_STRING[0]);
                    JSONObject obj = payment.getJSONObject(3);


                    for (int i = 0; i < payment.length(); i++) {
                        obj = payment.getJSONObject(i);
                        date = obj.getString("FIELD1");
                        monthFromData = Character.getNumericValue(date.charAt(3)) + Character.getNumericValue(date.charAt(4));
                        if (monthFromData - 1 == month)
                            sum[month] -= obj.getDouble("FIELD6");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        return sum;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        gd.onTouchEvent(event);
        return  super.onTouchEvent(event);
    }

     public boolean onSingleTapConfirmed(MotionEvent e)
    {
        TextView txt = (TextView)findViewById(R.id.textView);
        int x = (int)e.getX();
        int y = (int)e.getY();
        int i = 0;

        if(selected!=null && selected.isFile()) {
            i = x / (width / 12);

           if (y > heightOfWindow - 240 ) {
               Intent intent = new Intent(GetData.this, MonthStat.class);
               Bundle b = new Bundle();
               b.putString("monthname",monthName[i]);
               b.putInt("month",i);

               try {
                   JSONArray monthstat = new JSONArray(JSON_STRING[0]);
                   ;
                   intent.putExtra("monthstat", monthstat.toString());

               } catch (JSONException e1) {
                   e1.printStackTrace();
               }


               intent.putExtras(b);
               startActivity(intent);

           }
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}
