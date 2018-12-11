package com.beantownloner.popularmoviesstg2.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.beantownloner.popularmoviesstg2.objects.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static AppDatabase sInstance;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("drop table if exists favoritemovie");
            database.execSQL("CREATE TABLE IF NOT EXISTS `movie` (" +
                    "   'id' TEXT NOT NULL , " +
                    "   'title' TEXT , " +
                    "    'posterURL' TEXT , " +
                    "   'backdropURL' TEXT , " +
                    "    'releaseDate' TEXT , " +
                    "    'rating' TEXT , " +
                    "    'overview' TEXT , PRIMARY KEY(`id`) " +
                    ")");

        }
    };

    public static AppDatabase getInstance(Context context) {

        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .build();


            }
        }

        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();
}
