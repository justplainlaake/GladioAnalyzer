package com.gladio.analyzer;

import java.io.File;
import java.io.IOException;

import com.gladio.analyzer.analyzing.JarReader;

public class AnalyzerMain {

	public static void main(String[] args){
		try {
			JarReader jr = new JarReader(new File("./DeCompiledVersions/1.8.1/1.8.1.jar"));
		} catch (ClassNotFoundException | IOException e) {
			
		}
	}
	
}
