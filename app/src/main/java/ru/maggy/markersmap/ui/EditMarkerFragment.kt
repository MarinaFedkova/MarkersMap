package ru.maggy.markersmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.maggy.markersmap.databinding.FragmentEditMarkerBinding
import ru.maggy.markersmap.util.AndroidUtils
import ru.maggy.markersmap.util.StringArg
import ru.maggy.markersmap.viewmodel.MarkerViewModel

class EditMarkerFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditMarkerBinding.inflate(inflater, container, false)

        binding.editTitle.requestFocus()
        arguments?.textArg?.let(binding.editTitle::setText)
        binding.save.setOnClickListener {
            viewModel.changeContent(binding.editTitle.text.toString())
            viewModel.saveMarker()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }

}