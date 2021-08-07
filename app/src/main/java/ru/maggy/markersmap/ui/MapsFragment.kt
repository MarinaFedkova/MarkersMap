package ru.maggy.markersmap.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import ru.maggy.markersmap.R
import ru.maggy.markersmap.extensions.icon
import ru.maggy.markersmap.ui.EditMarkerFragment.Companion.textArg
import ru.maggy.markersmap.viewmodel.MarkerViewModel


class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

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


     /*       viewModel.data.observe(viewLifecycleOwner, { state ->
                val markerManager = MarkerManager(googleMap)

                val collection: MarkerManager.Collection = markerManager.newCollection().apply {
                    state.markers.forEach { marker ->
                        addMarker {
                            position(marker.position)
                            icon(getDrawable(requireContext(), R.drawable.ic_place_48)!!)
                            title(marker.title)
                        }
                    }
                }
            })*/
            val markerManager = MarkerManager(googleMap)

            val collection: MarkerManager.Collection = markerManager.newCollection()
                    addMarker {
                        position(marker.position)
                        icon(getDrawable(requireContext(), R.drawable.ic_place_48)!!)
                        title(marker.title)
                    }



                googleMap.setOnMapLongClickListener {
                    val marker = collection.addMarker {
                        position(it)
                        icon(getDrawable(requireContext(), R.drawable.ic_place_48)!!)
                        title("")
                    }
                    findNavController().navigate(R.id.action_mapsFragment_to_editMarkerFragment,
                        Bundle().apply
                        { textArg = marker.title })
                }

            val target = LatLng(55.751999, 37.617734)
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

private fun addMarker(collection: MarkerManager.Collection, position: LatLng) {
    collection.addMarker {
        position(position)
        icon(getDrawable(requireContext(), R.drawable.ic_place_48)!!)
        title(title)
    }

}


