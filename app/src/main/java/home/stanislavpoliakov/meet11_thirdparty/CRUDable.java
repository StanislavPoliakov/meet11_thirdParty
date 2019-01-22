package home.stanislavpoliakov.meet11_thirdparty;

import android.os.Bundle;

public interface CRUDable {
    void create(Entry entry);
    void update(Bundle entryInfo);
    void delete(Entry entry);
}
