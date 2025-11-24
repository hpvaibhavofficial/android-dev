package com.example.helpinghands.ui.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import com.example.helpinghands.data.model.Product
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.repository.data.repo.ECommerceRepository
import com.example.helpinghands.repository.data.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ecommerceRepo: ECommerceRepository,
    private val serviceRepo: ServiceRepository
) : ViewModel() {

    private val _featuredProducts = MutableStateFlow<List<Product>>(emptyList())
    val featuredProducts: StateFlow<List<Product>> = _featuredProducts

    private val _popularServices = MutableStateFlow<List<Service>>(emptyList())
    val popularServices: StateFlow<List<Service>> = _popularServices

    init {
        loadFeaturedProducts()
        loadPopularServices()
    }

    private fun loadFeaturedProducts() {
        viewModelScope.launch {
            ecommerceRepo.getFeaturedProducts()
                .catch { _featuredProducts.value = emptyList() }
                .collect { result ->
                    result.onSuccess { list ->
                        _featuredProducts.value = list
                    }
                }
        }
    }

    private fun loadPopularServices() {
        viewModelScope.launch {
            serviceRepo.getAllServices()
                .collect { result ->
                    result.onSuccess { list ->
                        _popularServices.value = list.take(5) // top 5
                    }
                    result.onFailure {
                        _popularServices.value = emptyList()
                    }
                }
        }
    }
}
