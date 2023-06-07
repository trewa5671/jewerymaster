package com.example.jewelrymaster;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnProductLikedListener onProductLikedListener;

    private ProductViewModel productViewModel;

    public interface OnProductLikedListener{
        void onProductLiked(Product product);
    }

    public void setOnProductLikedListener(OnProductLikedListener listener) {
        this.onProductLikedListener = listener;
    }

    public ProductAdapter(List<Product> productList, Context context, ProductViewModel productViewModel) {
        this.productList = productList;
        this.context = context;
        this.productViewModel = productViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Product product = productList.get(position);

        if (productViewModel.isProductLiked(product.getId())) {
            holder.likeButton.setImageResource(R.drawable.ic_liked);
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_like);
        }

        // Заполните элементы списка данными из модели Product
        holder.textName.setText(product.getName());
        holder.textPrice.setText(product.getPrice());
        //holder.textDescription.setText(product.getDescription());
        // Загрузка и отображение изображения
        Glide.with(context)
                .load(product.getImageUrl())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imageProduct);
        // и так далее для остальных полей модели данных

        if (product.getLikes() > 0) {
            holder.likeButton.setImageResource(R.drawable.ic_liked);
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_like);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = (RecyclerView) view.getParent();
                // Выполните действия, которые должны произойти при нажатии на элемент RecyclerView
                // Например, создайте интент и запустите новую активность
                int position = recyclerView.getChildAdapterPosition(view);
                Product product = productList.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                // Передача выбранного продукта через Intent
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("imageUrl", product.getImageUrl());

                context.startActivity(intent);
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProductLikedListener != null) {

                    boolean isLiked = productViewModel.isProductLiked(product.getId());
                    productViewModel.setProductLiked(product.getId(), !isLiked);

                    if (product.getLikes() > 0) {
                        product.setLikes(0);
                        holder.likeButton.setImageResource(R.drawable.ic_like);
                    } else {
                        product.setLikes(1);
                        holder.likeButton.setImageResource(R.drawable.ic_liked);
                    }

                    onProductLikedListener.onProductLiked(product);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public void filterList(List<Product> filteredList){
        productList = filteredList;
        notifyDataSetChanged();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textName;
        TextView textPrice;
        ImageButton likeButton;
        //  TextView textDescription;

        // и так далее для остальных элементов списка

        public ViewHolder(View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.image_product);
            textName = itemView.findViewById(R.id.text_name);
            textPrice = itemView.findViewById(R.id.text_price);
            likeButton = itemView.findViewById(R.id.like_button);
            //   textDescription = itemView.findViewById(R.id.text_description);
            // инициализируйте остальные элементы списка
        }
    }
}