package edu.monash.fit5046.fit5046a2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieActivity extends AppCompatActivity {
    private ImageView ivMovie;
    private TextView tvMovie;
    private String favouriteMovieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        setTitle("Movie");

        Intent intent = getIntent();
        favouriteMovieName = intent.getStringExtra("fMovie");


        ivMovie = (ImageView) findViewById(R.id.ivMovie);
        tvMovie = (TextView) findViewById(R.id.tvMovie);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params)
            {
                return GoogleSearch.getMovieImagePath(params[0]);
            }

            @Override
            protected void onPostExecute(String link)
            {
                new AsyncTask<String, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(String... params) {
                        return getURLimage(params[0]);
                    }

                    @Override
                    protected void onPostExecute (Bitmap bitmap) {
                        ivMovie.setImageBitmap(bitmap);
                        setMovieIntro();

                    }
                }.execute(link);
            }
        }.execute(favouriteMovieName);
    }

    public void setMovieIntro() {
        new AsyncTask<String, Void, String[]>() {
            @Override
            protected String[] doInBackground(String... params) {
                return (GoogleSearch.getMovieIntro(params[0]));
            }

            @Override
            protected void onPostExecute (String[] array) {
                String intro = array[0].substring(0, array[0].length() - 3);
                tvMovie.setText("Introduction: " + intro + "[find more on " + array[1] + "]");
            }
        }.execute(favouriteMovieName);
    }



    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);//not store in hard drive
            conn.connect();
            InputStream is = conn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
