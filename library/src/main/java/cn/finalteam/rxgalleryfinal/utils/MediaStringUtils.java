package cn.finalteam.rxgalleryfinal.utils;

import cn.finalteam.rxgalleryfinal.Configuration.MediaType;
import cn.finalteam.rxgalleryfinal.R;

/**
 * author  dengyuhan
 * created 2017/5/4 18:07
 */
public class MediaStringUtils {

    public static int getMediaTypeAllText(MediaType mediaType) {
        if (MediaType.IMAGE == mediaType) {
            return R.string.gallery_all_image;
        } else if (MediaType.VIDEO == mediaType) {
            return R.string.gallery_all_video;
        } else if (MediaType.ALL == mediaType) {
            //图片和视频也显示所有视频
            return R.string.gallery_all_video;
        }
        return R.string.gallery_all;
    }
}
