package com.tranetech.dges.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.GalleryData;
import com.tranetech.dges.seter_geter.PhotoData;

import java.util.List;

/**
 * Created by Markand on 28-06-2017 at 09:58 AM.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosHolder>{

    private List<PhotoData> photoDatas;
    private Context context;
    private Animator mCurrentAnimator;
    private ImageView exp;
    private FrameLayout fl;
    private int mShortAnimationDuration;


    public PhotosAdapter(List<PhotoData> photoDatas, Context contex, ImageView imgExpanded, FrameLayout fl) {
        this.photoDatas = photoDatas;
        this.context = contex;
        exp = imgExpanded;
        this.fl = fl;
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
                .crossFade()
                .into(holder.imgPhoto);
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                zoomImageFromThumb(holder.imgPhoto, ((BitmapDrawable) holder.imgPhoto.getDrawable()).getBitmap());
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

    public void addALL(List<GalleryData> alHWData) {
        this.photoDatas.addAll(photoDatas);
        notifyDataSetChanged();
    }
    private void zoomImageFromThumb(final View thumbView, Bitmap imgBitmap) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
//        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
//        expandedImageView.setImageBitmap(imgBitmap);

        exp.setImageBitmap(imgBitmap);
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        fl.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        exp.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        exp.setPivotX(0f);
        exp.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(exp, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(exp, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(exp, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(exp, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(exp, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(exp, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(exp, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(exp, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        exp.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        exp.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
