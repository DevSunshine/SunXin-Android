package com.sunshine.sunxin.plugin;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 插件的工具类，用到的工具静态方法在这里，包括zip、file、路径配置等等
 * <p/>
 * Created by huangjian on 2016/6/21.
 */
public class PluginUtil {

    private final static int BYTE_IN_SIZE = 4096;
    private static final int BUF_SIZE = 8192;

    private static final int CPU_AMR = 1;
    private static final int CPU_X86 = 2;
    private static final int CPU_MIPS = 3;

    private static String mInsidePluginPath = null;
    //start========================获取插件相关目录的方法======================



    /**
     * 关闭流
     *
     * @param closeable closeable
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    //start========================获取cpu类型的方法========================start

    /**
     * 获取cpu类型和架构
     *
     * @return 返回CPU的指令集类型，仅支持arm,x86和mips这三种，arm中不区分armv6，armv7和neon，有需要自行添加.
     */
    public static int getCpuArchitecture() {
        try {
            InputStream is = new FileInputStream("/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            try {
                String nameProcessor = "Processor";
                String nameModel = "model name";
                while (true) {
                    String line = br.readLine();
                    String[] pair;
                    if (line == null) {
                        break;
                    }
                    pair = line.split(":");
                    if (pair.length != 2)
                        continue;
                    String key = pair[0].trim();
                    String val = pair[1].trim();
                    if (key.compareTo(nameProcessor) == 0) {
                        if (val.contains("ARM")) {
                            return CPU_AMR;
                        }
                    }

                    if (key.compareToIgnoreCase(nameModel) == 0) {
                        if (val.contains("Intel")) {
                            return CPU_X86;
                        }
                    }

                    if (key.compareToIgnoreCase(nameProcessor) == 0) {
                        if (val.contains("MIPS")) {
                            return CPU_MIPS;
                        }
                    }
                }
            } finally {
                close(br);
                close(ir);
                close(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CPU_AMR;
    }

    /**
     * 获取当前应当执行的so文件的存放文件夹
     */
    public static String getLibFile(int cpuType) {
        switch (cpuType) {
            case CPU_AMR:
                return "lib/armeabi";
            case CPU_X86:
                return "lib/x86/";
            case CPU_MIPS:
                return "lib/mips/";
            default:
                return "lib/armeabi/";
        }
    }
    //end========================获取cpu类型的方法========================end


    //start========================文件相关的方法========================start

    /**
     * 创建文件夹
     *
     * @param dirPath 文件夹路径
     * @return 是否成功
     */
    public static boolean createDir(String dirPath) {
        File file = new File(dirPath);
        return !file.exists() && file.mkdirs();
    }

    /**
     * 删除文件夹
     *
     * @param file 文件对象
     */
    public static void deleteDirectory(File file) {
        if (!file.isDirectory()) {
            return;
        }
        File[] paths = file.listFiles();
        for (File pathF : paths) {
            if (pathF.isDirectory()) {
                deleteDirectory(pathF);
            } else {
                deleteFile(pathF);
            }
        }
        deleteFile(file);
    }

    /**
     * 为防止创建一个正在被删除的文件夹，所以在删除前先重命名该文件夹
     * 可以解决很多快速创建删除而产生的0字节大小文件问题
     *
     * @param file 文件对象
     * @return 是否成功
     */
    public static boolean deleteFile(File file) {
        File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);
        return to.delete();
    }

    /**
     * 重命名
     *
     * @param filePathName 原始文件路径
     * @param newPathName  新的文件路径
     * @return 是否成功
     */
    public static boolean rename(String filePathName, String newPathName) {
        if (TextUtils.isEmpty(filePathName)) return false;
        if (TextUtils.isEmpty(newPathName)) return false;

        delete(newPathName);

        File file = new File(filePathName);
        File newFile = new File(newPathName);
        if (!file.exists()) {
            return false;
        }
        File parentFile = newFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file.renameTo(newFile);
    }

    /**
     * 创建目录，整个路径上的目录都会创建
     *
     * @param path 路径
     * @return 文件
     */
    public static File createDirWithFile(String path) {
        File file = new File(path);
        if (!path.endsWith("/")) {
            file = file.getParentFile();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除文件
     */
    public static boolean delete(String filePathName) {
        if (TextUtils.isEmpty(filePathName)) return false;
        File file = new File(filePathName);
        return file.isFile() && file.exists() && file.delete();
    }

    /**
     * 文件是否存在
     *
     * @param filePathName 文件路径
     * @return 文件是否存在
     */
    public static boolean exists(String filePathName) {
        if (TextUtils.isEmpty(filePathName)) return false;
        File file = new File(filePathName);
        return (!file.isDirectory() && file.exists());
    }
    //end========================文件相关的方法========================

    //start========================压缩解压相关方法========================

    /**
     * 读取zip文件中某个文件为字符串
     *
     * @param zipFile     压缩文件
     * @param fileNameReg 需要获取的文件名
     * @return 获取的字符串
     */
    public static String readZipFileString(String zipFile, String fileNameReg) {
        String result = null;
        byte[] buffer = new byte[BUF_SIZE];
        InputStream in = null;
        ZipInputStream zipIn = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(zipFile);
            if (!file.exists()) return null;
            in = new FileInputStream(file);
            zipIn = new ZipInputStream(in);
            ZipEntry entry;
            while (null != (entry = zipIn.getNextEntry())) {
                String zipName = entry.getName();
                if (zipName.equals(fileNameReg)) {
                    int bytes;
                    int count = 0;
                    bos = new ByteArrayOutputStream();

                    while ((bytes = zipIn.read(buffer, 0, BUF_SIZE)) != -1) {
                        bos.write(buffer, 0, bytes);
                        count += bytes;
                    }
                    if (count > 0) {
                        result = bos.toString();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close(in);
                close(zipIn);
                close(bos);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将压缩文件中的某个文件夹拷贝到指定文件夹中
     *
     * @param zipFile     压缩文件
     * @param toDir       指定一个存放解压缩文件的文件夹,或者直接指定文件名方法自动识别
     * @param fileNameReg 需要解压的文件夹路径如：res/drawable-hdpi/
     * @return 是否成功
     */
    public static boolean unzipFile(String zipFile, String toDir, String fileNameReg) {
        boolean result = false;
        byte[] buffer = new byte[BUF_SIZE];
        InputStream in = null;
        ZipInputStream zipIn = null;
        try {
            File file = new File(zipFile);
            in = new FileInputStream(file);
            zipIn = new ZipInputStream(in);
            ZipEntry entry;
            while (null != (entry = zipIn.getNextEntry())) {
                String zipName = entry.getName();
                if (zipName.startsWith(fileNameReg)) {
                    String relName = toDir + zipName;
                    File unzipFile = new File(toDir);
                    if (unzipFile.isDirectory()) {
                        createDirWithFile(relName);
                        unzipFile = new File(relName);
                    }
                    FileOutputStream out = new FileOutputStream(unzipFile);
                    int bytes;

                    while ((bytes = zipIn.read(buffer, 0, BUF_SIZE)) != -1) {
                        out.write(buffer, 0, bytes);
                    }
                    close(out);
                }
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            close(in);
            close(zipIn);
        }
        return result;
    }


    //start========================反射相关方法========================

    /**
     * 反射的方式设置某个类的成员变量的值
     *
     * @param paramClass  类对象
     * @param paramString 域的名称
     * @param newClass    新的对象
     */
    public static void setField(Object paramClass, String paramString,
                                Object newClass) {
        if (paramClass == null || TextUtils.isEmpty(paramString)) return;
        Field field = null;
        Class cl = paramClass.getClass();
        for (; field == null && cl != null; ) {
            try {
                field = cl.getDeclaredField(paramString);
                if (field != null) {
                    field.setAccessible(true);
                }
            } catch (Throwable ignored) {

            }
            if (field == null) {
                cl = cl.getSuperclass();
            }
        }
        if (field != null) {
            try {
                field.set(paramClass, newClass);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            System.err.print(paramString + " is not found in " + paramClass.getClass().getName());
        }
    }

    /**
     * 反射的方式获取某个类的方法
     *
     * @param cl             类的class
     * @param name           方法名称
     * @param parameterTypes 方法对应的输入参数类型
     * @return 方法
     */
    public static Method getMethod(Class cl, String name, Class... parameterTypes) {
        Method method = null;
        for (; method == null && cl != null; ) {
            try {
                method = cl.getDeclaredMethod(name, parameterTypes);
                if (method != null) {
                    method.setAccessible(true);
                }
            } catch (Exception ignored) {

            }
            if (method == null) {
                cl = cl.getSuperclass();
            }
        }
        return method;
    }

    /**
     * 反射的方式获取某个类的某个成员变量值
     *
     * @param paramClass  类对象
     * @param paramString field的名字
     * @return field对应的值
     */
    public static Object getField(Object paramClass, String paramString) {
        if (paramClass == null) return null;
        Field field = null;
        Object object = null;
        Class cl = paramClass.getClass();
        for (; field == null && cl != null; ) {
            try {
                field = cl.getDeclaredField(paramString);
                if (field != null) {
                    field.setAccessible(true);
                }
            } catch (Exception ignored) {

            }
            if (field == null) {
                cl = cl.getSuperclass();
            }
        }
        try {
            if (field != null)
                object = field.get(paramClass);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
    //end========================反射相关方法========================
}
