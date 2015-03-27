package com.gladio.analyzer.identifier;

import org.objectweb.asm.tree.ClassNode;

public abstract class ClassIdentifier {
	
	public String className;
	
	private Class<? extends ClassIdentifier>[] reqs;
	
	public ClassIdentifier(Class<? extends ClassIdentifier>... reqs){
		this.reqs = reqs;
	}
	
	public abstract boolean identify(ClassNode cn);
	
	public boolean found(){
		return className!=null;
	}
	
	public boolean canRun(){
		for (Class<? extends ClassIdentifier> clazz : reqs){
			if (!Identifiers.isSuccesfull(clazz))
				return false;
		}
		return true;
	}
	
}
