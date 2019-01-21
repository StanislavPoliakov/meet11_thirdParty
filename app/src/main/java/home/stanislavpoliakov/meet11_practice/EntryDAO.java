package home.stanislavpoliakov.meet11_practice;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EntryDAO {

    @Query("SELECT * FROM entries")
    List<Entry> getEntries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEntry(Entry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateEntry(Entry entry);

    @Delete
    int deleteEntry(Entry entry);
}
