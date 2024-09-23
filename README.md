MICROSERVICE USING SPRING WEBFLUX 
SOME FEATURE USING ON IT
  - NOSQL: MONGODB
  - APACHE KAFKA
  - EUREKA
  - SPRING GATEWAY

I. SERVICE AND ENDPOINT
For an e-commerce platform like Shopee, using MongoDB with a microservice architecture, it's important to design the collections for each service efficiently. Here's a detailed breakdown of MongoDB collection design for each service:

1. User Service
   Collection: users
   Fields:
   _id: ObjectId (User ID)
   username: String (unique)
   email: String (unique)
   password: String (hashed)
   phone: String
   roles: Array of strings (e.g., ["CUSTOMER", "SHOP_OWNER"])
   profile: Object
   firstName: String
   lastName: String
   address: Array of embedded documents (list of user addresses)
   createdAt: Date
   updatedAt: Date
   Collection: user_sessions
   Fields:
   _id: ObjectId
   userId: ObjectId (reference to users)
   token: String (JWT or session token)
   createdAt: Date
   expiresAt: Date
2. Shop Service
   Collection: shops
   Fields:
   _id: ObjectId (Shop ID)
   ownerId: ObjectId (reference to users)
   shopName: String (unique)
   description: String
   location: String
   categories: Array of ObjectIds (references to categories)
   createdAt: Date
   updatedAt: Date
   Collection: shop_reviews
   Fields:
   _id: ObjectId
   shopId: ObjectId (reference to shops)
   customerId: ObjectId (reference to users)
   rating: Number (1-5)
   reviewText: String
   createdAt: Date
3. Product Service
   Collection: products
   Fields:
   _id: ObjectId (Product ID)
   shopId: ObjectId (reference to shops)
   productName: String
   description: String
   categoryId: ObjectId (reference to categories)
   price: Decimal
   discount: Decimal (optional)
   images: Array of strings (URLs or image paths)
   tags: Array of strings
   createdAt: Date
   updatedAt: Date
   Collection: product_reviews
   Fields:
   _id: ObjectId
   productId: ObjectId (reference to products)
   customerId: ObjectId (reference to users)
   rating: Number (1-5)
   reviewText: String
   createdAt: Date
4. Category Service
   Collection: categories
   Fields:
   _id: ObjectId (Category ID)
   categoryName: String (unique)
   parentCategoryId: ObjectId (optional, reference to categories)
   createdAt: Date
   updatedAt: Date
5. Inventory Service
   Collection: inventory
   Fields:
   _id: ObjectId
   productId: ObjectId (reference to products)
   shopId: ObjectId (reference to shops)
   stock: Number
   stockThreshold: Number (for low stock alerts)
   updatedAt: Date
6. Order Service
   Collection: orders
   Fields:
   _id: ObjectId (Order ID)
   customerId: ObjectId (reference to users)
   shopId: ObjectId (reference to shops)
   products: Array of embedded documents (products within the order)
   productId: ObjectId (reference to products)
   quantity: Number
   price: Decimal (price at the time of the order)
   discount: Decimal (discount at the time of the order, if any)
   totalPrice: Decimal (sum of product prices)
   shippingAddress: Embedded document
   addressLine1: String
   city: String
   postalCode: String
   country: String
   status: String (e.g., "pending", "shipped", "delivered", "cancelled")
   createdAt: Date
   updatedAt: Date
7. Payment Service
   Collection: payments
   Fields:
   _id: ObjectId (Payment ID)
   orderId: ObjectId (reference to orders)
   customerId: ObjectId (reference to users)
   paymentMethod: String (e.g., "credit card", "PayPal")
   amount: Decimal
   status: String (e.g., "pending", "completed", "failed")
   transactionId: String (external payment gateway ID)
   createdAt: Date
   updatedAt: Date
8. Shipping Service
   Collection: shipments
   Fields:
   _id: ObjectId (Shipment ID)
   orderId: ObjectId (reference to orders)
   shopId: ObjectId (reference to shops)
   shippingProvider: String
   trackingNumber: String
   shippingStatus: String (e.g., "pending", "shipped", "delivered")
   shippingDate: Date
   estimatedDeliveryDate: Date
   createdAt: Date
