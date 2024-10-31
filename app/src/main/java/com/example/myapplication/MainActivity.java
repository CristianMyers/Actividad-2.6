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
    private ArrayList<Integer> userIdList; // Para almacenar IDs de usuarios

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        btnAddUser = findViewById(R.id.btnAddUser);
        userListView = findViewById(R.id.userListView);

        // Inicializa la lista de usuarios y su adaptador
        userList = new ArrayList<>();
        userIdList = new ArrayList<>();
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(userAdapter);

        // Muestra los usuarios al iniciar la actividad
        mostrarUsuarios();

        // Manejo de clics en el botón para agregar usuario
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Manejo de clics en los usuarios para editarlos o eliminarlos
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditUserActivity.class);
                intent.putExtra("userId", userIdList.get(position)); // Envía el ID del usuario
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarUsuarios(); // Refresca la lista de usuarios al regresar
    }

    private void mostrarUsuarios() {
        Cursor cursor = db.getAllUsers();
        userList.clear();
        userIdList.clear(); // Limpia la lista de IDs

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            String username = cursor.getString(1); // Suponiendo que el nombre de usuario está en la segunda columna
            int id = cursor.getInt(0); // ID del usuario
            userList.add(username);
            userIdList.add(id); // Guarda el ID del usuario
        }

        userAdapter.notifyDataSetChanged(); // Notifica al adaptador que la lista ha cambiado
    }
}

