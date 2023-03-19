package com.sorongos.newsapp

/**html 형식으로 되어있음*/
data class NewsModel(
    val title: String,
    val link: String,
    var imageUrl: String? = null
)

/**NewsItem(xml) -> NewsModel(html) mapping을 위한 확장
 * NewsItem 형만 사용 가능한 함수*/
fun List<NewsItem>.transform(): List<NewsModel> {
    return this.map {
        NewsModel(
            title = it.title ?: "",
            link = it.link ?: "",
            imageUrl = null
        )
    }
}
