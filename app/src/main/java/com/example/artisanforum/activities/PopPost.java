package com.example.artisanforum.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artisanforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PopPost extends Activity {
    EditText editText;
    Button btn;
    ImageButton btn_;
    TextView addfiletext, textUri;
    private static final int RQS_OPEN = 1;
    RadioGroup grp;
    RadioButton filesYes, filesNo, radioButton;
    FirebaseAuth auth;
    ArrayList<Uri> uris = new ArrayList<>();
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    StorageReference ster;
    ProgressDialog progressDialog;
    private List<String> fileNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_post);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText = findViewById(R.id.description);
        btn = findViewById(R.id.posting);
        grp = findViewById(R.id.radiogroup);
        filesYes = findViewById(R.id.radiobtnyes);
        filesNo = findViewById(R.id.radiobtnno);
        btn_ = findViewById(R.id.files);
        addfiletext = findViewById(R.id.addfiletext);
        textUri = findViewById(R.id.filenames);
        fileNameList = new ArrayList<>();
        ster = FirebaseStorage.getInstance().getReference();
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                if (radioButton == filesYes) {
                    btn_.setVisibility(View.VISIBLE);
                    addfiletext.setVisibility(View.GONE);
                } else {
                    btn_.setVisibility(View.GONE);
                    addfiletext.setVisibility(View.VISIBLE);
                }
            }
        });

        final DatabaseReference ref = mRootref.child("post").push();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userposts = FirebaseDatabase.getInstance().getReference().child("Users").child("posts");
                userposts.child(ref.getKey()).setValue(ref.getKey());
                if (btn_.getVisibility() != View.VISIBLE) {
                    String x = editText.getText().toString();
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    DatabaseReference dataRef = mRootref.child("post").child("images");
                    ref.child("descrip").setValue(x);
                    ref.child("timestamp").setValue(timeStamp);
                    dataRef.child(timeStamp).setValue("null");
                    DatabaseReference userPosts  = FirebaseDatabase.getInstance().getReference();

                    finish();
                } else {
                    progressDialog = new ProgressDialog(PopPost.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Uploading file...");
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    String x = editText.getText().toString();
                    final String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    ref.child("descrip").setValue(x);
                    ref.child("timestamp").setValue(timeStamp);
                    final DatabaseReference dataRef = mRootref.child("post").child("images");
                    final int[] currentProgress = {0};
                    for (int i = 0; i < fileNameList.size(); i++) {
                        StorageReference filetoupload = ster.child("images").child(fileNameList.get(i));
                        final int finalI = i;
                        final int finalI1 = i;
                        filetoupload.putFile(uris.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String photoStringLink = uri.toString();
                                        dataRef.child(timeStamp).child(String.valueOf(finalI1)).setValue(photoStringLink).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PopPost.this, "Failed to upload file " + fileNameList.get(finalI), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PopPost.this, "Failed to upload any files", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                currentProgress[0] = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setProgress(currentProgress[0]);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(PopPost.this, "Done upoading ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                }
            }
        });
        btn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                String[] extraMimeTypes = {"image/*", "video/*", "audio/*", "text/*",
                        "application/*", "./*", "camera/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, RQS_OPEN);
                Toast.makeText(PopPost.this,
                        "Single-selection: Tap on any file.\n" +
                                "Multi-selection: Tap & Hold on the first file, " +
                                "tap for more, tap on OPEN to finish.",
                        Toast.LENGTH_LONG).show();
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
                    uris.add(uri);
                    String fileName = getFileName(uri);
                    fileNameList.add(fileName);
                } else {
                    s = new StringBuilder("clipData != null\n");
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        uris.add(uri);
                        String fileName = getFileName(uri);
                        fileNameList.add(fileName);
                        s.append(uri.toString()).append("\n");
                    }
                }
            }
        }
        textUri.setText(s.toString());
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