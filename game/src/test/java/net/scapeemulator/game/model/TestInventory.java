//package net.scapeemulator.game.model;
//
//import net.scapeemulator.cache.Cache;
//import net.scapeemulator.cache.FileStore;
//import net.scapeemulator.game.model.Inventory.StackMode;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import static org.junit.Assert.*;
//
//public final class TestInventory {
//
//    @BeforeClass
//    public static void setUpBeforeClass() throws IOException {
//        try {
//            ItemDefinitions.init(new Cache(FileStore.open("data/cache")));
//        } catch (FileNotFoundException ex) {
//            ItemDefinitions.init(new Cache(FileStore.open("game/data/cache")));
//        }
//    }
//
//    @Test
//    public void testAlwaysStacking() {
//        Inventory inventory = new Inventory(5, StackMode.ALWAYS);
//        inventory.add(new Item(526, 100)); /* bones are non-stackable normally */
//        inventory.add(new Item(526, 50));
//
//        assertEquals(4, inventory.freeSlots());
//        assertEquals(new Item(526, 150), inventory.get(0));
//        for (int i = 1; i < 5; i++)
//            assertNull(inventory.get(i));
//    }
//
//    @Test
//    public void testToArray() {
//        Inventory inventory = new Inventory(5);
//        inventory.add(new Item(995, 1000000));
//        inventory.add(new Item(4151));
//
//        Item[] items = inventory.toArray();
//        assertArrayEquals(new Item[]{
//                new Item(995, 1000000), new Item(4151), null, null, null
//        }, items);
//
//        /* check mutating array does not change Inventory */
//        items[0] = null;
//        assertNotNull(inventory.get(0));
//    }
//
//    @Test
//    public void testFreeSlots() {
//        /* start with an empty inventory, all slots free */
//        Inventory inventory = new Inventory(5);
//        assertEquals(5, inventory.freeSlots());
//
//        /* add three bones */
//        for (int i = 0; i < 3; i++)
//            inventory.add(new Item(526));
//        assertEquals(2, inventory.freeSlots());
//
//        /* add two more bones */
//        for (int i = 0; i < 2; i++)
//            inventory.add(new Item(526));
//        assertEquals(0, inventory.freeSlots());
//
//        /* remove four bones */
//        inventory.remove(new Item(526, 4));
//        assertEquals(4, inventory.freeSlots());
//
//        /* remove the last bone */
//        inventory.remove(new Item(526));
//        assertEquals(5, inventory.freeSlots());
//    }
//
//    @Test
//    public void testShift() {
//        Inventory inventory = new Inventory(10);
//        inventory.set(0, new Item(995, 100));
//        inventory.set(3, new Item(4151));
//        inventory.set(4, new Item(526));
//        inventory.set(7, new Item(1171));
//
//        inventory.shift();
//
//        assertEquals(new Item(995, 100), inventory.get(0));
//        assertEquals(new Item(4151), inventory.get(1));
//        assertEquals(new Item(526), inventory.get(2));
//        assertEquals(new Item(1171), inventory.get(3));
//        for (int i = 4; i < 10; i++)
//            assertNull(inventory.get(i));
//    }
//
//    @Test
//    public void testEmpty() {
//        /* start with an empty inventory */
//        Inventory inventory = new Inventory(10);
//        assertTrue(inventory.isEmpty());
//
//        /* add some items */
//        inventory.add(new Item(995, 100));
//        inventory.add(new Item(4151));
//        assertFalse(inventory.isEmpty());
//
//        /* empty it again*/
//        inventory.empty();
//        assertTrue(inventory.isEmpty());
//    }
//
//    @Test
//    public void testSwap() {
//        Inventory inventory = new Inventory(5);
//        inventory.add(new Item(1171));
//        inventory.add(new Item(1277));
//
//        inventory.swap(0, 1);
//
//        assertEquals(new Item(1277), inventory.get(0));
//        assertEquals(new Item(1171), inventory.get(1));
//    }
//
//}
