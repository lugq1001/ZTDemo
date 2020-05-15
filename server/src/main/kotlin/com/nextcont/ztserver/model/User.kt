package com.nextcont.ztserver.model

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index
import org.dizitart.no2.objects.Indices
import kotlin.random.Random.Default.nextInt


@Indices(Index(value = "username", type = IndexType.Fulltext))
data class User(
    @Id val id: String,
    val username: String,
    val password: String,
    val fullName: String,
    val age: Int,
    val job: String,
    val avatar: String,
    val isAdmin: Boolean
) {

    companion object {

        fun makeUsers(): Array<User> {
            val password = "111111"
            val userNames = listOf(
                "10000",
                "10001",
                "10002",
                "10003",
                "10004"
            )

            val fullNames = listOf(
                "张晓",
                "夏媛媛",
                "何丽",
                "陈诚",
                "王万里"
            )

            val jobs = listOf(
                "预备役少尉",
                "预备役上尉",
                "预备役少尉",
                "预备役中校",
                "预备役上校"
            )

            val avatars = listOf(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589189732741&di=60c3864e41173d0f2bc3d15db5545862&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201811%2F15%2F20181115184122_xvu8J.thumb.700_0.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589189810498&di=78543c063e9fdb54057043b78eb1ade7&imgtype=0&src=http%3A%2F%2Fpic3.58cdn.com.cn%2Fzhuanzh%2Fn_v28fd94202c8eb436f87dd6c2e67c8ce73.jpg%3Fw%3D750%26h%3D0",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589189828206&di=08f3bafac15f32e247a03a92f1f7751b&imgtype=0&src=http%3A%2F%2Fp0.meituan.net%2Fdpdeal%2Fb686c00b13705bd8b42b242f6c3b545331588.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589189845645&di=d68096fa3e4e0abdaff2019c36e03527&imgtype=0&src=http%3A%2F%2Fp.ssl.qhimg.com%2Ft01c2175c3870c7b6b0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1849689209,2982646862&fm=26&gp=0.jpg"
            )

            val users = mutableListOf<User>()
            for (index in 0..4) {
                val user = User(
                    "$index",
                    userNames[index],
                    password,
                    fullNames[index],
                    nextInt(18, 30),
                    jobs[index],
                    avatars[index],
                    index == 0
                )
                users.add(user)
            }
            return users.toTypedArray()
        }
    }
}