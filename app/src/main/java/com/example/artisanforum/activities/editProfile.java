package com.example.artisanforum.activities;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.artisanforum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class editProfile extends AppCompatActivity {
    EditText name, mobile, type;
    DatabaseReference ref;
    FirebaseAuth auth;
    private static final int RQS_OPEN = 1;
    Uri uris;
    private String fileNameList;
    StorageReference ster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name_text);
        mobile = findViewById(R.id.mobile_text);
        type = findViewById(R.id.type_text);
        ster = FirebaseStorage.getInstance().getReference();
        findViewById(R.id.post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                ref.child("name").setValue(name.getText().toString());
                ref.child("phone").setValue(mobile.getText().toString());
                ref.child("type").setValue(type.getText().toString());
//                final String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
//                ref.child("timestamp").setValue(timeStamp);
                StorageReference filetoupload = ster.child("images").child(fileNameList);
                filetoupload.putFile(uris)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ref.child("proimg").setValue(uri.toString());
                                    }
                                });
                            }
                        });
                finish();
            }
        });

        findViewById(R.id.pro_pic).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        String[] extraMimeTypes = {"image/*", "camera/*"};
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent, RQS_OPEN);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        StringBuilder s = new StringBuilder();
        if (resultCode == RESULT_OK) {
            if (requestCode == RQS_OPEN) {
                ClipData clipData = data.getClipData();
                if (clipData == null) {
                    s = new StringBuilder("clipData == null\n");
                    s.append(Objects.requireNonNull(data.getData()).toString());
                    Uri uri = data.getData();
                    uris = uri;
                    String fileName = getFileName(uri);
                    fileNameList = fileName;
                }
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getApplicationContext().getContentResolver()
                    .query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
