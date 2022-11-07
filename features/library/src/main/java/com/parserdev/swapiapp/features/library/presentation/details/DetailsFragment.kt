package com.parserdev.swapiapp.features.library.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ui_components.R
import com.parserdev.characters.databinding.FragmentDetailsBinding
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.domain.EMPTY
import com.parserdev.swapiapp.domain.LOCAL_DATA
import com.parserdev.swapiapp.domain.REMOTE_DATA
import com.parserdev.swapiapp.domain.UNABLE_TO_CONNECT
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import com.parserdev.swapiapp.domain.network.NetworkResult
import com.parserdev.swapiapp.features.library.di.LibraryComponent
import com.parserdev.swapiapp.features.library.di.LibraryComponentProvider
import com.parserdev.swapiapp.features.library.presentation.utils.setMargins
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

class DetailsFragment : Fragment() {

    private lateinit var libraryComponent: LibraryComponent

    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory
    private lateinit var detailsViewModel: DetailsViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectLibraryComponent()
        provideViewModel()
        loadItem()
        binding.bindButtons()
        binding.bindNetworkResult(
            item = detailsViewModel.item
        )
    }

    private fun injectLibraryComponent() {
        libraryComponent =
            (activity?.application as LibraryComponentProvider).provideLibraryComponent()
        libraryComponent.inject(this)
    }

    private fun provideViewModel() {
        detailsViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[DetailsViewModel::class.java]
    }

    private fun loadItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                detailsViewModel.accept(
                    DetailsAction.LoadItem(
                        url = args.url,
                        libraryCategory = args.category
                    )
                )
            }
        }
    }

    private fun FragmentDetailsBinding.bindButtons(
    ) {
        buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        buttonRetry.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                detailsViewModel.accept(
                    DetailsAction.LoadItem(
                        url = args.url,
                        libraryCategory = args.category
                    )
                )
            }
        }
    }

    private fun FragmentDetailsBinding.bindNetworkResult(
        item: LiveData<NetworkResult<DetailsItem>>
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            item.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> setSuccess(detailsItem = response.data)
                    is NetworkResult.Error -> setError(detailsItem = response.data)
                    is NetworkResult.Loading -> setLoading(detailsItem = response.data)
                }
            }
        }
    }

    private fun FragmentDetailsBinding.setSuccess(detailsItem: DetailsItem?) {
        progressBar.isVisible = false
        textStatus.text = REMOTE_DATA
        imageIcon.setMargins(bottom = 0)
        if (detailsItem != null) setData(detailsItem = detailsItem)
    }

    private fun FragmentDetailsBinding.setError(detailsItem: DetailsItem?) {
        progressBar.isVisible = false
        if (detailsItem == null) {
            imageIcon.setImageResource(R.drawable.ic_error)
            imageIcon.setMargins(bottom = 20)
            textStatus.text = EMPTY
            textTitle.text = UNABLE_TO_CONNECT
            buttonRetry.isVisible = true
        } else textStatus.text = LOCAL_DATA
    }

    private fun FragmentDetailsBinding.setLoading(detailsItem: DetailsItem?) {
        buttonRetry.isVisible = false
        progressBar.isVisible = true
        textStatus.text = EMPTY
        if (detailsItem != null) setData(detailsItem = detailsItem)
    }

    private fun FragmentDetailsBinding.setData(detailsItem: DetailsItem) {
        when (detailsItem.category) {
            LibraryCategory.CHARACTER -> setCharacterData(detailsItem)
            LibraryCategory.FILM -> setFilmData(detailsItem)
            LibraryCategory.PLANET -> setPlanetData(detailsItem)
            LibraryCategory.SPECIE -> setSpecieData(detailsItem)
            LibraryCategory.STARSHIP -> setStarshipData(detailsItem)
            LibraryCategory.VEHICLE -> setVehicleData(detailsItem)
        }
    }

    private fun FragmentDetailsBinding.setCharacterData(detailsItem: DetailsItem) {
        imageIcon.setImageResource(R.drawable.ic_character)
        textTitle.text = detailsItem.title
        textBody.text = String.format(
            getString(
                R.string.details_character,
                Date.from(ZonedDateTime.parse(detailsItem.text1).toInstant()),
                Date.from(ZonedDateTime.parse(detailsItem.text2).toInstant()),
                detailsItem.text3,
                detailsItem.text4,
                detailsItem.text5,
                detailsItem.text6,
                detailsItem.text7,
                detailsItem.text8,
                detailsItem.text9
            )
        )
    }

    private fun FragmentDetailsBinding.setFilmData(detailsItem: DetailsItem) {
        imageIcon.setImageResource(R.drawable.ic_film)
        textTitle.text = detailsItem.title
        textBody.text = String.format(
            getString(
                R.string.details_film,
                Date.from(ZonedDateTime.parse(detailsItem.text1).toInstant()),
                Date.from(ZonedDateTime.parse(detailsItem.text2).toInstant()),
                detailsItem.text3,
                detailsItem.text4,
                detailsItem.text5,
                detailsItem.text6,
                detailsItem.text7
            )
        )
    }

    private fun FragmentDetailsBinding.setStarshipData(details: DetailsItem) {
        imageIcon.setImageResource(R.drawable.ic_starship)
        textTitle.text = details.title
        textBody.text = String.format(
            getString(
                R.string.details_starship,
                Date.from(ZonedDateTime.parse(details.text1).toInstant()),
                Date.from(ZonedDateTime.parse(details.text2).toInstant()),
                details.text3,
                details.text4,
                details.text5,
                details.text6,
                details.text7,
                details.text8,
                details.text9,
                details.text10,
                details.text11,
                details.text12,
                details.text13,
                details.text14
            )
        )
    }

    private fun FragmentDetailsBinding.setVehicleData(detailsItem: DetailsItem) {
        imageIcon.setImageResource(R.drawable.ic_vehicle)
        textTitle.text = detailsItem.title
        textBody.text = String.format(
            getString(
                R.string.details_vehicle,
                Date.from(ZonedDateTime.parse(detailsItem.text1).toInstant()),
                Date.from(ZonedDateTime.parse(detailsItem.text2).toInstant()),
                detailsItem.text3,
                detailsItem.text4,
                detailsItem.text5,
                detailsItem.text6,
                detailsItem.text7,
                detailsItem.text8,
                detailsItem.text9,
                detailsItem.text10,
                detailsItem.text11,
                detailsItem.text12
            )
        )
    }

    private fun FragmentDetailsBinding.setSpecieData(detailsItem: DetailsItem) {
        imageIcon.setImageResource(R.drawable.ic_specie)
        textTitle.text = detailsItem.title
        textBody.text = String.format(
            getString(
                R.string.details_specie,
                Date.from(ZonedDateTime.parse(detailsItem.text1).toInstant()),
                Date.from(ZonedDateTime.parse(detailsItem.text2).toInstant()),
                detailsItem.text3,
                detailsItem.text4,
                detailsItem.text5,
                detailsItem.text6,
                detailsItem.text7,
                detailsItem.text8,
                detailsItem.text9,
                detailsItem.text10
            )
        )
    }

    private fun FragmentDetailsBinding.setPlanetData(detailsItem: DetailsItem) {
        imageIcon.setImageResource(R.drawable.ic_planet)
        textTitle.text = detailsItem.title
        textBody.text = String.format(
            getString(
                R.string.details_planet,
                Date.from(ZonedDateTime.parse(detailsItem.text1).toInstant()),
                Date.from(ZonedDateTime.parse(detailsItem.text2).toInstant()),
                detailsItem.text3,
                detailsItem.text4,
                detailsItem.text5,
                detailsItem.text6,
                detailsItem.text7,
                detailsItem.text8,
                detailsItem.text9,
                detailsItem.text10
            )
        )
    }

    override fun onStop() {
        detailsViewModel.clean()
        super.onStop()
    }

}