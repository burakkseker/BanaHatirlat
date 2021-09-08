package com.burak.banahatirlat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {
    EditText tarifadi,tarifaciklama;
    Button button;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tarifadi =findViewById(R.id.editText);
        tarifaciklama=findViewById(R.id.editText2);
        button=findViewById(R.id.button);
        database= this.openOrCreateDatabase("Tarifler",MODE_PRIVATE,null);
        Intent intent=getIntent();
        String info=intent.getStringExtra("info");
        if (info.matches("new")){
            tarifadi.setText("");
            tarifaciklama.setText("");
            button.setVisibility(View.VISIBLE);
        }else {
            int artId=intent.getIntExtra("artId",1);
            button.setVisibility(View.INVISIBLE);

            try{
                Cursor cursor= database.rawQuery("SELECT * FROM Tarifler WHERE id= ?",new String[]{String.valueOf(artId)});
                int tarifadiIx=cursor.getColumnIndex("TarifAdi");
                int tarifaciklaIx=cursor.getColumnIndex("Tarif");

                while (cursor.moveToNext()){
                    tarifadi.setText(cursor.getString(tarifadiIx));
                    tarifaciklama.setText(cursor.getString(tarifaciklaIx));


                }
                cursor.close();

            }catch (Exception e){

            }
        }
    }
    public void kaydet(View view){
        String tarifismi=tarifadi.getText().toString();
        String tarifacikla=tarifaciklama.getText().toString();
        try {
            database= this.openOrCreateDatabase("Tarifler",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS tarifler(id INTEGER PRIMARY KEY, tarifadi VARCHAR,tarifacikl VARCHAR)");
            String sqlstring="INSERT INTO tarifler(tarifadi,tarifacikl) VALUES(?,?)";
            SQLiteStatement sqLiteStatement=database.compileStatement(sqlstring);
            sqLiteStatement.bindString(1,tarifismi);
            sqLiteStatement.bindString(2,tarifacikla);
            sqLiteStatement.execute();

        }catch (Exception e)
        {

        }

        //finish();
        Intent intent=new Intent(Main2Activity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
