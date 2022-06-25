package com.inyongtisto.tokoonline.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inyongtisto.tokoonline.R;
import com.williamww.silkysignature.UI.SilkySignaturePad;
import com.inyongtisto.tokoonline.networking.ApiConfig;
import com.inyongtisto.tokoonline.networking.AppConfig;
import com.inyongtisto.tokoonline.networking.ServerResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signature extends AppCompatActivity {
    private SilkySignaturePad mSilkySignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private Button mCompressButton;
    private String postPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSilkySignaturePad = findViewById(R.id.signature_pad);
        mSilkySignaturePad.setOnSignedListener(new SilkySignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(signature.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
                mCompressButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
                mCompressButton.setEnabled(false);
            }
        });
        mClearButton = findViewById(R.id.clear_button);
        mSaveButton = findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSilkySignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSilkySignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(signature.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                    uploadFile();
                } else {
                    Toast.makeText(signature.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                if (addSvgSignatureToGallery(mSilkySignaturePad.getSignatureSvg())) {
                    Toast.makeText(signature.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(signature.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCompressButton = findViewById(R.id.compress_button);
        mCompressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = mSilkySignaturePad.getCompressedSignatureBitmap(50);
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(signature.this, "50% compressed signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(signature.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        postPath=file.getAbsolutePath();
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    @SuppressLint("DefaultLocale")
    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);

            scanMediaFile(photo);

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        signature.this.sendBroadcast(mediaScanIntent);
       postPath= contentUri.getPath();
    }

    @SuppressLint("DefaultLocale")
    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void uploadFile() {
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
            return;
        } else {


            // Map is used to multipart the file using okhttp3.RequestBody
            Map<String, RequestBody> map = new HashMap<>();
            File file = new File(postPath);

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<ServerResponse> call = getResponse.upload("token", map);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){

                            ServerResponse serverResponse = response.body();
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }else {

                        Toast.makeText(getApplicationContext(), "problem uploading image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.v("Response gotten is", t.getMessage());
                }
            });
        }
    }
public void InsertData(){


}
}