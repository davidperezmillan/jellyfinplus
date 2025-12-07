package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.model.Media;
import com.davidperezmillan.jellyfinplus.domain.ports.MediaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryMediaRepository implements MediaRepository {

    private final Map<String, Media> mediaStore = new ConcurrentHashMap<>();

    @Override
    public List<Media> findAll() {
        return new ArrayList<>(mediaStore.values());
    }

    @Override
    public Optional<Media> findById(String id) {
        return Optional.ofNullable(mediaStore.get(id));
    }

    @Override
    public Media save(Media media) {
        mediaStore.put(media.id(), media);
        return media;
    }

    @Override
    public void deleteById(String id) {
        mediaStore.remove(id);
    }
}
