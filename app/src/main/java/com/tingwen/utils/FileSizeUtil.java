package com.tingwen.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件大小工具
 */
public class FileSizeUtil {
	
	public static String getFileSize(String size) {
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		float fileSize;
		try {
			fileSize = Float.parseFloat(size);
			fileSize = fileSize / 1024 / 1024;
			return decimalFormat.format(fileSize) + "Mb";
		} catch (Exception e) {
            return "0Mb";
		}
	}

    /**
     * 指定文件夹获取文件夹中格式是.mp3格式的文件
     * @param filepath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readfile(String filepath) throws FileNotFoundException, IOException {
        List<String> listFile = new ArrayList<>();
        File file = new File(filepath);
        if (file.isDirectory()) {
            System.out.println("文件夹");
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    String fileName = filepath + "\\" + filelist[i];
                    String[] fileSplit = fileName.split(".");
                    if("mp3".equals(fileSplit[1].trim())){
                        listFile.add(fileName);
                    }
                }
            }
        }
        return listFile;
    }

//    /**
//     * 下载数据库里的数据在本地文件是否能找到，不能找到则删除数据库中的数据同时返回合并完的数据
//     * @param downloadList
//     * @param fileList
//     * @param sqlManager
//     * @return
//     */
//    public static ArrayList<NewsJson> containSQLDelete(ArrayList<NewsJson> downloadList, List<String> fileList, SQLManager sqlManager){
//        ArrayList<NewsJson> list = downloadList;
//        int countFile = 0;
//        for (int i = 0; i < list.size(); i++) {
//            for (String str: fileList ) {
//                String fileId = str.substring(str.indexOf("\\")+1,str.indexOf("."));
//                if(fileId.trim().equals(list.get(i).id)){
//                    countFile++;
//                }
//            }
//            if(countFile == 0){
//                if(sqlManager != null) {
//                    sqlManager.deleteSingleDate(TWContants.DOWNLOAD, list.get(i).id);
//                }
//            }
//            countFile = 0;
//        }
//        return list;
//    }
//
//    public static ArrayList<NewsJson> containFileDelete(ArrayList<NewsJson> downloadList, List<String> fileList, SQLManager sqlManager){
//        ArrayList<NewsJson> list = downloadList;
//        int countSQL = 0;
//        for (int i = 0; i <fileList.size() ; i++) {
//            String fileId = fileList.get(i).substring(fileList.get(i).indexOf("\\") + 1, fileList.get(i).indexOf("."));
//            for (NewsJson newsJson:list) {
//                if(fileId.trim().equals(newsJson.id)){
//                    countSQL++;
//                }
//            }
//            if(countSQL == 0){
//
//            }
//        }
//        return list;
//    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
