package com.zerologicgames.imageuploadapp;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
        Glide.with(holder.imageView.getContext()).load(urlList.get(position)).into(holder.imageView);
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

        public MyImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_imageView);
            constraintLayout = itemView.findViewById(R.id.parent_layout);

        }
    }

}

