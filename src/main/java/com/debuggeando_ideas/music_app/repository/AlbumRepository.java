package com.debuggeando_ideas.music_app.repository;

import com.debuggeando_ideas.music_app.entity.AlbumEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AlbumRepository extends CrudRepository<AlbumEntity, Long>{

	Set<AlbumEntity> findByPriceBetween(Double min, Double max);

}
