package com.catchroom.chat.feign.dto;

import com.catchroom.chat.message.dto.RoomResponse;
import com.catchroom.chat.message.type.AccommodationType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccommodationResponse {
    private Long id;

    private String name;

    private String region;

    private String introduction;

    private String service;

    private String thumbnailUrl;

    private String latitude;

    private String longitude;

    private String address;

    private AccommodationType type;

    private double star;

    private int roomCount;

    private List<RoomResponse> roomList;
}
