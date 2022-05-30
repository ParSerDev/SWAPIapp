package com.parserdev.swapiapp.presentation.swdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.parserdev.swapiapp.R
import com.parserdev.swapiapp.databinding.FragmentSwDetailsBinding
import com.parserdev.swapiapp.domain.model.SWCharacter
import com.parserdev.swapiapp.presentation.swdetails.viewmodel.SWDetailsViewModel
import com.parserdev.swapiapp.utils.collectLatestLifecycleFlow
import com.parserdev.swapiapp.utils.collectLifecycleFlow

class SWDetailsFragment : Fragment() {

    private val args: SWDetailsFragmentArgs by navArgs()

    private var _binding: FragmentSwDetailsBinding? = null
    private val binding get() = _binding!!

    private val swDetailsViewModel by activityViewModels<SWDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSwDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = args.url

        collectLatestLifecycleFlow(swDetailsViewModel.progressBarVisibilityFlow) { progressBarVisibility ->
            binding.progressBar.isVisible = progressBarVisibility
        }

        collectLifecycleFlow(swDetailsViewModel.getSWCharacterDataFlow(url = url)) { data ->
            if (data::class.java == SWCharacter::class.java) setCharacterData(character = data)
        }

    }

    private fun setCharacterData(character: SWCharacter) {
        binding.apply {
            textView1.text = String.format(getString(R.string.character_name, character.name))
            textView2.text = String.format(getString(R.string.character_gender, character.gender))
            textView3.text =
                String.format(getString(R.string.character_birth_year, character.birth_year))
            textView4.text = String.format(getString(R.string.character_height, character.height))
            textView5.text = String.format(getString(R.string.character_mass, character.mass))
            textView6.text =
                String.format(getString(R.string.character_hair_color, character.hair_color))
            textView7.text =
                String.format(getString(R.string.character_eye_color, character.eye_color))
            textView8.text =
                String.format(getString(R.string.character_skin_color, character.skin_color))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        swDetailsViewModel.toggleProgressBarVisibility()
    }
}