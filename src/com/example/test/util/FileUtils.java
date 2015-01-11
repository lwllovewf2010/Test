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
		// �õ���ǰ�ⲿ�洢�豸��Ŀ¼
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStream���������д�뵽SD����
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
	 * ��һ��String���������д�뵽SD����
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
	 * д�뵽�ڲ��洢��ֻ��Ҫ���ļ����磺a.html
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
	 * ���ڲ��洢��ȡ��ֻ��Ҫ���ļ����磺a.html
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
