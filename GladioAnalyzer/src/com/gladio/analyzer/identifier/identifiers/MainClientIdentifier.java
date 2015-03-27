package com.gladio.analyzer.identifier.identifiers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.gladio.analyzer.identifier.ClassIdentifier;
import com.gladio.analyzer.identifier.Identifiers;

public class MainClientIdentifier extends ClassIdentifier {

	public MainClientIdentifier(){
		super(MCCacheNameIdentifier.class, MCFutureTaskIdentifier.class); // Require these to be done before this one runs.
	}
	
	@Override
	public boolean identify(ClassNode cn) {
		if (className != null){
			return false;
		} else {
			MCFutureTaskIdentifier mcfti = (MCFutureTaskIdentifier) Identifiers.getIdenti(MCFutureTaskIdentifier.class);
			if (mcfti != null){
				if (mcfti.found()){
					if (cn.interfaces.contains(mcfti.className)){
						for (FieldNode fn : cn.fields){
							if (fn.desc.contains(Identifiers.getIdenti(MCCacheNameIdentifier.class).className)){
								this.className = cn.name;
								return true;
							}
						}
					}
				}
			}
		}
		return false;
		
	}

}
