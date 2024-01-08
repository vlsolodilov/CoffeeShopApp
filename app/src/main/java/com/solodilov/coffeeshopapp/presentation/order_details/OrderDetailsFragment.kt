package com.solodilov.coffeeshopapp.presentation.order_details

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.solodilov.coffeeshopapp.App
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.FragmentCafeListBinding
import com.solodilov.coffeeshopapp.databinding.FragmentCafeMenuBinding
import com.solodilov.coffeeshopapp.databinding.FragmentOrderDetailsBinding
import com.solodilov.coffeeshopapp.domain.entity.Coffee
import com.solodilov.coffeeshopapp.presentation.common.UiState
import com.solodilov.coffeeshopapp.presentation.common.ViewModelFactory
import com.solodilov.coffeeshopapp.presentation.common.collectFlow
import com.solodilov.coffeeshopapp.presentation.common.onError
import com.solodilov.coffeeshopapp.presentation.common.onSuccess
import com.solodilov.coffeeshopapp.presentation.common.showToast
import com.solodilov.coffeeshopapp.presentation.common.viewBinding
import com.solodilov.coffeeshopapp.presentation.menu.CafeMenuViewModel
import javax.inject.Inject

class OrderDetailsFragment : Fragment(R.layout.fragment_order_details) {
    private val binding by viewBinding(FragmentOrderDetailsBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CafeMenuViewModel by activityViewModels { viewModelFactory }

    private var orderDetailsAdapter: OrderDetailsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectFlow(viewModel.uiState, ::handleState)
    }

    private fun initViews() {
        orderDetailsAdapter = OrderDetailsAdapter(
            onDecreaseQuantityClick = { coffee ->  viewModel.decreaseQuantity(coffee.id) },
            onIncreaseQuantityClick = { coffee ->  viewModel.increaseQuantity(coffee.id) },
        )
        binding.order.adapter = orderDetailsAdapter
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
            binding.swipeContainer.isRefreshing = false
        }
        binding.errorLayout.tryButton.setOnClickListener { viewModel.refreshData() }
        binding.orderToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.payButton.setOnClickListener {  }

    }

    private fun handleState(state: UiState<List<Coffee>>) = with(binding) {
        progressBar.isVisible = state is UiState.Loading
        errorLayout.root.isVisible = state is UiState.Error
        order.isVisible = state is UiState.Success
        payButton.isVisible = state is UiState.Success

        state
            .onSuccess { data -> orderDetailsAdapter?.submitList(data.filter { coffee -> coffee.quantity > 0 }) }
            .onError { error -> showToast(error.message.toString()) }
    }

    override fun onDestroy() {
        orderDetailsAdapter = null
        super.onDestroy()
    }
}