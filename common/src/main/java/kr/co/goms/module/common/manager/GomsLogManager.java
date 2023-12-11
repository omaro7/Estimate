package kr.co.goms.module.common.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 사용법
 GomsLogManager.I().createFileLog(this, "GomsLog", "안녕하세요");
 GomsLogManager.I().fileWriteClose();

 GomsLogManager.I().fileReadOpen(this, Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/20221020/", "GomsLog_20221020_105138257.txt");
 GomsLogManager.I().fileReadClose();
 GomsLog.d(TAG, "여기입니다>>>" + GomsLogManager.I().getFileTxt());
 */
public class GomsLogManager {

    static GomsLogManager mGomsLogManager;  //인스턴스

    FileOutputStream mFileOutputStream;     //아웃스트림
    FileInputStream mFileInputStream;       //인풋스트림

    boolean isLog = true;                   //로그 여부

    public GomsLogManager() {
    }

    public static GomsLogManager I(){
        if(mGomsLogManager == null){
            mGomsLogManager = new GomsLogManager();
        }
        return mGomsLogManager;
    }

    public void destroy() {
        try {
            this.mFileOutputStream.close();
            this.mFileOutputStream = null;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     *
     * @param context
     * @param path      packageName + "yyyyMMdd"
     * @param filename  txtFileName + "_" + curTimeString + "." + "txt";
     * @return
     */
    public boolean fileWriteOpen(Context context, String path, String filename) {
        if (null == context) {
            return false;
        } else {
            File dir = new File(path);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            File file = new File(path, filename);
            try {
                mFileOutputStream = new FileOutputStream(file);
                return true;
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                return false;
            }
        }
    }

    public boolean fileReadOpen(Context context, String path, String filename) {
        if(null == context) {
            return false;
        } else {
            File dir = new File(path);
            if(!dir.isDirectory()) {
                dir.mkdirs();
            }

            File file = new File(path, filename);

            try {
                mFileInputStream = new FileInputStream(file);
                return true;
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                return false;
            }
        }
    }


    public boolean fileWriteClose() {
        if (null == mFileOutputStream) {
            return false;
        } else {
            try {
                mFileOutputStream.close();
                mFileOutputStream = null;
                return true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
        }
    }

    public boolean fileWrite(String write, boolean nextLine) {
        if (null == mFileOutputStream) {
            return false;
        } else {
            try {
                String enterItem = "\r\n";
                String dis = new String(enterItem.getBytes(), StandardCharsets.UTF_8);
                synchronized(mFileOutputStream) {
                    mFileOutputStream.write(write.getBytes(), 0, write.getBytes().length);
                    if (nextLine) {
                        mFileOutputStream.write(dis.getBytes(), 0, dis.getBytes().length);
                    }
                    return true;
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
        }
    }

    /**
     * createFileLog(context, "GomsLogFile", jsonString.toString());
     * @param context
     * @param txtFileName
     * @param data
     */
    public void createFileLog(Context context, String txtFileName, String data) {
        if(context == null) return;

        File outRootDir = Environment.getExternalStorageDirectory();
        String outAppDir = context.getPackageName();
        String externalStorage = outRootDir + "/" + outAppDir;

        String curTimeString = getCurrTime(System.currentTimeMillis());
        String fileName = txtFileName + "_" + curTimeString + "." + "txt";
        String dirString = curTimeString.substring(0, 8);

        String dirPath = externalStorage + "/" + dirString;

        fileWriteOpen(context, dirPath, fileName);
        fileWrite(data, true);
    }

    public boolean fileReadClose() {
        if(null == mFileInputStream) {
            return false;
        } else {
            try {
                mFileInputStream.close();
                mFileInputStream = null;
                return true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
        }
    }

    public InputStream getFileInputStream() {
        return mFileInputStream;
    }

    public String getFileTxt(){
        int i = 0;
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[10240];
        while (true){
            try {
                if (!(( i = mFileInputStream.read(b)) != -1)) break;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            sb.append(new String(b,0, i));
        }
        String str = sb.toString();
        return str;
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrTime(long time) {
        String s = null;
        SimpleDateFormat date = null;
        date = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        s = date.format(new Date(time));
        return s;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }
}
