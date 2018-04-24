package com.jude.rollviewpagerdome.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by garry on 17/11/10.
 */

public class FileUtil {
    private static final String LOG_TAG = "appcenter_file";

    public FileUtil() {
    }

    public static void mkDir(String dir) {
        File file = new File(dir);
        if(!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception var3) {
                ;
            }
        }

        file = null;
    }

    public static File createNewFile(String path, boolean append) {
        File newFile = new File(path);
        File parent;
        if(!append) {
            if(newFile.exists()) {
                newFile.delete();
            } else {
                parent = new File(path + ".png");
                if(parent != null && parent.exists()) {
                    parent.delete();
                }
            }
        }

        if(!newFile.exists()) {
            try {
                parent = newFile.getParentFile();
                if(parent != null && !parent.exists()) {
                    parent.mkdirs();
                }

                newFile.createNewFile();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return newFile;
    }

    public static boolean createFile(String destFileName, boolean replace) {
        File file = new File(destFileName);
        if(file.exists()) {
            if(!replace) {
                if(LogUtil.isDebuggable()) {
                    LogUtil.d("appcenter_file", "创建单个文件" + destFileName + "失败，目标文件已存在！");
                }

                return false;
            }

            file.delete();
        }

        if(destFileName.endsWith(File.separator)) {
            if(LogUtil.isDebuggable()) {
                LogUtil.d("appcenter_file", "创建单个文件" + destFileName + "失败，目标不能是目录！");
            }

            return false;
        } else {
            if(!file.getParentFile().exists()) {
                if(LogUtil.isDebuggable()) {
                    LogUtil.d("appcenter_file", "目标文件所在路径不存在，准备创建。。。");
                }

                if(!file.getParentFile().mkdirs()) {
                    if(LogUtil.isDebuggable()) {
                        LogUtil.d("appcenter_file", "创建目录文件所在的目录失败！");
                    }

                    return false;
                }
            }

            try {
                if(file.createNewFile()) {
                    if(LogUtil.isDebuggable()) {
                        LogUtil.d("appcenter_file", "创建单个文件" + destFileName + "成功！");
                    }

                    return true;
                } else {
                    if(LogUtil.isDebuggable()) {
                        LogUtil.d("appcenter_file", "创建单个文件" + destFileName + "失败！");
                    }

                    return false;
                }
            } catch (IOException var4) {
                var4.printStackTrace();
                if(LogUtil.isDebuggable()) {
                    LogUtil.d("appcenter_file", "创建单个文件" + destFileName + "失败！");
                }

                return false;
            }
        }
    }

    public static boolean saveStringToSDFile(String string, String fileName) {
        if(TextUtils.isEmpty(string)) {
            return false;
        } else {
            try {
                return saveByteToSDFile(string.getBytes("UTF-8"), fileName);
            } catch (Exception var3) {
                var3.printStackTrace();
                return false;
            }
        }
    }

    public static boolean saveByteToSDFile(byte[] byteData, String filePathName) {
        if(byteData != null && !TextUtils.isEmpty(filePathName)) {
            boolean result = false;

            try {
                File newFile = createNewFile(filePathName, false);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                fileOutputStream.write(byteData);
                fileOutputStream.flush();
                fileOutputStream.close();
                result = true;
            } catch (FileNotFoundException var5) {
                var5.printStackTrace();
            } catch (SecurityException var6) {
                var6.printStackTrace();
            } catch (IOException var7) {
                var7.printStackTrace();
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return result;
        } else {
            return false;
        }
    }

    public static boolean saveInputStreamToSDFile(InputStream inputStream, String filePathName) {
        boolean result = false;
        FileOutputStream os = null;

        try {
            File file = createNewFile(filePathName, false);
            os = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            boolean var6 = false;

            int len;
            while((len = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            os.flush();
            result = true;
        } catch (Exception var15) {
            var15.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception var14) {
                var14.printStackTrace();
            }

        }

        return result;
    }

    public static void copyFile(String srcStr, String decStr) {
        File srcFile = new File(srcStr);
        if(srcFile.exists()) {
            File decFile = new File(decStr);
            if(!decFile.exists()) {
                File parent = decFile.getParentFile();
                parent.mkdirs();

                try {
                    decFile.createNewFile();
                } catch (Exception var21) {
                    var21.printStackTrace();
                    return;
                }
            }

            InputStream input = null;
            FileOutputStream output = null;

            try {
                input = new FileInputStream(srcFile);
                output = new FileOutputStream(decFile);
                byte[] data = new byte[4096];

                while(true) {
                    int len = input.read(data);
                    if(len <= 0) {
                        break;
                    }

                    output.write(data);
                }
            } catch (Exception var22) {
                ;
            } finally {
                if(null != input) {
                    try {
                        input.close();
                    } catch (Exception var20) {
                        ;
                    }
                }

                if(null != output) {
                    try {
                        output.close();
                    } catch (Exception var19) {
                        ;
                    }
                }

            }

        }
    }

    public static byte[] readByteFromSDFile(String filePathName) {
        byte[] bs = null;

        try {
            File newFile = new File(filePathName);
            FileInputStream fileInputStream = new FileInputStream(newFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedInputStream inPutStream = new BufferedInputStream(dataInputStream);
            bs = new byte[(int)newFile.length()];
            inPutStream.read(bs);
            fileInputStream.close();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (SecurityException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return bs;
    }

    public static String readFileToString(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            File file = new File(filePath);
            if(!file.exists()) {
                return null;
            } else {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    return readInputStream(inputStream, "UTF-8");
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static String readInputStream(InputStream in, String charset) throws IOException {
        if(in == null) {
            return "";
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean var3 = true;

            try {
                byte[] buf = new byte[1024];
                boolean var6 = false;

                int len;
                while((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                byte[] data = out.toByteArray();
                String var7 = new String(data, TextUtils.isEmpty(charset)?"UTF-8":charset);
                return var7;
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                if(in != null) {
                    in.close();
                }

                if(out != null) {
                    out.close();
                }

            }

            return null;
        }
    }

    public static String readInputStreamWithLength(InputStream in, String charset, int length) throws IOException {
        if(in == null) {
            return "";
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean var4 = true;

            try {
                byte[] buf = new byte[1024];

                int len;
                for(int i = 0; (len = in.read(buf)) > 0 && i < length; ++i) {
                    out.write(buf, 0, len);
                }

                byte[] data = out.toByteArray();
                String var9 = new String(data, TextUtils.isEmpty(charset)?"UTF-8":charset);
                return var9;
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                if(in != null) {
                    in.close();
                }

                if(out != null) {
                    out.close();
                }

            }

            return null;
        }
    }

    public static boolean deleteDirectory(String sPath) {
        if(!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }

        File dirFile = new File(sPath);
        if(dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if(files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if(!flag) {
                        break;
                    }
                } else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if(!flag) {
                        break;
                    }
                }
            }

            return !flag?false:dirFile.delete();
        } else {
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {
        boolean result = false;
        if(!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if(file.exists()) {
                result = file.delete();
            }
        }

        return result;
    }

    public static boolean isFileExist(String filePath) {
        boolean result = false;

        try {
            File file = new File(filePath);
            result = file.exists();
            file = null;
        } catch (Exception var3) {
            ;
        }

        return result;
    }

    public static long getFileSize(String path) {
        long size = 0L;
        if(path != null) {
            File file = new File(path);
            size = file.length();
        }

        return size;
    }
}
