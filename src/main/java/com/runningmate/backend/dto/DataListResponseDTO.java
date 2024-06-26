package com.runningmate.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataListResponseDTO<T> {
    private List<T> data;

    public DataListResponseDTO(List<T> data) {
        this.data = data;
    }
}