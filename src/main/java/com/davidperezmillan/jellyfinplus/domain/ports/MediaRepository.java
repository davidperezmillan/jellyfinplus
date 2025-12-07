package com.davidperezmillan.jellyfinplus.domain.ports;

import com.davidperezmillan.jellyfinplus.domain.model.Media;
import java.util.List;
import java.util.Optional;

public interface MediaRepository {
    List<Media> findAll();
    Optional<Media> findById(String id);
    Media save(Media media);
    void deleteById(String id);
}
