package com.example.postsappdemo.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Article
import com.example.postsappdemo.R
import com.example.postsappdemo.adapter.ArticlesAdapter
import com.example.postsappdemo.databinding.FragmentArticlesBinding
import com.example.postsappdemo.state.MainScreenState
import com.example.postsappdemo.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.postsappdemo.viewmodel.ArticlesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesFragment : Fragment() {
    private lateinit var fgBinding: FragmentArticlesBinding
    private var articleAdapter: ArticlesAdapter? = null

    //    private var isLoadingEmails = false
    private val viewModel: ArticlesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fgBinding = FragmentArticlesBinding.inflate(inflater, container, false)
        return fgBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fgBinding.articlesRv
        articleAdapter = ArticlesAdapter(onClicked)
        lifecycleScope.launch {
            viewModel.readNews.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Log.d(TAG, "readCachedData:() ")
                    articleAdapter!!.submitList(it)
                } else {
                    requestApiData()
                }
            }
        }
        fgBinding.articlesRv.adapter = articleAdapter
        fgBinding.articlesRv.addOnScrollListener(this.scrollListener)
    }


    private fun requestApiData() {
        lifecycleScope.launch {
            viewModel.screenState.observe(viewLifecycleOwner) {
                when (it) {
                    is MainScreenState.Success -> {
                        fgBinding.apply {
                            successLayout.visibility = View.VISIBLE
                            errorLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            isLoading = false
                        }
                        viewModel.articles.observe(viewLifecycleOwner) {
                            viewModel.insertRecipes(it!!.articles)
                            articleAdapter!!.submitList(it!!.articles)
                            val totalpages = it.totalResults / QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.newsPage == totalpages.toInt()
                            if (isLastPage) {
                                fgBinding.articlesRv.setPadding(0, 0, 0, 0)
                            }
                        }
                    }
                    is MainScreenState.Error -> {
                        fgBinding.apply {
                            successLayout.visibility = View.GONE
                            errorLayout.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            tvError.text = it.message
                            isLoading = false
                        }
                    }
                    is MainScreenState.Loading -> {
                        fgBinding.apply {
                            successLayout.visibility = View.GONE
                            errorLayout.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                            isLoading = true
                        }

                    }
                }
            }
        }
        viewModel.getArticlesFromRemote()
    }

    private val onClicked = object : ArticlesAdapter.OnItemClickListener {
        override fun onClicked(article: Article) {
            val articleTitle = article.title
            val articleDes = article.description
            val articleImg = article.urlToImage
            val action1 = ArticlesFragmentDirections.actionArticlesFragmentToArticleDetFragment(
                articleTitle, articleDes, articleImg
            )
            findNavController().navigate(action1)

        }
    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getArticlesFromRemote()
                isScrolling = false
            } else {
                fgBinding.articlesRv.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }


    }
}



