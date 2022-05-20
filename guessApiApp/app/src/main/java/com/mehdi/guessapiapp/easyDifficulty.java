package com.mehdi.guessapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class easyDifficulty extends AppCompatActivity {
    ImageView img;
    Pattern p;
    Matcher m;
    Random rand = new Random();
    int randint = rand.nextInt(101);
    int buttonrand = rand.nextInt(4);
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> alts = new ArrayList<>();
    ArrayList<String> finalimages = new ArrayList<>();
    ArrayList<String> altimg = new ArrayList<>();
    Button btn;
    Button btn2;
    Button btn3;
    Button btn4;
    int playcount = 0;
    TextView playcounttxt;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    result += line;
//                    Log.i("Test", line);
                }

//                Log.i("Test", result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }


            //Setting connection to server
//            return null;
        }


    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();

                Bitmap DownloadedImage = BitmapFactory.decodeStream(in);

                return DownloadedImage;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
    }

    public void btnclick(View view) {
        Button button = (Button) view;
        if (playcount < 30) {
            playcount++;
            if (button.getText().equals(altimg.get(randint))) {

                Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                randint = rand.nextInt(101);
                button.setText(altimg.get(randint));


                buttonrand = rand.nextInt(4);
                switch (buttonrand) {
                    case 0:
                        btn.setText(altimg.get(randint));
                        btn2.setText(altimg.get(rand.nextInt(101)));
                        btn3.setText(altimg.get(rand.nextInt(101)));
                        btn4.setText(altimg.get(rand.nextInt(101)));
                        break;
                    case 1:
                        btn2.setText(altimg.get(randint));
                        btn.setText(altimg.get(rand.nextInt(101)));
                        btn3.setText(altimg.get(rand.nextInt(101)));
                        btn4.setText(altimg.get(rand.nextInt(101)));
                        break;
                    case 2:
                        btn3.setText(altimg.get(randint));
                        btn2.setText(altimg.get(rand.nextInt(101)));
                        btn.setText(altimg.get(rand.nextInt(101)));
                        btn4.setText(altimg.get(rand.nextInt(101)));
                        break;
                    case 3:
                        btn4.setText(altimg.get(randint));
                        btn2.setText(altimg.get(rand.nextInt(101)));
                        btn3.setText(altimg.get(rand.nextInt(101)));
                        btn.setText(altimg.get(rand.nextInt(101)));
                        break;

                }
                img = (ImageView) findViewById(R.id.imageView);
                ImageDownloader task2 = new ImageDownloader();
                Bitmap downloadedImage;
                try {

                    downloadedImage = task2.execute(finalimages.get(randint)).get();
                    img.setImageBitmap(downloadedImage);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
            }
        } else {
            playcounttxt = (TextView) findViewById(R.id.playcounttext);
            playcounttxt.setText("Play count ended!");
            btn.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btn4.setVisibility(View.INVISIBLE);
        }
    }

    public void getImagesAndAlts() {
        DownloadTask task = new DownloadTask();
        String result = null;

        try {

            result = task.execute("https://www.pcmag.com/picks/best-android-apps").get();

        } catch (Exception e) {
            e.printStackTrace();
        }

        p = Pattern.compile("<img src=\"(.*?)\" alt=\"(.*?)\"");
        m = p.matcher(result);


        while (m.find()) {
            images.add(m.group(1));
            alts.add(m.group(2));
        }


        p = Pattern.compile("data-image-loader=\"(.*$)");
        for (int i = 0; i < images.size(); i++) {
            m = p.matcher(images.get(i));
            while (m.find())
                finalimages.add(m.group(1));
        }

        for (int i = 0; i < finalimages.size(); i++) {
            Log.i("finalimgs array: ", finalimages.get(i));
        }
//        Log.i("Alts array: ", alts.toString());

        for (int i = 0; i < alts.size(); i++) {
            if (alts.get(i).contains("Image")) {
                String x = alts.get(i).substring(0, alts.get(i).lastIndexOf(" "));
                altimg.add(x);
//                altimg.add(alts.get(i));
            }
        }

        Log.i("Alt text Array:", altimg.toString());
    }

    public void decodeImage() {
        img = (ImageView) findViewById(R.id.imageView);
        ImageDownloader task2 = new ImageDownloader();
        Bitmap downloadedImage;
        try {

            downloadedImage = task2.execute(finalimages.get(randint)).get();
            img.setImageBitmap(downloadedImage);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        getImagesAndAlts();
        decodeImage();

        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button4);
        btn4 = (Button) findViewById(R.id.button3);

        switch (buttonrand) {
            case 0:
                btn.setText(altimg.get(randint));
                btn2.setText(altimg.get(rand.nextInt(101)));
                btn3.setText(altimg.get(rand.nextInt(101)));
                btn4.setText(altimg.get(rand.nextInt(101)));
                break;
            case 1:
                btn2.setText(altimg.get(randint));
                btn.setText(altimg.get(rand.nextInt(101)));
                btn3.setText(altimg.get(rand.nextInt(101)));
                btn4.setText(altimg.get(rand.nextInt(101)));
                break;
            case 2:
                btn3.setText(altimg.get(randint));
                btn2.setText(altimg.get(rand.nextInt(101)));
                btn.setText(altimg.get(rand.nextInt(101)));
                btn4.setText(altimg.get(rand.nextInt(101)));
                break;
            case 3:
                btn4.setText(altimg.get(randint));
                btn2.setText(altimg.get(rand.nextInt(101)));
                btn3.setText(altimg.get(rand.nextInt(101)));
                btn.setText(altimg.get(rand.nextInt(101)));
                break;

        }

    }
}