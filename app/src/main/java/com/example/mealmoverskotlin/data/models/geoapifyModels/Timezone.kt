package com.example.mealmoverskotlin.data.models.geoapifyModels

data class Timezone(
    var abbreviation_DST: String? = null,
    var abbreviation_STD: String? = null,
    var name: String? = null,
    var offset_DST: String? = null,
    var offset_DST_seconds: Int? = null,
    var offset_STD: String? = null,
    var offset_STD_seconds: Int? = null
)