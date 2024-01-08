package com.solodilov.coffeeshopapp.presentation.cafe_list_map

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.solodilov.coffeeshopapp.App
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.FragmentCafeListMapBinding
import com.solodilov.coffeeshopapp.presentation.cafe_list.CafeListFragmentDirections
import com.solodilov.coffeeshopapp.presentation.cafe_list.CafeListViewModel
import com.solodilov.coffeeshopapp.presentation.cafe_list.CafeUi
import com.solodilov.coffeeshopapp.presentation.common.UiState
import com.solodilov.coffeeshopapp.presentation.common.ViewModelFactory
import com.solodilov.coffeeshopapp.presentation.common.collectFlow
import com.solodilov.coffeeshopapp.presentation.common.onError
import com.solodilov.coffeeshopapp.presentation.common.onSuccess
import com.solodilov.coffeeshopapp.presentation.common.showToast
import com.solodilov.coffeeshopapp.presentation.common.viewBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import javax.inject.Inject

class CafeListMapFragment : Fragment(R.layout.fragment_cafe_list_map) {
    private val binding by viewBinding(FragmentCafeListMapBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CafeListViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapKitFactory.initialize(requireContext())
        initViews()
        collectFlow(viewModel.uiState, ::handleState)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun initViews() {
        binding.errorLayout.tryButton.setOnClickListener { viewModel.getData() }
        binding.cafeListMapToolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    }

    private fun handleState(state: UiState<List<CafeUi>>) = with(binding) {
        progressBar.isVisible = state is UiState.Loading
        errorLayout.root.isVisible = state is UiState.Error
        mapview.isVisible = state is UiState.Success

        state
            .onSuccess { data ->
                binding.mapview.mapWindow.map.move(CameraPosition(
                    viewModel.findMidpoint(data),
                    //viewModel.findMidpoint(data),
                    /* zoom = */ 10.0f,
                    /* azimuth = */ 0.0f,
                    /* tilt = */ 30.0f
                ))
                data.forEach (::drawMapPoint)
            }
            .onError { error -> showToast(error.message.toString()) }
    }

    private fun drawMapPoint(cafe: CafeUi) {
        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.ic_cafe)
        binding.mapview.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(cafe.point.latitude, cafe.point.longitude)
            setIcon(imageProvider)
            setText(cafe.name, TextStyle().apply {
                size = 14f
                color = ContextCompat.getColor(requireContext(), R.color.brown)
                placement = TextStyle.Placement.BOTTOM
            })
            addTapListener { _, _ ->
                startCafeMenuFragment(cafe.id)
                true
            }
        }
    }

    private fun startCafeMenuFragment(cafeId: Int) {
        findNavController().navigate(CafeListMapFragmentDirections.actionCafeListMapFragmentToCafeMenuFragment(cafeId))
    }

}