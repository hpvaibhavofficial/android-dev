package com.example.helpinghands.repository.data.repo

import com.example.helpinghands.data.model.CartItem
import com.example.helpinghands.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ECommerceRepository {

    // -------------------------------
    // PRODUCT CATALOG
    // -------------------------------

    /** Real-time list of all products */
    fun getAllProducts(): Flow<Result<List<Product>>>

    /** Real-time list of featured products for Home screen */
    fun getFeaturedProducts(): Flow<Result<List<Product>>>

    /** Real-time list of products by category */
    fun getProductsByCategory(categoryId: String): Flow<Result<List<Product>>>

    /** Get single product by ID */
    suspend fun getProductById(productId: String): Result<Product>

    // -------------------------------
    // ADMIN PRODUCT MANAGEMENT
    // -------------------------------

    /** Create new or update existing product */
    suspend fun saveProduct(product: Product): Result<Unit>

    /** Delete a product by ID */
    suspend fun deleteProduct(productId: String): Result<Unit>

    // -------------------------------
    // CART OPERATIONS
    // -------------------------------

    /** Real-time flow of cart items */
    fun getCartItems(userId: String): Flow<Result<List<CartItem>>>

    /** Add product or increase item quantity */
    suspend fun addToCart(userId: String, product: Product, quantity: Int): Result<Unit>

    /** Increase/decrease quantity OR delete if <= 0 */
    suspend fun updateCartItemQuantity(
        userId: String,
        productId: String,
        newQuantity: Int
    ): Result<Unit>
}
