package com.debuggeando_ideas.music_app.repository;

import com.debuggeando_ideas.music_app.entity.TrackEntity;
import org.springframework.data.repository.CrudRepository;

public interface TrackRepository extends CrudRepository<TrackEntity, Long>{

}
