package com.doyoteam.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.MainApp;
import com.doyoteam.drawables.DrawableHotspotTouch;
import com.doyoteam.drawables.LollipopDrawable;
import com.doyoteam.drawables.LollipopDrawablesCompat;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 *
 * @author ligang
 */
public class Tools {

    /**
     * 验证邮箱地址格式是否正确
     *
     * @param email 待检测的邮件地址
     * @return true 匹配； false 不匹配
     */
    public static boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)"
                + "*\\.[a-zA-Z]+\\s*$");
        return p.matcher(email).matches();
    }

    /**
     * 检查手机号码是否合法
     *
     * @param tel 待检测的手机号
     * @return 是否有效
     */
    public static boolean checkTel(String tel) {
        Pattern pattern = Pattern.compile("^[1][3|4|5|7|8]\\d{9}$");
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches();
    }

    /**
     * @param postCode
     * @describe 邮编是否正确，这里不使用正则，只要是6位数字都正确
     */
    public static boolean checkPostCode(String postCode) {
        Pattern pattern = Pattern.compile("^\\d{6}$");
        Matcher matcher = pattern.matcher(postCode);
        return matcher.matches();
    }

    /**
     * 获取格式化Code
     * @param code  12341234123412
     * @return 1234 1234 1234 12
     */
    public static String getFormatCode(String code) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < code.length(); i++) {
            if(0 < i && i < code.length() && i % 4 == 0)
                builder.append(" ");
            builder.append(code.charAt(i));
        }
        return builder.toString();
    }

    /**
     * 统一管理Toast的消息显示
     *
     * @param msg 需要展示的字符
     */
    public static void showToast(String msg) {
        Toast toast = Toast.makeText(MainApp.getContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 统一管理Log消息调试
     *
     * @param msg 需要调试的字符
     */
    public static void showLog(String msg) {
        if (Constants.IS_LOG_ON) {
            Log.d("debug", msg);
        }
    }

    /**
     * @return true 可用; false 不可用
     */
    public static boolean isNetUsable() {
        Context context = MainApp.getContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 渲染水纹点击效果背景
     *
     * @param context 所适应的Activity
     * @param view    所应用的背景
     * @param resId   背景绘制效果rippleDrawable资源
     */
    public static void renderBackground(Context context, View view, int resId) {
        view.setBackground(LollipopDrawablesCompat.getDrawable(context.getResources(), resId,
                context.getTheme()));
        view.setClickable(true); // if we don't set it true, ripple will not be played
        view.setOnTouchListener(new DrawableHotspotTouch((LollipopDrawable) view.getBackground()));
    }
    public static void renderBackgroundNoListener(Context context, View view, int resId) {
        view.setBackground(LollipopDrawablesCompat.getDrawable(context.getResources(), resId,
                context.getTheme()));
    }
    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealPathFromURI(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore
                    .Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static File getCompressFile(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight; // 源图的高度
        int w = options.outWidth; // 源图上的宽度
        int dh = (int) (512);
        int dw = (int) (512);
        int sampleSize = h >= w ? h / dh : w / dw;
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize; // 设置采样率，为1时不压缩，2则为原图的1/2....
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
//        bitmap = BitmapFactory.decodeFile(filePath, options);

        String tempPath = Tools.getTempImgPath();
        File file = new File(tempPath);
        try {
//			Tools.showLog(""+file.length());
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //图片压缩率 90%
            BitmapFactory.decodeFile(filePath, options).compress(Bitmap.CompressFormat.JPEG, 80,
                    bos);

            byte[] bitmapdata = bos.toByteArray();
//            Tools.showLog(""+bitmapdata.length);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.close();
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    /**
     * 将图片保存在指定路径
     *
     * @param photo
     * @param spath
     * @return
     */
    public static boolean saveImage(Bitmap photo, String spath) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 90, bos);//压缩率90% by guoyaobin 2015年8月6日
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 压缩指定路径的图片
     *
     * @param filePath
     * @return
     */
    public static boolean saveImage(String filePath) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath, false));
            Bitmap bitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//压缩率90% by guoyaobin 2015年8月6日
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * @return
     * @describe 获取临时图片路径
     */
    public static String getTempImgPath() {
        return SystemModel.getInstance().getTempDir() + "/temp_img.jpg";
    }

    /**
     * @param path
     * @return
     * @describe 根据传入的文件路径返回一个临时文件路径
     */
    public static String getTempPath(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        return SystemModel.getInstance().getTempDir() + "/temp_" + fileName;
    }

    /**
     * 从Assets中读取json文件
     */
    public static JSONObject getJSONObject(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open
                    (fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return (new JSONObject(stringBuilder.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    //检测是否为 null 或者 "" 或者"null"
    public static boolean checkNull(String data) {
        if (data == null) {
            return false;
        } else if (data.equals("")) {
            return false;
        }   else if (data.equals("null")) {
            return false;
        }
        return true;
    }

    // 获取定位距离
    public static String getDistance(int distance) {
        if (0 < distance && distance < 1000 * 1000) {
            if (distance < 300) return "<300米";
            if (distance < 1000) return (distance + 5) / 10 * 10 + "米";
            else return String.format("%d.1", (distance + 50) / 1000) + "公里";
        } else return "无效距离";
    }

    public static class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
            return cc;
        }

    }

    //格式化文章字符串
    public static ArrayList<String> getFormatData(Article article,ArrayList<Image> imageArrayList)
    {
        ArrayList<String> bodys = new ArrayList<String>();
        String body = article.body;
        String newBody = body;
        if(imageArrayList!=null)
        for (int i=0;i<imageArrayList.size();i++)
        {
            String[] str = newBody.split(imageArrayList.get(i).img_mark);
            bodys.add(str[0]);
            newBody = str[1];
            if(i==(imageArrayList.size()-1))
            {
                bodys.add(newBody);
            }
        }
        if(bodys.size()==0)
        {
            bodys.add(body);
        }
        return  bodys;
    }

    //fragment for adapter
    public static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
