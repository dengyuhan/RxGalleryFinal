package cn.finalteam.rxgalleryfinal.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Desction:支持的Media类型
 * Author:pengjianbo
 * Date:16/5/5 下午5:03
 */
public enum  MediaType implements Serializable{

    // ============== images ==============
    JPG("image/jpeg", new HashSet<String>() {{
        add("jpg");
        add("jpeg");
    }}),
    PNG("image/png", new HashSet<String>() {{
        add("png");
    }}),
    GIF("image/gif", new HashSet<String>() {{
        add("gif");
    }}),
    BMP("image/x-ms-bmp", new HashSet<String>() {{
        add("bmp");
    }}),
    WEBP("image/webp", new HashSet<String>() {{
        add("webp");
    }}),

    // ============== videos ==============
    MPEG("video/mpeg", new HashSet<String>() {{
        add("mpeg");
        add("mpg");
    }}),
    MP4("video/mp4", new HashSet<String>() {{
        add("mp4");
        add("m4v");
    }}),
    QUICKTIME("video/quicktime", new HashSet<String>() {{
        add("mov");
    }}),
    THREEGPP("video/3gpp", new HashSet<String>() {{
        add("3gp");
        add("3gpp");
    }}),
    THREEGPP2("video/3gpp2", new HashSet<String>() {{
        add("3g2");
        add("3gpp2");
    }}),
    MKV("video/x-matroska", new HashSet<String>() {{
        add("mkv");
    }}),
    WEBM("video/webm", new HashSet<String>() {{
        add("webm");
    }}),
    TS("video/mp2ts", new HashSet<String>() {{
        add("ts");
    }}),
    AVI("video/avi", new HashSet<String>() {{
        add("avi");
    }});

    private final String mMimeTypeName;
    private final Set<String> mExtensions;

    MediaType(String mimeTypeName, Set<String> extensions) {
        mMimeTypeName = mimeTypeName;
        mExtensions = extensions;
    }

    public static Set<MediaType> ofAll() {
        return EnumSet.allOf(MediaType.class);
    }

    public static Set<MediaType> of(MediaType type, MediaType... rest) {
        return EnumSet.of(type, rest);
    }

    public static Set<MediaType> ofImage() {
        return EnumSet.of(JPG, PNG, GIF, BMP, WEBP);
    }

    public static Set<MediaType> ofVideo() {
        return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI);
    }

    @Override
    public String toString() {
        return mMimeTypeName;
    }

    public boolean hasVideo() {
        return this == MP4;
    }
}
