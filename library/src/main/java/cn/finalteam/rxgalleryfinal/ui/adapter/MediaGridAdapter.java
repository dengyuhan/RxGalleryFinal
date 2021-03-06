package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digizen.rxgalleryfinal.MediaActivityDelegate;

import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;
import cn.finalteam.rxgalleryfinal.utils.MediaType;
import cn.finalteam.rxgalleryfinal.utils.OsCompat;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;
import cn.finalteam.rxgalleryfinal.utils.VideoUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/18 下午7:48
 */
public class MediaGridAdapter extends RecyclerView.Adapter<MediaGridAdapter.GridViewHolder> {

    private MediaActivityDelegate mMediaActivity;
    private List<MediaBean> mMediaBeanList;
    private LayoutInflater mInflater;
    private int mImageSize;
    private Configuration mConfiguration;
    private Drawable mDefaultImage;
    private Drawable mImageViewBg;
    private Drawable mCameraImage;
    private int mCameraTextColor;

    private static IMultiImageCheckedListener iMultiImageCheckedListener;

    public MediaGridAdapter(MediaActivityDelegate mediaActivity, List<MediaBean> list, int screenWidth, Configuration configuration) {
        this.mMediaActivity = mediaActivity;
        this.mMediaBeanList = list;
        AppCompatActivity activity = mediaActivity.getActivity();
        this.mInflater = LayoutInflater.from(activity);
        this.mImageSize = screenWidth / 3;
        int defaultResId = ThemeUtils.resolveDrawableRes(activity, R.attr.gallery_default_image, R.drawable.gallery_default_image);
        this.mDefaultImage = activity.getResources().getDrawable(defaultResId);
        this.mConfiguration = configuration;

        this.mImageViewBg = ThemeUtils.resolveDrawable(activity,
                R.attr.gallery_imageview_bg, R.drawable.gallery_default_image);
        this.mCameraImage = ThemeUtils.resolveDrawable(activity, R.attr.gallery_camera_bg,
                R.drawable.gallery_ic_camera);
        this.mCameraTextColor = ThemeUtils.resolveColor(activity, R.attr.gallery_take_image_text_color,
                R.color.gallery_default_take_image_text_color);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gallery_adapter_media_grid_item, parent, false);
        return new GridViewHolder(mMediaActivity.getActivity(), view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        if (mediaBean.getId() == Integer.MIN_VALUE) {
            holder.mCbCheck.setVisibility(View.GONE);
            holder.mIvMediaImage.setVisibility(View.GONE);
            holder.mLlCamera.setVisibility(View.VISIBLE);
            holder.mIvCameraImage.setImageDrawable(mCameraImage);
            holder.mTvCameraTxt.setTextColor(mCameraTextColor);
        } else {
            if (mConfiguration.isRadio()) {
                holder.mCbCheck.setVisibility(View.GONE);
            } else {
                holder.mCbCheck.setVisibility(View.VISIBLE);
                holder.mCbCheck.setOnClickListener(new OnCheckBoxClickListener(mediaBean));
                holder.mCbCheck.setOnCheckedChangeListener(new OnCheckBoxCheckListener(mediaBean));
            }
            holder.mIvMediaImage.setVisibility(View.VISIBLE);
            holder.mLlCamera.setVisibility(View.GONE);
            if (mMediaActivity.getCheckedList() != null && mMediaActivity.getCheckedList().contains(mediaBean)) {
                holder.mCbCheck.setChecked(true);
            } else {
                holder.mCbCheck.setChecked(false);
            }

            if (mediaBean.getMimeType().contains(MediaType.GIF.name().toLowerCase())) {
                //gif
                holder.tv_video_duration.setText("");
                holder.tv_video_duration.setVisibility(View.VISIBLE);
                holder.tv_video_duration.setChecked(true);
            } else {
                holder.tv_video_duration.setChecked(false);
                if (mediaBean.getVideoDuration() > 0) {
                    holder.tv_video_duration.setVisibility(View.VISIBLE);
                    holder.tv_video_duration.setText(VideoUtils.formatDuration(mediaBean.getVideoDuration()));
                } else {
                    holder.tv_video_duration.setVisibility(View.GONE);
                }
            }

            /*String bitPath = mediaBean.getThumbnailSmallPath();
            String smallPath = mediaBean.getThumbnailSmallPath();

            if (!new File(bitPath).exists() || !new File(smallPath).exists()) {
                Job job = new ImageThmbnailJobCreate(mMediaActivity.getActivity(), mediaBean).create();
                RxJob.getDefault().addJob(job);
            }
            String path = mediaBean.getThumbnailSmallPath();
            if (TextUtils.isEmpty(path)) {
                path = mediaBean.getThumbnailBigPath();
            }
            if (TextUtils.isEmpty(path)) {
                path = mediaBean.getOriginalPath();
            }*/
            OsCompat.setBackgroundDrawableCompat(holder.mIvMediaImage, mImageViewBg);

            mConfiguration.getImageLoader()
                    .displayImage(mMediaActivity.getActivity(), mediaBean.getOriginalPath(), holder.mIvMediaImage, mDefaultImage, mConfiguration.getImageConfig(),
                            true, mImageSize, mImageSize, mediaBean.getOrientation());
        }
    }

