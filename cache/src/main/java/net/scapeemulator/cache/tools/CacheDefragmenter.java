//package net.scapeemulator.cache.tools;
//
//import net.scapeemulator.cache.Container;
//import net.scapeemulator.cache.FileStore;
//import net.scapeemulator.cache.ReferenceTable;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
//public final class CacheDefragmenter {
//
//	public static void main(String[] args) throws IOException {
//		try (FileStore in = FileStore.open("../game/data/cache/")) {
//			try (FileStore out = FileStore.create("/tmp/defragmented-cache", in.getTypeCount())) {
//				for (int type = 0; type < in.getTypeCount(); type++) {
//					ByteBuffer buf = in.read(255, type);
//					buf.mark();
//					out.write(255, type, buf);
//					buf.reset();
//
//					ReferenceTable rt = ReferenceTable.decode(Container.decode(buf).getData());
//					for (int file = 0; file < rt.capacity(); file++) {
//						if (rt.getEntry(file) == null)
//							continue;
//
//						out.write(type, file, in.read(type, file));
//					}
//				}
//			}
//		}
//	}
//
//}
