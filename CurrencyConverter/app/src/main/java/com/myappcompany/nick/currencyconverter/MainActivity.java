package com.myappcompany.nick.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    final static String API_KEY = "b7a728cac148defa0d03e4b8fefade34";

    public void convert(View view)
    {
        button.setEnabled(false);
        EditText currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        Log.i("Info", "Working!");

        String amntInEuros = currencyEditText.getText().toString();                                                            //converting the amount in Euros to a string variable called amntInEuros

        double currency = Double.parseDouble(amntInEuros);                                                                     //Converting amntInEuros to a currency of type double

        final String[] responseFromAPI = new String[1];

        double rate;
        try {

            Thread thread = new Thread(new Runnable(){

                @Override
                public void run(){

                    String urlToRead = "http://data.fixer.io/api/latest?access_key=" + API_KEY + "&symbols=CAD";               //Reading the API key and base and symbol conversions on the fixer API site
                    StringBuilder result = new StringBuilder();

                    Log.i("URL:", urlToRead);                                                                             //Displaying the returned urlToRead in the Logcat

                    try {
                        URL url = new URL(urlToRead);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        String line;
                        while ((line = rd.readLine()) != null) {
                            Log.i("LINE: ", line);
                            result.append(line);
                        }

                        rd.close();
                        responseFromAPI[0] = (String)(result.toString());
                        Log.i("responseFromAPI: ", responseFromAPI[0]);
                    } catch ( Exception e ) {}
                }
            });
            thread.start();                                                                                                   //Starts the thread

            Thread.sleep(500);                                                                                          //Disabling the button for half a second, allowing for the toast to display without the button being clicked again
            String response = responseFromAPI[0];
            Log.i("Response", response);                                                                                 //Message displayed in the log for debugging purposes
            String toSearchFor = "\"CAD\":";                                                                                 //What to search for in the string returned by the API
            int index = response.lastIndexOf(toSearchFor)+toSearchFor.length();                                              // start of the rate from the string returned from the API

            String rateAsString = "";
            for (int i = index; i < response.length(); i++) {
                if (Character.isDigit(response.charAt(i)) || response.charAt(i)=='.') {
                    rateAsString = new StringBuilder().append(rateAsString).append(response.charAt(i)).toString();
                }
            }
            rate = Double.parseDouble(rateAsString);                                                                         //Parsing the rate from a string to a double
            Log.i("Index", Integer.toString(index));                                                                    //Displaying the index in the Logcat for debugging purposes
            Log.i("Rate", Double.toString(rate));                                                                       //Displaying the rate in the Logcat fro debugging purposes

            double temp = currency * rate;                                                                                  //Calculating the amount of Euros in dollars

            String amntInDollars = String.format("%.2f", temp);                                                             //Converting the calculated dollar amount to a string of only two decimal places

            Toast.makeText(this, "€" + amntInEuros + " is " + "$" + amntInDollars, Toast.LENGTH_LONG).show();   //Displaying the converted amount through a toast

        } catch ( Exception e )
        {
            rate = 1.53;                                                                                                    //Default rate to use if an exception is thrown
            Log.i("USING DEFAULT RATE", "1.53");                                                                  //Displays message in the Log for debugging purposes
        }

        button.setEnabled(true);                                                                                           //Enabling the button
    }
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
    }
}
