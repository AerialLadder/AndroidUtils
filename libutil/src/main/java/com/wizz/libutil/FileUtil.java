package com.wizz.libutil;

import java.io.File;

/**
 * =====================================================================================
 * Summary:
 * File: FileUtil.java
 *
 * @author xwy
 * @date 2019/7/13 15:21
 * =====================================================================================
 */
public class FileUtil {

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录和目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        return deleteDirectory(dir, true);
    }


    /**
     * 删除目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @param isDeleteDir 是否删除当前目录
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir, boolean isDeleteDir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        return deleteDirectory(dirFile, isDeleteDir);
    }

    /**
     * 删除目录和目录下的文件
     *
     * @param dirFile 要删除的目录
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(File dirFile) {
        return deleteDirectory(dirFile, true);
    }

    /**
     * 删除目录和目录下的文件
     *
     * @param dirFile 要删除的目录
     * @param isDeleteDir 是否删除当前目录
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(File dirFile, boolean isDeleteDir) {
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (null == dirFile || !dirFile.exists() || !dirFile.isDirectory()) {
            System.out.println("删除目录失败：目录不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i]
                        .getAbsolutePath(), true);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (isDeleteDir && dirFile.delete()) {
            System.out.println("删除目录成功！");
            return true;
        } else {
            return false;
        }
    }
}
