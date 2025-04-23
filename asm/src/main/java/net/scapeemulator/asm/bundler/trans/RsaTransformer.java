package net.scapeemulator.asm.bundler.trans;

import net.scapeemulator.asm.util.InsnMatcher;
import net.scapeemulator.util.crypto.RsaKeySet;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

public final class RsaTransformer extends Transformer {

	private static final String JAGEX_EXPONENT = "58778699976184461502525193738213253649000149147835990136706041084440742975821";
	private static final String JAGEX_MODULUS = "7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789";

	@SuppressWarnings("unchecked")
	@Override
	public void transform(ClassNode node) {
		for (MethodNode method : (List<MethodNode>) node.methods) {
			InsnMatcher matcher = new InsnMatcher(method.instructions);
			for (Iterator<AbstractInsnNode[]> it = matcher.match("LDC"); it.hasNext();) {
				AbstractInsnNode[] match = it.next();
				LdcInsnNode ldc = (LdcInsnNode) match[0];

				if (ldc.cst.equals(JAGEX_EXPONENT))
					method.instructions.set(ldc, new LdcInsnNode(RsaKeySet.PUBLIC_KEY));
				else if (ldc.cst.equals(JAGEX_MODULUS))
					method.instructions.set(ldc, new LdcInsnNode(RsaKeySet.MODULUS));
			}
		}
	}

}
