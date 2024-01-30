package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.AvailableTime;
import com.iglooclub.nungil.domain.enums.Location;
import com.iglooclub.nungil.domain.enums.Yoil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleUpdateRequest {

    private Location location;

    private List<AvailableTime> availableTimeList;

    private List<Yoil> yoilList;
}
