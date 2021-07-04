package com.example.scan_it;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    int numPhotos = 0;
    String rootPath = Environment.getExternalStorageDirectory() + "/Scan-It";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
    }

    //PDF
    public void pdf(View v)
    {
        Button btnS = findViewById(R.id.btnSave);
        btnS.setVisibility(View.INVISIBLE);
        EditText pdfN = (EditText) findViewById(R.id.textBoxPDFName);
        String pdfName = pdfN.getText().toString();
        Bitmap bitmap;
        PdfDocument pdf = new PdfDocument();
        File pdfFolder = new File(rootPath + "/ScansPDF");
        if (!pdfFolder.mkdir())
        {
            pdfFolder.mkdir();
        }
            for (int a = 0 ; a < numPhotos ; a++)
            {
                String photo = rootPath + "/ScansTemp/" + a + ".jpg";
                bitmap = BitmapFactory.decodeFile(photo);
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), a).create();
                PdfDocument.Page page = pdf.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                canvas.drawBitmap(bitmap, 0, 0, null);
                pdf.finishPage(page);
            }
        File PDFFile = new File(pdfFolder + "/" + pdfName + ".pdf");
        try
        {
            pdf.writeTo(new FileOutputStream(PDFFile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        pdf.close();
        setContentView(R.layout.end_page);
        waitingPageSkipEnd();
     }
     //End PDF


    //Camera
    public void capturePhoto(View v)
    {
        Button btnF = findViewById(R.id.btnFinish);
        if(camera!=null)
        {
            camera.takePicture(null,null,mPictureCallback);
        }   
        btnF.setVisibility(View.VISIBLE);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            File picture_file = getOutputMediaFile();
            if(picture_file != null)
            {
                try
                {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();
                    camera.startPreview();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                return;
            }
        }
    };

    public File getOutputMediaFile()
    {
        File outputFile = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                File pathFolder = new File(rootPath);
                if(!pathFolder.mkdir())
                {
                    pathFolder.mkdir();
                }
                File photosFolder = new File(rootPath + "/ScansTemp");
                if (!photosFolder.mkdir())
                {
                    photosFolder.mkdir();
                }
                String photoName = String.valueOf(numPhotos) + ".jpg";
                File output = new File(photosFolder,photoName);
                numPhotos++;
                outputFile = output;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Can't write on external storage", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Please permit storage in the definitions", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Doesn't have external storage", Toast.LENGTH_SHORT).show();
        }
        return outputFile;
    }
    //End Camera

    public boolean checkPermission(String permission)
    {
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void goTWelcomePage(View v)
    {
        setContentView(R.layout.welcome_page);
    }

    public void goTMainPage(View v)
    {
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.cameraPreview);
        camera = Camera.open();
        showCamera = new ShowCamera(this,camera);
        frameLayout.addView(showCamera);
    }

    public void goTSavePage(View v)
    {
        setContentView(R.layout.waiting_page);
        waitingPageSkip();
    }

    public void waitingPageSkip()
    {
        CountDownTimer timer;
        timer = new CountDownTimer(10000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
//                Toast.makeText(getApplicationContext(), Long.toString(millisUntilFinished) , Toast.LENGTH_SHORT).show();
            }

            public void onFinish()
            {
                setContentView(R.layout.save_page);
            }
        }.start();
    }

    public void waitingPageSkipEnd()
    {
        CountDownTimer timer;
        timer = new CountDownTimer(10000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
            }

            public void onFinish()
            {
                setContentView(R.layout.welcome_page);
                numPhotos = 0;
            }
        }.start();
    }
}