package net.scapeemulator.asm.bundler.trans;

import net.scapeemulator.asm.util.InsnMatcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;

public final class HostnameTransformer extends Transformer {

	@SuppressWarnings("unchecked")
	@Override
	public void transform(ClassNode node) {
		for (MethodNode method : (List<MethodNode>) node.methods) {
			InsnMatcher matcher = new InsnMatcher(method.instructions);
			for (Iterator<AbstractInsnNode[]> it = matcher.match("INVOKEVIRTUAL INVOKEVIRTUAL INVOKEVIRTUAL"); it.hasNext();) {
				AbstractInsnNode[] match = it.next();
				MethodInsnNode insn1 = (MethodInsnNode) match[0];
				MethodInsnNode insn2 = (MethodInsnNode) match[1];
				MethodInsnNode insn3 = (MethodInsnNode) match[2];

				if (!insn1.name.equals("getDocumentBase"))
					continue;

				if (!insn2.name.equals("getHost"))
					continue;

				if (!insn3.name.equals("toLowerCase"))
					continue;

				InsnNode iconst = new InsnNode(Opcodes.ICONST_1);
				InsnNode ireturn = new InsnNode(Opcodes.IRETURN);
				method.instructions.clear();
				method.tryCatchBlocks.clear();
				method.instructions.add(iconst);
				method.instructions.add(ireturn);
			}
		}
	}

}
