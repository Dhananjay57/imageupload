package com.dhananjay.imageuploadapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dhananjay.imageuploadapp.ui.FullImageViewActivity;

import java.util.ArrayList;
import java.util.List;

public class DownloadedImageAdapter  extends RecyclerView.Adapter<DownloadedImageAdapter.MyImageHolder> {

    private Context mContext;
    public List<String> urlList;

    public DownloadedImageAdapter(Context mContext, ArrayList<String> mList) {
        this.mContext = mContext;
        this.urlList = mList;
    }

    @Override
    public DownloadedImageAdapter.MyImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_my_pool, parent, false);
        return new DownloadedImageAdapter.MyImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder ( DownloadedImageAdapter.MyImageHolder holder, int position) {
       holder.imageView.setVisibility(View.VISIBLE);
        Glide
                .with(holder.imageView.getContext())
                .load(urlList.get(position))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
//        Glide.with(holder.imageView.getContext()).load(R.drawable.fab_add).into(holder.imageView);
        holder.constraintLayout.setOnClickListener(view -> {
           // Toast.makeText(mContext, "Mmmm", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mContext, FullImageViewActivity.class);
            intent.putExtra("imageUrl", urlList.get(holder.getAdapterPosition()));
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return  urlList.size();
    }

   public class MyImageHolder extends RecyclerView.ViewHolder {
    private ImageView imageView ;
    private ConstraintLayout constraintLayout;
   public ProgressBar progressBar;

        public MyImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_imageView);
            constraintLayout = itemView.findViewById(R.id.parent_layout);
progressBar = itemView.findViewById(R.id.progress);        }
    }

}

