package com.redcity.cureprostate.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

/**
* 对文件处理的工具类 
*/ 
public class FileUtils {
    private static final String TAG = "FileUtils";

    public static File currentFile;
    /**
     * 输入的参数，用于文件名中，可以直接获取显示
     */
    public static String maxElectricity;
    public static String maxVoltage;
    public static String maxCoulomb;


    /**
     * 获取可以使用的缓存目录
     *
     * @param context
     * @param
     * * @return
     */
    public static File getDiskCacheDir(Context context) {
        final String cachePath = checkSDCard() ? getExternalCacheDir().getPath() : getAppCacheDir(context);
        File cacheDirFile = new File(cachePath);
        if (!cacheDirFile.exists()) {
            cacheDirFile.mkdirs();
        }

        return cacheDirFile;
    }

    /**
     * 获取程序外部的缓存目录
     *
     * @param
     * @return
     */
    public static File getExternalCacheDir() {
        // 这个sd卡中文件路径下的内容会随着，程序卸载或者设置中清除缓存后一起清空
//        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        final String cacheDir =File.separator+"cureProstate"+File.separator + File.separator+"姓名+ID"+File.separator;
        return new File(Environment.getExternalStorageDirectory()+ cacheDir);
    }



    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public static boolean checkSDCard() {
        final String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @return cache path
     */
    public static String getAppCacheDir(Context context) {
        return context.getCacheDir().getPath();
    }


    /**
     * 创建缓存文件夹
     */
    public static void initCacheFile(Context context) {
        Calendar calendar = Calendar.getInstance();
//        if (LogUtils.DEBUG) {
//            LogUtils.v("initCacheFile");
//        }

//        final String cacheDir = FileUtils.getDiskCacheDir(context).getAbsolutePath();
//        final String cacheDir = FileUtils.getExternalCacheDir(context).getAbsolutePath();
        final String cacheDir = getDiskCacheDir(context).getAbsolutePath()+File.separator;
        final String yearDirPath = cacheDir + calendar.get(Calendar.YEAR)+"年" + File.separator;
        final File yearFileDir = new File(yearDirPath);
        if (!yearFileDir.exists()) {
            boolean isOk = yearFileDir.mkdirs();
            if (isOk) {
                Log.v(TAG,yearDirPath + " 文件夹创建isOk" + isOk);
            }
        }

        final String MonthDirPath = yearDirPath + (calendar.get(Calendar.MONTH)+1)+"月" +File.separator;
        final File monthFileDir = new File(MonthDirPath);
        if (!monthFileDir.exists()) {
            boolean isOk = monthFileDir.mkdirs();
            if (isOk) {
                Log.v(TAG,MonthDirPath + " 文件夹创建isOk" + isOk);
            }
        }

        final String DayDirPath = MonthDirPath + calendar.get(Calendar.DAY_OF_MONTH)+"日" +File.separator;
        final File dayFileDir = new File(DayDirPath);
        if (!dayFileDir.exists()) {
            boolean isOk = dayFileDir.mkdirs();
            if (isOk) {
                Log.v(TAG,DayDirPath + " 文件夹创建isOk" + isOk);
            }
        }
        final String filePath = DayDirPath+calendar.get(Calendar.HOUR_OF_DAY)+"H"+calendar.get(Calendar.MINUTE)+"_"+maxElectricity+"_"+maxVoltage+"_"+maxCoulomb+".txt";
        currentFile = new File(filePath);
        if (!currentFile.exists()){
            try {
                boolean isOk = currentFile.createNewFile();
                if (isOk){
                    Log.v(TAG,filePath + " 文件创建isOk" + isOk+"..."+currentFile.length());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void writeToSDCard(byte[] data){
        if (currentFile.exists() && data != null){
            try {
            RandomAccessFile raf = new RandomAccessFile(currentFile, "rw");
                raf.seek(currentFile.length());
                raf.write(data);
                raf.close();
                Log.d(TAG,"write......???"+ Arrays.toString(data)+"..."+currentFile.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static byte[] readToSDCard(int off) {
        byte[] bytes = new byte[20];
        try {
            RandomAccessFile ra = new RandomAccessFile(currentFile, "r");
            ra.seek(off * 20);
            ra.read(bytes,0,bytes.length);
            ra.close();
            Log.d(TAG,"read.....????"+Arrays.toString(bytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }






    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
//        if (LogUtils.DEBUG) {
//            LogUtils.e("deleteFile path " + filePath);
//        }

        if (!TextUtils.isEmpty(filePath)) {
            final File file = new File(filePath);
//            if (LogUtils.DEBUG) {
//                LogUtils.e("deleteFile path exists " + file.exists());
//            }
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 删除文件夹下所有文件
     *
     * @return
     */
    public static void deleteDirectoryAllFile(String directoryPath) {
        final File file = new File(directoryPath);
        deleteDirectoryAllFile(file);
    }

    public static void deleteDirectoryAllFile(File file) {
        if (!file.exists()) {
            return;
        }

        boolean rslt = true;// 保存中间结果
        if (!(rslt = file.delete())) {// 先尝试直接删除
            // 若文件夹非空。枚举、递归删除里面内容
            final File subs[] = file.listFiles();
            final int size = subs.length - 1;
            for (int i = 0; i <= size; i++) {
                if (subs[i].isDirectory())
                    deleteDirectoryAllFile(subs[i]);// 递归删除子文件夹内容
                rslt = subs[i].delete();// 删除子文件夹本身
            }
            // rslt = file.delete();// 删除此文件夹本身
        }

        if (!rslt) {
//            if (LogUtils.DEBUG) {
//                LogUtils.w("无法删除:" + file.getName());
//            }
            return;
        }
    }


    /**
     * 删除文件夹内所有文件
     *
     * @param delpath delpath path of file
     * @return boolean the result
     */
    public static boolean deleteAllFile(String delpath) {
        try {
            // create file
            final File file = new File(delpath);

            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {

                final String[] filelist = file.list();
                final int size = filelist.length;
                for (int i = 0; i < size; i++) {

                    // create new file
                    final File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        // digui
                        deleteFile(delpath + "/" + filelist[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception ex) {
//            if (LogUtils.DEBUG) {
//                LogUtils.e(ex);
//            }
            return false;
        }
        return true;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {

        if (TextUtils.isEmpty(sPath)) {
            return false;
        }

        boolean flag;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        final File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        final File[] files = dirFile.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag)
                        break;
                } // 删除子目录
                else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag)
                        break;
                }
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取文件名
     *
     * @param path 全路径
     * @return
     */
    public static String getFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf(File.separator) + 1);
        }
        return "";
    }

    /**
     * 获取文件所在的文件路径
     *
     * @param path
     * @return
     */
    public static String getFilePath(String path) {
        return path.substring(0, path.lastIndexOf(File.separator) + 1);
    }


    /**
     * 获取目录文件个数
     *
     * @param f
     * @return
     */
    public static long getlist(File f) {
        long size = 0;
        try {
            File flist[] = f.listFiles();
            size = flist.length;
            for (int i = 0; i < flist.length; i++) {
                final File file = flist[i];
                if (file == null) {
                    continue;
                }
                if (file.isDirectory()) {
                    size = size + getlist(file);
                    size--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取文件夹下所有文件大小
     *
     * @param f
     * @return
     */
    public static long getFileSize(File f) {
        long size = 0;
        try {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                final File file = flist[i];
                if (file == null) {
                    continue;
                }
                if (file.isDirectory()) {
                    size = size + getFileSize(file);
                } else {
                    size = size + file.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     *
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(File file) {
        if (file == null) {
            return "0B";
        }

        final long blockSize = getFileSize(file);

//        if (LogUtils.DEBUG) {
//            LogUtils.d("getAutoFileOrFilesSize 文件大小：" + blockSize);
//        }

        return FormetFileSize(blockSize);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        final DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
            fileSizeString = df.format((double) fileS * 1024) + "B";
        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
            fileSizeString = df.format((double) fileS * 1048576) + "B";
        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
            fileSizeString = df.format((double) fileS * 1073741824) + "B";
        }
        return fileSizeString;
    }

}