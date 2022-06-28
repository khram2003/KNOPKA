package com.example.Auth

import kotlinx.serialization.Serializable

@Serializable
data class Knopka(val name : String ="", val style : String ="", var pushes : Long = 0, var id : Long = 0, var authorId : Long = 0/* knopka style*/)

