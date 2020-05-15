package com.nextcont.ztserver.model

import org.dizitart.no2.objects.Id

data class Evaluation(
    @Id val id: Int,
    val title: String,
    val name: String,
    val score: String,
    val gender: Int,
    val createAt: String,
    val no: String,
    val age: Int
) {
    companion object {

        fun makeExamines(): Array<Evaluation> {
            val no = 2020003234331
            val examines = listOf(
                Evaluation(0, "2分钟俯卧撑", "蒋进如", "7.8", 0, "2020-05-03", "${no + 1}", 23),
                Evaluation(1,"5英里跑", "张晟涵", "3.5", 1, "2020-01-04", "${no + 2}", 18),
                Evaluation(2,"2分钟仰卧起坐", "王可岚", "9.7", 1, "2020-04-21", "${no + 3}", 28),
                Evaluation(3,"俯卧撑", "曹讯波", "6.4", 0, "2020-03-14", "${no + 4}", 25),
                Evaluation(4,"负重训练", "宋依伊", "8.1", 1, "2020-02-16", "${no + 5}", 21),
                Evaluation(5,"野外求生", "卢邦楠", "6.9", 0, "2020-1-02", "${no + 6}", 19),
                Evaluation(6,"单杠引体向上", "吴军卓", "4.7", 0, "2020-04-08", "${no + 7}", 24),
                Evaluation(7,"3公里越野跑", "李初云", "10", 1, "2020-05-28", "${no + 8}", 29),
                Evaluation(8,"跳绳", "吴夏曼", "7.2", 1, "2020-01-30", "${no + 9}", 30)
            )

            return examines.toTypedArray()
        }
    }
}