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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    String currentPhotoPath;
    Bitmap sbmp;
    String testText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.cameraPreview);
        camera = Camera.open();
        showCamera = new ShowCamera(this,camera);
        frameLayout.addView(showCamera);
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
            Toast.makeText(getApplicationContext(),"Shutter clicked",Toast.LENGTH_SHORT).show();
        }
        camera.stopPreview();
        camera.startPreview();


//        if(camera!=null)
//        {
//            camera.takePicture(null,null,mPictureCallback);
//            Toast.makeText(getApplicationContext(),"Shutter clicked",Toast.LENGTH_SHORT).show();
//        }
//        camera.stopPreview();
//        camera.startPreview();


        btnF.setVisibility(View.VISIBLE);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile();
            Toast.makeText(getApplicationContext(), picture_file.toString(), Toast.LENGTH_SHORT).show();
            if(picture_file == null)
            {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                return;
            }
                try
                {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();

                    Toast.makeText(getApplicationContext(), "Photo saved II", Toast.LENGTH_SHORT).show();

                    camera.startPreview();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    };

    public File getOutputMediaFile()
    {
        File outputFile = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            Toast.makeText(getApplicationContext(), "Has External Storage", Toast.LENGTH_SHORT).show();
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                File pathFolder = new File(Environment.getExternalStorageDirectory() + "/Scan-It");
                if(!pathFolder.mkdir())
                {
                    pathFolder.mkdir();
                    Toast.makeText(getApplicationContext(),"Root Folder Created",Toast.LENGTH_SHORT).show();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File output = new File(pathFolder,"IMG_" + timeStamp);
                Toast.makeText(getApplicationContext(), "Photo Created " + output.toString(), Toast.LENGTH_SHORT).show();
                outputFile = output;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Can't write on external storage", Toast.LENGTH_SHORT).show();
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
    }

    public void goTSavePage(View v)
    {
        setContentView(R.layout.save_page);
        TextView tvt = (TextView)findViewById(R.id.textTestPage);
        tvt.setText(testText);
    }
}