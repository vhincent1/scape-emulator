//package net.scapeemulator.cache.tools;
//
//import net.scapeemulator.cache.Container;
//import net.scapeemulator.cache.FileStore;
//import net.scapeemulator.cache.ReferenceTable;
//import net.scapeemulator.cache.ReferenceTable.Entry;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.zip.CRC32;
//
//public final class CacheVerifier {
//
//	public static void main(String[] args) throws IOException {
//		try (FileStore store = FileStore.open("../game/data/cache/")) {
//			for (int type = 0; type < store.getFileCount(255); type++) {
//				ReferenceTable table = ReferenceTable.decode(Container.decode(store.read(255, type)).getData());
//				for (int file = 0; file < table.capacity(); file++) {
//					Entry entry = table.getEntry(file);
//					if (entry == null)
//						continue;
//
//					ByteBuffer buffer;
//					try {
//						buffer = store.read(type, file);
//					} catch (IOException ex) {
//						System.out.println(type+":"+file+ " error");
//						continue;
//					}
//
//					if (buffer.capacity() <= 2) {
//						System.out.println(type+":"+file+ " missing");
//						continue;
//					}
//
//					byte[] bytes = new byte[buffer.limit() - 2]; // last two bytes are the version and shouldn't be included
//					buffer.position(0);
//					buffer.get(bytes, 0, bytes.length);
//
//					CRC32 crc = new CRC32();
//					crc.update(bytes, 0, bytes.length);
//
//					if ((int) crc.getValue() != entry.getCrc()) {
//						System.out.println(type+":"+file+ " corrupt");
//					}
//
//					buffer.position(buffer.limit() - 2);
//					if ((buffer.getShort() & 0xFFFF) != entry.getVersion()) {
//						System.out.println(type+":"+file+ " out of date");
//					}
//				}
//			}
//		}
//	}
//
//}
