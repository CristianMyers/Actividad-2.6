package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Button btnAddUser;
    private ListView userListView;
    private ArrayList<String> userList;
    private ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        btnAddUser = findViewById(R.id.btnAddUser);
        userListView = findViewById(R.id.userListView);

        // Configurar el ListView y su adapter
        userList = new ArrayList<>();
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(userAdapter);

        // Mostrar los usuarios al iniciar la actividad
        mostrarUsuarios();

        // Configura el botón para abrir RegisterActivity
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Actualiza la lista al regresar de la actividad de registro
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Seleccionado: " + userList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarUsuarios(); // Refrescar lista de usuarios al regresar
    }

    // Método para cargar y mostrar usuarios
    private void mostrarUsuarios() {
        Cursor cursor = db.getAllUsers();
        userList.clear();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            String username = cursor.getString(1); // Suponiendo que la columna de nombre de usuario es la segunda
            userList.add(username);
        }

        userAdapter.notifyDataSetChanged(); // Notifica al adaptador que la lista ha cambiado
    }
}
