package com.parserdev.swapiapp.presentation.swlibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.parserdev.swapiapp.databinding.FragmentSwLibraryBinding
import com.parserdev.swapiapp.presentation.swlibrary.adapter.SWLibraryAdapter
import com.parserdev.swapiapp.presentation.swlibrary.viewmodel.SWLibraryViewModel
import com.parserdev.swapiapp.utils.collectLatestLifecycleFlow
import com.parserdev.swapiapp.utils.collectLifecycleFlow

class SWLibraryFragment : Fragment() {

    private val swLibraryViewModel by activityViewModels<SWLibraryViewModel>()

    private lateinit var swLibraryAdapter: SWLibraryAdapter

    private var _binding: FragmentSwLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSwLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        collectLatestLifecycleFlow(swLibraryViewModel.progressBarVisibilityFlow) { progressBarVisibility ->
            binding.progressBar.isVisible = progressBarVisibility
        }

        collectLifecycleFlow(swLibraryViewModel.itemsListFlow) { itemsList ->
            swLibraryAdapter.itemsList = itemsList
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        swLibraryViewModel.toggleProgressBarVisibility()
    }

    private fun setupRecyclerView() = binding.recyclerViewSWCharacters.apply {
        swLibraryAdapter = SWLibraryAdapter()
        adapter = swLibraryAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


}
