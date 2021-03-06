package com.example.ecommerciandroiapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerciandroiapp.BookDetailActivity;
import com.example.ecommerciandroiapp.Database.DataBaseQueries;
import com.example.ecommerciandroiapp.Model.WishListModel;
import com.example.ecommerciandroiapp.R;

import java.util.ArrayList;
import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {


    private List<WishListModel> wishListModelList = new ArrayList<>();
    private boolean wishlist;
    private int lastPosition = -1;

    public WishListAdapter(List<WishListModel> wishListModelList, boolean wishlist) {
        this.wishListModelList = wishListModelList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        String id = wishListModelList.get(position).getBookID();
        String resource = wishListModelList.get(position).getBookImage();
        String title = wishListModelList.get(position).getBookTitle();
        String author = wishListModelList.get(position).getBookAuthor();
        String price = wishListModelList.get(position).getBookPrice();
        String cuttedPrice = wishListModelList.get(position).getCuttedPrice();
        long rating = wishListModelList.get(position).getRating();
        long totalRating = wishListModelList.get(position).getTotalRating();
        holder.setData(id,resource,title,author,price,cuttedPrice,rating,totalRating,position);

        if(lastPosition < position){
            Context context;
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fragment_fade_enter);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView bookImage;
        private TextView bookTitle,bookAuthor ,bookPrice,cuttedPrice,rating,totalRating;
        private View cuttedPriceDivider;
        private ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPrice = itemView.findViewById(R.id.book_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            rating = itemView.findViewById(R.id.book_avg_rating);
            totalRating = itemView.findViewById(R.id.total_rating);
            cuttedPriceDivider = itemView.findViewById(R.id.cutted_price_divider);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        }
        private void setData(final String id, String resource, String title, String author, String price, String cutted, long ratings, long totalRatings, final int index){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.sachtienganh)).into(bookImage);
            bookTitle.setText(title);
            bookAuthor.setText(author);
            bookPrice.setText(formatPrice(price));
            cuttedPrice.setText(formatPrice(cutted));
            rating.setText(String.valueOf(ratings));
            totalRating.setText(String.format("(%s đánh giá)",totalRatings));
            if(wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }
            else{
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!BookDetailActivity.RUNNING_WISHLIST_QUERY){
                        BookDetailActivity.RUNNING_WISHLIST_QUERY = true;
                        DataBaseQueries.removeWishList(index,itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bookDetailsIntent = new Intent(itemView.getContext(), BookDetailActivity.class);
                    bookDetailsIntent.putExtra("book_id",id);
                    itemView.getContext().startActivity(bookDetailsIntent);
                }
            });
        }
        private String formatPrice(String price){
            if(price.equals("")){
                return "0 đ";
            }else {
                int prices = 0;
                if (price != null)
                    prices = Integer.parseInt(price);
                return String.format("%,d đ", prices);
            }
        }
    }
}
