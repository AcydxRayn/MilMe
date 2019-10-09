package com.ks49.milme.data

data class User(
    // TODO-AGM: Make sure to use Firebase user ID and assign it to the Apps User ID field
    val id: String,
    val name: String,
    val branch: String,
    val mos: String,
    val yearsOfService: Int,
    val isActiveDuty: Boolean
    ) {
    constructor() : this("", "", "", "", 0,false     )
}