package ru.maggy.markersmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.maggy.markersmap.R
import ru.maggy.markersmap.adapter.MarkersAdapter
import ru.maggy.markersmap.adapter.OnInterfactionListener
import ru.maggy.markersmap.databinding.FragmentMarkersListBinding
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.ui.EditMarkerFragment.Companion.textArg
import ru.maggy.markersmap.viewmodel.MarkerViewModel

class MarkersListFragment : Fragment() {

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

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
                findNavController().navigate(R.id.action_markersListFragment_to_editMarkerFragment,
                Bundle().apply
                 {
                     textArg = marker.title
                     viewModel.edit(marker)
                 })
            }
        })

        binding.markersList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.markers)
        })



        return binding.root
    }
}