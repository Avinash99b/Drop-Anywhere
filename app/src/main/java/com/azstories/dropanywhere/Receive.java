package com.azstories.dropanywhere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Receive extends AppCompatActivity {

    String result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        TextView textView = findViewById(R.id.text);
        EditText editText = findViewById(R.id.id);
        sendpost sendpost = new sendpost();
        ImageView copybtn = findViewById(R.id.copy);

        Button receivefile = findViewById(R.id.receivefile);


        Button receivebtn = findViewById(R.id.receivebtn);

        receivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = sendpost.gettexxt(editText.getText().toString());
                if(result.contains("File not found") && result.contains("Text not found")){

                    Toast.makeText(getApplicationContext(), "There is no Text or File with this id", Toast.LENGTH_SHORT).show();

                }else if (result.contains("File not found")){
                    Toast.makeText(getApplicationContext(), "There is no File with this id", Toast.LENGTH_SHORT).show();
                } else if (result.contains("Text not found")) {
                    Toast.makeText(getApplicationContext(), "There is no Text with this id", Toast.LENGTH_SHORT).show();

                }else {
                    textView.setText(result);
                }
            }
        });

        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                ClipData clipData = ClipData.newPlainText("text" ,result1 );

                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(Receive.this, "Copied data to clipboard successfully" , Toast.LENGTH_SHORT).show();

            }
        });


        receivefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().length() == 6){
                    Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://drophere.cloud/uploads/" + getfilenameforfile(Integer.valueOf(editText.getText().toString()) )));

                    AndroidNetworking.initialize(getApplicationContext());

                    AndroidNetworking.get("https://drophere.cloud/uploads/" + getfilenameforfile(Integer.valueOf(editText.getText().toString()))).setOkHttpClient(new OkHttpClient()).build().getAsOkHttpResponse(new OkHttpResponseListener() {
                        @Override
                        public void onResponse(Response response) {

                            String responsestring = response.toString();
                            Log.e("res", responsestring);

                            if(responsestring.contains("404")){
                                Log.e("error","file not found");

                                Toast.makeText(getApplicationContext(), "The given id not valid for file download", Toast.LENGTH_SHORT).show();
                            }else if (editText.getText().toString().length() != 6) {

                                Toast.makeText(getApplicationContext() , "Please enter File receiving code correctly" , Toast.LENGTH_SHORT).show();

                            }else {
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
                   // startActivity(intent);
                } else if (editText.getText().toString().length() != 6) {

                    Toast.makeText(getApplicationContext() , "Please enter File receiving code correctly" , Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public String getfilename(Integer fileid){

        sendpost sendpost = new sendpost();

        return sendpost.gettexxt(fileid.toString());
    }

    public String getfilenameforfile(Integer fileid){

        sendpost sendpost = new sendpost();

        return sendpost.gettexxt(fileid.toString() , "");
    }
}