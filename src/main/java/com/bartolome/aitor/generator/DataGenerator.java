//package com.bartolome.aitor.generator;
//
//import com.bartolome.aitor.model.entities.*;
//import com.bartolome.aitor.repository.CartItemRepository;
//import com.bartolome.aitor.repository.CartRepository;
//import com.bartolome.aitor.repository.CategoryRepository;
//import com.bartolome.aitor.repository.OrderItemRepository;
//import com.bartolome.aitor.repository.OrderRepository;
//import com.bartolome.aitor.repository.ProductRepository;
//import com.bartolome.aitor.repository.UserRepository;
//import com.github.javafaker.Faker;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//@Component
//@RequiredArgsConstructor
//public class DataGenerator implements CommandLineRunner {
//
//    private final CategoryRepository categoryRepository;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
//    private final OrderRepository orderRepository;
//    private final OrderItemRepository orderItemRepository;
//
//    // Inicializamos Faker con la localización en español
//    private final Faker faker = new Faker(new Locale("es"));
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        try {
//            // 1. Generar categorías (10 categorías)
//            List<Category> categories = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                Category cat = Category.builder()
//                        .nombre(faker.commerce().department())
//                        .build();
//                categories.add(cat);
//            }
//            categoryRepository.saveAll(categories);
//            System.out.println("Categorías generadas: " + categories.size());
//
//            // 2. Generar 500 usuarios
//            List<User> users = new ArrayList<>();
//            for (int i = 0; i < 500; i++) {
//                String email = faker.internet().emailAddress();
//                User user = User.builder()
//                        .nombre(faker.name().fullName())
//                        .email(email)
//                        .password(faker.internet().password(6, 12))
//                        .rol("cliente")
//                        .build();
//                users.add(user);
//            }
//            userRepository.saveAll(users);
//            System.out.println("Usuarios generados: " + users.size());
//
//            // 3. Generar 500 productos
//            List<Product> products = new ArrayList<>();
//            for (int i = 0; i < 500; i++) {
//                // Aquí se reemplaza la coma por punto para asegurar la conversión a Double
//                double precio = Double.valueOf(
//                        faker.commerce().price(10.0, 500.0).replace(",", ".")
//                );
//                Product product = Product.builder()
//                        .nombre(faker.commerce().productName())
//                        .descripcion(faker.lorem().sentence())
//                        .precio(precio)
//                        .stock(faker.number().numberBetween(1, 100))
//                        .imagenUrl(faker.internet().avatar())
//                        .categoria(categories.get(faker.number().numberBetween(0, categories.size())))
//                        .build();
//                products.add(product);
//            }
//            productRepository.saveAll(products);
//            System.out.println("Productos generados: " + products.size());
//
//            // 4. Crear un carrito para cada usuario
//            List<Cart> carts = new ArrayList<>();
//            for (User u : users) {
//                Cart cart = Cart.builder()
//                        .usuario(u)
//                        .items(new ArrayList<>())
//                        .build();
//                carts.add(cart);
//            }
//            cartRepository.saveAll(carts);
//            System.out.println("Carritos generados: " + carts.size());
//
//            // 5. Generar ítems de carrito para cada carrito (entre 1 y 5 ítems)
//            List<CartItem> cartItems = new ArrayList<>();
//            for (Cart cart : carts) {
//                int itemsCount = faker.number().numberBetween(1, 6);
//                for (int j = 0; j < itemsCount; j++) {
//                    Product prod = products.get(faker.number().numberBetween(0, products.size()));
//                    CartItem cartItem = CartItem.builder()
//                            .carrito(cart)
//                            .producto(prod)
//                            .cantidad(faker.number().numberBetween(1, 5))
//                            .build();
//                    cartItems.add(cartItem);
//                }
//            }
//            cartItemRepository.saveAll(cartItems);
//            System.out.println("Items de carrito generados: " + cartItems.size());
//
//            // 6. Generar pedidos para cada usuario (entre 0 y 5 pedidos por usuario)
//            List<Order> orders = new ArrayList<>();
//            for (User u : users) {
//                int orderCount = faker.number().numberBetween(0, 6);
//                for (int k = 0; k < orderCount; k++) {
//                    Order order = Order.builder()
//                            .usuario(u)
//                            .fechaCreacion(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)))
//                            .estado("COMPLETADO")
//                            .items(new ArrayList<>())
//                            .build();
//                    orders.add(order);
//                }
//            }
//            orderRepository.saveAll(orders);
//            System.out.println("Pedidos generados: " + orders.size());
//
//            // 7. Generar ítems de pedido para cada pedido (entre 1 y 5 ítems) y calcular el total
//            List<OrderItem> orderItems = new ArrayList<>();
//            for (Order order : orders) {
//                int itemCount = faker.number().numberBetween(1, 6);
//                double total = 0;
//                for (int j = 0; j < itemCount; j++) {
//                    Product prod = products.get(faker.number().numberBetween(0, products.size()));
//                    int quantity = faker.number().numberBetween(1, 5);
//                    OrderItem orderItem = OrderItem.builder()
//                            .pedido(order)
//                            .producto(prod)
//                            .cantidad(quantity)
//                            .precioUnitario(prod.getPrecio())
//                            .build();
//                    total += prod.getPrecio() * quantity;
//                    orderItems.add(orderItem);
//                }
//                order.setTotal(total);
//            }
//            orderItemRepository.saveAll(orderItems);
//            // Se actualizan los pedidos para guardar los totales calculados
//            orderRepository.saveAll(orders);
//            System.out.println("OrderItems generados: " + orderItems.size());
//            System.out.println("Datos generados exitosamente.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
