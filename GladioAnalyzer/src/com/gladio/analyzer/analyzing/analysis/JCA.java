package com.gladio.analyzer.analyzing.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import com.gladio.analyzer.analyzing.JarReader;

public class JCA extends ClassVisitor {

	public JCA() {
		super(org.objectweb.asm.Opcodes.ASM4);
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if (!JarReader.useOneFileForOutput) {
			try {
				File f = new File(JarReader.parentDir + "/" + name.replace("/", ".") + ".txt");
				System.setOut(new PrintStream(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println(name + " extends " + superName + " {");
	}

	public void visitSource(String source, String debug) {
	}

	public void visitOuterClass(String owner, String name, String desc) {
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	public void visitAttribute(Attribute attr) {
	}

	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		System.out.println("\tField[" + name + " as " + desc.replace("L", "") + "]");
		return null;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		System.out.println("\tMethod[" + name + desc + "]");
		return null;
	}

	public void visitEnd() {
		System.out.println("}\n\n");
	}

}
