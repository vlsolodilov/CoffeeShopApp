package com.solodilov.coffeeshopapp.presentation.menu

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.solodilov.coffeeshopapp.App
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.FragmentCafeMenuBinding
import com.solodilov.coffeeshopapp.domain.entity.Coffee
import com.solodilov.coffeeshopapp.presentation.common.UiState
import com.solodilov.coffeeshopapp.presentation.common.ViewModelFactory
import com.solodilov.coffeeshopapp.presentation.common.collectFlow
import com.solodilov.coffeeshopapp.presentation.common.onError
import com.solodilov.coffeeshopapp.presentation.common.onSuccess
import com.solodilov.coffeeshopapp.presentation.common.showToast
import com.solodilov.coffeeshopapp.presentation.common.viewBinding
import javax.inject.Inject

class CafeMenuFragment : Fragment(R.layout.fragment_cafe_menu) {
    private val binding by viewBinding(FragmentCafeMenuBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CafeMenuViewModel by activityViewModels { viewModelFactory }

    private var cafeMenuAdapter: CafeMenuAdapter? = null
    private val args by navArgs<CafeMenuFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel.setCafeId(args.cafeId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectFlow(viewModel.uiState, ::handleState)
    }

    private fun initViews() {
        cafeMenuAdapter = CafeMenuAdapter(
            onDecreaseQuantityClick = { coffee ->  viewModel.decreaseQuantity(coffee.id) },
            onIncreaseQuantityClick = { coffee ->  viewModel.increaseQuantity(coffee.id) },
        )
        binding.cafeMenu.adapter = cafeMenuAdapter
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshData()
            binding.swipeContainer.isRefreshing = false
        }
        binding.errorLayout.tryButton.setOnClickListener { viewModel.refreshData() }
        binding.cafeMenuToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.goToPaymentButton.setOnClickListener { startOrderDetailsFragment() }

    }

    private fun handleState(state: UiState<List<Coffee>>) = with(binding) {
        progressBar.isVisible = state is UiState.Loading
        errorLayout.root.isVisible = state is UiState.Error
        cafeMenu.isVisible = state is UiState.Success
        goToPaymentButton.isVisible = state is UiState.Success

        state
            .onSuccess { data -> cafeMenuAdapter?.submitList(data) }
            .onError { error -> showToast(error.message.toString()) }
    }

    private fun startOrderDetailsFragment() {
        findNavController().navigate(R.id.action_cafeMenuFragment_to_orderDetailsFragment)
    }

    override fun onDestroy() {
        cafeMenuAdapter = null
        super.onDestroy()
    }
}