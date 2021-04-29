package com.examp.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.examp.artbooktesting.R
import com.examp.artbooktesting.databinding.FragmentArtDetailsBinding
import com.examp.artbooktesting.util.Status
import com.examp.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
        val glide : RequestManager
)
    : Fragment(R.layout.fragment_art_details) {

    lateinit var viewModel : ArtViewModel
    private var fragmentBinding : FragmentArtDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding =binding

        subscribeToObservers()

        binding.artImageView.setOnClickListener {

            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToİmageApiFragment())
        }

        // geri tuşuyla geri gelme
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        // yeni art kaydetme işlemi
        binding.saveButon.setOnClickListener {
            viewModel.makeArt(binding.nameText.text.toString(),binding.artistText.text.toString(),binding.yearText.text.toString())
        }
    }

    private fun subscribeToObservers() {
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url ->
            fragmentBinding?.let {
                glide.load(url).into(it.artImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertMsg()

                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()

                }

                Status.LOADING -> {

                }
            }
        })

    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}