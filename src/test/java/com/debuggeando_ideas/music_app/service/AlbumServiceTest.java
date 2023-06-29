package com.debuggeando_ideas.music_app.service;

import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.entity.AlbumEntity;
import com.debuggeando_ideas.music_app.repository.AlbumRepository;
import com.debuggeando_ideas.music_app.repository.RecordCompanyRepository;
import com.debuggeando_ideas.music_app.repository.TrackRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlbumServiceTest extends  ServiceSpec {

    @MockBean
    private AlbumRepository albumRepositoryMock;
    @MockBean
    private TrackRepository trackRepositoryMock;
    @MockBean
    private RecordCompanyRepository recordCompanyRepositoryMock;

    @Autowired
    private IAlbumService albumService;

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 2L;

    @BeforeEach
    void setMocks() {
        when(this.albumRepositoryMock.findById(VALID_ID))
                .thenReturn(Optional.of(DataDummy.ALBUM));

        when(this.albumRepositoryMock.findById(INVALID_ID))
                .thenReturn(Optional.empty());

        when(this.albumRepositoryMock.save(any(AlbumEntity.class)))
                .thenReturn(DataDummy.ALBUM);

    }

    @AfterEach
    void resetMocks() {
        reset(this.albumRepositoryMock);
    }

    @Test
    @DisplayName("findById should works")
    void findById() {
        var result = this.albumService.findById(VALID_ID);
        verify(albumRepositoryMock, times(1)).findById(eq(VALID_ID));
        assertEquals(DataDummy.ALBUM_DTO, result);

        assertThrows(NoSuchElementException.class,
                () -> {
                    this.albumService.findById(INVALID_ID);
                    verify(albumRepositoryMock).findById(eq(INVALID_ID));
                });
    }


    @Test
    @DisplayName("getAll should works")
    void getAll() {
      when(this.albumRepositoryMock.findAll())
              .thenReturn(Collections.emptyList());

      assertThrows(NoSuchElementException.class,
              () -> {
                    this.albumService.getAll();
                    verify(this.albumRepositoryMock, times(1)).findAll();
              });

        when(this.albumRepositoryMock.findAll())
                .thenReturn(List.of(DataDummy.ALBUM));

        var result = this.albumService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(this.albumRepositoryMock, times(2)).findAll();
    }


    @Test
    @DisplayName("save should works")
    void save() {
        when(this.recordCompanyRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(DataDummy.RECORD_COMPANY));

        var result = this.albumService.save(DataDummy.ALBUM_DTO);

        assertEquals(DataDummy.ALBUM_DTO, result);

        verify(this.recordCompanyRepositoryMock, times(1)).findById(anyString());
        verify(this.albumRepositoryMock, times(1)).save(any(AlbumEntity.class));
    }

    @Test
    @DisplayName("delete should works")
    void delete() {
        this.albumService.delete(VALID_ID);
        verify(this.albumRepositoryMock, times(1)).deleteById(eq(VALID_ID));

        assertThrows(NoSuchElementException.class,
                () -> {
                    this.albumService.delete(INVALID_ID);
                    verify(this.albumRepositoryMock, times(1)).deleteById(eq(INVALID_ID));
                });
    }

    @Test
    @DisplayName("update should works")
    void update() {
        when(this.recordCompanyRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(DataDummy.RECORD_COMPANY));


        var result = this.albumService.update(DataDummy.ALBUM_DTO, VALID_ID);

        assertEquals(DataDummy.ALBUM_DTO, result);

        verify(this.albumRepositoryMock, times(1)).save(eq(DataDummy.ALBUM));
    }

    @Test
    @DisplayName("findBetweenPrice should works")
    void findBetweenPrice() {
        when(this.albumRepositoryMock.findByPriceBetween(anyDouble(), anyDouble()))
                .thenReturn(Set.of(DataDummy.ALBUM));

        var result = this.albumService.findBetweenPrice(10.0, 100.0);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(this.albumRepositoryMock, times(1)).findByPriceBetween(eq(10.0), eq(100.0));

        when(this.albumRepositoryMock.findByPriceBetween(anyDouble(), anyDouble()))
                .thenReturn(Collections.emptySet());

        assertThrows(NoSuchElementException.class,
                () -> {
                    this.albumService.findBetweenPrice(0.0, 1.0);
                    verify(this.albumRepositoryMock, times(1)).findByPriceBetween(eq(0.0), eq(1.0));

                });
    }

    @Test
    @DisplayName("addTrack should works")
    void addTrack() {
        assertThrows(NoSuchElementException.class,
                () -> {
                    this.albumService.addTrack(DataDummy.TRACK_1_DTO, INVALID_ID);
                    verify(this.albumRepositoryMock, times(1)).findById(eq(INVALID_ID));
                });

        var result = this.albumService.addTrack(DataDummy.TRACK_1_DTO, VALID_ID);

        assertEquals(DataDummy.ALBUM_DTO, result);
        verify(this.albumRepositoryMock, times(2)).findById(eq(VALID_ID));
        verify(this.albumRepositoryMock, times(1)).save(any(AlbumEntity.class));

    }

    @Test
    @DisplayName("removeTrack should works")
    void removeTrack() {
        when(this.trackRepositoryMock.existsById(INVALID_ID))
                .thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> {
                    this.albumService.removeTrack(DataDummy.TRACK_1_DTO, INVALID_ID);
                    verify(this.albumRepositoryMock, times(1)).findById(eq(INVALID_ID));
                });

        when(this.trackRepositoryMock.existsById(DataDummy.TRACK_1_DTO.getTrackId()))
                .thenReturn(true);

        var result = this.albumService.removeTrack(DataDummy.TRACK_1_DTO, VALID_ID);

        assertEquals(DataDummy.ALBUM_DTO, result);

       verify(this.albumRepositoryMock, times(1)).findById(eq(VALID_ID));
       verify(this.albumRepositoryMock, times(2)).save(any(AlbumEntity.class));

    }


}
