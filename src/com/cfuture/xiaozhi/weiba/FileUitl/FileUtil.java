/**
 * @Project: weiboweiba2
 * @Title: FileUtil.java
 * @Package com.cfuture.xiaozhi.weiba.FileUitl
 * @Description TODO
 * @author cfuture_小智
 * @data 2012-12-10 下午03:36:38
 * @Copyright 2012 www.lurencun.com
 * @version v1.0 
 */
package com.cfuture.xiaozhi.weiba.FileUitl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.cfuture.xiaozhi.weiba.other.ShellCommand;
import com.cfuture.xiaozhi.weiba.other.ShellCommand.Shell;

/**
 * 文件操作
 * 
 * @author cfuture_小智
 */
public class FileUtil {

	private final static String SYSTEM_BUILD = "system/build.prop";

	/**
	 * 读取一个文件
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream readSDFile(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		if (file.isDirectory()) {
			return null;
		}
		FileInputStream fis = new FileInputStream(file);

		return fis;
	}

	/**
	 * 修改手机型号厂商信息
	 * 
	 * @param model
	 *            型号
	 * @param manufacturer
	 *            厂商
	 * @return
	 */
	public StringBuffer modifyPhoneType(String model, String manufacturer) {
		StringBuffer sb = new StringBuffer();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					readSDFile(SYSTEM_BUILD)));
			String temp;

			while ((temp = input.readLine()) != null) {

				String sort[] = temp.split("=");
				// 判断是否是厂商,如果是,则更改
				if ("ro.product.manufacturer".equals(sort[0])) {
					System.out.println(sort[0] + ":" + sort[1]);
					temp = "ro.product.manufacturer=" + manufacturer;
				}
				if ("ro.product.model".equals(sort[0])) {
					System.out.println(sort[0] + ":" + sort[1]);
					temp = "ro.product.model=" + model;
				}
				sb.append(temp + "\n");
			}

			// 把替换掉的内容写回到原来的文件中
			writeFile2SD(SYSTEM_BUILD, sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb;
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 * @param sb
	 */
	public void writeFile2SD(String path, StringBuffer sb) {
		File file = new File(path);
		OutputStreamWriter outWriter = null;
		try {
			outWriter = new OutputStreamWriter(new FileOutputStream(file));
			outWriter.write(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException E) {
			E.printStackTrace();
		} finally {
			// Log.i(tag, msg)
			if (outWriter != null) {
				try {
					outWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 修改文件权限
	 * 
	 * @param path
	 * @return
	 */
	public boolean modifyFilePression(String path, String permission) {
		Shell su = new ShellCommand().su;
		try {
			su.runWaitFor(permission + path);
			Thread.sleep(1000);
			// Log.i("qx", new File(path).canWrite()+":"+path);
			// System.out.println(new File(path).canWrite() + ":" + path);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 目录是否可写
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isDirWrite(String path) {
		return (new File(path).canWrite());
	}

	/**
	 * 把文本文件每一行添加到一个List中
	 * @param input
	 * @return
	 */
	public static List<String> readLines(InputStream input) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		List<String> list = new ArrayList<String>();
		String line;
		try {
			line = reader.readLine();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;

	}
}
