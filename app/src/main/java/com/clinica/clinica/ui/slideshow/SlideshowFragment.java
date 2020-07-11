package com.clinica.clinica.ui.slideshow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.clinica.clinica.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ListView listView;
    ArrayAdapter<String> arrayAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        listView = (ListView)view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), arrayAdapter.getItem(i).toString(), Toast.LENGTH_LONG).show();
            }
        });

        downloadJson("http://192.168.1.67:8080/medico/list");

        return view;
    }

    private void downloadJson(final String urlWebService){

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {

                try {

                    java.net.URL url = new URL(urlWebService);
                    // Objeto para abrir la conexión URL con el servidor de aplicaciones
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();

                    // Construcción de objetos
                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(
                                    con.getInputStream()
                            )
                    );

                    String json;


                    while((json = bufferedReader.readLine()) != null){
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s){

                super.onPostExecute(s);

                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                try{

                    loadIntoListView(s);

                }catch(JSONException e){

                    e.printStackTrace();

                }

            }

        }

        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();

    }


    private void loadIntoListView(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);

        String[] stocks = new String[jsonArray.length()];

        // Recorre el arreglo JSONArray, para extraer cad objeto JSON
        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject obj = jsonArray.getJSONObject(i);

            stocks[i] = obj.getString("nombre");

        }

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,stocks);
        listView.setAdapter(arrayAdapter);

    }

}
