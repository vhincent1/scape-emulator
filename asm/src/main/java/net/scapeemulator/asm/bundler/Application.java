package net.scapeemulator.asm.bundler;

import net.scapeemulator.asm.bundler.trans.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private final Map<String, ClassNode> classes = new HashMap<>();

	public static Application merge(Application... applications) {
		Application merged = new Application();
		for (Application application : applications)
			for (Map.Entry<String, ClassNode> entry : application.classes.entrySet())
				merged.classes.put(entry.getKey(), entry.getValue());
		return merged;
	}

	public static Application unpack200(File file) throws IOException {
		logger.info("Unpacking pack200 file " + file + "...");
		/* read the .pack200 file into memory and tack on the GZIP header */
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out.write(31);
			out.write(-117);

			try (InputStream is = new FileInputStream(file)) {
				for (;;) {
					byte[] buf = new byte[4096];
					int len = is.read(buf);
					if (len == -1)
						break;

					out.write(buf, 0, len);
				}
			}
		} finally {
			out.close();
		}

		/* convert the pack200 file to a temporary jar file */
		File tmp = File.createTempFile("tmp", ".jar");
		JarOutputStream os = new JarOutputStream(new FileOutputStream(tmp));
		try {
			InputStream in = new GZIPInputStream(new ByteArrayInputStream(out.toByteArray()));
			try {
				Pack200.Unpacker unpacker = Pack200.newUnpacker();
				unpacker.unpack(in, os);
			} finally {
				in.close();
			}
		} finally {
			os.close();
		}

		/* unpack the temporary jar file */
		return unpackJar(tmp);
	}

	public static Application unpackJar(File file) throws IOException {
		logger.info("Unpacking jar file " + file + "...");

		Application application = new Application();
		JarFile jar = new JarFile(file);
		try {
			for (Enumeration<JarEntry> it = jar.entries(); it.hasMoreElements();) {
				JarEntry entry = it.nextElement();
				if (!entry.getName().endsWith(".class"))
					continue;

				InputStream is = jar.getInputStream(entry);
				try {
					ClassReader reader = new ClassReader(is);
					ClassNode node = new ClassNode();
					reader.accept(node, 0);
					application.classes.put(entry.getName(), node);
				} finally {
					is.close();
				}
			}
		} finally {
			jar.close();
		}

		return application;
	}

	public void pack200(File file) throws IOException {
		File tmp = File.createTempFile("tmp", ".jar");
		packJar(tmp);

		logger.info("Packing pack200 file " + file + "..");

		JarFile jar = new JarFile(tmp);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				OutputStream os = new GZIPOutputStream(out);
				try {
					Pack200.Packer packer = Pack200.newPacker();
					packer.pack(jar, os);
				} finally {
					os.close();
				}
			} finally {
				out.close();
			}

			byte[] bytes = out.toByteArray();
			byte[] strippedBytes = new byte[bytes.length - 2];
			System.arraycopy(bytes, 2, strippedBytes, 0, strippedBytes.length);

			OutputStream os = new FileOutputStream(file);
			try {
				os.write(strippedBytes);
			} finally {
				os.close();
			}
		} finally {
			jar.close();
		}
	}

	public void packJar(File file) throws IOException {
		logger.info("Packing jar file " + file + "...");

		JarOutputStream os = new JarOutputStream(new FileOutputStream(file));
		try {
			for (Map.Entry<String, ClassNode> entry : classes.entrySet()) {
				os.putNextEntry(new JarEntry(entry.getKey()));

				ClassWriter writer = new ClassWriter(0);
				entry.getValue().accept(writer);
				os.write(writer.toByteArray());
			}
		} finally {
			os.close();
		}
	}

	public void transform(Transformer transformer) {
		for (ClassNode node : classes.values())
			transformer.transform(node);
	}

}
