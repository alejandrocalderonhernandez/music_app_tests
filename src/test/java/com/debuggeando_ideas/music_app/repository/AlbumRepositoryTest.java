package com.debuggeando_ideas.music_app.repository;

import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.entity.AlbumEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumRepositoryTest extends RepositorySpec {

    @Autowired
    AlbumRepository albumRepository;


    private static final Long VALID_ID = 100L;
    private static final Long INVALID_ID = 900L;

    @Test
    @DisplayName("findById should works")
    void findById() {
        var result = this.albumRepository.findById(VALID_ID);
        assertTrue(result.isPresent());

        var albumResult = result.get();
        assertAll(
                () -> assertEquals("fear of the dark", albumResult.getName()),
                () -> assertEquals("iron maiden", albumResult.getAutor()),
                () -> assertEquals(280.50, albumResult.getPrice())
        );

        result = this.albumRepository.findById(INVALID_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll should works")
    void findAll() {
        var result = (List<AlbumEntity>) this.albumRepository.findAll();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }


    @Test
    @DisplayName("save should works")
    void save() {
        var result = this.albumRepository.save(DataDummy.ALBUM);
        assertEquals(DataDummy.ALBUM, result);
    }

    @Test
    @DisplayName("findByPriceBetween should works")
    void findByPriceBetween() {
        var result = this.albumRepository.findByPriceBetween(270.00, 271.0);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("deleteById should works")
    void deleteById() {
        var records = (List<AlbumEntity>) this.albumRepository.findAll();
        var totalRecords = records.size();
        assertEquals(2, totalRecords);

        var albumToDelete = this.albumRepository.findById(VALID_ID);
        this.albumRepository.deleteById(albumToDelete.get().getAlbumId());

        records = (List<AlbumEntity>) this.albumRepository.findAll();
        totalRecords = records.size();
        assertEquals(1, totalRecords);
    }


}
