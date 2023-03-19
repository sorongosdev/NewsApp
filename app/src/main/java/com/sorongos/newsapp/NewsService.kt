package com.sorongos.newsapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("rss?hl=ko&gl=KR&ceid=KR%3Ako")
    fun mainFeed(): Call<NewsRss>

    @GET("rss/topics/CAAqIQgKIhtDQkFTRGdvSUwyMHZNRFp4WkRNU0FtdHZLQUFQAQ?hl=ko&gl=KR&ceid=KR%3Ako")
    fun koreaNews(): Call<NewsRss>

    @GET("rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx1YlY4U0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako")
    fun globalNews(): Call<NewsRss>

    @GET("rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx6TVdZU0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako")
    fun businessNews(): Call<NewsRss>

    @GET("rss/topics/CAAqKAgKIiJDQkFTRXdvSkwyMHZNR1ptZHpWbUVnSnJieG9DUzFJb0FBUAE?hl=ko&gl=KR&ceid=KR%3Ako")
    fun scienceNews(): Call<NewsRss>

    @GET("rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako")
    fun sportsNews(): Call<NewsRss>

    @GET("rss/search?hl=ko&gl=KR&ceid=KR%3Ako")
    fun search(@Query("q") query: String): Call<NewsRss>

    /**sbs image load*/
//    @GET("SectionRssFeed.do?sectionId=01&plink=RSSREADER")
//    fun politicsNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=01&plink=RSSREADER")
//    fun politicsNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=02&plink=RSSREADER")
//    fun economicsNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=03&plink=RSSREADER")
//    fun socialNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=07&plink=RSSREADER")
//    fun lifeNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=08&plink=RSSREADER")
//    fun globalNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=14&plink=RSSREADER")
//    fun entertainNews(): Call<NewsRss>
//    @GET("SectionRssFeed.do?sectionId=09&plink=RSSREADER")
//    fun sportsNews(): Call<NewsRss>
}