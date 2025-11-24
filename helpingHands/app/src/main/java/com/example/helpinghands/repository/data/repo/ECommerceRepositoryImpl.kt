package com.example.helpinghands.repository.data.repo

import com.example.helpinghands.data.model.CartItem
import com.example.helpinghands.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import javax.inject.Inject

class ECommerceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ECommerceRepository {

    private val productsCollection = firestore.collection("products")
    private fun cartCollection(userId: String) =
        firestore.collection("users").document(userId).collection("cart")

    // -----------------------------------------------------------------------
    // 🟦 PRODUCTS — REAL-TIME FULL LIST
    // -----------------------------------------------------------------------
    override fun getAllProducts(): Flow<Result<List<Product>>> = callbackFlow {
        val listener = productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                cancel()
                return@addSnapshotListener
            }
            val list = snapshot?.toObjects(Product::class.java) ?: emptyList()
            trySend(Result.success(list))
        }
        awaitClose { listener.remove() }
    }

    // -----------------------------------------------------------------------
    // ⭐ FEATURED PRODUCTS FOR HOME SCREEN
    // -----------------------------------------------------------------------
    override fun getFeaturedProducts(): Flow<Result<List<Product>>> = callbackFlow {
        val listener = productsCollection
            .whereEqualTo("isFeatured", true)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Result.failure(error))
                    cancel()
                    return@addSnapshotListener
                }

                val list = snapshot?.toObjects(Product::class.java) ?: emptyList()
                trySend(Result.success(list))
            }

        awaitClose { listener.remove() }
    }

    // -----------------------------------------------------------------------
    // 🟩 CATEGORY BASED PRODUCTS
    // -----------------------------------------------------------------------
    override fun getProductsByCategory(categoryId: String): Flow<Result<List<Product>>> =
        callbackFlow {
            val listener = productsCollection
                .whereEqualTo("categoryId", categoryId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        trySend(Result.failure(error))
                        cancel()
                        return@addSnapshotListener
                    }

                    val list = snapshot?.toObjects(Product::class.java) ?: emptyList()
                    trySend(Result.success(list))
                }

            awaitClose { listener.remove() }
        }

    // -----------------------------------------------------------------------
    // 🔍 GET SINGLE PRODUCT BY ID
    // -----------------------------------------------------------------------
    override suspend fun getProductById(productId: String): Result<Product> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = productsCollection.document(productId).get().await()
                val product = snapshot.toObject(Product::class.java)
                if (product != null) Result.success(product)
                else Result.failure(Exception("Product not found"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // -----------------------------------------------------------------------
    // 🛠 ADMIN CREATE OR UPDATE PRODUCT
    // -----------------------------------------------------------------------
    override suspend fun saveProduct(product: Product): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val docRef = if (product.id.isEmpty())
                    productsCollection.document()
                else
                    productsCollection.document(product.id)

                docRef.set(product.copy(id = docRef.id)).await()

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // -----------------------------------------------------------------------
    // ❌ DELETE PRODUCT
    // -----------------------------------------------------------------------
    override suspend fun deleteProduct(productId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                productsCollection.document(productId).delete().await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // -----------------------------------------------------------------------
    // 🛒 CART — REAL-TIME LISTENER
    // -----------------------------------------------------------------------
    override fun getCartItems(userId: String): Flow<Result<List<CartItem>>> =
        callbackFlow {
            if (userId.isEmpty()) {
                trySend(Result.failure(Exception("User not authenticated")))
                return@callbackFlow
            }

            val listener = cartCollection(userId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        trySend(Result.failure(error))
                        cancel()
                        return@addSnapshotListener
                    }

                    val list = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
                    trySend(Result.success(list))
                }

            awaitClose { listener.remove() }
        }

    // -----------------------------------------------------------------------
    // ➕ ADD TO CART
    // -----------------------------------------------------------------------
    override suspend fun addToCart(
        userId: String,
        product: Product,
        quantity: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val cartRef = cartCollection(userId).document(product.id)
            val snapshot = cartRef.get().await()

            if (snapshot.exists()) {
                val current = snapshot.toObject(CartItem::class.java)!!
                cartRef.update("quantity", current.quantity + quantity).await()
            } else {
                cartRef.set(
                    CartItem(
                        productId = product.id,
                        name = product.name,
                        price = product.price,
                        quantity = quantity
                    )
                ).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -----------------------------------------------------------------------
    // 🔁 UPDATE CART ITEM QUANTITY
    // -----------------------------------------------------------------------
    override suspend fun updateCartItemQuantity(
        userId: String,
        productId: String,
        newQuantity: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val cartRef = cartCollection(userId).document(productId)

            if (newQuantity <= 0) {
                cartRef.delete().await()
            } else {
                cartRef.update("quantity", newQuantity).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}





























//package com.example.helpinghands.repository.data.repo
//
//import com.example.helpinghands.data.model.CartItem
//import com.example.helpinghands.data.model.Product
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//import kotlin.coroutines.cancellation.CancellationException
//import javax.inject.Inject
//
//class ECommerceRepositoryImpl @Inject constructor(
//    private val firestore: FirebaseFirestore
//) : ECommerceRepository {
//
//    private val productsCollection = firestore.collection("products")
//    private fun cartCollection(userId: String) = firestore.collection("users").document(userId).collection("cart")
//
//    // --- Product Catalog Operations (Real-Time Flow) ---
//
//    override fun getAllProducts(): Flow<Result<List<Product>>> = callbackFlow {
//        val listenerRegistration = productsCollection.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                trySend(Result.failure(error))
//                cancel(CancellationException("Error listening to products: ${error.message}", error))
//                return@addSnapshotListener
//            }
//
//            val products = snapshot?.toObjects(Product::class.java) ?: emptyList()
//            trySend(Result.success(products))
//        }
//
//        awaitClose { listenerRegistration.remove() }
//    }
//
//    override suspend fun getProductById(productId: String): Result<Product> = withContext(Dispatchers.IO) {
//        try {
//            val snapshot = productsCollection.document(productId).get().await()
//            val product = snapshot.toObject(Product::class.java)
//            if (product != null) Result.success(product) else Result.failure(Exception("Product not found."))
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    // --- ADMIN CRUD OPERATIONS (NEW) ---
//
//    override suspend fun saveProduct(product: Product): Result<Unit> = withContext(Dispatchers.IO) {
//        return@withContext try {
//            // Determine if it's an update (ID exists) or a create (ID is empty).
//            val docRef = if (product.id.isEmpty()) productsCollection.document() else productsCollection.document(product.id)
//
//            // Set the document, ensuring the model's ID matches the document ID
//            docRef.set(product.copy(id = docRef.id)).await()
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun deleteProduct(productId: String): Result<Unit> = withContext(Dispatchers.IO) {
//        return@withContext try {
//            productsCollection.document(productId).delete().await()
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    // --- Cart Operations (Real-Time Flow & Actions) ---
//
//    override fun getCartItems(userId: String): Flow<Result<List<CartItem>>> = callbackFlow {
//        if (userId.isEmpty()) {
//            trySend(Result.failure(Exception("User not authenticated for cart access.")))
//            return@callbackFlow
//        }
//
//        val listenerRegistration = cartCollection(userId).addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                trySend(Result.failure(error))
//                cancel(CancellationException("Error listening to cart: ${error.message}", error))
//                return@addSnapshotListener
//            }
//
//            val items = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
//            trySend(Result.success(items))
//        }
//
//        awaitClose { listenerRegistration.remove() }
//    }
//
//    override suspend fun addToCart(userId: String, product: Product, quantity: Int): Result<Unit> = withContext(Dispatchers.IO) {
//        try {
//            val cartRef = cartCollection(userId).document(product.id)
//            val cartSnapshot = cartRef.get().await()
//
//            if (cartSnapshot.exists()) {
//                val currentItem = cartSnapshot.toObject(CartItem::class.java)!!
//                val newQuantity = currentItem.quantity + quantity
//                cartRef.update("quantity", newQuantity).await()
//            } else {
//                val newCartItem = CartItem(
//                    productId = product.id,
//                    name = product.name,
//                    price = product.price,
//                    quantity = quantity
//                )
//                cartRef.set(newCartItem).await()
//            }
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun updateCartItemQuantity(userId: String, productId: String, newQuantity: Int): Result<Unit> = withContext(Dispatchers.IO) {
//        try {
//            val cartRef = cartCollection(userId).document(productId)
//            if (newQuantity <= 0) {
//                cartRef.delete().await()
//            } else {
//                cartRef.update("quantity", newQuantity).await()
//            }
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}