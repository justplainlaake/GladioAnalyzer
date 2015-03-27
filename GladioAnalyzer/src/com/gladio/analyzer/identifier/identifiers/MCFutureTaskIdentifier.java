package com.gladio.analyzer.identifier.identifiers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.gladio.analyzer.identifier.ClassIdentifier;

public class MCFutureTaskIdentifier extends ClassIdentifier {

	private boolean hasListenableFutureMethod = false;
	public static boolean foundFutureTaskClass = false;
	
	public static MCFutureTaskIdentifier instance;
	public MCFutureTaskIdentifier(){
		instance = this;
	}
	
	@Override
	public boolean identify(ClassNode cn) {
		if (className != null){
			return false;
		}
		if (cn.methods.size() == 2){
			for (MethodNode mn : cn.methods){
				if (mn.desc.contains("ListenableFuture") && mn.desc.contains("Runnable")){
					this.hasListenableFutureMethod = true;
					this.className = cn.name;
					foundFutureTaskClass = true;
					return true;
				}
			}
		}
		return false;
	}

}
