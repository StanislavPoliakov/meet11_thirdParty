package home.stanislavpoliakov.meet11_practice;

import android.os.Bundle;

public interface CRUDable {
    void create(Entry entry);
    void update(Bundle entryInfo);
    void delete(Entry entry);
}
