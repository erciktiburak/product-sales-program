import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * @author BURAK ERÇIKTI
 * @since 13.05.2020 
 */
public class productSales {
	public static void main(String[]args)
	{
		Store s1 = new Store("Migros","www.migros.com.tr");
		Store s2 = new Store("BIM","www.bim.com.tr");

		Customer c = new Customer ("CSE 102");

		Customer cc = new Customer("Club CSE 102");
//		s1.addCustomer(c); // compiler error because no phone number given.
		s1.addCustomer(cc, "05555555555555");


		Product p = new Product("Computer", 1000.00);
		FoodProduct fp = new FoodProduct("Snickers", 2,250,false,true,false);
		ClothingProduct cp = new ClothingProduct("Shoes",89,"44");
		System.out.println(cp);

		s1.addToInventory(p,20);
		s2.addToInventory(p,10);
		s2.addToInventory(fp,100);
		s1.addToInventory(cp,28);
	
		System.out.println(s1.getName() + " has "+s1.getCount()+" products");
		System.out.println(s1.getProductCount(p));

		System.out.println(s1.purchase(p,2));
		s1.addToInventory(p,3);
		System.out.println(s1.getProductCount(p));
		System.out.println(s2.getProductCount(p));

		//System.out.println(s1.getProductCount(fp)); // results in Exception
		//System.out.println(s2.purchase(fp,200)); // results in Exception

		c.addToCart(s1, p, 2);
		c.addToCart(s1, fp, 1); // NOTE: This does not stop the program because the Exception is caught
		c.addToCart(s1, cp, 1);
		System.out.println("Total due - " +c.getTotalDue(s1));
		System.out.println("\n\nReceipt:\n" + c.receipt(s1));
		//System.out.println("\n\nReceipt:\n" + c.receipt(s2)); //results in Exception

		//System.out.println("After paying: " + c.pay(s1,2000)); // results in Exception
		
		System.out.println("After paying: " + c.pay(s1,2100)); 

		//System.out.println("Total due - " +c.getTotalDue(s1)); // results in Exception
		//System.out.println("\n\nReceipt:\n" + c.receipt(s1)); //results in Exception
		cc.addToCart(s2, fp, 2);
		cc.addToCart(s2, fp, 1);
		System.out.println(cc.receipt(s2));

		cc.addToCart(s2, fp, 10);
		System.out.println(cc.receipt(s2));
		
		c.addToCart(s1, p, 2);
		c.addToCart(s1, fp, 1); // NOTE: This does not stop the program because the Exception is caught
		c.addToCart(s1, cp, 1);
		System.out.println("Total due - " +c.getTotalDue(s1));
		System.out.println("\n\nReceipt:\n" + c.receipt(s1));
		//System.out.println("\n\nReceipt:\n" + c.receipt(s2)); //results in Exception

		//System.out.println("After paying: " + c.pay(s1,2000)); // results in Exception
		
		System.out.println("After paying: " + c.pay(s1,4200)); 

	}
}

/**************Product Class **************/


	class Product {
	
	private String name;
	private double price;
		
	public Product(String name, double price) {
		this.setName(name);
		this.setPrice(price);				
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		if (price >= 0) {
			this.price = price;
		} else throw new InvalidPriceException( price );				
	}			
	@Override
	public String toString() {
		return  this.name + " @ " + this.price;
	}
		
}


/**************FoodProduct Class **************/


	class FoodProduct extends Product {
	
	private int calories;
	private boolean gluten;
	private boolean dairy;
	private boolean meat;
		
	public FoodProduct(String name,double price,int calories,
					   boolean gluten,boolean dairy,boolean meat) {
		super(name, price);
		this.calories = calories;
		this.gluten = gluten;
		this.dairy=dairy;
		this.meat=meat;
	}

	public int getCalories() {
		return calories;
	}
	public void setCalories(int calories) {
		if (calories<0) {
			throw new InvalidAmountException( calories );
		}
		else {
			this.calories = calories;
		}
	}			
	public boolean containsGluten() {
		return gluten;	
	}
	public boolean containsDairy() {
		return dairy;	
	}
	public boolean containsMeat() {
		return meat;	
	  }
	}


/**************ClothingProduct Class **************/


	class ClothingProduct extends Product{

	private String size;
	
		public ClothingProduct(String name,double price,String size) {
		super(name, price);
		this.size = size;
		}				
		public String getSize() {
		return size;
		}
		public void setSize(String size) {
		this.size = size;
		}		
	}
	

