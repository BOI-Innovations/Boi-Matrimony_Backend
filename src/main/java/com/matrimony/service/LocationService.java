package com.matrimony.service;

import com.matrimony.model.dto.request.LocationRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface LocationService {

	ResponseEntity getLocationForCurrentUser();

	ResponseEntity createLocationForCurrentUser(LocationRequest request);

	ResponseEntity updateLocationForCurrentUser(LocationRequest request);

	ResponseEntity deleteLocationForCurrentUser();
}
