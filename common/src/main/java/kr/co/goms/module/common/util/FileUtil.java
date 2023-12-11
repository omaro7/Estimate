package kr.co.goms.module.common.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import kr.co.goms.module.common.AppConstant;

public class FileUtil {

	private final static String TAG = FileUtil.class.getSimpleName();
	private static final int FILE_EXTENSION_MAX_CHARS = 4;

	public static String generalizePath(String path)
	{
		StringBuffer sb = new StringBuffer();
		int l = path.length();
		for(int i = 0; i < l; i++)
		{
			char ch = path.charAt(i);
			if(ch == '\\' || ch == '/')
			{
				sb.append(File.separatorChar);
				for(; i < l - 1 && (path.charAt(i + 1) == '\\' || path.charAt(i + 1) == '/'); i++);
			} else
			{
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static boolean isExist(String fileName)
	{
		boolean ise = true;
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(generalizePath(fileName));
		}
		catch(FileNotFoundException e)
		{
			ise = false;
		}
		return ise;
	}

	public static String getFileDirPath(String _path){
		String targetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + _path;
		return targetDirPath;
	}

	public static String getFilePath(String _dir, String _filename){
		String dir = getFileDirPath(_dir);
		String path = dir + File.separator + _filename;
		return path;
	}

	public static File getFile(String _dir, String _filename){
		String dir = getFileDirPath(_dir);
		File newOrderJsonfile = new File( dir + File.separator + _filename);
		return newOrderJsonfile;
	}

	public static boolean deleteFile(String _filePath){
		File file = new File(_filePath);
		if(file.delete()){
			return true;
		}else{
			return false;
		}
	}
	public static boolean deleteFile(File _file){
		if(_file.delete()){
			return true;
		}else{
			return false;
		}
	}

	public static String getFileContents(File file) throws FileNotFoundException {
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader streamReader = new InputStreamReader(fis);
			BufferedReader bufferReader = new BufferedReader(streamReader);

			String line;
			while((line = bufferReader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}

			bufferReader.close();
			streamReader.close();
			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return (file.exists() && file.isFile());
	}

	public static boolean isVideoFile(String filePath) {
		if (filePath.endsWith(".mp4"))
		// Add other formats as desired
		{
			return true;
		}
		return false;
	}

	public static boolean isImageFile(String filePath) {
		if (filePath.endsWith(".png") || filePath.endsWith(".jpg"))
		// Add other formats as desired
		{
			return true;
		}
		return false;
	}

	public static boolean isUmzzalFile(String filePath) {
		if (filePath.endsWith(".gif"))
		// Add other formats as desired
		{
			return true;
		}
		return false;
	}


	public static String formatFileSize(long size) {
		String hrSize = null;

		double b = size;
		double k = size/1024.0;
		double m = ((size/1024.0)/1024.0);
		double g = (((size/1024.0)/1024.0)/1024.0);
		double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

		DecimalFormat dec = new DecimalFormat("0.00");

		if ( t>1 ) {
			hrSize = dec.format(t).concat(" TB");
		} else if ( g>1 ) {
			hrSize = dec.format(g).concat(" GB");
		} else if ( m>1 ) {
			hrSize = dec.format(m).concat(" MB");
		} else if ( k>1 ) {
			hrSize = dec.format(k).concat(" KB");
		} else {
			hrSize = dec.format(b).concat(" Bytes");
		}

		return hrSize;
	}


	public static String getNewFileName(String filePath){

		String strFileName= FilenameUtils.getBaseName(filePath);
		String strFileExtention= FilenameUtils.getExtension(filePath);

		File file = new File(filePath);
		String sDir = file.getParent();
		File dir = new File(sDir);
		File[] files = dir.listFiles();

		int i = 1;
		while (true)
		{
			if (file.exists())
			{
				file = new File(dir, strFileName + "_" + i + "." + strFileExtention);
				i++;
				continue;
			}
			else
			{
				break;
			}
		}
		return file.getName();
	}

	public static File newFileName(File f) { // File f는 원본 파일
		if (createNewFile(f))
			return f; // 생성된 f가 중복되지 않으면 리턴

		String name = f.getName();
		String body = null;
		String ext = null;

		int dot = name.lastIndexOf(".");
		if (dot != -1) { // 확장자가 없을때
			body = name.substring(0, dot);
			ext = name.substring(dot);
		} else { // 확장자가 있을때
			body = name;
			ext = "";
		}

		int count = 0;
		// 중복된 파일이 있을때
		// 파일이름뒤에 a숫자.확장자 이렇게 들어가게 되는데 숫자는 9999까지 된다.
		while (!createNewFile(f) && count < 9999) {
			count++;
			//String newName = body + "_" +count + ext;
			String newName = body + count + ext;
			GomsLog.d(TAG, "newName : " + newName);
			f = new File(f.getParent(), newName);
		}
		return f;
	}

	private static boolean createNewFile(File f) {
		try {
			return f.createNewFile(); // 존재하는 파일이 아니면
		} catch (IOException ignored) {
			return false;
		}
	}

	private static boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),filePath.length());

		if (AppConstant.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault()))){
			return true;
		}else{
			return false;
		}
	}

	/* Get the newest file for a specific extension */
	public File getTheNewestFile(String filePath, String ext) {
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + ext);
		File[] files = dir.listFiles(fileFilter);

		if (files.length > 0) {
			/** The newest file comes first **/
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			theNewestFile = files[0];
		}

		return theNewestFile;
	}

	/**
	 * Split out a filename's extension and return it.
	 * @param filename a file name
	 * @return the file extension (max of 5 chars including period, like ".docx"), or null
	 */
	public static String getFileExtension(String filename) {
		String extension = null;
		int index = !TextUtils.isEmpty(filename) ? filename.lastIndexOf('.') : -1;
		// Limit the suffix to dot + four characters
		if (index >= 0 && filename.length() - index <= FILE_EXTENSION_MAX_CHARS + 1) {
			extension = filename.substring(index);
		}
		return extension;
	}

	/**
	 * 파일 복사
	 * @param fromPath Environment.getExternalStorageDirectory().getAbsolutePath().getAbsolutePath()+"/kaic1/imagem.jpg"
	 * @param toPath Environment.getExternalStorageDirectory().getAbsolutePath()+"/kaic2/imagem.jpg"
	 * @return
	File from = new File(mFilePath);
	File to = new File(AppConstant.FULL_SAVE_IMAGE_FOLD_NAME, mTargetFileName);
	from.renameTo(to);
	 */
	public static void moveFile(String fromPath, String toPath){
		File from = new File(fromPath);
		File to = new File(toPath);
		from.renameTo(to);
	}

	/**
	 *
	 * @throws IOException
	 */
	public static void copyFile(String fromPath, String toPath) throws IOException {
		File from = new File(fromPath);
		File to = new File(toPath);
		FileInputStream var2 = new FileInputStream(from);
		FileOutputStream var3 = new FileOutputStream(to);
		byte[] var4 = new byte[1024];

		int var5;
		while ((var5 = var2.read(var4)) > 0) {
			var3.write(var4, 0, var5);
		}

		var2.close();
		var3.close();
	}
	/**
	 * 저장 폴더 가지고 오기
	 * @param filePath   /storage/emulated/0/StoreCamera/StoreCameraPhoto/storecamera_20160615164254_0.jpg
	 * @return /StoreCamera/StoreCameraPhoto
	 */
	public static String getGPUIImageFileFolder(String filePath){
		File file = new File(filePath);
		String folder = file.getParent();
		String envFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
		String tmpFolder = folder.replace(envFolder, "");
		Log.d(TAG, "fileFolder : " + tmpFolder);
		return tmpFolder;
	}

	/**
	 * 저장 폴더 가지고 오기
	 * @param filePath   /storage/emulated/0/Pictures/StoreCamera/StoreCameraPhoto/storecamera_20160615164254_0.jpg
	 * @return Pictures/StoreCamera/StoreCameraPhoto
	 */
	public static String getFileFolder(String filePath){
		File file = new File(filePath);
		String folder = file.getParent();
		String envFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
		String tmpFolder = folder.replace(envFolder, "");
		return tmpFolder;
	}

	/**
	 * 풀 저장 폴더 가지고 오기
	 * @param filePath   /storage/emulated/0/Pictures/StoreCamera/StoreCameraPhoto/storecamera_20160615164254_0.jpg
	 * @return /storage/emulated/0/Pictures/StoreCamera/StoreCameraPhoto/
	 */
	public static String getFileFolderFullPath(String filePath){
		File file = new File(filePath);
		String folder = file.getParent();
		return folder;
	}

	/**
	 * 파일명만 가지고 오기
	 * @param filePath   /storage/emulated/0/Pictures/StoreCamera/StoreCameraPhoto/storecamera_20160615164254_0.jpg
	 * @return storecamera_20160615164254_0.jpg
	 */
	public static String getFileName(String filePath){
		File file = new File(filePath);
		String name = file.getName();
		return name;
	}

	public static long getFileTime(String filePath){
		File file = new File(filePath);
		long time = file.lastModified();
		return time;
	}

	/**
	 *
	 * @param folderPath
	 * @return
	 */
	public static boolean checkFolderExists(String folderPath)
	{
		boolean ret = false;
		File dir = new File(folderPath);
		if(dir.exists() && dir.isDirectory())
			ret = true;
		return ret;
	}

	public static boolean isZipValid(File file) {
		ZipFile zipfile = null;
		ZipInputStream zis = null;
		try {
			zipfile = new ZipFile(file);
			zis = new ZipInputStream(new FileInputStream(file));
			ZipEntry ze = zis.getNextEntry();
			if(ze == null) {
				return false;
			}
			while(ze != null) {
				// if it throws an exception fetching any of the following then we know the file is corrupted.
				zipfile.getInputStream(ze);
				ze.getCrc();
				ze.getCompressedSize();
				ze.getName();
				ze = zis.getNextEntry();
			}
			return true;
		} catch (ZipException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (zipfile != null) {
					zipfile.close();
					zipfile = null;
				}
			} catch (IOException e) {
				return false;
			} try {
				if (zis != null) {
					zis.close();
					zis = null;
				}
			} catch (IOException e) {
				return false;
			}

		}
	}

	/**
	 * 해당 폴더의 파일 전체 삭제
	 * @param context
	 * @param tmpPath	AppConstant.FULL_SAVE_TEMP_FOLD_NAME
	 */
	public static void deleteLocalTempFileList(Context context, ContentResolver contentResolver, String tmpPath) {
		GomsLog.d(TAG, "deleteLocalTempFileList : " + tmpPath);
		try {
			File cleaner = new File(tmpPath);
			if (cleaner.isDirectory()) {
				String[] children = cleaner.list();
				GomsLog.d(TAG, "cleaner.isDirectory() : " + cleaner.isDirectory());
				GomsLog.d(TAG, "length : " + children.length);

				for (int i = 0; i < children.length; i++) {
					GomsLog.d(TAG, "deleteLocalFile : " + tmpPath + File.separator + children[i]);

					try {
						String filePath = tmpPath + File.separator + children[i];
						Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='" + filePath + "'", null, null);
						cursor.moveToNext();
						int id = cursor.getInt(0);

						Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
						GomsLog.d("URI",uri.toString());
						contentResolver.delete(uri, null, null);

					}catch(Exception e){
						GomsLog.d(TAG, "e : " + e.toString());
					}
/*
					Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
					GomsLog.d("URI",uri.toString());
					contentResolver.delete(uri, null, null);
*/

					/*
					//contentResolver.delete(imageUriLcl, null, null);
					MediaScannerConnection.scanFile(context, new String[]{tmpPath + File.separator + children[i]}, null, new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							Log.e("ExternalStorage", "Scanned " + path + ":");
							Log.e("ExternalStorage", "-> uri=" + uri);
							contentResolver.delete(uri, null, null);
						}
					});
					//contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?", new String[]{tmpPath + File.separator + children[i]});
					*/
				}
			}
		}catch(NullPointerException e){

		}
	}


	public static String  getFileName (Uri uri){
		return uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1);
	}

	public static String getFileNameParser(String filePath){
		File file = new File(filePath);
		String name = file.getName();
		String body = null;

		int dot = name.lastIndexOf(".");
		if (dot != -1) { // 확장자가 없을때
			body = name.substring(0, dot);
		} else { // 확장자가 있을때
			body = name;
		}
		return body;
	}
	public static String getExtensionFromUrl(String urlString) {

		Uri uri = Uri.parse(urlString);
		String fileName = uri.getPath();

		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
			return null; // No extension found
		}
		return fileName.substring(dotIndex + 1);

	}
}