9. Review and Rating Service
   Collection: reviews
   Fields:
   _id: ObjectId (Review ID)
   userId: ObjectId (reference to users)
   targetId: ObjectId (could be a productId or shopId)
   rating: Number (1-5)
   reviewText: String
   createdAt: Date
10. Discount and Promotion Service
    Collection: discounts
    Fields:
    _id: ObjectId (Discount ID)
    productId: ObjectId (reference to products)
    shopId: ObjectId (reference to shops)
    discountType: String (e.g., "percentage", "fixed")
    value: Decimal (discount value, based on type)
    startDate: Date
    endDate: Date
    createdAt: Date
    updatedAt: Date
    Collection: coupons
    Fields:
    _id: ObjectId (Coupon ID)
    code: String (unique coupon code)
    shopId: ObjectId (optional, reference to shops)
    discountType: String (e.g., "percentage", "fixed")
    value: Decimal
    usageLimit: Number (total times coupon can be used)
    usedCount: Number
    startDate: Date
    endDate: Date
    createdAt: Date
    updatedAt: Date
11. Notification Service
    Collection: notifications
    Fields:
    _id: ObjectId (Notification ID)
    userId: ObjectId (reference to users)
    message: String
    readStatus: Boolean (default: false)
    createdAt: Date
12. Search and Filtering Service
    For the search service, you may not need a specific collection since MongoDB provides a full-text search capability. You can create indexes on fields that you want to search against (e.g., product name, description).

13. Recommendation Service
    Collection: recommendations
    Fields:
    _id: ObjectId (Recommendation ID)
    userId: ObjectId (reference to users)
    recommendedProducts: Array of ObjectIds (references to products)
    createdAt: Date
14. Customer Support Service
    Collection: support_tickets
    Fields:
    _id: ObjectId (Ticket ID)
    userId: ObjectId (reference to users)
    orderId: ObjectId (reference to orders, optional)
    subject: String
    description: String
    status: String (e.g., "open", "in-progress", "closed")
    createdAt: Date
    updatedAt: Date


For an e-commerce platform like Shopee, implemented using microservices, here’s a breakdown of features for each service along with key functionality to include. Each microservice will handle a specific domain to ensure loose coupling and scalability.

1. User Service
   Handles user-related functionality like registration, authentication, and profile management.

Features:
User Registration
Register new users (customer, shop owner).
Validate email, phone number, and unique username.
Login/Logout
User authentication (JWT-based or OAuth2).
Issue access and refresh tokens.
Logout and invalidate tokens.
Profile Management
View and update user profile (name, contact details, password).
Manage addresses (add, edit, delete shipping addresses).
Role Management
Assign roles like "CUSTOMER" or "SHOP_OWNER".
Password Recovery
Allow users to reset passwords via email verification.
2. Shop Service
   Manages shop creation, shop info, and reviews.

Features:
Shop Registration
Allow shop owners to create and manage their shops.
Add details like shop name, description, and location.
Shop Profile Management
Edit shop information.
Upload shop logo and cover images.
Shop Reviews and Ratings
Allow customers to rate and review shops.
Display shop ratings based on customer reviews.
Shop Performance Metrics
Provide shop owners with analytics on sales, orders, and customer feedback.
3. Product Service
   Handles product listings, reviews, and search functionality.

Features:
Product Creation and Management
Add, edit, and delete products.
Manage product details (name, description, category, price, discount, tags).
Upload product images.
Product Reviews
Allow customers to rate and review products.
Display ratings and reviews for each product.
Search and Filtering
Search for products by name, description, category, or tags.
Filter products by price, rating, category, and more.
Product Recommendations
Provide personalized product recommendations to customers.
4. Category Service
   Manages the categories that products are organized under.

Features:
Category Management
Create, edit, delete product categories.
Support hierarchical categories (parent-child relationships).
Category Search
Retrieve products by category for display.
5. Inventory Service
   Tracks stock levels for products in shops.

