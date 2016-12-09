package com.inred.kuaimawork.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSystemHelper {
    public static void checkPathAndMakeThem(String path) throws IOException {
        if (null == path || path.length() == 0) {
            return;
        }
        File f = new File(path);
        if (!f.isDirectory()) {
            if (!f.mkdirs()) {
                throw new IOException("fail to create: " + path);
            }
        }
    }

    public static boolean exists(String path) {
        if (null == path || path.length() == 0) {
            return false;
        }
        return (new File(path)).exists();
    }

    public static String getExternalPathOfApp(String appName) {
        String root = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        String path = root + "/" + appName + "/";
        try {
            checkPathAndMakeThem(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getExternalPathOfTemp(String appName) {
        String path = getExternalPathOfApp(appName) + "/temp/";
        try {
            checkPathAndMakeThem(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getOutgoingFilePath(String appName) {
        String path = getExternalPathOfApp(appName) + "/outgoing/";
        try {
            checkPathAndMakeThem(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
    public static String getIncomingFilePath(String appName) {
        String path = getExternalPathOfApp(appName) + "/incoming/";
        try {
            checkPathAndMakeThem(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


    /**
     * 得到amr的时长
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static long getAmrDuration(File file)  {
        long duration = -1;
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();//文件的长度
            int pos = 6;//设置初始位置
            int frameCount = 0;//初始帧数
            int packedPos = -1;
            /////////////////////////////////////////////////////
            byte[] datas = new byte[1];//初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }
            /////////////////////////////////////////////////////
            duration += frameCount * 20;//帧数*20
        }catch (Exception ex){
            return 0;
        }finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }catch (Exception ex) {
                    return 0;
                }
            }
        }
        return duration;
    }

}
