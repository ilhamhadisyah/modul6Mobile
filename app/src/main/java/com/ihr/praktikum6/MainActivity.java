package com.ihr.praktikum6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText idtxt;
    private EditText namatxt;
    private EditText jurusantxt;
    private EditText emailtxt;
    private Button tambahbtn;
    private Button listbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idtxt = (EditText)findViewById(R.id.id);
        namatxt = (EditText)findViewById(R.id.nama);
        jurusantxt =(EditText)findViewById(R.id.jurusan);
        emailtxt = (EditText)findViewById(R.id.email);

        tambahbtn = (Button) findViewById(R.id.tambahbtn);
        listbtn = (Button) findViewById(R.id.listbtn);
        tambahbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("tambah data klik");
                tambahData();
            }
        });

        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("list data klik");
                startActivity(new Intent(MainActivity.this,listMahasiswa.class));
            }
        });
    }

    private void tambahData(){
        final String ID = idtxt.getText().toString().trim();
        final String NAMA = namatxt.getText().toString().trim();
        final String JURUSAN = jurusantxt.getText().toString().trim();
        final String EMAIL = emailtxt.getText().toString().trim();

        System.out.println(NAMA);

        class tambahMhs extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(config.KEY_ID,ID);
                params.put(config.KEY_NAMA,NAMA);
                params.put(config.KEY_JURUSAN,JURUSAN);
                params.put(config.KEY_EMAIL,EMAIL);
                requestHandler rh = new requestHandler();
                String res = rh.sendPostRequest(config.URL_ADD,params);
                return res;
            }
        }
        tambahMhs tambahMhs = new tambahMhs();
        tambahMhs.execute();
    }
    @Override
    public void onClick(View v){

        /*
        if (v==tambahbtn){
            System.out.println("tambah data klik");
            tambahData();
        }
        if (v==listbtn){
            System.out.println("list data klik");
            startActivity(new Intent(MainActivity.this,listMahasiswa.class));
        }

         */
    }
}
