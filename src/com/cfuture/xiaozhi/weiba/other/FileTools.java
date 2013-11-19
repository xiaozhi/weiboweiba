package com.cfuture.xiaozhi.weiba.other;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作工具
 * @author Thunder
 *
 */
public class FileTools {
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		if (new File(filePath).exists()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 复制文件方法
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(String sourceFile, String targetFile)
			throws IOException {
		
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(new File(sourceFile)));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(new File(targetFile)));

			// 缓冲数组
			byte[] b = new byte[1024];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
	
	/**
	 * 以Stream的形式复制文件
	 * @param inputStream
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFileToSys(InputStream inputStream, OutputStream outputStream) 
			throws IOException {
		
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(inputStream);

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(outputStream);

			// 缓冲数组
			byte[] b = new byte[1024];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
	
	/**
	 * 文件流， 文件路径方式
	 * @param inputStream
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFileToSys(InputStream inputStream, String targetFile) 
			throws IOException {
		
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(inputStream);

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(new File(targetFile)));

			// 缓冲数组
			byte[] b = new byte[1024];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
	
	/**
	 * 删除文件方法
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		return (new File(filePath).delete());
	}
	
	/**
	 * 判断目录是否可写
	 * @return
	 */
	public static boolean isDirCanWrite(String fileDir) {
		return (new File(fileDir).canWrite());
	}
}
