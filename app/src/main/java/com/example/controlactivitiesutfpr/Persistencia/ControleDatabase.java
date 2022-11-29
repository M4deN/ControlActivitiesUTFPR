package com.example.controlactivitiesutfpr.Persistencia;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.controlactivitiesutfpr.Model.Campus;
import com.example.controlactivitiesutfpr.Model.Controle;
import com.example.controlactivitiesutfpr.R;

import java.util.concurrent.Executors;

@Database(entities = {Controle.class, Campus.class}, version = 1, exportSchema = false)@TypeConverters({Converters.class})
public abstract class ControleDatabase extends RoomDatabase {

    public abstract ControleDAO controleDAO();

    public abstract CampusDao campusDao();

    private static ControleDatabase instance;

    public static ControleDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (ControleDatabase.class) {
                if (instance == null) {
                    RoomDatabase.Builder  builder = Room.databaseBuilder(context, ControleDatabase.class, "controle.db");

                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    carregaCampusIniciais(context);
                                }
                            });
                        }
                    });
                    instance = (ControleDatabase) builder.build();
                }
            }
        }
        return instance;
    }

    private static void carregaCampusIniciais(final Context context){

        String[] descricoes = context.getResources().getStringArray(R.array.campus);

        for (String descricao : descricoes) {

            Campus campus = new Campus(descricao);

            instance.campusDao().insert(campus);
        }
    }
}
