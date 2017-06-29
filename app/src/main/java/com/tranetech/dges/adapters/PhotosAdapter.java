package com.tranetech.dges.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.PhotoData;

import java.util.List;

/**
 * Created by Markand on 28-06-2017 at 09:58 AM.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosHolder>{

    private List<PhotoData> photoDatas;
    private Context context;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    public PhotosAdapter(List<PhotoData> photoDatas, Context contex) {
        this.photoDatas = photoDatas;
        this.context = contex;
        mShortAnimationDuration = contex.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public PhotosAdapter.PhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_photos, parent, false);

        return new PhotosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotosAdapter.PhotosHolder holder, int position) {
        PhotoData pd = photoDatas.get(position);
        Glide.with(context)
                .load(pd.getsPhotoImgURL())
                .override(240,120)
                .crossFade()
                .into(holder.imgPhoto);
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoDatas.size();
    }

    public class PhotosHolder extends RecyclerView.ViewHolder {
        public ImageView imgPhoto;

        public PhotosHolder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photos);
        }
    }

    public void clear() {
        photoDatas.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<PhotoData> alHWData) {
        this.photoDatas.addAll(photoDatas);
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PhotosAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PhotosAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
