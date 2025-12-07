package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Media;
import com.davidperezmillan.jellyfinplus.domain.ports.MediaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public Optional<Media> getMediaById(String id) {
        return mediaRepository.findById(id);
    }

    public Media createMedia(Media media) {
        return mediaRepository.save(media);
    }

    public void deleteMedia(String id) {
        mediaRepository.deleteById(id);
    }
}
