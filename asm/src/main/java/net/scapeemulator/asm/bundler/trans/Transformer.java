package net.scapeemulator.asm.bundler.trans;

import org.objectweb.asm.tree.ClassNode;

public abstract class Transformer {

	public abstract void transform(ClassNode node);

}
