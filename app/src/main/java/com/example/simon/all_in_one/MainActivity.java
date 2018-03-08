package com.example.simon.all_in_one;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String [] currency = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button       = findViewById(R.id.button);
        final TextView textView   = findViewById(R.id.textView);
        final Spinner spinner     = findViewById(R.id.spinner);

        currency [1] = "percent_change_24h";
        currency [2] = "percent_change_1h";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            currency[0] = spinner.getSelectedItem().toString();

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                       currency  = RequestMethod(currency);
                        textView.setText("price_usd:" +currency[0] + "$" + "\n24h change:" + currency[1] +"\n1h change" +currency[2]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    public String [] RequestMethod(String [] currency) throws IOException, JSONException {
        String [] result2 = new String[4];
        String result;
        String URL ="https://api.coinmarketcap.com/v1/ticker/"+currency[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(URL));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            result = out.toString();
            out.close();

        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        JSONArray jsonarray = new JSONArray(result);
        JSONObject jsonobject = jsonarray.getJSONObject(0);

        for(int i=0; i<3; i++){
            result2[i] = jsonobject.getString(currency[i]);
        }

        return result2;
    }



}
