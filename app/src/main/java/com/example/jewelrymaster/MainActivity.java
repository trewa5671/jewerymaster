package com.example.jewelrymaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Устанавливаем HomeFragment в качестве фрагмента по умолчанию
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContainer, new HomeFragment())
                .commit();

        // Обработчик выбора пунктов меню
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Определение выбранного фрагмента на основе идентификатора пункта меню
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.menu_news) {
                    selectedFragment = new NewsFragment();
                } else if (itemId == R.id.menu_profile) {
                    selectedFragment = new ProfileFragment();
                }

                // Замена текущего фрагмента выбранным фрагментом
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayoutContainer, selectedFragment)
                            .commit();
                }

                return true;
            }
        });
        View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();

                // Вычисление разницы между высотой экрана и высотой видимой области
                int heightDiff = screenHeight - (r.bottom - r.top);

                // Проверка, является ли разница в высоте достаточно большой для определения открытия или закрытия клавиатуры
                if (heightDiff > screenHeight * 0.15) {
                    // Клавиатура открыта
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    // Клавиатура закрыта
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}