package com.azstories.dropanywhere;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import okhttp3.Response;

public class PostSender extends AppCompatActivity {

     File classfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sender);
        Button btn = findViewById(R.id.posttext);
        EditText edt = findViewById(R.id.editTextTextMultiLine);
        Button filebtn = findViewById(R.id.file);
        Button sendfile = findViewById(R.id.btn1);

        TextView filepathview = findViewById(R.id.filepathview);

        ProgressBar progressBar = findViewById(R.id.progress);
        TextView percentview = findViewById(R.id.percentview);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        sendpost sendpost = new sendpost();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.codeview);
               if(edt.getText().toString().length() > 0){
                   String result = sendpost.posttext(edt.getText().toString().replaceAll("\n", "<br>"));

                   textView.setText(result);
               }else {
                   toaster("Please enter text to continue");
               }


            }
        });


        filebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            String path = Environment.getExternalStorageDirectory() + "/" + "Downloads" + "/";
                            Uri uri = Uri.parse(path);
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setDataAndType(uri, "*/*");

                        }
                        startActivityForResult(intent , 1001);
            }
        });

        sendfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classfile != null) {

                    AndroidNetworking.initialize(getApplicationContext());

                    Log.e("Progress", "Begin");


                    try {
                        AndroidNetworking.upload("https://drophere.cloud/appdefault.php").setPriority(Priority.HIGH).addMultipartFile("file" , classfile).build().setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long l, long l1) {
                                Log.e("Progress", String.valueOf(l));
                                Log.e("Progress 2", String.valueOf(l1));

                                progressBar.setMax((int) l1);

                                progressBar.setProgress((int) l, true);

                                String progressed = String.valueOf(Integer.valueOf((int) ((l * 100) / l1)));
 //                                progressed = String.valueOf(Integer.valueOf(progressed));


                                percentview.setText(progressed + "% " + "uploaded." );
                            }
                        }).getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                            @Override
                            public void onResponse(Response response, String s) {
                                Log.e("res from me " ,  s);

                                TextView codeview = findViewById(R.id.codeview);

                                codeview.setText(s);
                            }

                            @Override
                            public void onError(ANError anError) {

                                Log.e("error uploading", anError.toString());

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    toaster("Please select a file to continue");
                }
                }
        });
    }
    public void toaster(String text){
        Toast.makeText(PostSender.this, text, Toast.LENGTH_SHORT).show();
    }
    public void toaster(String text , Context context){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {

            try {

                int uploaded = 0 , progress = 0 ;

                Uri fileUri = data.getData();
                String fileName = FileUtil.getFileName(getApplicationContext(), fileUri);

                FileInputStream fileInputStream = (FileInputStream) getContentResolver().openInputStream(fileUri);
                final int filesize = fileInputStream.available();



                if (fileInputStream != null) {
                    File file = new File(getFilesDir() + "/" + fileName);

                    if (file.exists()) {
                        file.delete();
                    }



                    file.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = fileInputStream.read(buffer)) > 0) {
                        Log.e("progress" , String.valueOf(length));
                        fileOutputStream.write(buffer, 0, length);
                        uploaded+=length;
                        progress = ((uploaded*100)/filesize);



                    }

                    toaster(String.valueOf(progress) );
                    fileOutputStream.close();
                    fileInputStream.close();



                    this.classfile = file;


                }

            } catch (Exception e) {
//                toaster("File not found");
            }

        super.onActivityResult(requestCode, resultCode, data);
    }
    class handlefilebg extends  Thread{

        Intent data;

        File classfile;

        public handlefilebg(@Nullable Intent data){
            this.data = data;
        }
        public handlefilebg(){
        }



        public void run(){

            try {

                int uploaded = 0 , progress = 0 ;

                Uri fileUri = data.getData();
                String fileName = FileUtil.getFileName(getApplicationContext(), fileUri);

                FileInputStream fileInputStream = (FileInputStream) getContentResolver().openInputStream(fileUri);
                final int filesize = fileInputStream.available();



                if (fileInputStream != null) {
                    File file = new File(getFilesDir() + "/" + fileName);

                    if (file.exists()) {
                        file.delete();
                    }



                    file.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = fileInputStream.read(buffer)) > 0) {
                        Log.e("progress" , String.valueOf(length));
                        fileOutputStream.write(buffer, 0, length);
                        uploaded+=length;
                        progress = ((uploaded*100)/filesize);



                    }

                    toaster(String.valueOf(progress) );
                    fileOutputStream.close();
                    fileInputStream.close();



                   this.classfile = file;


                }

            } catch (Exception e) {
//                toaster("File not found");
            }
        }
        public String getfilename() {

            Uri fileUri = data.getData();
            String fileName = FileUtil.getFileName(getApplicationContext(), fileUri);

            return fileName;
        }

        public File getfile(){
            return this.classfile;
        }

    }

}

}