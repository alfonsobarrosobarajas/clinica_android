package com.clinica.clinica.ui.medico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.clinica.clinica.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MedicoFragment extends Fragment {

    // Declaración de los objetos que vamos a manipular en la vista: XML
    private MedicoViewModel mViewModel;
    private EditText clave;
    private EditText nombre;
    private EditText aPaterno;
    private EditText aMaterno;
    private Button agregar;
    private Button editar;
    private Button remover;

    public static MedicoFragment newInstance() {
        return new MedicoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Se declara un objeto view, para controlar la vista y todos los elementos que posee
        View view = inflater.inflate(R.layout.fragment_medico, container, false);

        // Se relacionan los objetos de la vista, con los objetos de la presente clase
        clave = (EditText)view.findViewById(R.id.editClave);
        nombre = (EditText)view.findViewById(R.id.editNombre);
        aPaterno = (EditText)view.findViewById(R.id.editA_Paterno);
        aMaterno = (EditText)view.findViewById(R.id.editA_Materno);

        agregar = (Button)view.findViewById(R.id.agregar);
        editar = (Button)view.findViewById(R.id.editar);
        remover = (Button)view.findViewById(R.id.remover);

        // Agrega un listener al botón de agregar, para al darle clic, el botón reaccione y ejecute el código
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombreString = nombre.getText().toString();
                formJson();
                Toast.makeText(getContext(), "Agregando " + nombreString, Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MedicoViewModel.class);
        // TODO: Use the ViewModel
    }

    // Método para convertir en JSON los datos que se capturan desde la vista
    private void formJson(){
        // Se incluye librería JSONObject
        JSONObject jo = new JSONObject();

        // Agregar los datos al objeto, se indica la 'key'
        // Lado izquierdo es la key, derecho es el value
        try {

            // Formar el objeto JSON, con los datos
            jo.put("id", 0); // 0, el campo de la tabla es autoincrement
            jo.put("codigo", clave.getText().toString());
            jo.put("nombre", nombre.getText().toString());
            jo.put("aPaterno", aPaterno.getText().toString());
            jo.put("aMaterno", aMaterno.getText().toString());

            // String que representa el URL del backend
            String url = "http://192.168.1.67:8080/medico/create"; // <- Es el método add que está en el RestController
            // La dirección ip, es la del servidor

            // Se llama al método, para enviar los datos al servidor
            postJsonObject(url, jo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String postJsonObject(String postUrl, JSONObject jsonObject){

        // Permite el uso y conexion con el backend, para envío y recepción de datos, por el protocolo http
        HttpURLConnection conn = null;

        StringBuffer response = null;

        try {

            URL url = new URL(postUrl);

            // Solicitamos servicio de la url, "Nos conectamos"
            conn = (HttpURLConnection)url.openConnection();

            // Establecemos el tipo de información a transmitir
            conn.setRequestProperty("Content-Type", "application/json");
            // Indicamos que es "salida" de información
            conn.setDoOutput(true);
            // Indicamos el tipo de método http que se utilizará
            conn.setRequestMethod("POST");

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            writer.write(jsonObject.toString());
            writer.close();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }

}
