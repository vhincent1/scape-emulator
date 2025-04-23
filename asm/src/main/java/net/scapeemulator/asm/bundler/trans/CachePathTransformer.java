package net.scapeemulator.asm.bundler.trans;

import net.scapeemulator.asm.util.InsnMatcher;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

public final class CachePathTransformer extends Transformer {

	@SuppressWarnings("unchecked")
	@Override
	public void transform(ClassNode node) {
		for (MethodNode method : (List<MethodNode>) node.methods) {
			InsnMatcher matcher = new InsnMatcher(method.instructions);
			for (Iterator<AbstractInsnNode[]> it = matcher.match("ICONST ANEWARRAY (DUP ICONST NEW DUP INVOKESPECIAL LDC INVOKEVIRTUAL ILOAD INVOKEVIRTUAL INVOKEVIRTUAL AASTORE)+ ASTORE"); it.hasNext();) {
				AbstractInsnNode[] match = it.next();
				if (match.length == 25) {
					method.instructions.set(match[7], new LdcInsnNode(".scapeemu_cache_"));
					method.instructions.set(match[18], new LdcInsnNode(".scapeemu_cache_"));
				}
			}
		}
	}

}
