package dfh.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClassUtil {

	private static final Log log = LogFactory.getLog(ClassUtil.class);

	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			log.warn((new StringBuilder("用户定义包名 ")).append(packageName).append(" 下没有任何文件").toString());
			return;
		}
		File dirfiles[] = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return recursive && file.isDirectory() || file.getName().endsWith(".class");
			}
		});
		File afile[];
		int j = (afile = dirfiles).length;
		for (int i = 0; i < j; i++) {
			File file = afile[i];
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile((new StringBuilder(String.valueOf(packageName))).append(".").append(file.getName()).toString(), file.getAbsolutePath(), recursive, classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Class.forName((new StringBuilder(String.valueOf(packageName))).append('.').append(className).toString()));
				} catch (ClassNotFoundException e) {
					log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}

	}

	public static Set getClasses(Package pack) {
		Set classes = new LinkedHashSet();
		boolean recursive = true;
		String packageName = pack.getName();
		String packageDirName = packageName.replace('.', '/');
		try {
			for (Enumeration dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName); dirs.hasMoreElements();) {
				URL url = (URL) dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol))
					try {
						JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
						for (Enumeration entries = jar.entries(); entries.hasMoreElements();) {
							JarEntry entry = (JarEntry) entries.nextElement();
							String name = entry.getName();
							if (name.charAt(0) == '/')
								name = name.substring(1);
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								if (idx != -1)
									packageName = name.substring(0, idx).replace('/', '.');
								if ((idx != -1 || recursive) && name.endsWith(".class") && !entry.isDirectory()) {
									String className = name.substring(packageName.length() + 1, name.length() - 6);
									try {
										classes.add(Class.forName((new StringBuilder(String.valueOf(packageName))).append('.').append(className).toString()));
									} catch (ClassNotFoundException e) {
										log.error("添加用户自定义视图类错误 找不到此类的.class文件");
										e.printStackTrace();
									}
								}
							}
						}

					} catch (IOException e) {
						log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	public static String[] getPackageAllClassName(String classLocation, String packageName) {
		String packagePathSplit[] = packageName.split("[.]");
		String realClassLocation = classLocation;
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++)
			realClassLocation = (new StringBuilder(String.valueOf(realClassLocation))).append(File.separator).append(packagePathSplit[i]).toString();

		File packeageDir = new File(realClassLocation);
		if (packeageDir.isDirectory()) {
			String allClassName[] = packeageDir.list();
			return allClassName;
		} else {
			return null;
		}
	}

	public static void main(String args[]) {
		try {
			Class.forName("dfh.model.Accountinfo");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Iterator it = getClasses(Package.getPackage("dfh.model")).iterator();
		System.out.println("<list>");
		while (it.hasNext()) {
			Class c = (Class) it.next();
			if (!c.getCanonicalName().contains("enums"))
				System.out.println((new StringBuilder("<value>")).append(c.getCanonicalName()).append("</value>").toString());
		}
		System.out.println("</list>");
	}

	public ClassUtil() {
	}

}
