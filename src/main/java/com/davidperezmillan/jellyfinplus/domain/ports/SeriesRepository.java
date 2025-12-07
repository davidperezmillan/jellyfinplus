package com.davidperezmillan.jellyfinplus.domain.ports;

import com.davidperezmillan.jellyfinplus.domain.model.Series;
import java.util.List;

public interface SeriesRepository {
    List<Series> findAll();

}
