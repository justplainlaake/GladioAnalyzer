package com.gladio.analyzer.analyzing.analysis.insn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Finds a sequence of instructions in a method's bytecode.
 * 
 * @author Arvid Svensson
 */
public class InstructionFinder {
	/**
	 * The method to search in.
	 */
	private MethodNode methodNode;

	/**
	 * The instruction opcodes of the instruction in method to search.
	 */
	private String methodOpcodes;

	/**
	 * Create an <code>InstructionFinder</code> fot the specified method
	 * 
	 * @param methodNode
	 *            The <code>MethodNode</code> to search in.
	 */
	public InstructionFinder(MethodNode methodNode) {
		this.methodNode = methodNode;

		// Line up all the method's instruction opcodes in a string
		StringBuffer buffer = new StringBuffer();
		for (Iterator iter = methodNode.instructions.iterator(); iter.hasNext();) {
			int opcode = ((AbstractInsnNode) iter.next()).getOpcode();
			buffer.append((char) opcode);
		}
		methodOpcodes = buffer.toString();
	}

	/**
	 * Search for the specified sequence of opcodes.
	 * 
	 * @param opcodes
	 *            The opcode sequence to find.
	 * @return A <code>Collection</code> of <code>List</code>s containing the matched <code>AbstractInsnNode</code>s.
	 */
	public Collection search(int[] opcodes) {
		return search(opcodes, new Condition() {
			public boolean include(List abstractInsnNodeList) {
				return true;
			}
		});
	}

	/**
	 * Search for the specified sequence of opcodes where each instruction of type <code>MethodInsnNode</code> matching the corresponding regex.
	 * 
	 * @param opcodes
	 *            The opcode sequence to find.
	 * @param regex
	 *            The _expression_ to match. <code>null</code> entries will be skipped. Format: "owner#method:desc", e.g. "java/lang/String#.*:\(\).*" will match any empty-argument method in java.lang.String.
	 * @return A <code>Collection</code> of <code>List</code>s containing the matched <code>AbstractInsnNode</code>s.
	 */
	public Collection search(int[] opcodes, final String[] regex) {
		Pattern[] patterns = new Pattern[regex.length];
		for (int i = 0; i < regex.length; i++) {
			if (regex[i] != null) {
				patterns[i] = Pattern.compile(regex[i]);
			}
		}

		return search(opcodes, patterns);
	}

	/**
	 * Search for the specified sequence of opcodes where each instruction of type <code>MethodInsnNode</code> matching the corresponding regex.
	 * 
	 * @param opcodes
	 *            The opcode sequence to find.
	 * @param patterns
	 *            The <code>Pattern</code> to match. <code>null</code> entries will be skipped. Format: "owner#method:desc", e.g. "java/lang/String#.*:\(\).*" will match any empty-argument method in java.lang.String.
	 * @return A <code>Collection</code> of <code>List</code>s containing the matched <code>AbstractInsnNode</code>s.
	 */
	public Collection search(int[] opcodes, final Pattern[] patterns) {
		// ok, let's create a Condition that matches the patterns
		return search(opcodes, new Condition() {
			public boolean include(List abstractInsnNodeList) {
				for (int i = 0; i < patterns.length; i++) {
					if (patterns[i] != null) {
						AbstractInsnNode abstractInsnNode = (AbstractInsnNode) abstractInsnNodeList.get(i);
						// we're only interesed in method calling instructions
						if (abstractInsnNode instanceof MethodInsnNode) {
							MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
							String signature = methodInsnNode.owner + "#" + methodInsnNode.name + ":" + methodInsnNode.desc;
							if (!patterns[i].matcher(signature).matches()) {
								return false;
							}
						}
					}
				}

				return true;
			}
		});
	}

	/**
	 * Search for the specified sequence of opcodes where the specofoed <code>Condition</code> is met.
	 * 
	 * @param opcodes
	 *            The opcode sequence to find.
	 * @param condition
	 *            <code>Condition</code> for the encountered sequence.
	 * @return A <code>Collection</code> of <code>List</code>s containing the matched <code>AbstractInsnNode</code>s.
	 */
	public Collection search(int[] opcodes, Condition condition) {
		// Line up all the wanted instruction opcodes in a string
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < opcodes.length; i++) {
			buffer.append((char) opcodes[i]);
		}
		String sequence = buffer.toString();

		Collection result = new ArrayList();

		// find all sub sequences in the method's
		int start = 0;
		while (start < methodOpcodes.length()) {
			int index = methodOpcodes.indexOf(sequence, start);
			if (index == -1) {
				break;
			} else {
				int end = index + sequence.length();
				List subList = Arrays.asList(Arrays.copyOfRange(methodNode.instructions.toArray(), index, end));
				if (condition.include(subList)) {
					result.add(subList);
				}
				start = end;
			}
		}

		return result;
	}

	/**
	 * A condition evaluating if a <code>List</code> of <code>AbstractInsnNode</code>s is of interest.
	 * 
	 * @author Arvid Svensson
	 */
	public static interface Condition {
		/**
		 * Checks if the instructions are of interest.
		 * 
		 * @param abstractInsnNodeList
		 * @return <code>true</code> if interesting, <code>false</code> otherwise.
		 */
		boolean include(List abstractInsnNodeList);
	}
}