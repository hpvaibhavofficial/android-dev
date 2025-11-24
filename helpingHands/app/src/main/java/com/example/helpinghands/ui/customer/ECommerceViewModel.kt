package com.example.helpinghands.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpinghands.data.model.CartItem
import com.example.helpinghands.data.model.Product
import com.example.helpinghands.repository.data.repo.ECommerceRepository
import com.example.helpinghands.repository.data.repo.AuthRepository // ✅ NEW: Injecting AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of the product catalog (E-Mart) and the user's shopping cart.
 */
@HiltViewModel
class ECommerceViewModel @Inject constructor(
    private val eCommerceRepository: ECommerceRepository,
    private val authRepository: AuthRepository // ✅ FIX: Injecting Repository
) : ViewModel() {

    // ✅ FIX: Get currentUserId directly from the injected Repository
    private val currentUserId: String? = authRepository.currentUserId

    // --- Product Catalog State ---
    private val _products = MutableStateFlow<Result<List<Product>>>(Result.success(emptyList()))
    val products: StateFlow<Result<List<Product>>> = _products

    // --- Cart State ---
    private val _cartItems = MutableStateFlow<Result<List<CartItem>>>(Result.success(emptyList()))
    val cartItems: StateFlow<Result<List<CartItem>>> = _cartItems

    init {
        fetchProducts()
        fetchCartItems()
    }

    private fun fetchProducts() {
        eCommerceRepository.getAllProducts()
            .onEach { result ->
                _products.value = result
            }
            .launchIn(viewModelScope)
    }

    private fun fetchCartItems() {
        val userId = currentUserId
        if (userId != null) {
            eCommerceRepository.getCartItems(userId)
                .onEach { result ->
                    _cartItems.value = result
                }
                .launchIn(viewModelScope)
        } else {
            _cartItems.value = Result.failure(Exception("Cannot load cart: User ID is missing."))
        }
    }

    // --- CUSTOMER ACTION METHODS ---

    fun addToCart(product: Product, quantity: Int = 1) {
        val userId = currentUserId
        if (userId != null) {
            viewModelScope.launch {
                eCommerceRepository.addToCart(userId, product, quantity)
            }
        } else {
            // Error handling for unauthenticated user
        }
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        val userId = currentUserId
        if (userId != null) {
            viewModelScope.launch {
                eCommerceRepository.updateCartItemQuantity(userId, productId, newQuantity)
            }
        }
    }

    // --- ADMIN PRODUCT CRUD ACTIONS ---

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            eCommerceRepository.saveProduct(product)
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            eCommerceRepository.deleteProduct(productId)
        }
    }
}