/**************Customer Class **************/


	class Customer {

	private String name;
	
	private ArrayList<Product> productList;
	private ArrayList<Integer> countList;
	private ArrayList<Store> storeList;
	private int addedItem = -1; // to count the products added on the card, like a loop. 	
	private double subTotal; // I transfer the total amount here.
	
	public Customer(String name) {
		
		this.name =name;
		
		productList = new ArrayList<>();
		countList = new ArrayList<>();
		storeList = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public void addToCart(Store store, Product product, int count) {

		try {
				store.purchase(product, count);

				if(productList.isEmpty())
				{
					if (storeList.contains(store)) {
						productList.add(product);
						countList.add(count);
						subTotal += product.getPrice()*count;
						addedItem ++;
					} 
					else
					{
						storeList.add(store);
						productList.add(product);
						countList.add(count);
						subTotal += product.getPrice()*count;
						addedItem ++;
					}
				}
				else
				{
						if(productList.contains(product))
						{
							countList.set(addedItem, countList.get(addedItem) + count);
							subTotal += product.getPrice()*count;

						}
						else if(productList.get(addedItem)!= product)
						{
							productList.add(product);
							countList.add(count);
							subTotal += product.getPrice()*count;
							addedItem ++;
						}

					}									
			}
			catch (ProductNotFoundException ex) {
	            System.out.println(ex.getMessage()); 
			}
	}              
	
	public String receipt(Store store) {
		String text = "" ;	
		String header = "Customer receipt for " + store.getName();
		String result = "" ;

		
			if (!storeList.contains(store)) {
	    		throw new StoreNotFoundException(store.getName()); 
			}
			else {
				  Iterator<Product> iterator = productList.iterator(); 				
				for (int n = 0; n < productList.size(); n++) {							
				text += iterator.next() + " X " + countList.get(n) 
				+ " ... " + productList.get(n).getPrice() * countList.get(n) + "\n" ;
				 }
			}
				text += " \n--------------------------------\n ";
				text += " \nTotal Due - " + getTotalDue(store) + "\n";
				result = header + "\n\n" + text;
			
		return result;

	}

	public double getTotalDue(Store store) {
		
		if (storeList.contains(store)) {
			return subTotal;
		}
		else {
    		throw new StoreNotFoundException(store.getName()); 
		}		
	}
	
	
	public double pay(Store store, double amount) {
	
		double due = 0.0;		
		if (!storeList.contains(store)) {
    		throw new StoreNotFoundException( store.getName() ); 
			}		
		else if (amount<getTotalDue(store)) {
			throw new NotEnoughPaymentException( amount, getTotalDue(store) );
			}							
		else {
			due = amount - getTotalDue(store);
			System.out.println("Thank you for your business");	
			productList.clear();
			countList.clear();
			storeList.clear();
			addedItem = -1;
		}
			return due;	
			}	
		public String toString() {
			return getName();
		}
	}	
	
	
	



/**************Store Class **************/


	class Store {
	private String name;
	private String website;
	private int count;
	private Map<Product , Integer> mapProduct;
	private Map<String , Customer> mapPhone;
	
	public Store(String name, String website) {
		this.name = name;
		this.website = website;
		
		mapProduct = new HashMap<>();
		mapPhone = new HashMap<>();

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}

	public int getCount() {
		return mapProduct.size();
	}
	
	public void addCustomer(Customer customer , String phone) {
		mapPhone.put(phone, customer);
	}

	
	public int getProductCount(Product product) {

		for (Map.Entry<Product, Integer> entrySet : mapProduct.entrySet()) {			
    	    if (entrySet.getKey().equals(product)) {
    	    	  return entrySet.getValue();
    	    	}
    	    }
		throw new ProductNotFoundException(product.getName());
        }
  
	
	public Customer getCustomer(String phone)  {
		if(!mapPhone.containsKey(phone))
		{
		throw new CustomerNotFoundException(phone);
		}
		else
		{
			return mapPhone.get(phone);
		}

    }	 
	
	public void removeProduct(Product product) {

		if (!mapProduct.containsKey(product)) 
		{
		throw new ProductNotFoundException(name);	
		}
		else
		{
		  mapProduct.remove(product); 
		}
 	
    } 		
	
	

	public void addToInventory(Product product,int amount) {
		if (amount<0) {
			throw new InvalidAmountException(amount);
		}
		if (mapProduct.containsKey(product)) {
			mapProduct.put(product, mapProduct.get(product) + amount);
		}
		else if (!mapProduct.containsKey(product)) {
    		mapProduct.put(product, amount);
		}
		else {
    		throw new ProductNotFoundException(product.getName()); 
		}		
	}	
	
	
	public double purchase(Product product,int amount) {
		if (!mapProduct.containsKey(product)) {
			this.count = amount;
			throw new ProductNotFoundException(product.getName());
		}
		else if (amount < 0 || amount > getProductCount(product)) {
			throw new InvalidPurchaseException(amount, mapProduct.get(product));
		} else {
			mapProduct.put(product, mapProduct.get(product) - amount);
			return (amount * product.getPrice());
		}
	}
	@Override
	public String toString() {
		return getName() + getCount();
	}
	
}

/**************Custom Exceptions **************/
	
	class InvalidPriceException extends RuntimeException {
		
		public InvalidPriceException(double price) {
			super( " InvalidPriceException: " + price );
		}
		
	}

	class InvalidAmountException extends RuntimeException {	
		
		public InvalidAmountException(int amount) {
			super( amount + " (You must input positive number.) " );
		}
		
	}

	class InvalidPurchaseException extends RuntimeException {
		
		public InvalidPurchaseException(int amount, int remaining) {
			super( amount + " requested, " +remaining + " remaining " );
		}
		
	}

	class NotEnoughPaymentException extends RuntimeException {
		
		public NotEnoughPaymentException(double amount, double due) {
			super( due + " due, but only " + amount + " given " );
		}
		
	}

	class ProductNotFoundException extends RuntimeException {
		
		public ProductNotFoundException(String name) {
			super( "ERROR: ProductNotFoundException: "  + name );
		}
		
	}

	class CustomerNotFoundException extends RuntimeException {	
		
		public CustomerNotFoundException(String phone) {
			super( "CustomerNotFoundException : " + phone );
		}
		
	}
	
	class StoreNotFoundException extends RuntimeException {		
		
		public StoreNotFoundException(String name) {
			super( name );
		}		
	}