//package com.example.yolharakatiqoidalari.database
//
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import com.example.yolharakatiqoidalari.models.Category
//import com.example.yolharakatiqoidalari.models.Rules
//
//class DataBase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
//
//    companion object {
//        const val DB_NAME = "road_sign_db"
//        const val DB_VERSION = 1
//
//        const val TABLE_CATEGORIES = "categories"
//        const val TABLE_RULES = "rules"
//
//        const val ID = "id"
//        const val NAME = "name"
//        const val ABOUT_RULE = "about_rule"
//        const val IMAGE_PATH = "imagePath"
//        const val FAVOURITE = "favourite"
//        const val CATEGORY_ID = "category_id"
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        val categoriesQuery = "create table $TABLE_CATEGORIES (" +
//                "$ID integer not null primary key autoincrement unique," +
//                "$NAME text not null)"
//        val rulesQuery = "create table $TABLE_RULES (" +
//                "$ID integer not null primary key autoincrement unique," +
//                "$NAME text not null," +
//                "$ABOUT_RULE text not null," +
//                "$IMAGE_PATH text not null," +
//                "$FAVOURITE integer not null," +
//                "$CATEGORY_ID integer not null, " +
//                "foreign key ($ID) references $TABLE_CATEGORIES ($CATEGORY_ID))"
//        db?.execSQL(categoriesQuery)
//        db?.execSQL(rulesQuery)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
//
//    fun addCategory(category: Category) {
//        val contentValues = ContentValues()
//        contentValues.put(NAME, category.name)
//        writableDatabase.insert(TABLE_CATEGORIES, null, contentValues)
//        writableDatabase.close()
//    }
//
//    fun getAllCategories(): ArrayList<Category> {
//        val list = ArrayList<Category>()
//        val query = "select * from $TABLE_CATEGORIES"
//        val cursor = readableDatabase.rawQuery(query, null)
//
//        if (cursor.moveToFirst()) {
//            do {
//                val category = Category(
//                    cursor.getInt(0),
//                    cursor.getString(1)
//                )
//                list.add(category)
//            } while (cursor.moveToNext())
//        }
//
//        cursor.close()
//        return list
//    }
//
//    fun addRule(rules: Rules) {
//        val contentValues = ContentValues()
//        contentValues.put(NAME, rules.ruleName)
//        contentValues.put(ABOUT_RULE, rules.aboutRule)
//        contentValues.put(IMAGE_PATH, rules.imagePath)
//        contentValues.put(FAVOURITE, if (rules.favourite) 1 else 0)
//        contentValues.put(CATEGORY_ID, rules.category.id)
//        writableDatabase.insert(TABLE_RULES, null, contentValues)
//        writableDatabase.close()
//    }
//
//    fun editRule(rules: Rules) {
//        val contentValues = ContentValues()
//        contentValues.put(ID, rules.id)
//        contentValues.put(NAME, rules.ruleName)
//        contentValues.put(ABOUT_RULE, rules.aboutRule)
//        contentValues.put(IMAGE_PATH, rules.imagePath)
//        contentValues.put(FAVOURITE, if (rules.favourite) 1 else 0)
//        contentValues.put(CATEGORY_ID, rules.category.id)
//        writableDatabase.update(TABLE_RULES, contentValues, "$ID = ?", arrayOf("${rules.id}"))
//        writableDatabase.close()
//    }
//
//    fun removeRule(rules: Rules) {
//        val database = writableDatabase
//        database.delete(TABLE_RULES, "$ID = ?", arrayOf("${rules.id}"))
//        database.close()
//    }
//
//    fun getAllRules(): HashMap<String, ArrayList<Rules>> {
//        val rulesHashMap = HashMap<String, ArrayList<Rules>>()
//        val categoriesList = getAllCategories()
//
//        for (category in categoriesList) {
//            val cursor = readableDatabase.query(
//                TABLE_RULES,
//                arrayOf(
//                    ID,
//                    NAME,
//                    ABOUT_RULE,
//                    IMAGE_PATH,
//                    FAVOURITE,
//                    CATEGORY_ID
//                ),
//                "$CATEGORY_ID = ?",
//                arrayOf("${category.id}"),
//                null, null, null
//            )
//
//            val list = ArrayList<Rules>()
//            if (cursor.moveToFirst()) {
//                do {
//                    val rules = Rules(
//                        cursor.getInt(0),
//                        cursor.getString(1),
//                        cursor.getString(2),
//                        cursor.getString(3),
//                        cursor.getInt(4) == 1,
//                        category
//                    )
//                    list.add(rules)
//                } while (cursor.moveToNext())
//            }
//
//            cursor.close()
//            rulesHashMap[category.name] = list
//        }
//
//        return rulesHashMap
//    }
//
//    fun getFavouriteRules(): ArrayList<Rules> {
//        val list = ArrayList<Rules>()
//        val cursor = readableDatabase.query(
//            TABLE_RULES,
//            arrayOf(
//                ID,
//                NAME,
//                ABOUT_RULE,
//                IMAGE_PATH,
//                FAVOURITE,
//                CATEGORY_ID
//            ),
//            "$FAVOURITE = ?",
//            arrayOf("1"),
//            null, null, null
//        )
//
//        if (cursor.moveToFirst()) {
//            do {
//                val rules = Rules(
//                    cursor.getInt(0),
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getString(3),
//                    cursor.getInt(4) == 1,
//                    getCategoriesById(cursor.getInt(0))
//                )
//                list.add(rules)
//            } while (cursor.moveToNext())
//        }
//
//        cursor.close()
//        return list
//    }
//
//    fun getCategoriesById(categoryId: Int): Category {
//        val cursor = readableDatabase.query(
//            TABLE_CATEGORIES,
//            arrayOf(
//                ID,
//                NAME
//            ),
//            "$ID = ?",
//            arrayOf(categoryId.toString()),
//            null, null, null
//        )
//
//        cursor.moveToFirst()
//        val category = Category(
//            cursor.getInt(0),
//            cursor.getString(1)
//        )
//
//        cursor.close()
//        return category
//    }
//}