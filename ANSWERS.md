Checkpoint 1 — Answers:

1. Which class contains new StandardPricingService()? Where would you switch to promotional pricing?

ManualWiring contains new StandardPricingService() inside its main method. To switch to promotional pricing, I would replace it with new PromotionalPricingService().

2. Why doesn't ProductService need to change when switching pricing strategies?

Because ProductService depends on the PricingService interface, not on a specific implementation. Therefore, I can pass any class that implements this interface without changing ProductService.

3. Who owns all the new calls, and what is the problem?

The developer owns all the object creation using new. This creates extra work because the developer must manually create objects and manage their dependencies.

4. What happens if we change a Product's SKU after inserting it into a HashMap?

The object remains stored in the bucket calculated using its old SKU hash code. If we search using the new SKU, the HashMap calculates a different bucket, so it might not find the object.

-------------------------------------------------------------------------

Checkpoint 2 — Answers:

1. Which classes appear? AppConfig, InMemoryProductRepository, ProductService, PromotionalPricingService, StandardPricingService. Domain classes such as Product and Category aren't Spring beans.

2. Removing @Primary: Spring reports ambiguity between PromotionalPricingService and StandardPricingService.

3. Scanning vs creating: @ComponentScan discovers classes and registers bean definitions. It doesn't create the objects at that moment. Spring creates singleton beans during context initialization.

--------------------------------------------------------------------------

Stage 3 — Checkpoint Answers:

1. ShippingQuoteService asks for two ShippingCalculators. Show which rule gives each one its bean (@Primaryvs @Qualifier)?

- @Primary: Defines the default bean Spring should inject when multiple candidates exist.
- @Qualifier: Explicitly selects a specific bean by its name.

2. What would happen if you added a third calculator and forgot to mark it? 

- Spring throws NoUniqueBeanDefinitionException because it cannot decide which implementation to inject.

3. Write the one-line test: quote(Governorate.CAIRO, 5000.00) = 0.00 but quote(Governorate.MATROUH,
999999.00) = 120.00 ?

- Cairo gets free shipping because its zone is eligible and the subtotal reaches the 5000 threshold.

- Matrouh doesn't get free shipping because its zone (REMOTE) is not eligible, regardless of the subtotal.

4. Add a PushNotificationSender that prints [PUSH] to .... Which existing files did you have to edit? (The answer should be none.) this is Open/Closed Principle .

- Spring automatically injects all beans implementing NotificationSender into List<NotificationSender>.

- Adding PushNotificationSender doesn't require modifying NotificationService because Spring discovers and injects the new implementation automatically.


-----------------------------------------------------------------

Stage 4 — Checkpoint Answers:


1-Why does getBean(ProductService.class) == getBean(ProductService.class) return true, 
while the same comparison for ShoppingCart returns false?

-Because:
- ProductService is a singleton by default. Spring creates one object and returns the same reference every time.
- ShoppingCart is prototype-scoped. Spring creates a new object every time we request it.


2-The repository has a print statement inside @PostConstruct. Does it print once or every time we call getBean()? Why?

-Because InMemoryProductRepository is a singleton bean.
-Spring creates the repository once, initializes it, and calls its @PostConstruct method once.
-Even if multiple services request the repository, they receive the same object.
-Similarly, @PreDestroy runs when the application context closes.


3-What goes wrong if a singleton service receives ShoppingCart through its constructor? 
Who should request each new cart?

-Although ShoppingCart is prototype-scoped, Spring creates CartService only once.
-When Spring creates CartService, it requests one ShoppingCart and injects it.
-The singleton keeps that same cart reference.
-Therefore, all customers using this CartService would share the same cart, which is incorrect.

Who requests each new cart?

-The component responsible for handling a new customer's cart must request a new instance from Spring, for example:
-ShoppingCart cart = context.getBean(ShoppingCart.class);
-Each request produces a new prototype object.


4-Why do we use lines.getOrDefault(sku, 0) instead of lines.get(sku)?
What happens if the SKU isn't already in the cart?

-getOrDefault(sku, 0) returns 0 if the SKU doesn't exist in the map, instead of returning null like get(sku).
-Without it, adding a quantity to null causes a NullPointerException.


5-Why did we choose LinkedHashMap instead of HashMap?

Both store key-value pairs, but they differ in iteration order.
- HashMap: doesn't guarantee insertion order.
-With LinkedHashMap, displaying the cart preserves that order.
-This is useful for a shopping cart because customers expect products to appear in the order they added them.


-----------------------------------------------------------------

Stage 6 — Checkpoint Answers:

