package com.capstone.project.tourify

import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem

object DataDummy {

    fun generateDummyArticleResponse(): List<ArticlesResponseItem> {
        val items: MutableList<ArticlesResponseItem> = arrayListOf()
        for (i in 0..100) {
            val article = ArticlesResponseItem(
                i.toString(),
                "Rekomendasi Kuliner Pontianak Paling Enak dan Legendaris",
                "Pontianak",
                "https://phinemo.com/wp-content/uploads/2017/12/23594516_1641361239236641_7260894878224089088_n.jpg",
                "Terkenal sebagai kota khatulistiwa, Pontianak seakan menjadi magnet tersendiri bagi wisatawan. Keragaman budaya Dayak, Melayu dan Tionghoa menambah keistimewaan Kota Khatulistiwa ini di mata wisatawan. Bahkan karena keragaman budaya itu,  kuliner Pontianak juga populer sangat variatif.",
                "https://phinemo.com/kuliner-pontianak-enak-dan-legendaris/"
            )
            items.add(article)
        }
        return items
    }
}