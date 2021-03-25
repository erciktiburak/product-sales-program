import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    static Store boyner;
    static Store a101;

    static Customer taha;
    static Customer alperen;

    static ClothingProduct shirt;
    static Product zenbook13;
    static Product logitechMouse;
    static FoodProduct cheese;
    static FoodProduct yoghurt;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        boyner = new Store("Boyner", "www.boyner.com.tr");
        a101 = new Store("A101", "www.a101.com.tr");

        taha = new Customer("Taha Yigit ALKAN");
        alperen = new Customer("Alperen AKSOY");

        shirt = new ClothingProduct("Shirt (blue)", 99.90, "S");
        zenbook13 = new Product("ASUS zenbook 13\" i7 GeForce MX250 2GB", 10499.90);
        logitechMouse = new Product("Logitech Wireless Mouse (Black)", 89.49);

        cheese = new FoodProduct("Tahsildaroglu Beyaz Peynir", 22, 180, false, true, false);
        yoghurt = new FoodProduct("Yorukoglu Suzme Yogurt ", 10.50, 150, false, true, false);
    }

    @Test
    public void testA_AddToInventory() throws Exception {
        boyner.addToInventory(shirt, 100);
        assertEquals(100, boyner.getProductCount(shirt));

        boyner.addToInventory(zenbook13, 9);
        assertEquals(9, boyner.getProductCount(zenbook13));

        boyner.addToInventory(logitechMouse, 50);
        assertEquals(50, boyner.getProductCount(logitechMouse));

        boyner.addToInventory(shirt, 50);
        assertEquals(150, boyner.getProductCount(shirt));

        assertEquals(3, boyner.getCount());

        a101.addToInventory(cheese, 500);
        assertEquals(500, a101.getProductCount(cheese));

        a101.addToInventory(yoghurt, 100);
        assertEquals(100, a101.getProductCount(yoghurt));

        a101.addToInventory(yoghurt, 25);
        assertEquals(125, a101.getProductCount(yoghurt));

    }

    @Test(expected = ProductNotFoundException.class)
    public void testB_RemoveProductException() throws ProductNotFoundException {
        Product p = new Product("Iphone5", 500);
        boyner.removeProduct(p);
    }

    @Test
    public void testC_RemoveProduct() throws Exception {
        boyner.removeProduct(logitechMouse);
        assertEquals(2, boyner.getCount());
    }

    @Test
    public void testD_CustomerCheck() throws Exception {
        boyner.addCustomer(taha, "05555555555");
        boyner.addCustomer(alperen, "055588888888");

        assertEquals(taha, boyner.getCustomer("05555555555"));
        try {
            Customer c = boyner.getCustomer("02222222222");
            // this line should not be reached
            assertTrue("getCustomer method should throw CustomerNotFoundException", false);
        } catch (Exception e) {
            assertTrue(e.getClass().getName().contains("CustomerNotFoundException"));
        }
    }

    @Test
    public void testE_Purchase() throws Exception {
        // Successfull purchase
        boyner.purchase(shirt, 10);
        assertEquals(140, boyner.getProductCount(shirt));

        // Invalid Purchase
        try {
            a101.purchase(yoghurt, 1000);
            assertTrue("purchase method should throw InvalidPurchaseException", false);
        } catch (Exception e) {
            assertTrue(e.getClass().getName().contains("InvalidPurchaseException"));
        }

        assertEquals(125, a101.getProductCount(yoghurt));
    }

    @Test
    public void testF_addToCartWrong() throws Exception {
        alperen.addToCart(a101, shirt, 1);
        try {
            // total due may be 0 or this statement may throw an exception
            double totalDue = alperen.getTotalDue(a101);

            assertEquals(0, totalDue, 0.9);
        } catch (Exception e) {
            assertTrue(e.getClass().getName().contains("StoreNotFoundException"));
        }
    }

    @Test
    public void testG_addToCart() throws Exception {
        taha.addToCart(boyner, shirt, 9);
        taha.addToCart(boyner, zenbook13, 1);
        taha.addToCart(boyner, shirt, 1);

        assertEquals(130, boyner.getProductCount(shirt));
        assertEquals(11498.9, taha.getTotalDue(boyner), 0.9);

        taha.addToCart(a101, cheese, 5);
        taha.addToCart(a101, yoghurt, 200);

        assertEquals(495, a101.getProductCount(cheese));
        assertEquals(110, taha.getTotalDue(a101), 0.5);
    }

    @Test
    public void testH_Receipt() throws Exception {
        String boynerReceipt = taha.receipt(boyner).toLowerCase();

        assertTrue("receipt must contains total", boynerReceipt.contains("total"));
        assertTrue("receipt must contains shirt (blue)", boynerReceipt.contains(shirt.getName().toLowerCase()));
        assertTrue("receipt must contains 999", boynerReceipt.contains("999"));
        assertTrue("receipt must contains zenbook 13", boynerReceipt.contains(zenbook13.getName().toLowerCase()));
        assertTrue("receipt must contains 10499.9", boynerReceipt.contains("10499.9"));
        assertTrue("receipt must contains 11498.9", boynerReceipt.contains("11498.9"));
    }

    @Test
    public void testI_PayException() {
        try {
            alperen.pay(boyner, 15);
        } catch (Exception e) {
            assertTrue(e.getClass().getName().contains("StoreNotFoundException"));
        }

        try {
            taha.pay(boyner, 5);
        } catch (Exception e) {
            assertTrue(e.getClass().getName().contains("NotEnoughPaymentException"));
        }
    }

    @Test
    public void testJ_PaySuccess() throws Exception {
        double change = taha.pay(boyner, 12000);
        assertEquals(501.1, change, 0.1);
    }

}