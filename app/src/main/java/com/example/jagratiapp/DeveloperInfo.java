package com.example.jagratiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;

public class DeveloperInfo extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        MaterialToolbar toolbar = findViewById(R.id.developerToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeveloperInfo.super.onBackPressed();
            }
        });

        ImageView harshgithub = findViewById(R.id.harsh_image);
        harshgithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/harsh-hash");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.github.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/harsh-hash")));
                }

            }
        });

        ImageView khushalGithub = findViewById(R.id.khushal_image);
        khushalGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/khushal1707");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.github.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/khushal1707")));
                }

            }
        });

        ImageView akshayGithub = findViewById(R.id.akshay_image);
        akshayGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/AkshaySh007");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.github.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/AkshaySh007")));
                }

            }
        });

        ImageView repo = findViewById(R.id.contribute);
        repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/Jagrati-IIITDMJ/Jagrati-Android-App");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.github.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/Jagrati-IIITDMJ/Jagrati-Android-App")));
                }
            }
        });

       /* ImageButton harshInsta = findViewById(R.id.harsh_insta);
        ImageButton khushalInsta = findViewById(R.id.khushal_insta);
        ImageButton akshayInsta = findViewById(R.id.akshay_insta);

        ImageButton harshLink = findViewById(R.id.harsh_link);
        ImageButton khushalLink = findViewById(R.id.khushal_link);
        ImageButton akshayLink = findViewById(R.id.akshay_link);


        harshInsta.setOnClickListener(this);
        khushalInsta.setOnClickListener(this);
        akshayInsta.setOnClickListener(this);
        harshLink.setOnClickListener(this);
        khushalLink.setOnClickListener(this);
        akshayLink.setOnClickListener(this);*/

    }

 /*  @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.harsh_insta:
                Uri uri = Uri.parse("http://instagram.com/_u/har_sh_99");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/har_sh_99")));
                }
                break;

            case R.id.khushal_insta:
                Uri uri1 = Uri.parse("http://instagram.com/_u/khu.xhal");
                Intent likeIng1 = new Intent(Intent.ACTION_VIEW, uri1);

                likeIng1.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng1);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/khu.xhal")));
                }
                break;

            case R.id.akshay_insta:
                Uri uri2 = Uri.parse("http://instagram.com/_u/siel_jugador");
                Intent likeIng2 = new Intent(Intent.ACTION_VIEW, uri2);

                likeIng2.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng2);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/siel_jugador")));
                }
                break;


            case R.id.harsh_link:
                Uri urilink2 = Uri.parse("https://www.linkedin.com/in/harsh-chaurasiya-1ba2141a4");
                Intent likeIng5 = new Intent(Intent.ACTION_VIEW, urilink2);

                likeIng5.setPackage("com.linkedin.android");

                try {
                    startActivity(likeIng5);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.linkedin.com/in/harsh-chaurasiya-1ba2141a4")));
                }
                break;

            case R.id.khushal_link:
                Uri urilink3 = Uri.parse("https://www.linkedin.com/in/khushal-uttam-631806135");
                Intent likeIng6 = new Intent(Intent.ACTION_VIEW, urilink3);

                likeIng6.setPackage("com.linkedin.android");

                try {
                    startActivity(likeIng6);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.linkedin.com/in/khushal-uttam-631806135")));
                }
                break;

            case R.id.akshay_link:
                Uri urilink1 = Uri.parse("https://www.linkedin.com/in/akshay-sharma-a51a98185");
                Intent likeIng4 = new Intent(Intent.ACTION_VIEW, urilink1);

                likeIng4.setPackage("com.linkedin.android");

                try {
                    startActivity(likeIng4);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.linkedin.com/in/akshay-sharma-a51a98185")));
                }
                break;
        }
    }*/
}