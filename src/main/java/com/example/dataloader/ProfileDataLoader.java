package com.example.dataloader;

import com.example.model.Profile;
import com.example.repository.DataRepository;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.BatchLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsDataLoader(name = "PROFILE")
@Component
public class ProfileDataLoader implements BatchLoader<String, Profile> {
    private static final Logger logger = LoggerFactory.getLogger(ProfileDataLoader.class);
    private final DataRepository dataRepository;

    public ProfileDataLoader(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public CompletableFuture<List<Profile>> load(List<String> authorIds) {
        logger.info("Пакетне завантаження профілів для авторів: {}", authorIds);
        List<Profile> profiles = authorIds.stream()
                .map(id -> {
                    if (dataRepository.getAuthorById(id) != null) {
                        return dataRepository.getAuthorById(id).getProfile();
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(profiles);
    }
}
