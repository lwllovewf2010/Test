package com.example.test.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

public class FileUtils {

	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将一个String里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName, String input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			output.write(input.getBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	/*
	 * 写入到内部存储，只需要传文件名如：a.html
	 * */
	public void write2InnerStorage(String path, String str, Context context) {
		try {
			File file = context.getDir(path, Context.MODE_PRIVATE);
			file.mkdirs();
			//
			// FileOutputStream fos =
			// context.getApplicationContext().openFileOutput(path,Context.MODE_PRIVATE);
			// fos.write(str.getBytes());
			// fos.flush();
			// fos.close();

			file = new File(context.getFilesDir(), path);
			FileOutputStream fos = new FileOutputStream(file);

			fos.write(str.getBytes());
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 从内部存储读取，只需要传文件名如：a.html
	 * */
	public String readInnerFile(String path, Context context) {
		try {
			FileInputStream fis = context.openFileInput(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int len = -1;
			while ((len = fis.read(data)) != -1) {
				baos.write(data, 0, len);
			}
			return new String(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
