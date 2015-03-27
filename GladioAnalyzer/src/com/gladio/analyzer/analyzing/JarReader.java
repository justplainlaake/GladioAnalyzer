package com.gladio.analyzer.analyzing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.gladio.analyzer.identifier.Identifiers;

public class JarReader {

	public static File parentDir;

	public final static boolean useOneFileForOutput = true;

	private List<String> classNames = new ArrayList<>();

	public JarReader(File file) throws ClassNotFoundException, IOException {
		/*
		 * if (useOneFileForOutput){ parentDir = new File(file.getParentFile() + "/output.txt"); System.setOut(new PrintStream(parentDir)); } else { parentDir = new File(file.getParentFile() + "/output/"); parentDir.mkdirs(); }
		 */
		JarFile jarFile = new JarFile(file);
		for (int i = 0; i < Identifiers.identifiersSize(); i++) {
			Enumeration e = jarFile.entries();

			URL[] urls = { new URL("jar:file:" + file + "!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());

			while (e.hasMoreElements()) {
				JarEntry je = (JarEntry) e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				ClassNode classNode = new ClassNode();

				InputStream classFileInputStream = jarFile.getInputStream(je);
				try {
					ClassReader classReader = new ClassReader(classFileInputStream);
					classReader.accept(classNode, 0);
					Identifiers.runThroughIdentifiers(classNode);
				} finally {
					classFileInputStream.close();
				}
			}
		}
	}
}
