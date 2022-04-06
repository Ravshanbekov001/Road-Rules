package com.example.yolharakatiqoidalari.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Rules : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "rule_name")
    var ruleName: String? = null

    @ColumnInfo(name = "about_rule")
    var aboutRule: String? = null

    @ColumnInfo(name = "type_of_rule")
    var typeOfRule: String? = null

    @ColumnInfo(name = "image_path")
    var imagePath: String? = null

    @ColumnInfo(name = "favourite")
    var favourite: String? = null

    constructor()

    constructor(
        id: Int?,
        ruleName: String?,
        aboutRule: String?,
        typeOfRule: String?,
        imagePath: String?,
        favourite: String?,
    ) {
        this.id = id
        this.ruleName = ruleName
        this.aboutRule = aboutRule
        this.typeOfRule = typeOfRule
        this.imagePath = imagePath
        this.favourite = favourite
    }

    constructor(
        ruleName: String?,
        aboutRule: String?,
        typeOfRule: String?,
        imagePath: String?,
        favourite: String,
    ) {
        this.ruleName = ruleName
        this.aboutRule = aboutRule
        this.typeOfRule = typeOfRule
        this.imagePath = imagePath
        this.favourite = favourite
    }

    constructor(ruleName: String?, aboutRule: String?, typeOfRule: String?, imagePath: String?) {
        this.ruleName = ruleName
        this.aboutRule = aboutRule
        this.typeOfRule = typeOfRule
        this.imagePath = imagePath
    }
}