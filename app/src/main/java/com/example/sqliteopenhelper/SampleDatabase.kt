package com.example.sqliteopenhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.AccessControlContext

private const val DB_NAME = "SampleDatabase"
private const val DB_VERSION = 1

class SampleDBOpenHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        // テーブルの作成
        db?.execSQL("CREATE TABLE texts ( " +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT, "  +
        "text TEXT NOT NULL, " +
        " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun queryTexts(context: Context) : List<String> {
    // 読み込み用のデータベースを開く
    val database = SampleDBOpenHelper(context).readableDatabase
    // データベースから全権検索する
    val cursor = database.query(
        "texts", null, null, null, null, null, "created_at DESC")

    val texts = mutableListOf<String>()
    cursor.use {
        // カーソルで順次処理していく
        while(cursor.moveToNext()){
            // 保存されているテキストを得る
            val text = cursor.getString(cursor.getColumnIndex("text"))
            texts.add(text)
        }
    }
    database.close()
    return texts
}

fun insertText(context: Context, text: String) {
    // 書き込み可能なデータベースを開く
    val database = SampleDBOpenHelper(context).writableDatabase

    // 挿入するレコード
    database.use { db ->
        val record = ContentValues().apply {
            put("text", text)
        }
        // データベースに挿入する
        db.insert("texts", null, record)
    }
}