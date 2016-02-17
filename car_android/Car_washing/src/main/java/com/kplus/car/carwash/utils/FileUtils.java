package com.kplus.car.carwash.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.kplus.car.carwash.module.CNCarWashApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 */
public class FileUtils {

    public static final String TAG = "FileUtils";

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * write file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        if (null == file || null == stream)
            return false;

        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * get folder name from path
     * <p/>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }


    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * <li>return {@link File #makeFolder}</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Description: createNewFile
     * <br>Comments: 创建文件
     *
     * @param filePath
     * @return
     * @author FU ZHIXUE
     */
    public static boolean createNewFile(String filePath) {
        if (!isFileExist(filePath)) {
            File file = new File(filePath);
            try {
                return file.createNewFile();
            } catch (IOException e) {

            }
        }
        return false;
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     *
     */
    //按日期排序
    public static File[] orderByDate(String fliePath) {
        File file = new File(fliePath);
        File[] fs = file.listFiles();
        Arrays.sort(fs, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return -1;
                else if (diff == 0)
                    return 0;
                else
                    return 1;
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
        return fs;
    }

    public static  long getFileLength(String path) {
        if (isFileExist(path)) {
            try {
                File file = new File(path);
                FileInputStream fis = new FileInputStream(file);
                return fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 写入系列化文件
     *
     * @param ser  系列化 实体类
     * @param file 文件路径
     * @return 是否成功
     */
    public static synchronized boolean saveObject(Serializable ser, File file) {
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(ser);
            outStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.trace(TAG, "obj File path：" + file.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.trace(TAG, "obj File path：" + file.toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.trace(TAG, "obj File path：" + file.toString());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取对象
     *
     * @param file 文件路径
     * @return Object 可能是一个系列化实体
     * @throws java.io.IOException
     */
    public static synchronized Object readObject(File file) {
        // 如果该文件不存在直接返回null
        if (null == file || !file.exists())
            return null;

        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            fis.close();
            return obj;
        } catch (FileNotFoundException e) {
            Log.trace(TAG, "FileNotFoundException!" + e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized String getContextCacheFileDir() {
        String folder = CNCarWashApp.getIns().getApplicationContext().getFilesDir().toString();
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        return folder;
    }

    public static String getAppRootPath() {
        String filePath = "";
        String ExternalStorageState = android.os.Environment.getExternalStorageState();
        if (android.os.Environment.MEDIA_MOUNTED.equals(ExternalStorageState)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + "/car/"; //有SD卡
        } else {
            File filetmp = new File("/mnt");
            File[] subFile = filetmp.listFiles();
            for (int i = 0; i < subFile.length; i++) {
                if (!subFile[i].isDirectory())
                    continue;
                String dirname = subFile[i].getName();
                if (dirname == null || dirname.length() <= 0)
                    continue;
                if (dirname.indexOf("sdcard") < 0)
                    continue;
                String path = subFile[i].getAbsolutePath();
                File file1 = new File(subFile[i].getAbsolutePath() + "/car/");
                boolean canWrite = file1.exists();
                if (!file1.exists())
                    canWrite = file1.mkdirs();
                if (canWrite) {
                    filePath = subFile[i].getAbsolutePath() + "/car/";
                    break;
                }
            }
            if (filePath.length() <= 0)
                filePath = Environment.getDataDirectory().getPath() + "/data/com.kplus.car/files/";
        }

        makeDirs(filePath);
        return filePath;
    }

    /**
     * 保存图片
     *
     * @param fileName
     * @param bitmap
     * @return
     */
    public static boolean saveBitmap(String fileName, Bitmap bitmap) {
        if (TextUtils.isEmpty(fileName) || null == bitmap) {
            throw new IllegalArgumentException("文件名或图片为空！");
        }
        boolean isSaveSuccess = false;
        FileUtils.deleteFile(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            isSaveSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSaveSuccess = false;
        } finally {
            try {
                if (null != fos) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSaveSuccess;
    }

    /**
     * 在相应图片目录中创建此文件，该目录中图片不会被扫描出来
     */
    public static final String NOMEDIA_FILE = ".nomedia";

    public static void createNomediaFile(String folder) {
        if (!folder.endsWith("/"))
            folder += "/";

        FileUtils.makeDirs(folder);

        // 在目录中创建一个文件，使不会被扫描到该目录下的图片文件
        String nomeDiaFile = NOMEDIA_FILE;
        if (!FileUtils.isFileExist(folder + nomeDiaFile)) {
            FileUtils.createNewFile(folder + nomeDiaFile);
        }
    }


}
