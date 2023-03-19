package com.sorongos.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
//        .baseUrl("https://news.google.com/")
        .baseUrl("https://news.sbs.co.kr/news/")
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

        newsAdapter = NewsAdapter()
        val newsService = retrofit.create(NewsService::class.java)

        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        binding.politicsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.politicsChip.isChecked = true

            newsService.politicsNews().submitList()
        }
        binding.economicsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.economicsChip.isChecked = true

            newsService.economicsNews().submitList()
        }
        binding.socialChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.socialChip.isChecked = true

            newsService.socialNews().submitList()
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
        binding.entertainChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.entertainChip.isChecked = true

            newsService.entertainNews().submitList()
        }
        binding.lifeChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.lifeChip.isChecked = true

            newsService.lifeNews().submitList()
        }

        newsService.politicsNews().submitList()
    }

    private fun Call<NewsRss>.submitList(){
        //call newsRss
        enqueue(object : Callback<NewsRss> {
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                Log.e("MainActivity", "${response.body()?.channel?.items}")

                val list =
                    response.body()?.channel?.items.orEmpty().transform() //newsItem -> newsmodel
                newsAdapter.submitList(list)

                //list와 listAdapter의 index가 동일
                list.forEachIndexed { index, news ->
                    //not allowed to control network in the main thread.
                    Thread {
                        try {
                            val jsoup = Jsoup.connect(news.link).get() // link, document type

                            //meta 태그를 찾고 og:~ property를 찾음
                            val elements = jsoup.select("meta[property^=og:]")
                            Log.e("Main","elements : $elements")

                            val ogImageNode = elements.find { node ->
                                node.attr("property") == "og:image"
                            }
                            Log.e("Main","ogImageNode : $ogImageNode")

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