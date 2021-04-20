package com.examp.artbooktesting.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examp.artbooktesting.R
import com.examp.artbooktesting.adapter.ArtRecyclerAdapter
import com.examp.artbooktesting.databinding.FragmentArtsBinding
import com.examp.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtsFragment @Inject constructor(
    val artRecyclerAdapter: ArtRecyclerAdapter
): Fragment(R.layout.fragment_arts){

    lateinit var viewModel : ArtViewModel
    private var fragmentBinding : FragmentArtsBinding? = null

    // swipe
    private val swipeCallback = object  : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            viewModel.deleteArt(selectedArt)
        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentArtsBinding.bind(view)
        fragmentBinding = binding

        subscribeToObservers()

        binding.recyclerViewArt.adapter = artRecyclerAdapter
        binding.recyclerViewArt.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerViewArt)

        binding.fab.setOnClickListener {
            findNavController().navigate(ArtsFragmentDirections.actionArtsFragmentToArtDetailsFragment())
        }

    }

    private fun subscribeToObservers() {
        viewModel.artList.observe(viewLifecycleOwner, Observer {
            artRecyclerAdapter.arts = it
        })
    }


    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}