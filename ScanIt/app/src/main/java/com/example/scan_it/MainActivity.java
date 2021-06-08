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
    int numPhotos = 0;
    String pF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
    }

    //PDF
    public void pfd(View v)
    {
        int lenghPAP = photosToAddPDF.length();
        int startLPAP = 0;
        EditText pdfN = (EditText) findViewById(R.id.textBoxPDFName);
        String pdfName = pdfN.getText().toString();
        PdfDocument pdf = new PdfDocument();
        for (int a = 1 ; a <= numPhotos ; a++)
        {
            for(int b = startLPAP ; b <= lenghPAP ; b++)
            {
                Toast.makeText(getApplicationContext(), String.valueOf(b), Toast.LENGTH_SHORT).show();
                String photo = pF + b;
//                Bitmap bitmap = BitmapFactory.decodeFile(photo);
                Bitmap bitmap = BitmapFactory.decodeFile((getOutputMediaFile()).toString());
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(5120,3840,a).create(); //Test dimensions :3
                PdfDocument.Page page = pdf.startPage(pageInfo);
                page.getCanvas().drawBitmap(bitmap,0,0,null);
                pdf.finishPage(page);
            }
            if(a == 1)
            {
                startLPAP = photosToAddPDF.indexOf("/");
            }
            else
            {
                startLPAP = photosToAddPDF.indexOf("/", photosToAddPDF.indexOf("/") + a);
            }
        }
//        File file = new File(pF + "/PDF_Scans/" + pdfName + ".pdf");
        String file = pF + "/PDF_Scans/hithere.pdf";
        File PDFFile = new File(file);
        Toast.makeText(getApplicationContext(), "Donne PDF", Toast.LENGTH_SHORT).show();
        try {
            pdf.writeTo(new FileOutputStream(PDFFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                pF = photosFolder.toString();
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
                numPhotos++;
                int locate_ = photosToAddPDF.indexOf("/");
                Toast.makeText(getApplicationContext(), photosToAddPDF, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), String.valueOf(numPhotos), Toast.LENGTH_SHORT).show();
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