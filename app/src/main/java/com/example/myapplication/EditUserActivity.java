package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditUserActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText editUsername, editPassword;
    private Button btnUpdate, btnDelete;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Obtener el ID del usuario desde el Intent
        userId = getIntent().getIntExtra("userId", -1);

        // Cargar datos del usuario para editarlos
        cargarDatosUsuario();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                if (db.updateUser(userId, username, password)) {
                    Toast.makeText(EditUserActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                    finish(); // Regresa a MainActivity
                } else {
                    Toast.makeText(EditUserActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteUser(userId);
                Toast.makeText(EditUserActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                finish(); // Regresa a MainActivity
            }
        });
    }

    private void cargarDatosUsuario() {
        Cursor cursor = db.getAllUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0); // Obtén el ID de la columna 0
                if (id == userId) { // Si el ID coincide
                    String username = cursor.getString(1); // Obtén el nombre de usuario de la columna 1
                    String password = cursor.getString(2); // Obtén la contraseña de la columna 2
                    editUsername.setText(username);
                    editPassword.setText(password);
                    break; // Sal del bucle una vez que encuentres el usuario
                }
            }
            cursor.close(); // Asegúrate de cerrar el cursor
        }
    }
}

