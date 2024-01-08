package com.solodilov.coffeeshopapp.presentation.cafe_list

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.solodilov.coffeeshopapp.App
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.FragmentCafeListBinding
import com.solodilov.coffeeshopapp.presentation.common.UiState
import com.solodilov.coffeeshopapp.presentation.common.ViewModelFactory
import com.solodilov.coffeeshopapp.presentation.common.collectFlow
import com.solodilov.coffeeshopapp.presentation.common.onError
import com.solodilov.coffeeshopapp.presentation.common.onSuccess
import com.solodilov.coffeeshopapp.presentation.common.showToast
import com.solodilov.coffeeshopapp.presentation.common.viewBinding
import javax.inject.Inject

class CafeListFragment : Fragment(R.layout.fragment_cafe_list) {
    private val binding by viewBinding(FragmentCafeListBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CafeListViewModel by viewModels { viewModelFactory }

    private var cafeListAdapter: CafeListAdapter? = null

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) { viewModel.getData() }
        }
    private val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        checkAndRequestLocationPermission()
        collectFlow(viewModel.uiState, ::handleState)
    }

    private fun initViews() {
        cafeListAdapter = CafeListAdapter { cafe -> startCafeMenuFragment(cafe.id) }
        binding.cafeList.adapter = cafeListAdapter
        binding.swipeContainer.setOnRefreshListener {
            viewModel.getData()
            binding.swipeContainer.isRefreshing = false
        }
        binding.onMapButton.setOnClickListener { startCafeListMapFragment() }
        binding.errorLayout.tryButton.setOnClickListener { viewModel.getData() }
        binding.cafeListToolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    }

    private fun handleState(state: UiState<List<CafeUi>>) = with(binding) {
        progressBar.isVisible = state is UiState.Loading
        errorLayout.root.isVisible = state is UiState.Error
        cafeList.isVisible = state is UiState.Success

        state
            .onSuccess { data -> cafeListAdapter?.submitList(data) }
            .onError { error -> showToast(error.message.toString()) }
    }

    private fun startCafeListMapFragment() {
        findNavController().navigate(R.id.action_cafeListFragment_to_cafeListMapFragment)
    }

    private fun startCafeMenuFragment(cafeId: Int) {
        findNavController().navigate(CafeListFragmentDirections.actionCafeListFragmentToCafeMenuFragment(cafeId))
    }

    private fun checkAndRequestLocationPermission() {
        if (
            ContextCompat.checkSelfPermission(requireContext(), fineLocationPermission)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), coarseLocationPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(fineLocationPermission)
                && shouldShowRequestPermissionRationale(coarseLocationPermission)
            ) {
                showPermissionExplanationDialog()
            } else {
                requestLocationPermissionLauncher.launch(
                    arrayOf(fineLocationPermission, coarseLocationPermission)
                )
            }
        }

    }

    private fun showPermissionExplanationDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle(getString(R.string.permission_required))
            setMessage(getString(R.string.permission_explanation_message))
            setPositiveButton(getString(R.string.Yes)) { _, _ ->
                requestLocationPermissionLauncher.launch(
                    arrayOf(
                        fineLocationPermission,
                        coarseLocationPermission
                    )
                )
            }
            setNegativeButton(getString(R.string.No)) { _, _ -> }
            create().show()
        }
    }

    override fun onDestroy() {
        cafeListAdapter = null
        super.onDestroy()
    }
}