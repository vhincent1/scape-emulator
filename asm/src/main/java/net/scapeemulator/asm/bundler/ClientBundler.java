package net.scapeemulator.asm.bundler;

import net.scapeemulator.asm.bundler.trans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

public final class ClientBundler {

	private static final Logger logger = LoggerFactory.getLogger(ClientBundler.class);

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		ClientBundler bundler = new ClientBundler();
		bundler.bundle();
	}

	public void bundle() throws IOException, NoSuchAlgorithmException {
		/* unpack source files */
		logger.info("Starting client bundler...");
		Application client = Application.unpackJar(new File("data/runescape.jar"));
		Application glClient = Application.unpack200(new File("data/runescape_gl.pack200"));
		Application loader = Application.unpackJar(new File("data/loader.jar"));
		Application glLoader = Application.unpackJar(new File("data/loader_gl.jar"));
		Application jogl = Application.unpackJar(new File("data/jogl.jar"));

		/* update client RSA keys */
		logger.info("Transforming RSA keys...");
		Transformer transformer = new RsaTransformer();
		client.transform(transformer);
		glClient.transform(transformer);

		/* remove hostname protection */
		logger.info("Transforming hostname protection...");
		transformer = new HostnameTransformer();
		client.transform(transformer);
		glClient.transform(transformer);

		/* write everything but the loaders back out */
		logger.info("Bundling clients and libraries...");
		File clientOut = new File("../game/data/www/runescape.pack200");
		File glClientOut = new File("../game/data/www/runescape_gl.pack200");
		File joglOut = new File("../game/data/www/jogl.pack200");
		client.packJar(new File("../game/data/www/runescape.jar")); // unsigned client output
		client.pack200(clientOut);
		glClient.pack200(glClientOut);
		jogl.pack200(joglOut);

		/* compute SHA1 checksums */
		logger.info("Computing SHA1 checksums and file sizes...");
		Resource[] resources = new Resource[] {
			Resource.create(new File("data/game_unpacker.dat")),
			Resource.create(clientOut),
			Resource.NULL
		};
		Resource[] glResources = new Resource[] {
			Resource.create(new File("data/game_unpacker.dat")),
			Resource.create(glClientOut),
			Resource.NULL,
			Resource.create(joglOut),
			Resource.NULL,
			Resource.create(new File("data/windows-x86/jogl.dll")),
			Resource.create(new File("data/windows-x86/jogl_awt.dll")),
			Resource.create(new File("data/windows-amd64/jogl.dll")),
			Resource.create(new File("data/windows-amd64/jogl_awt.dll")),
			Resource.create(new File("data/linux-x86/libjogl.so")),
			Resource.create(new File("data/linux-x86/libjogl_awt.so")),
			Resource.create(new File("data/linux-x86/libgluegen-rt.so")),
			Resource.create(new File("data/linux-amd64/libjogl.so")),
			Resource.create(new File("data/linux-amd64/libjogl_awt.so")),
			Resource.create(new File("data/linux-amd64/libgluegen-rt.so")),
			Resource.create(new File("data/mac-ppc/libjogl.jnilib")),
			Resource.create(new File("data/mac-ppc/libjogl_awt.jnilib")),
			Resource.create(new File("data/mac-universal/libjogl.jnilib")),
			Resource.create(new File("data/mac-universal/libjogl_awt.jnilib")),
			Resource.create(new File("data/solaris-amd64/libjogl.so")),
			Resource.create(new File("data/solaris-amd64/libjogl_awt.so")),
			Resource.create(new File("data/solaris-amd64/libgluegen-rt.so")),
			Resource.create(new File("data/solaris-sparcv9/libjogl.so")),
			Resource.create(new File("data/solaris-sparcv9/libjogl_awt.so")),
			Resource.create(new File("data/solaris-sparcv9/libgluegen-rt.so")),
			Resource.create(new File("data/solaris-sparc/libjogl.so")),
			Resource.create(new File("data/solaris-sparc/libjogl_awt.so")),
			Resource.create(new File("data/solaris-sparc/libgluegen-rt.so")),
			Resource.create(new File("data/solaris-x86/libjogl.so")),
			Resource.create(new File("data/solaris-x86/libjogl_awt.so")),
			Resource.create(new File("data/solaris-x86/libgluegen-rt.so"))
		};

		/* update SHA1 checksums in the loader */
		logger.info("Transforming SHA1 checksums and file sizes...");
		transformer = new FileTransformer(resources);
		loader.transform(transformer);
		transformer = new FileTransformer(glResources);
		glLoader.transform(transformer);

		/* transform cache path */
		logger.info("Transforming cache path...");
		transformer = new CachePathTransformer();
		loader.transform(transformer);
		glLoader.transform(transformer);

		/* write the loaders back out */
		logger.info("Bundling loaders...");
		loader.packJar(new File("../game/data/www/loader.jar"));
		glLoader.packJar(new File("../game/data/www/loader_gl.jar"));

		/* copy natives/unpacker */
		logger.info("Copying native libraries and game unpacker...");
		copy("data/game_unpacker.dat", "../game/data/www/unpackclass.pack");
		copy("data/windows-x86/jogl.dll", "../game/data/www/jogl_0_0.lib");
		copy("data/windows-x86/jogl_awt.dll", "../game/data/www/jogl_0_1.lib");
		copy("data/windows-amd64/jogl.dll", "../game/data/www/jogl_1_0.lib");
		copy("data/windows-amd64/jogl_awt.dll", "../game/data/www/jogl_1_1.lib");
		copy("data/linux-x86/libjogl.so", "../game/data/www/jogl_2_0.lib");
		copy("data/linux-x86/libjogl_awt.so", "../game/data/www/jogl_2_1.lib");
		copy("data/linux-x86/libgluegen-rt.so", "../game/data/www/jogl_2_2.lib");
		copy("data/linux-amd64/libjogl.so", "../game/data/www/jogl_3_0.lib");
		copy("data/linux-amd64/libjogl_awt.so", "../game/data/www/jogl_3_1.lib");
		copy("data/linux-amd64/libgluegen-rt.so", "../game/data/www/jogl_3_2.lib");
		copy("data/mac-ppc/libjogl.jnilib", "../game/data/www/jogl_4_0.lib");
		copy("data/mac-ppc/libjogl_awt.jnilib", "../game/data/www/jogl_4_1.lib");
		copy("data/mac-universal/libjogl.jnilib", "../game/data/www/jogl_5_0.lib");
		copy("data/mac-universal/libjogl_awt.jnilib", "../game/data/www/jogl_5_1.lib");
		copy("data/solaris-amd64/libjogl.so", "../game/data/www/jogl_6_0.lib");
		copy("data/solaris-amd64/libjogl_awt.so", "../game/data/www/jogl_6_1.lib");
		copy("data/solaris-amd64/libgluegen-rt.so", "../game/data/www/jogl_6_2.lib");
		copy("data/solaris-sparcv9/libjogl.so", "../game/data/www/jogl_7_0.lib");
		copy("data/solaris-sparcv9/libjogl_awt.so", "../game/data/www/jogl_7_1.lib");
		copy("data/solaris-sparcv9/libgluegen-rt.so", "../game/data/www/jogl_7_2.lib");
		copy("data/solaris-sparc/libjogl.so", "../game/data/www/jogl_8_0.lib");
		copy("data/solaris-sparc/libjogl_awt.so", "../game/data/www/jogl_8_1.lib");
		copy("data/solaris-sparc/libgluegen-rt.so", "../game/data/www/jogl_8_2.lib");
		copy("data/solaris-x86/libjogl.so", "../game/data/www/jogl_9_0.lib");
		copy("data/solaris-x86/libjogl_awt.so", "../game/data/www/jogl_9_1.lib");
		copy("data/solaris-x86/libgluegen-rt.so", "../game/data/www/jogl_9_2.lib");

		/* sign loaders */
		logger.info("Signing loaders...");
		Runtime.getRuntime().exec("jarsigner -keystore ../util/data/scapeemu.keystore -storepass scapeemu ../game/data/www/loader.jar scapeemu");
		Runtime.getRuntime().exec("jarsigner -keystore ../util/data/scapeemu.keystore -storepass scapeemu ../game/data/www/loader_gl.jar scapeemu");

		/* all done! */
		logger.info("Client bundler completed successfully.");
	}

	private void copy(String srcPath, String destPath) throws IOException {
		Path src = new File(srcPath).toPath();
		Path dest = new File(destPath).toPath();
		Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
	}

}
