package ru.maggy.markersmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import ru.maggy.markersmap.R
import ru.maggy.markersmap.adapter.MarkersAdapter
import ru.maggy.markersmap.adapter.OnInterfactionListener
import ru.maggy.markersmap.databinding.FragmentMarkersListBinding
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.ui.EditMarkerFragment.Companion.textArg
import ru.maggy.markersmap.util.PositionArg
import ru.maggy.markersmap.viewmodel.MarkerViewModel

class MarkersListFragment : Fragment() {
    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.positionData: LatLng? by PositionArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMarkersListBinding.inflate(inflater, container, false)

        val adapter = MarkersAdapter(object : OnInterfactionListener {

            override fun onDelete(marker: Marker) {
                viewModel.deleteMarker(marker.id)
            }

            override fun onEdit(marker: Marker) {
                viewModel.edit(marker)
                findNavController().navigate(R.id.action_markersListFragment_to_editMarkerFragment,
                    Bundle().apply
                    {
                        textArg = marker.title
                    })
            }

            override fun moveToMarker(marker: Marker) {
                viewModel.selectMarker(marker)
                findNavController().navigateUp()
            }
        /*    override fun moveToMarker(marker: Marker) {
                viewModel.edit(marker)
                findNavController().navigate(R.id.action_markersListFragment_to_mapsFragment,
                Bundle().apply {
                     positionData = marker.position
                 })
            }*/

        })

        binding.markersList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { markers ->
            adapter.submitList(markers)
        })

   /*     viewModel.edited.observe(viewLifecycleOwner, { marker ->
            if (marker.id == 0) {
                return@observe
            }
        })*/


        return binding.root
    }
}