package org.kevsoft.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Simple library class for working with JNI (Java Native Interface)
 * 
 * @see http://adamheinrich.com/2012/how-to-load-native-jni-library-from-jar
 * @see http://web.archive.org/web/20150418215650/http://adamheinrich.com/blog/
 *      2012/how-to-load-native-jni-library-from-jar/
 *      
 * @author Kevin Waldock
 * @author Adam Heirnich <adam@adamh.cz>, http://www.adamh.cz
 */
public class NativeUtils {

	/**
	 * Private constructor - this class will never be instanced
	 */
	private NativeUtils() {
	}

	private static boolean loadLibraryNoExcept(String libname) {
		try {
			System.loadLibrary(libname);
			return true;
		} catch (UnsatisfiedLinkError e) {
			return false;
		}
	}
	
	private static boolean loadLibraryJarNoExcept(String path) {
		try {
			loadLibraryFromJar(path);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	

	/**
	 * Will first try to load native library by java.library.path. If this fails
	 * then the JAR resource will be checked:
	 * 
	 * jar: /{path}/{arch}bit/lib{libname}.{ending}
	 * 
	 * where {arch} is the "bitness" (32 or 64) where {ending} is the system
	 * specific library ending.
	 * 
	 * @param path
	 *            equals {path}
	 * @param libname
	 *            equals {libname}
	 * @throws RuntimeException If the native library failed to load.
	 * 
	 * @since 17.07.2015
	 */
	public static void loadLibrarySmart(String path, String libname) {
		if (!path.startsWith("/"))
			path = "/" + path;
		
		String arch = System.getProperty("sun.arch.data.model");
		String mappedLibName = System.mapLibraryName(libname);

		if (loadLibraryNoExcept(libname))
			return;

		if (!libname.startsWith("lib")) {
			if (loadLibraryNoExcept("lib" + libname))
				return;
		}

		try {
			if(loadLibraryJarNoExcept(path + "/" + arch + "bit/" + mappedLibName))
				return;
			if(loadLibraryJarNoExcept(path + "/" + arch + "bit/lib" + mappedLibName))
				return;
		} catch (UnsatisfiedLinkError e) {
			throw new RuntimeException("Failed to load \"" + libname + "\" library due to link error", e);
		}
		
		throw new RuntimeException("Failed to find \"" + libname + "\" library. Following paths has been checked: \n"
				+ "in java.library.path for " + libname + "\n"
				+ "in java.library.path for lib" + libname + "\n"
				+ "in jar:" + path + "/" + arch + "bit/" + mappedLibName + "\n"
				+ "in jar:" + path + "/" + arch + "bit/lib" + mappedLibName);
	}

	/**
	 * Loads library from current JAR archive
	 * 
	 * The file from JAR is copied into system temporary directory and then
	 * loaded. The temporary file is deleted after exiting. Method uses String
	 * as filename because the pathname is "abstract", not system-dependent.
	 * 
	 * @param filename
	 *            The filename inside JAR as absolute path (beginning with '/'),
	 *            e.g. /package/File.ext
	 * @throws IOException
	 *             If temporary file creation or read/write operation fails
	 * @throws IllegalArgumentException
	 *             If source file (param path) does not exist
	 * @throws IllegalArgumentException
	 *             If the path is not absolute or if the filename is shorter
	 *             than three characters (restriction of
	 *             {@see File#createTempFile(java.lang.String,
	 *             java.lang.String)}).
	 */
	public static void loadLibraryFromJar(String path) throws IOException {

		if (!path.startsWith("/")) {
			throw new IllegalArgumentException("The path has to be absolute (start with '/').");
		}

		// Obtain filename from path
		String[] parts = path.split("/");
		String filename = (parts.length > 1) ? parts[parts.length - 1] : null;

		// Split filename to prexif and suffix (extension)
		String prefix = "";
		String suffix = null;
		if (filename != null) {
			parts = filename.split("\\.", 2);
			prefix = parts[0];
			suffix = (parts.length > 1) ? "." + parts[parts.length - 1] : null; // Thanks,
																				// davs!
																				// :-)
		}

		// Check if the filename is okay
		if (filename == null || prefix.length() < 3) {
			throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
		}

		// Prepare temporary file
		File temp = File.createTempFile(prefix, suffix);
		temp.deleteOnExit();

		if (!temp.exists()) {
			throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist.");
		}

		// Prepare buffer for data copying
		byte[] buffer = new byte[1024];
		int readBytes;

		// Open and check input stream
		InputStream is = NativeUtils.class.getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException("File " + path + " was not found inside JAR.");
		}

		// Open output stream and copy data between source file in JAR and the
		// temporary file
		OutputStream os = new FileOutputStream(temp);
		try {
			while ((readBytes = is.read(buffer)) != -1) {
				os.write(buffer, 0, readBytes);
			}
		} finally {
			// If read/write fails, close streams safely before throwing an
			// exception
			os.close();
			is.close();
		}

		// Finally, load the library
		System.load(temp.getAbsolutePath());

		// Fix for windows!
		// See:
		// http://web.archive.org/web/20150418215650/http://adamheinrich.com/blog/2012/how-to-load-native-jni-library-from-jar/#comment-1754835848
		final String libraryPrefix = prefix;
		final String lockSuffix = ".lock";

		// create lock file
		final File lock = new File(temp.getAbsolutePath() + lockSuffix);
		lock.createNewFile();
		lock.deleteOnExit();

		// file filter for library file (without .lock files)
		FileFilter tmpDirFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().startsWith(libraryPrefix) && !pathname.getName().endsWith(lockSuffix);
			}
		};

		// get all library files from temp folder
		String tmpDirName = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tmpDirName);
		File[] tmpFiles = tmpDir.listFiles(tmpDirFilter);

		// delete all files which don't have n accompanying lock file
		for (int i = 0; i < tmpFiles.length; i++) {
			// Create a file to represent the lock and test.
			File lockFile = new File(tmpFiles[i].getAbsolutePath() + lockSuffix);
			if (!lockFile.exists()) {
				tmpFiles[i].delete();
			}
		}
	}
}
