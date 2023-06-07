package com.example.jewelrymaster;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView avatarImageView;
    private TextView emailTextView;
    private EditText nameEditText;
    private Button chooseImageButton;
    private Button saveButton;
    private Uri imageUri;

    private DatabaseReference ref;
    private FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Получение ссылки на базу данных Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");

        // Получение текущего пользователя Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Найти элементы пользовательского интерфейса
        avatarImageView = view.findViewById(R.id.avatar_image);
        emailTextView = view.findViewById(R.id.email_text);
        nameEditText = view.findViewById(R.id.name_edit);
        chooseImageButton = view.findViewById(R.id.choose_image_button);
        saveButton = view.findViewById(R.id.save_button);

        // Получить данные о пользователе
        // Получить данные о пользователе
        if (currentUser != null) {
            String email = currentUser.getEmail();
            emailTextView.setText(email);
        }

        // Установить слушатель для кнопки выбора изображения
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Установить слушатель для кнопки сохранения имени пользователя
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserName();
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            avatarImageView.setImageURI(imageUri);
        }
    }

    private void saveUserName() {
        String name = nameEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (currentUser != null) {
                // Получение идентификатора текущего пользователя
                String userId = currentUser.getUid();

                // Получение ссылки на узел пользователя в базе данных
                DatabaseReference userRef = ref.child(userId);

                // Сохранение имени пользователя в базе данных
                userRef.child("name").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Имя успешно сохранено", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Ошибка сохранения имени", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
        }
    }
}