package com.gladio.analyzer.identifier.identifiers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.gladio.analyzer.identifier.ClassIdentifier;

public class MCCacheNameIdentifier extends ClassIdentifier {

	@Override
	public boolean identify(ClassNode cn) {
		if (this.className != null){
			return false;
		} else {
			if (cn.fields.size() == 2){
				for (FieldNode fn : cn.fields){
					if (fn.access != 20 || !fn.desc.contains("String")){
						return false;
					}
				}
				this.className = cn.name;
				return true;
			}
		}
		return false;
	}

}
