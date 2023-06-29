package com.debuggeando_ideas.music_app.controller;

import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.service.IAlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AlbumControllerStatus200Test extends AlbumControllerSpec {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IAlbumService albumServiceMock;

    ObjectMapper objectMapper;

    private static final Long VALID_ID = 1L;
    private static final String RESOURCE_PATH = "/v1/album";

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setupMocks() {
        when(this.albumServiceMock.findById(eq(VALID_ID)))
                .thenReturn(DataDummy.ALBUM_DTO);

        when(this.albumServiceMock.save(eq(DataDummy.ALBUM_DTO)))
                .thenReturn(DataDummy.ALBUM_DTO);
    }


    @Test
    @DisplayName("call findById should works")
    void findById() throws Exception {
        final var uri = RESOURCE_PATH + "/" + VALID_ID;
        this.mockMvc.perform(
                get(uri).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(DataDummy.ALBUM_DTO.getName()))
                .andExpect(jsonPath("$.autor").value(DataDummy.ALBUM_DTO.getAutor()))
                .andExpect(jsonPath("$.price").value(DataDummy.ALBUM_DTO.getPrice()));

        verify(this.albumServiceMock).findById(eq(VALID_ID));
    }

    @Test
    @DisplayName("call save should works")
    void save() throws Exception {
        this.mockMvc.perform(
                post(RESOURCE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(DataDummy.ALBUM_DTO)))
                .andExpect(status().isCreated());
        verify(this.albumServiceMock).save(eq(DataDummy.ALBUM_DTO));
    }

    @Test
    @DisplayName("call update should works")
    void update() throws Exception {
        final var uri = RESOURCE_PATH + "/" + VALID_ID;
        final var albumToUpdate = DataDummy.ALBUM_DTO;
        albumToUpdate.setAutor("Autor updated");

        when(this.albumServiceMock.update(eq(albumToUpdate), eq(VALID_ID)))
                .thenReturn(albumToUpdate);

        mockMvc.perform(
                put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(albumToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.autor").value("Autor updated"));

        verify(this.albumServiceMock).update(eq(albumToUpdate), eq(VALID_ID));
        verify(this.albumServiceMock).update(eq(albumToUpdate), eq(VALID_ID));
    }

    @Test
    @DisplayName("call delete should works")
    void delete() throws Exception {
        final var uri = RESOURCE_PATH + "/" + VALID_ID;

        this.mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(status().isNoContent());

        verify(this.albumServiceMock).delete(eq(VALID_ID));
    }
}
