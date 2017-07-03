package com.tranetech.dges.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.PhotoData;

import java.util.ArrayList;

/**
 * Created by Markand on 29-06-2017 at 10:40 AM.
 */

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    ArrayList<PhotoData> photoDatas;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView txtImgCount, txtImgTitle, txtImgDate;

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private int selectedPosition = 0;

    public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.vp);
        txtImgCount = (TextView) v.findViewById(R.id.txt_img_count);
        txtImgDate = (TextView) v.findViewById(R.id.txt_img_date);
        txtImgTitle = (TextView) v.findViewById(R.id.txt_img_title);

        photoDatas = (ArrayList<PhotoData>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "Position:" + selectedPosition);
        Log.e(TAG, "ImageSize:" + photoDatas.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position);
        displayMetaInfo(selectedPosition);
    }

    private void displayMetaInfo(int position) {
        txtImgCount.setText((position + 1) + " of " + photoDatas.size());
        PhotoData image = photoDatas.get(position);
        txtImgTitle.setText(image.getsPhotoTitle());
        txtImgDate.setText(image.getsImgDate());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public class MyViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter(){
        }

        @Override
        public int getCount() {
            return photoDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview,container,false);
            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            PhotoData image = photoDatas.get(position);
            Glide.with(getActivity())
                    .load(image.getsPhotoImgURLLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .into(imageViewPreview);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
