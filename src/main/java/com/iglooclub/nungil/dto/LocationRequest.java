package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {

    private Location location;

}