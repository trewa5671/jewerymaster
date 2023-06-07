package com.example.jewelrymaster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductLikedListener  {
    private DatabaseReference productsRef;
    private StorageReference storageRef;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private SearchView searchView_home;

    private ProductViewModel productViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Получение ссылки на базу данных Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("image");

        searchView_home = view.findViewById(R.id.searchView_home);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, getContext(), productViewModel);
        productAdapter.setOnProductLikedListener(this);
        recyclerView.setAdapter(productAdapter);

        loadProducts();

        productAdapter.setOnProductLikedListener(this);

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter (newText);
                return true;
            }
        });

        return view;
    }



    private void filter(String newText) {

        if (TextUtils.isEmpty(newText)) {
            // Отобразить все товары
        }
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(product);
            }
        }
        productAdapter.filterList(filteredList);
    }


    private void loadProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки чтения данных из базы данных Firebase
            }
        });
    }

    @Override
    public void onProductLiked(Product product) {
        Collections.sort(productList, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Integer.compare(p2.getLikes(), p1.getLikes());
            }
        });

        productAdapter.notifyDataSetChanged();
    }
}