Features:
Stock Management
Track inventory for each product in the shop.
Notify shop owners when stock levels are low (set thresholds).
Stock Adjustments
Allow shop owners to update inventory levels.
Log changes to inventory for tracking.
6. Order Service
   Manages order creation, status tracking, and history.

Features:
Order Placement
Create new orders with selected products, quantity, and shipping details.
Calculate total cost (price * quantity + shipping fees).
Order Tracking
Allow customers to track the status of their orders (pending, shipped, delivered).
Notify customers about order status changes.
Order History
Provide customers with order history and details of past orders.
Order Cancellation
Allow customers to cancel orders before shipping.
7. Payment Service
   Handles payment processing and transactions.

Features:
Payment Integration
Integrate with payment gateways (PayPal, Stripe, etc.).
Support multiple payment methods (credit card, wallet, etc.).
Payment Processing
Process payments securely and confirm transactions.
Handle payment status (pending, completed, failed).
Refunds
Allow for refund requests and process refunds.
Transaction History
Maintain a transaction log for users to view their payment history.
8. Shipping Service
   Manages shipment tracking and delivery for orders.

Features:
Shipping Label Creation
Generate shipping labels for orders.
Calculate shipping costs based on destination and carrier.
Shipment Tracking
Integrate with shipping providers (e.g., FedEx, DHL) to track shipments.
Provide real-time updates on shipment status.
Delivery Notifications
Notify customers when their orders are shipped and delivered.
9. Discount and Promotion Service
   Handles discounts, coupons, and promotional offers.

Features:
Discount Management
Allow shop owners to create percentage-based or fixed discounts on products.
Set discount periods (start and end dates).
Coupon Management
Create promotional coupons for customers.
Track coupon usage and enforce limits (e.g., one-time use).
Automatic Promotions
Support for flash sales or time-based discounts.
10. Notification Service
    Manages notifications for users and shop owners.

Features:
Order Notifications
Send notifications for order status updates (e.g., shipped, delivered).
Payment Notifications
Notify users about successful payments or failed transactions.
Promotional Notifications
Notify users about ongoing promotions, new products, or discounts.
System Notifications
Notify users about important events (e.g., password changes).
11. Analytics Service
    Collects and reports data for business insights and decision-making.

Features:
Sales Analytics
Provide detailed reports on sales performance for shop owners.
Analyze sales trends over time.
Customer Behavior Analytics
Track customer interactions with products (views, purchases, wishlist).
Revenue Tracking
Track total revenue, average order value, and sales per category.
User Activity Metrics
Track user activity, including sign-ups, active users, and session times.
12. Review and Rating Service
    Handles product and shop reviews from customers.

Features:
Review Submission
Allow customers to submit reviews and ratings for products and shops.
Review Moderation
Allow shop owners to flag or respond to reviews.
Provide an admin interface for managing flagged reviews.
Rating System
Calculate average ratings for products and shops.
Display user reviews with ratings for others to view.
13. Customer Support Service
    Provides support functionality for handling customer queries.

Features:
Ticket System
Allow customers to create support tickets for issues with orders or payments.
Live Chat
Implement a live chat system for real-time customer support.
Ticket Management
Track ticket statuses (open, in-progress, closed).
Allow customer support agents to manage and respond to tickets.
14. Search Service
    Handles search operations for products and shops.

Features:
Product Search
Implement full-text search for products using MongoDB indexes.
Provide autocomplete suggestions for popular searches.
Shop Search
Enable shop name and category-based searching for customers.
Faceted Filtering
Provide filters for price, category, brand, rating, etc.
15. Recommendation Service
    Generates personalized product recommendations for users.

Features:
Personalized Recommendations
Generate recommendations based on user purchase history and browsing behavior.
Related Products
Display products related to the current item being viewed (e.g., "Customers also bought...").
Shop Recommendations
Suggest popular shops based on categories or customer preferences.
16. Cart Service
    Handles shopping cart management for users.

Features:
Add to Cart
Allow customers to add products to their cart.
Cart Management
Update, remove items from the cart.
Save cart items for logged-in users across sessions.
Cart Price Calculation
Calculate cart total, apply discounts, and taxes before checkout.
