package home.stanislavpoliakov.meet11_practice;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Entry.class}, version = 1)
public abstract class EntryDatabase extends RoomDatabase {
    public abstract EntryDAO getEntryDAO();
}
