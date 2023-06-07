package com.example.jewelrymaster;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{


    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Проверка наличия введенных данных
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Введите email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Введите пароль");
                    return;
                }

                // Создание нового пользователя с помощью Firebase
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    ref.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("email").setValue(editTextEmail.getText().toString());
                                    ref.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("password").setValue(editTextPassword.getText().toString());

                                    // Регистрация успешна, переходим на другую активность, например, MainActivity
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    // Обработка ошибки регистрации
                                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}