package id.anantyan.livecode.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.anantyan.livecode.common.calculateSpanCount
import id.anantyan.livecode.data.model.ResultsItem
import id.anantyan.livecode.databinding.FragmentHomeBinding
import id.anantyan.livecode.ui.home.HomeAdapter
import id.anantyan.livecode.ui.home.HomeLoadStateAdapter
import id.anantyan.livecode.ui.home.HomeSearchAdapter
import id.anantyan.livecode.ui.home.HomeViewModel
import javax.inject.Inject

/**
 * Created by Arya Rezza Anantya on 11/07/24.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var homeAdapter: HomeAdapter
    @Inject
    lateinit var homeSearchAdapter: HomeSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
        bindView()
    }

    private fun bindObserver() {
        viewModel.homePage.observe(viewLifecycleOwner) { state: PagingData<ResultsItem> ->
            homeAdapter.submitData(lifecycle, state)
        }

        viewModel.searchPage.observe(viewLifecycleOwner) { state: PagingData<ResultsItem> ->
            homeSearchAdapter.submitData(lifecycle, state)
        }
    }

    private fun bindView() {
        val loadStateHeader = HomeLoadStateAdapter { homeAdapter.retry() }
        val loadStateFooter = HomeLoadStateAdapter { homeAdapter.retry() }
        val concatAdapter = homeAdapter.withLoadStateHeaderAndFooter(
            header = loadStateHeader,
            footer = loadStateFooter
        )

        val loadStateHeader1 = HomeSearchStateAdapter { homeSearchAdapter.retry() }
        val loadStateFooter1 = HomeSearchStateAdapter { homeSearchAdapter.retry() }
        val concatAdapter1 = homeSearchAdapter.withLoadStateHeaderAndFooter(header = loadStateHeader1, footer = loadStateFooter1)

        binding.searchBar.isDefaultScrollFlagsEnabled = false
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            viewModel.searchPage(binding.searchView.text.toString())
            false
        }

        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), requireActivity().windowManager.calculateSpanCount())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = concatAdapter1
        binding.recyclerView.isNestedScrollingEnabled = false

        homeSearchAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        homeSearchAdapter.addLoadStateListener { loadState ->
            binding.progressSearch.isVisible = loadState.source.refresh is LoadState.Loading
            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
        }
        homeSearchAdapter.setOnItemClickListener { position, item ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment((item.id ?: 0).toLong())
            findNavController().navigate(action)
        }

        binding.rvCategory.setHasFixedSize(false)
        binding.rvCategory.layoutManager = GridLayoutManager(requireContext(), requireActivity().windowManager.calculateSpanCount())
        binding.rvCategory.itemAnimator = DefaultItemAnimator()
        binding.rvCategory.isNestedScrollingEnabled = false
        binding.rvCategory.adapter = concatAdapter

        homeAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        homeAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.rvCategory.isVisible = loadState.source.refresh is LoadState.NotLoading
        }
        homeAdapter.setOnItemClickListener { position, item ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment((item.id ?: 0).toLong())
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}