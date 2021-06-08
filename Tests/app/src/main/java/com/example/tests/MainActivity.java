package com.example.tests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PDF(View view)
    {


        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Scan-It/07_06_2021/IMG_20210607_164925.jpg");
//        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Scan-It/07_06_2021/IMG_20210607_164925.jpg");
        PdfDocument pdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(5120,3840,1).create(); //Test dimensions :3
        PdfDocument.Page page = pdf.startPage(pageInfo);
        page.getCanvas().drawBitmap(bitmap,0,0,null);
        pdf.finishPage(page);
        String file = Environment.getExternalStorageDirectory() + "/Scan-It/PDF_Scans/hithere.pdf";
        File PDFFile = new File(file);
        try {
            pdf.writeTo(new FileOutputStream(PDFFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Donne PDF", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),file,Toast.LENGTH_SHORT).show();
        pdf.close();
    }
}