    @Override
    public int getItemCount() {
        return mMediaBeanList.size();
    }

    class OnCheckBoxClickListener implements View.OnClickListener {

        private MediaBean mediaBean;

        public OnCheckBoxClickListener(MediaBean bean) {
            this.mediaBean = bean;
        }

        @Override
        public void onClick(View view) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox) view;
            if (mConfiguration.getMaxSize() == mMediaActivity.getCheckedList().size() &&
                    !mMediaActivity.getCheckedList().contains(mediaBean)) {
                checkBox.setChecked(false);
              /*  Toast.makeText(mMediaActivity, mMediaActivity.getResources()
                        .getString(R.string.gallery_image_max_size_tip, mConfiguration.getMaxSize()), Toast.LENGTH_SHORT).show();*/
            } else {
                if (mConfiguration.getOnCheckMediaListener().onChecked(mediaBean, checkBox.isChecked())) {
                    RxBus.getDefault().post(new MediaCheckChangeEvent(mediaBean));
                } else {
                    checkBox.setChecked(false);
                }
            }
        }
    }

    /**
     * @author KARL-dujinyang
     */
    class OnCheckBoxCheckListener implements CompoundButton.OnCheckedChangeListener {
        private MediaBean mediaBean;

        public OnCheckBoxCheckListener(MediaBean bean) {
            this.mediaBean = bean;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mConfiguration.getMaxSize() == mMediaActivity.getCheckedList().size() &&
                    !mMediaActivity.getCheckedList().contains(mediaBean)) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) buttonView;
                checkBox.setChecked(false);
                if (iMultiImageCheckedListener != null)
                    iMultiImageCheckedListener.selectedImgMax(buttonView, isChecked, mConfiguration.getMaxSize());
            } else {
                if (iMultiImageCheckedListener != null)
                    iMultiImageCheckedListener.selectedImg(buttonView, isChecked);
            }

        }
    }


    /**
     * RecyclerView.ViewHolder
     */
    static class GridViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvMediaImage;
        AppCompatCheckBox mCbCheck;

        LinearLayout mLlCamera;
        TextView mTvCameraTxt;
        ImageView mIvCameraImage;

        CheckedTextView tv_video_duration;

        public GridViewHolder(Context context, View itemView) {
            super(itemView);
            mIvMediaImage = (ImageView) itemView.findViewById(R.id.iv_media_image);
            mCbCheck = (AppCompatCheckBox) itemView.findViewById(R.id.cb_check);

            mLlCamera = (LinearLayout) itemView.findViewById(R.id.ll_camera);
            mTvCameraTxt = (TextView) itemView.findViewById(R.id.tv_camera_txt);
            mIvCameraImage = (ImageView) itemView.findViewById(R.id.iv_camera_image);
            tv_video_duration = (CheckedTextView) itemView.findViewById(R.id.tv_video_duration);

            int checkTint = ThemeUtils.resolveColor(context, R.attr.colorAccent, R.color.gallery_default_checkbox_button_tint_color);
            CompoundButtonCompat.setButtonTintList(mCbCheck, ColorStateList.valueOf(checkTint));
        }
    }


    public static void setCheckedListener(IMultiImageCheckedListener checkedListener) {
        iMultiImageCheckedListener = checkedListener;
    }
}
