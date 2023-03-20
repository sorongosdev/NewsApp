package com.sorongos.newsapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.sorongos.newsapp.databinding.ActivityMainBinding
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://news.google.com/")
//        .baseUrl("https://news.sbs.co.kr/news/")
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false) //mapping 안된 xml에 대해 exception을 날리지 말아줘!
                    .build()
            )
        ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter { url ->
            startActivity(
                Intent(this, WebViewActivity::class.java).apply {
                    putExtra("url", url)
                }
            )
        }
        val newsService = retrofit.create(NewsService::class.java)

        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        binding.mainFeedChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.mainFeedChip.isChecked = true

            newsService.mainFeed().submitList()
        }
        binding.koreaChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.koreaChip.isChecked = true

            newsService.koreaNews().submitList()
        }
        binding.globalChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.globalChip.isChecked = true

            newsService.globalNews().submitList()
        }
        binding.sportsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.sportsChip.isChecked = true

            newsService.sportsNews().submitList()
        }

        binding.scienceChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.scienceChip.isChecked = true

            newsService.scienceNews().submitList()
        }

        binding.businessChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.businessChip.isChecked = true

            newsService.businessNews().submitList()
        }

        //자판의 엔터버튼 리스너 - 검색
        binding.searchTextInputEditText.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.chipGroup.clearCheck()

                //remove focus
                binding.searchTextInputEditText.clearFocus()

                //키보드 닫아줌
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)

                newsService.search(binding.searchTextInputEditText.text.toString()).submitList()

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        //default
        newsService.mainFeed().submitList()
    }

    private fun Call<NewsRss>.submitList() {
        //call newsRss
        enqueue(object : Callback<NewsRss> {
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                Log.e("MainActivity", "${response.body()?.channel?.items}")

                val list =
                    response.body()?.channel?.items.orEmpty().transform() //newsItem -> newsmodel
                newsAdapter.submitList(list)

                //검색된 리스트가 없다면 결과없음창 띄움민
                binding.notFoundView.isVisible = list.isEmpty()

                //list와 listAdapter의 index가 동일
                list.forEachIndexed { index, news ->
                    //not allowed to control network in the main thread.
                    Thread {
                        try {
                            val jsoup = Jsoup.connect(news.link).get() // link, document type

                            //meta 태그를 찾고 og:~ property를 찾음
                            val elements = jsoup.select("meta[property^=og:]")
                            Log.e("Main", "elements : $elements")

                            val ogImageNode = elements.find { node ->
                                node.attr("property") == "og:image"
                            }
                            Log.e("Main", "ogImageNode : $ogImageNode")

                            news.imageUrl = ogImageNode?.attr("content")
                            Log.e("MainActivity", "imageUrl: ${news.imageUrl}")

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        runOnUiThread {
                            newsAdapter.notifyItemChanged(index)
                        }
                    }.start()
                }
            }

            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}