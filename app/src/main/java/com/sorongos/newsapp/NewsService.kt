package com.sorongos.newsapp

import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("SectionRssFeed.do?sectionId=01&plink=RSSREADER")
    fun politicsNews(): Call<NewsRss>

    @GET("SectionRssFeed.do?sectionId=02&plink=RSSREADER")
    fun economicsNews(): Call<NewsRss>
    @GET("SectionRssFeed.do?sectionId=03&plink=RSSREADER")
    fun socialNews(): Call<NewsRss>
    @GET("SectionRssFeed.do?sectionId=07&plink=RSSREADER")
    fun lifeNews(): Call<NewsRss>
    @GET("SectionRssFeed.do?sectionId=08&plink=RSSREADER")
    fun globalNews(): Call<NewsRss>
    @GET("SectionRssFeed.do?sectionId=14&plink=RSSREADER")
    fun entertainNews(): Call<NewsRss>
    @GET("SectionRssFeed.do?sectionId=09&plink=RSSREADER")
    fun sportsNews(): Call<NewsRss>
}