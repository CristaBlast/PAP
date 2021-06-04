package com.example.scan_it;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Environment;
import android.provider.MediaStore;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    String photosToAddPDF = "";
    Bitmap sbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        //Important bmp = BitmapFactory.decodeResource(getResources(),)//path)

    }

    //PDF
    public void pfd(View v)
    {
        String pdfName;
        EditText pdfN = (EditText) findViewById(R.id.textBoxPDFName);
        pdfName = pdfN.getText().toString();
        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        int numPhotos;
        for()



        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400,600,1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(sbmp,400,600,paint);




        File file = new File(Environment.getExternalStorageDirectory(),"/" + pdfName + ".pdf");



        
        pdf.finishPage(page);
        pdf.close();
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String folderNameDate = new SimpleDateFormat ("dd_MM_yyyy").format(new Date());
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            Toast.makeText(getApplicationContext(), "Has External Storage", Toast.LENGTH_SHORT).show();
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                File pathFolder = new File(Environment.getExternalStorageDirectory() + "/Scan-It");
                if(!pathFolder.mkdir())
                {
                    pathFolder.mkdir();
                }
                File photosFolder = new File(pathFolder + "/" + folderNameDate);
                if (!photosFolder.mkdir())
                {
                    photosFolder.mkdir();
                }
                String photoName = "IMG_" + timeStamp + ".jpg";
                File output = new File(photosFolder,photoName);
                if(photosToAddPDF == "")
                {
                    photosToAddPDF = photoName;
                }
                else
                {
                    photosToAddPDF = photosToAddPDF + "/" + photoName;
                }
                Toast.makeText(getApplicationContext(), photosToAddPDF, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.save_page);
        TextView tvt = (TextView)findViewById(R.id.textTestPage);
    }
}