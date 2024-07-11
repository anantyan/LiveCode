package id.anantyan.livecode.ui.main.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import id.anantyan.livecode.common.UIState
import id.anantyan.livecode.data.model.Credits
import id.anantyan.livecode.data.model.MoviesDetail
import id.anantyan.livecode.databinding.FragmentDetailBinding
import id.anantyan.livecode.ui.detail.CasterAdapter
import id.anantyan.livecode.ui.detail.GenresAdapter
import javax.inject.Inject

/**
 * Created by Arya Rezza Anantya on 11/07/24.
 */
@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var casterAdapter: CasterAdapter
    @Inject
    lateinit var genresAdapter: GenresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
        bindView()
    }

    @SuppressLint("SetTextI18n")
    private fun bindObserver() {
        viewModel.detailPage.observe(viewLifecycleOwner) { state: UIState<MoviesDetail> ->
            when (state) {
                is UIState.Loading -> {
                    binding.progressHomeDetail.visibility = View.VISIBLE
                    binding.layoutHomeDetail.visibility = View.GONE
                }
                is UIState.Success -> {
                    binding.progressHomeDetail.visibility = View.GONE
                    binding.layoutHomeDetail.visibility = View.VISIBLE

                    binding.backdropPath.load(state.data?.backdropPath) {
                        crossfade(true)
                        size(ViewSizeResolver(binding.backdropPath))
                    }

                    binding.imgPosterPath.load(state.data?.posterPath) {
                        transformations(RoundedCornersTransformation(16F))
                        size(ViewSizeResolver(binding.imgPosterPath))
                    }

                    binding.materialToolbar.title = state.data?.title
                    binding.txtTitle.text = state.data?.title
                    binding.releaseDate.text = state.data?.releaseDate
                    binding.status.text = "Status : ${state.data?.status}"
                    binding.voteAverage.text = "${state.data?.voteAverage}/10"
                    binding.voteAverageStar.numStars = 5
                    binding.voteAverageStar.stepSize = 0.5F
                    binding.voteAverageStar.rating = (state.data?.voteAverage?.div(2))?.toFloat() ?: 0f
                    binding.homepage.text = if (state.data?.homepage.isNullOrEmpty()) "-" else state.data?.homepage

                    binding.homepage.setOnClickListener { _ ->
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(if (state.data?.homepage.isNullOrEmpty()) "not-found" else state.data?.homepage))
                        startActivity(browserIntent)
                    }

                    binding.runtime.text = "Durasi : ${state.data?.runtime} menit"
                    binding.overview.text = state.data?.overview

                    genresAdapter.submitList(state.data?.genres ?: emptyList())
                    binding.rvGenres.adapter = genresAdapter
                }
                is UIState.Error -> {
                    binding.progressHomeDetail.visibility = View.GONE
                    binding.layoutHomeDetail.visibility = View.GONE
                }
            }
        }

        viewModel.creditsPage.observe(viewLifecycleOwner) { state: UIState<Credits> ->
            when(state) {
                is UIState.Success -> {
                    casterAdapter.submitList(state.data?.cast ?: emptyList())
                    binding.rvCaster.adapter = casterAdapter
                    binding.progressCaster.visibility = View.GONE
                }
                is UIState.Loading -> {
                    binding.progressCaster.visibility = View.VISIBLE
                }
                is UIState.Error -> {
                    binding.progressCaster.visibility = View.GONE
                }
            }
        }

        viewModel.detailWithCreditsPage(args.movieId.toString())
    }

    private fun bindView() {
        binding.materialToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.rvCaster.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = true
            adapter = casterAdapter
        }

        binding.rvGenres.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = true
            adapter = genresAdapter
        }

        casterAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        genresAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}