package com.example.restdatabase.dto

data class BookDTO (
    var categoryID : Int = 0,
    var name : String = "",
    var author : String = "",
    var pageNumber : Int = -1,
    var stock : Int = 1,
)