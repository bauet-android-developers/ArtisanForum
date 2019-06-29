package com.example.artisanforum.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.artisanforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PopPost extends Activity {
    EditText editText;
    Button btn;
    ImageButton btn_;
    private static final int RQS_OPEN = 1;
    FirebaseAuth auth;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_post);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        editText = findViewById(R.id.description);
        btn = findViewById(R.id.posting);

        final DatabaseReference ref = mRootref.child("post").push();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = editText.getText().toString();
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                ref.child("descrip").setValue(x);
                ref.child("timestamp").setValue(timeStamp);
                finish();
            }
        });
//        btn_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                String[] extraMimeTypes = {"image/*", "video/*", "audio/*", "text/*",
//                        "application/*", "./*", "camera/*"};
//                intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(intent, RQS_OPEN);
//                Toast.makeText(PopPost.this,
//                        "Single-selection: Tap on any file.\n" +
//                                "Multi-selection: Tap & Hold on the first file, " +
//                                "tap for more, tap on OPEN to finish.",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }
}