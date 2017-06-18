package com.homechart.app.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.homechart.app.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class ImageUtils {

    //加载矩形大图的options
    private static final DisplayImageOptions rectangleoptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.white)
            .showImageOnFail(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.ALPHA_8)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    //demo圆形的options
    private static final DisplayImageOptions roundOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .displayer(new RoundedBitmapDisplayer(360))
            .cacheOnDisk(true)
            .showImageOnFail(R.color.white)
            .showImageOnLoading(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo圆角的options
    private static final DisplayImageOptions filletOptions = new DisplayImageOptions.Builder()
            .showStubImage(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .showImageOnFail(R.color.white)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new RoundedBitmapDisplayer(8))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //某种情况加载的图片北京莫名其妙变成黑色，可以使用这种情况
    private static final DisplayImageOptions blackOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.white)
            .showImageOnFail(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    /**
     * 加载圆形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayRoundImage(String imageUrl, ImageView imageView) {
        if (imageView != null)
            ImageLoader.getInstance().displayImage(imageUrl, imageView, roundOptions);
    }

    /**
     * 加载圆角图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayFilletImage(String imageUrl, ImageView imageView) {
        if (imageView != null)
            ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptions);
    }

    /**
     * 加载矩形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disRectangleImage(String imageUrl, ImageView imageView) {
        if (imageView != null)
            ImageLoader.getInstance().displayImage(imageUrl, imageView, rectangleoptions);
    }

    /**
     * 某种情况加载的图片北京莫名其妙变成黑色，可以使用这种情况
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disBlackImage(String imageUrl, ImageView imageView) {
        if (imageView != null)
            ImageLoader.getInstance().displayImage(imageUrl, imageView, blackOptions);
    }


    //.................................初始化ImageLoader.................................

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(DISPLAY_WIDTH, DISPLAY_HEIGHT) // default = device screen dimensions
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .diskCache(new UnlimitedDiskCache(createDefaultCacheDir())) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        ImageLoader.getInstance().init(config);
    }

    private static File createDefaultCacheDir() {
        File cache = new File(getCacheDir());
        if (!cache.exists()) {
            if (cache.mkdirs()) {
                Log.d("tag", "Cache files to create success");
            } else {
                Log.d("tag", "The cache file creation failed");
            }
        }
        return cache;
    }

    public static String getCacheDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "demo/" + IMAGE_CACHE;
    }

    private static final String IMAGE_CACHE = "image_cache";
    public static final int DISPLAY_WIDTH = 720;
    public static final int DISPLAY_HEIGHT = 1280;

}
