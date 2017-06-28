package com.tranetech.dges.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tranetech.dges.R;
import com.tranetech.dges.activities.PhotosActivity;
import com.tranetech.dges.seter_geter.GalleryData;

import java.util.List;

/**
 * Created by Markand on 27-06-2017 at 12:31 PM.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<GalleryData> alGalleryData;
    private Context context;
    private ProgressDialog mProgressDialog;


    public GalleryAdapter(List<GalleryData> alGalleryData, Context context) {
        this.alGalleryData = alGalleryData;
        this.context = context;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_gallery, parent, false);

        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        final GalleryData galleryData = alGalleryData.get(position);
        holder.txtAlbumDate.setText(galleryData.getStrAlbumDate());
        holder.txtAlbumTitle.setText(galleryData.getStrAlbumTitle());
        holder.txtAlbumCount.setText(galleryData.getStrAlbumCount());
        if (position % 2 == 0) {
            holder.llGallary.setBackgroundResource(R.drawable.bg_album1);
        } else {
            holder.llGallary.setBackgroundResource(R.drawable.bg_album2);
        }
        Glide.with(context)
                .load(galleryData.getStrAlbumImgUrl())
                .crossFade()
                .into(holder.imgAlbum);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotosActivity.class);
                intent.putExtra("gtid",galleryData.getsGTid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alGalleryData.size();
    }

    public void clear() {
        alGalleryData.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<GalleryData> alHWData) {
        this.alGalleryData.addAll(alGalleryData);
        notifyDataSetChanged();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtAlbumTitle, txtAlbumCount, txtAlbumDate;
        public LinearLayout llGallary;
        public ImageView imgAlbum;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            txtAlbumTitle = (TextView) itemView.findViewById(R.id.txt_album_title);
            txtAlbumCount = (TextView) itemView.findViewById(R.id.txt_photo_count);
            txtAlbumDate = (TextView) itemView.findViewById(R.id.txt_gallery_date);
            imgAlbum = (ImageView) itemView.findViewById(R.id.img_album);
            llGallary = (LinearLayout) itemView.findViewById(R.id.llGallery);
        }
    }
}
