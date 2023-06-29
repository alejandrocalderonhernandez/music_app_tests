package com.debuggeando_ideas.music_app.service;

import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.entity.TrackEntity;
import com.debuggeando_ideas.music_app.repository.TrackRepository;
import com.debuggeando_ideas.music_app.service.impl.TrackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class TrackServiceTest extends ServiceSpec{

    @Mock
    private TrackRepository repositoryMock;

    @InjectMocks
    private TrackServiceImpl trackService;

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 2L;

    @BeforeEach
    void SetMocks() {
        when(this.repositoryMock.findById(eq(VALID_ID)))
                .thenReturn(Optional.of(DataDummy.TRACK_1));

        when(this.repositoryMock.findById(eq(INVALID_ID)))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("findById should works")
    void findById() {
        var result = this.trackService.findById(VALID_ID);
        assertEquals(DataDummy.TRACK_1, result);
        assertThrows(NoSuchElementException.class,
                () ->  this.trackService.findById(INVALID_ID));

    }


    @Test
    @DisplayName("getAll should works")
    void getAll() {
        var expected = Set.of(DataDummy.TRACK_4, DataDummy.TRACK_2);
        when(this.repositoryMock.findAll())
                .thenReturn(expected);
        var result = this.trackService.getAll();
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("save should works")
    void save() {
        this.trackService.save(DataDummy.TRACK_2);
        verify(this.repositoryMock, times(1)).save(any(TrackEntity.class));
    }

    @Test
    @DisplayName("delete should works")
    void delete() {
        this.trackService.delete(VALID_ID);
        verify(this.repositoryMock, times(1)).deleteById(eq(VALID_ID));
    }

    @Test
    @DisplayName("update should works")
    void update() {
        when(this.repositoryMock.existsById(VALID_ID)).thenReturn(true);
        when(this.repositoryMock.existsById(INVALID_ID)).thenReturn(false);

        when(this.repositoryMock.save(any(TrackEntity.class)))
                .thenReturn(DataDummy.TRACK_2);

        var result = this.trackService.update(DataDummy.TRACK_1, VALID_ID);

        assertEquals(DataDummy.TRACK_2, result);

        assertThrows(NoSuchElementException.class,
                () ->  this.trackService.update(DataDummy.TRACK_1, INVALID_ID));
        verify(this.repositoryMock, times(2)).existsById(anyLong());
    }
}
