package com.example.beeriq.data.local.beerDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BeerDatabaseDao {

    @Insert
    suspend fun insertBeer(beer: Beer)

    @Query("""
        SELECT * FROM beer_table
        JOIN beer_table_fts ON beer_table_fts.beer_full_name == beer_table.beer_full_name
        WHERE beer_table_fts.beer_full_name MATCH :fullName
    """)
    suspend fun getBeerFullName(fullName: String): List<Beer>

    @Query("""
        SELECT * FROM beer_table
        JOIN beer_table_fts ON beer_table_fts.rowid = beer_table.id
        WHERE beer_table_fts.beer_full_name MATCH :query
    """)
    suspend fun searchBeer(query: String): List<Beer>

    @Query("""
        SELECT * FROM beer_table
        WHERE style LIKE :style
    """)
    suspend fun searchStyle(style: String): List<Beer>
}