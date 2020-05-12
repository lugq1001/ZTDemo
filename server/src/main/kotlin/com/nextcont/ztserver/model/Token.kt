package com.nextcont.ztserver.model

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index
import org.dizitart.no2.objects.Indices

@Indices(Index(value = "userId", type = IndexType.Unique))
data class Token(
    @Id val id: String,
    val userId: String,
    val createAt: Long
)