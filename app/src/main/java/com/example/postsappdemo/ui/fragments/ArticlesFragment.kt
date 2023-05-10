package com.example.postsappdemo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.postsappdemo.R
import com.example.postsappdemo.adapter.ArticlesAdapter
import com.example.postsappdemo.databinding.FragmentArticlesBinding
import com.example.postsappdemo.state.MainScreenState
import com.example.postsappdemo.viewmodel.ArticlesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesFragment : Fragment() {
    private lateinit var fgBinding: FragmentArticlesBinding
    private val articleAdapter = ArticlesAdapter()
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
        lifecycleScope.launch {
            viewModel.screenState.observe(viewLifecycleOwner) {
                when (it) {
                    is MainScreenState.Success -> {
                        fgBinding.successLayout.visibility = View.VISIBLE
                        fgBinding.errorLayout.visibility = View.GONE
                        fgBinding.progressBar.visibility = View.GONE
                        viewModel.articles.observe(viewLifecycleOwner) {
                            articleAdapter!!.submitList(it?.articles)
                            fgBinding.articlesRv.adapter = articleAdapter
                        }
                    }
                    is MainScreenState.Error -> {
                        fgBinding.successLayout.visibility = View.GONE
                        fgBinding.errorLayout.visibility = View.VISIBLE
                        fgBinding.progressBar.visibility = View.GONE
                        fgBinding.tvError.text = it.message
                    }
                    is MainScreenState.Loading -> {
                        fgBinding.successLayout.visibility = View.GONE
                        fgBinding.errorLayout.visibility = View.GONE
                        fgBinding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
            viewModel.getArticles()
        }
    }


}