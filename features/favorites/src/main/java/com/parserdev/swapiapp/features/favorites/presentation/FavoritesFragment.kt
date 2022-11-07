package com.parserdev.swapiapp.features.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.parserdev.swapiapp.features.favorites.databinding.FragmentFavoritesBinding
import com.parserdev.swapiapp.features.favorites.di.FavoritesComponent
import com.parserdev.swapiapp.features.favorites.di.FavoritesComponentProvider

class FavoritesFragment : Fragment() {

    lateinit var favoritesComponent: FavoritesComponent

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectFavoritesComponent()
    }

    private fun injectFavoritesComponent() {
        favoritesComponent =
            (activity?.application as FavoritesComponentProvider).provideFavoritesComponent()
        favoritesComponent.inject(this)
    }

}