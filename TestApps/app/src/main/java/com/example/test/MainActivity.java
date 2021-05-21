package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.usage.ExternalStorageStats;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

//    Button btn;
//    TextureView textureView;
//    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
//    static {
//        ORIENTATIONS.append(Surface.ROTATION_0,90);
//        ORIENTATIONS.append(Surface.ROTATION_90,0);
//        ORIENTATIONS.append(Surface.ROTATION_180,270);
//        ORIENTATIONS.append(Surface.ROTATION_270,180);
//    }
//    private String cameraId;
//    CameraDevice cameraDevice;
//    CameraCaptureSession cameraCaptureSession;
//    CaptureRequest captureRequest;
//    CaptureRequest.Builder captureRequestBuilder;
//    private Size imageDimensions;
//    private ImageReader imageReader;
//    private File file;
//    Handler mBackgroundHandler;
//    HandlerThread mBackgroundTread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//        textureView = (TextureView) findViewById(R.id.texView);
//        btn = (Button) findViewById(R.id.btnPhoto);
//        textureView.setSurfaceTextureListener(textureListener);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//                {
//                    takePicture();
//                }
//        });
//
//    }
//
//    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
//            try {
//                openCamera();
//            }
//            catch (CameraAccessException e)
//            {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
//
//        }
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
//            return false;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
//
//        }
//    };
//
//    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void onOpened(CameraDevice camera) {
//            cameraDevice = camera;
//            try {
//                createCameraPreview();
//            }
//            catch (CameraAccessException e)
//            {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onDisconnected(CameraDevice camera) {
//            cameraDevice.close();
//        }
//
//        @Override
//        public void onError(CameraDevice camera, int error) {
//            cameraDevice.close();
//            cameraDevice = null;
//        }
//    };
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void createCameraPreview()
//    {
//        SurfaceTexture texture = textureView.getSurfaceTexture();
//        texture.setDefaultBufferSize(imageDimensions.getWidth(),imageDimensions.getHeight());
//        Surface surface = new Surface(texture);
//        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//        captureRequestBuilder.addTarget(surface);
//        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback()
//        {
//            @Override
//            public void onConfigured(CameraCaptureSession session)
//            {
//                if(cameraDevice == null)
//                {
//                    return;
//                }
//                cameraCaptureSession = session;
//                try {
//                    updatePreview();
//                } catch (CameraAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onConfigureFailed(CameraCaptureSession session)
//            {
//                Toast.makeText(getApplicationContext(),"Configuration Changed",Toast.LENGTH_LONG).show();
//            }
//        },null);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void updatePreview() throws CameraAccessException {
//        if(cameraDevice == null)
//        {
//            return;
//        }
//        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
//        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void openCamera() {
//        CameraManager manager = (CameraManager)getSystemService((Context.CAMERA_SERVICE));
//         cameraId = manager.getCameraIdList()[0];
//        CameraCharacteristics characteristics = manager.getCameraCharacteristics((cameraId));
//        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
//            return;
//        }
//        manager.openCamera(cameraId,stateCallback,null);
//    }
//
//    private void takePicture()
//    {
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(textureView.isAvailable())
//        {
//            try {
//                openCamera();
//            }
//            catch (CameraAccessException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else
//        {
//            textureView.setSurfaceTextureListener(textureListener);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }

    public static final String FILE_NAME = "mytext.txt";

    public void clicked(View v)
    {
        //External
        Toast.makeText(getApplicationContext(),"Btn clicked",Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory() + "/177013");
        if(!file.mkdir())
        {
            file.mkdir();
            Toast.makeText(getApplicationContext(),"Folder Created",Toast.LENGTH_SHORT).show();
        }
        String filepath = file.getAbsolutePath();
        TextView tvt = (TextView) findViewById(R.id.testText);
        tvt.setText(filepath);
        String text = "stuff here :3";
                File textFile = new File(Environment.getExternalStorageDirectory(),FILE_NAME);
                Toast.makeText(getApplicationContext(), textFile.toString(), Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream fos = new FileOutputStream(textFile);
                    fos.write(text.getBytes());
                    fos.close();
                    Toast.makeText(getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Cannot Shit ;-;", Toast.LENGTH_SHORT).show();
                }
//        Toast.makeText(getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Folder Path",Toast.LENGTH_SHORT).show();


//        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
//        {
//            Toast.makeText(getApplicationContext(), "Stotare State Positive :3", Toast.LENGTH_SHORT).show();
//            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            {
//                String text = "stuff here :3";
//                File textFile = new File(Environment.getExternalStorageDirectory(),FILE_NAME);
//                Toast.makeText(getApplicationContext(), textFile.toString(), Toast.LENGTH_SHORT).show();
//                try {
//                    FileOutputStream fos = new FileOutputStream(textFile);
//                    fos.write(text.getBytes());
//                    fos.close();
//                    Toast.makeText(getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Cannot Shit ;-;", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "Fuck doesn't write extertnal storage ....", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(), "Fuck ;-;", Toast.LENGTH_SHORT).show();
//        }



        //Internally

//        String txt = "Hi pls kill me :3";
//        FileOutputStream outputStream = null;
//        File file = new File(FILE_NAME);
//        try {
//            outputStream = openFileOutput(FILE_NAME,MODE_PRIVATE);
//            outputStream.write(txt.getBytes());
//            Toast.makeText(getApplicationContext(),outputStream.toString(),Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"File Create success",Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"File not created",Toast.LENGTH_LONG).show();
//        }finally {
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public boolean checkPermission(String permission)
    {
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void nextP (View v)
    {
        setContentView(R.layout.camera);
    }
}