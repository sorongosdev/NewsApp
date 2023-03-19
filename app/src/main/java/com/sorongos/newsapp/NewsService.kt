package com.sorongos.newsapp

import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("SectionRssFeed.do?sectionId=01&plink=RSSREADER")
    fun mainFeed(): Call<NewsRss>
}