1. Print context.getBeanDefinitionNames() again. Find your five controllers, CartRegistry and the three beans of WebConfig. Is StoreConsole in the list? Why not?

-The five controllers, CartRegistry, and the three WebConfig beans appear in the Spring context. StoreConsole does not appear because it is created manually in Main using new StoreConsole(...), not managed as a Spring bean.

2. A controller is a singleton and a cart is a prototype. Show the line where a new cart is requested for each customer, and who asks the container for it.

-CartRegistry.open() requests a new cart using cartProvider.getObject(). The ObjectProvider<ShoppingCart> asks the Spring container for a fresh prototype instance.

3. Create a cart in the console, then open /carts in the browser. Why does the browser see it? What would change if StoreConsole created its own new CartRegistry()?

-The browser sees the console's cart because both use the same singleton CartRegistry from the same Spring context. If StoreConsole created its own new CartRegistry(), it would have a separate collection of carts.

4. Why does POST /carts return redirect:/carts/N and not the view name carts/view? What does F5 do in each case?

-POST /carts uses redirect:/carts/N to follow the Post/Redirect/Get pattern and prevent accidental form resubmission. Refreshing after a redirect repeats the GET request; returning carts/view directly could resubmit the POST when F5 is pressed.

5. Two tabs press "Place order" on the same cart at the same moment. Which keyword decides who wins, and what does the loser see on screen?

-The synchronized keyword in CheckoutService.placeOrder() allows only one thread to execute checkout at a time. The second request sees an error because the cart has already been checked out or is empty.

6. Remove @Controller from CartController and restart. What do you see in the browser, and why?

-Removing @Controller makes Spring stop registering CartController as a controller bean. Its routes are no longer mapped, so opening its URLs results in a 404 Not Found response.


---------------------------------------------------------------------------------------

Principles:

1. Encapsulation: Product and ShoppingCart use private fields to protect their data, and ShoppingCart controls access to its lines and quantity changes.

2. Abstraction and interfaces: Repository, PricingService, ShippingCalculator, and NotificationSender allow their callers to depend on interfaces rather than concrete implementations.

3. Polymorphism: CheckoutService uses a list of NotificationSender implementations, while StoreException.status() provides different status codes through polymorphism.

4. Inheritance: AbstractNotificationSender contains shared notification behavior, and the StoreException subclasses inherit common exception behavior.

5. Composition over inheritance: CheckoutService combines its five collaborators through constructor injection instead of extending another class.

6. SOLID: The design follows SOLID through focused classes such as TaxCalculator, interchangeable shipping implementations, small interfaces, and constructor injection.

7. Generics: Repository<T, ID> provides a reusable generic repository contract, and Check.found provides a generic helper for retrieving values from Optional.

8. Collections: CartRegistry, ShoppingCart, ProductService, and the notification classes use collections such as ConcurrentHashMap, LinkedHashMap, EnumMap, EnumSet, and CopyOnWriteArrayList according to their needs.

9. Streams and lambdas: ProductService, ShoppingCart, and OrderRegistry use streams and lambdas for operations such as searching, grouping, calculating subtotals, and calculating revenue.

10. Optional: ProductRepository.findBySku and OrderRegistry.findByNumber use Optional so that finder methods do not return null.

11. Exceptions: The StoreException family represents business errors, while InMemoryProductRepository catches CSV loading IOException and wraps it in a StoreException.

12. Immutability and records: Customer, Order, OrderLine, Quote, CartView, and LineView use records to represent immutable data.

13. Enums: Category, Zone, and Governorate represent fixed domain choices using enums with fields, constructors, and methods.

14. equals, hashCode, and toString: Product uses Lombok to generate equals and hashCode based only on its SKU, along with toString.

15. Numbers and BigDecimal: Money and ShippingQuoteService use BigDecimal for monetary calculations and comparisons.

16. Static, final, and nested: Money and Check are utility classes, while CartView contains its related LineView record.

17. Default methods: ProductRepository.findBySku and NotificationSender.supports provide default behavior to their implementations.

18. Concurrency: CartRegistry and OrderRegistry use thread-safe collections, while CheckoutService.placeOrder uses synchronization to protect checkout operations.

19. I/O and try-with-resources: InMemoryProductRepository uses try-with-resources when reading the CSV file, and Main manages the application context and web server lifecycle.

20. Date and time: OrderRegistry uses LocalDate.now(clock) and DateTimeFormatter.BASIC_ISO_DATE to generate order dates.

21. Strings and formatting: Money.format formats monetary values, while OrderRegistry uses String.format with Locale.US to generate order numbers.




