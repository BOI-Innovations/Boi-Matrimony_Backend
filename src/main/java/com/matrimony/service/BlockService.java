package com.matrimony.service;

import com.matrimony.model.dto.request.BlockRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface BlockService {

    ResponseEntity blockUser(BlockRequest request);

    ResponseEntity unblockUser(Long blockedUserId);

    ResponseEntity getBlockedUsers();

    ResponseEntity getBlockedByUsers();

    ResponseEntity deleteAllBlocksForUser();

    ResponseEntity isBlocked(Long otherUserId);
}
