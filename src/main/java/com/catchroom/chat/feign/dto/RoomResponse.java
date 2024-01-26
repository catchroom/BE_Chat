package com.catchroom.chat.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RoomResponse {
    private Long id;

    private int normalCapacity;

    private int maxCapacity;

    private int price;

    private String introduction;

    private String name;

    private String service;

    private int count;
}
