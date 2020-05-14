package com.ihr.praktikum6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class select extends AppCompatActivity implements View.OnClickListener {

    private EditText idtxt;
    private EditText namatxt;
    private EditText jurusantxt;
    private EditText emailtxt;
    private Button update;
    private Button delete;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Intent intent = getIntent();

        id = intent.getStringExtra(config.MAHASISWA_ID);
        idtxt = findViewById(R.id.idmhs);
        namatxt = findViewById(R.id.namamhs);
        jurusantxt = findViewById(R.id.jurusanmhs);
        emailtxt = findViewById(R.id.emailmhs);

        update = findViewById(R.id.button);
        delete = findViewById(R.id.button2);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);

        idtxt.setText(id);

        getMahasiswa();

    }

    private void getMahasiswa() {
        class GetMahasiswa extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this, "Fetching ...", "Wait...", false, false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showMahasiswa(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                requestHandler rh = new requestHandler();
                String s = rh.sendGetRequestParam(config.URL_SELECT, id);
                return s;
            }
        }

        GetMahasiswa ge = new GetMahasiswa();
        ge.execute();
    }
    private void showMahasiswa(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String id = c.getString(config.TAG_JSON_ID);
            String name = c.getString(config.TAG_JSON_NAMA);
            String jurusan = c.getString(config.TAG_JSON_JURUSAN);
            String email = c.getString(config.TAG_JSON_EMAIL);

            idtxt.setText(id);
            namatxt.setText(name);
            jurusantxt.setText(jurusan);
            emailtxt.setText(email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateMahasiswa() {
        final String ID = idtxt.getText().toString().trim();
        final String NAMA = namatxt.getText().toString().trim();
        final String JURUSAN = jurusantxt.getText().toString().trim();
        final String EMAIL = emailtxt.getText().toString().trim();

        class UpdateMahasiswa extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this, "Mengupdate...", "Silahkan Tunggu", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(select.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(config.KEY_ID, ID);
                hashMap.put(config.KEY_NAMA, NAMA);
                hashMap.put(config.KEY_JURUSAN, JURUSAN);
                hashMap.put(config.KEY_EMAIL, EMAIL);

                requestHandler rh = new requestHandler();
                String s = rh.sendPostRequest(config.URL_UPDATE, hashMap);
                return s;
            }
        }
        UpdateMahasiswa ue = new UpdateMahasiswa();
        ue.execute();
    }

    private void deleteMahasiswa() {
        class DeleteMahasiswa extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this, "Menghapus..", "Silahkan tunggu..", false, false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(select.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                requestHandler rh = new requestHandler();
                String s = rh.sendGetRequestParam(config.URL_DELETE, id);

                return s;
            }
        }

        DeleteMahasiswa deleteMahasiswa = new DeleteMahasiswa();
        deleteMahasiswa.execute();
    }
    private void confirmDeleteMhs() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setMessage("yakin ingin menghapus?");
        alert.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMahasiswa();
                        startActivity(new Intent(select.this, listMahasiswa.class));
                    }
                });

        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
        @Override
        public void onClick (View view){
            if (view==update){
                updateMahasiswa();
        }
            if (view==delete){
                confirmDeleteMhs();
            }
    }
}
