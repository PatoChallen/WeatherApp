package com.patofernandez.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.binding.FragmentDataBindingComponent
import com.patofernandez.weatherapp.databinding.HomeFragmentBinding
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.utils.autoCleared
import com.patofernandez.weatherapp.view.HomeFragment
import com.patofernandez.weatherapp.view.WeatherViewModel
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(HomeFragment.TAG, "onActivityCreated")
//        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
//        btnSelectLocation.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_selectLocationFragment)
//        }
//        viewModel.getFavoriteLocations().observe(requireActivity(), Observer {
//            mFavoriteLocations.adapter = FavoriteLocationsAdapter(it, object : FavoriteLocationsAdapter.OnFavoriteActionListener {
//                override fun onFavoriteLocationClick(favoriteLocation: CurrentWeatherApiResponse) {
//                    viewModel.setCurrentWeather(favoriteLocation)
//                    findNavController().navigate(R.id.action_homeFragment_to_locationWeatherFragment)
//                }
//
//                override fun onFavoriteLocationDelete(favoriteLocation: CurrentWeatherApiResponse) {
//                    viewModel.removeFavoriteLocation(favoriteLocation)
//                }
//            })
//        })
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<HomeFragmentBinding>()

    var adapter by autoCleared<FavoriteLocationsAdapter>()

    val weatherViewModel: WeatherViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false,
            dataBindingComponent
        )

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.lifecycleOwner = viewLifecycleOwner
//        initRecyclerView()
//        val rvAdapter = RepoListAdapter(
//            dataBindingComponent = dataBindingComponent,
//            appExecutors = appExecutors,
//            showFullName = true
//        ) { repo ->
//            findNavController().navigate(
//                SearchFragmentDirections.showRepo(repo.owner.login, repo.name)
//            )
//        }
//        binding.query = weatherViewModel.query
//        binding.repoList.adapter = rvAdapter
//        adapter = rvAdapter
//
//        initSearchInputListener()
//
//        binding.callback = object : RetryCallback {
//            override fun retry() {
//                weatherViewModel.refresh()
//            }
//        }
//    }
//
//    private fun initSearchInputListener() {
//        binding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                doSearch(view)
//                true
//            } else {
//                false
//            }
//        }
//        binding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                doSearch(view)
//                true
//            } else {
//                false
//            }
//        }
//    }
//
//    private fun doSearch(v: View) {
//        val query = binding.input.text.toString()
//        // Dismiss keyboard
//        dismissKeyboard(v.windowToken)
//        weatherViewModel.setQuery(query)
//    }
//
    private fun initRecyclerView() {

//        binding.repoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                val lastPosition = layoutManager.findLastVisibleItemPosition()
//                if (lastPosition == adapter.itemCount - 1) {
//                    weatherViewModel.loadNextPage()
//                }
//            }
//        })
        binding.searchResult = weatherViewModel.results
        weatherViewModel.results.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
        })

        weatherViewModel.loadMoreStatus.observe(viewLifecycleOwner, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                binding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
//
//    private fun dismissKeyboard(windowToken: IBinder) {
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//        imm?.hideSoftInputFromWindow(windowToken, 0)
//    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
