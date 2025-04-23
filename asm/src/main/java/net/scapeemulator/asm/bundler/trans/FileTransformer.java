package net.scapeemulator.asm.bundler.trans;

import net.scapeemulator.asm.bundler.Resource;
import net.scapeemulator.asm.util.InsnMatcher;
import net.scapeemulator.asm.util.InsnNodeUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

public final class FileTransformer extends Transformer {

	private static final String RESOURCE_CONSTRUCTOR_PATTERN = "NEW DUP LDC LDC ICONST_4 ANEWARRAY (DUP ICONST LDC AASTORE)+ (ICONST | BIPUSH | SIPUSH | LDC) (ICONST | BIPUSH | SIPUSH | LDC) BIPUSH NEWARRAY (DUP (ICONST | BIPUSH) (ICONST | BIPUSH | SIPUSH | LDC) IASTORE)+ INVOKESPECIAL";

	private final Resource[] resources;

	public FileTransformer(Resource[] resources) {
		this.resources = resources;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void transform(ClassNode node) {
		for (MethodNode method : (List<MethodNode>) node.methods) {
			if (!method.name.equals("<clinit>"))
				continue;

			int resourceNum = 0;

			InsnMatcher matcher = new InsnMatcher(method.instructions);
			for (Iterator<AbstractInsnNode[]> it = matcher.match(RESOURCE_CONSTRUCTOR_PATTERN); it.hasNext();) {
				AbstractInsnNode[] match = it.next();
				if (match.length != 107)
					continue;

				Resource resource = resources[resourceNum++];
				method.instructions.set(match[22], InsnNodeUtils.createNumericPushInsn(resource.getUncompressedSize()));
				method.instructions.set(match[23], InsnNodeUtils.createNumericPushInsn(resource.getCompressedSize()));

				int[] checksum = resource.getChecksum();
				for (int i = 0; i < checksum.length; i++) {
					int j = 28 + i * 4;
					method.instructions.set(match[j], InsnNodeUtils.createNumericPushInsn(checksum[i]));
				}
			}
		}
	}

}
