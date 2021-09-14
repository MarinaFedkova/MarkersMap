package ru.maggy.markersmap.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import kotlinx.coroutines.launch
import ru.maggy.markersmap.R
import ru.maggy.markersmap.adapter.MarkersAdapter
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.extensions.icon
import ru.maggy.markersmap.ui.EditMarkerFragment.Companion.textArg
import ru.maggy.markersmap.ui.MarkersListFragment.Companion.positionData
import ru.maggy.markersmap.util.PositionArg
import ru.maggy.markersmap.viewmodel.MarkerViewModel


class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.positionData: LatLng? by PositionArg
    }

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                    mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            } else {
                Toast.makeText(requireContext(), R.string.permission_no, Toast.LENGTH_LONG)
                    .show()
                return@registerForActivityResult
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        val listMarkers = view.findViewById<View>(R.id.list_markers)

        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapsFragment.awaitMap().apply {
                isTrafficEnabled = true
                isBuildingsEnabled = true

                uiSettings.apply {
                    isZoomControlsEnabled = true
                    setAllGesturesEnabled(true)
                }
            }

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                    }

                    val fusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(requireActivity())

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        println(it)
                    }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(requireContext(), R.string.permission_requied, Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            listMarkers.setOnClickListener {
                findNavController().navigate(R.id.action_mapsFragment_to_markersListFragment)
            }
            val markerManager = MarkerManager(googleMap)

            val collection: MarkerManager.Collection = markerManager.newCollection()
            viewModel.data.observe(viewLifecycleOwner) {
                it.forEach {
                    collection.addMarker {
                        position(it.position)
                        icon(getDrawable(requireContext(), R.drawable.ic_place_48)!!)
                        title(it.title)
                    }.apply {
                        tag = it
                    }
                }
            }
            googleMap.setOnMapLongClickListener {
                MarkerOptions()
                    .position(it)
                    .title("")
                    .icon(getDrawable(requireContext(), R.drawable.ic_place_48)!!)
                viewModel.changePosition(it)
                findNavController().navigate(R.id.action_mapsFragment_to_editMarkerFragment)
            }

            collection.setOnMarkerClickListener { marker ->
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.menu_marker)
                builder.setPositiveButton(R.string.menu_edit) { dialog, _ ->
                    findNavController().navigate(R.id.action_mapsFragment_to_editMarkerFragment,
                        Bundle().apply
                        { textArg = marker.title })
                }
                builder.setNegativeButton(R.string.menu_delete) { dialog, _ ->
                    viewModel.deleteMarker((marker.tag as Marker).id)
                    marker.remove()
                    marker.showInfoWindow()
                }
                builder.setNeutralButton(R.string.menu_cancel) { dialog, _ ->
                    marker.showInfoWindow()
                    dialog.cancel()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }

            val target = LatLng(55.751999, 37.617734)
            val userTarget = arguments?.positionData

            viewModel.selectMarker.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    googleMap.awaitAnimateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition {
                                target(it.position)
                                zoom(15F)
                            }
                        ))
                }
            }

            googleMap.awaitAnimateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition {
                        target(target)
                        zoom(15F)
                    }
                ))
        }
    }
}





