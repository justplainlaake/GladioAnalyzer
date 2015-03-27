package com.gladio.analyzer.identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import com.gladio.analyzer.identifier.identifiers.MCCacheNameIdentifier;
import com.gladio.analyzer.identifier.identifiers.MCFutureTaskIdentifier;
import com.gladio.analyzer.identifier.identifiers.MainClientIdentifier;

public class Identifiers {
	private static List<ClassIdentifier> identifi = new ArrayList<>();
	private static Map<Class<? extends ClassIdentifier>, ClassIdentifier> identifiers = new HashMap<>();
	private static List<Class<? extends ClassIdentifier>> succesfullIdentifiers = new ArrayList<>();

	static {
		addIdent(new MainClientIdentifier());
		addIdent(new MCFutureTaskIdentifier());
		addIdent(new MCCacheNameIdentifier());
	}

	private static void addIdent(ClassIdentifier ident) {
		identifi.add(ident);
		identifiers.put(ident.getClass(), ident);
	}

	public static int identifiersSize() {
		return identifi.size();
	}

	public static ClassIdentifier getIdenti(Class<? extends ClassIdentifier> clazz) {
		if (identifiers.containsKey(clazz)) {
			return identifiers.get(clazz);
		} else {
			return null;
		}
	}

	public static void runThroughIdentifiers(ClassNode cn) {
		for (ClassIdentifier ci : identifi) {
			if (ci.canRun()) {
				if (ci.identify(cn)) {
					System.out.println("Identified[" + cn.name + "] by Identifier[" + ci.getClass().getSimpleName() + "]");
					succesfullIdentifiers.add(ci.getClass());
					return;
				}
			}
		}
	}

	public static boolean isSuccesfull(Class<? extends ClassIdentifier> clazz) {
		return succesfullIdentifiers.contains(clazz);
	}
}
