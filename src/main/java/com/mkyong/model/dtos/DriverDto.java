package com.mkyong.model.dtos;

import lombok.Data;

@Data
public class DriverDto {
    String id;
    String name;

    public DriverDto(String id_, String name_) {
        this.name = name_;
        this.id = id_;
    }
}
