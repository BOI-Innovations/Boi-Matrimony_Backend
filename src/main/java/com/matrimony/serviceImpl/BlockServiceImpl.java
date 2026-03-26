package com.matrimony.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.request.BlockRequest;
import com.matrimony.model.dto.response.BlockResponse;
import com.matrimony.model.entity.Block;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.BlockRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.BlockService;

import jakarta.transaction.Transactional;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }

    private BlockResponse mapToResponse(Block block) {
        BlockResponse response = new BlockResponse();
        response.setId(block.getId());
        response.setBlockerId(block.getBlocker().getId());
        response.setBlockerName(block.getBlocker().getUsername());
        response.setBlockedId(block.getBlocked().getId());
        response.setBlockedName(block.getBlocked().getUsername());
        response.setReason(block.getReason());
        response.setBlockedAt(block.getBlockedAt());
        return response;
    }

    @Override
    @Transactional
    public ResponseEntity blockUser(BlockRequest request) {
        try {
            Long currentUserId = getCurrentUserId();
            User blocker = userRepository.findById(currentUserId).orElse(null);
            User blocked = userRepository.findById(request.getUserIdToBlock()).orElse(null);

            if (blocker == null || blocked == null) {
                return new ResponseEntity("User not found", 404, null);
            }

            if (blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
                return new ResponseEntity("User already blocked", 400, null);
            }

            Block block = new Block();
            block.setBlocker(blocker);
            block.setBlocked(blocked);
            block.setReason(request.getReason());
            blockRepository.save(block);

            return new ResponseEntity("User blocked successfully", 200, mapToResponse(block));

        } catch (Exception e) {
            return new ResponseEntity("Error blocking user: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity unblockUser(Long blockedUserId) {
        try {
            Long currentUserId = getCurrentUserId();
            User blocker = userRepository.findById(currentUserId).orElse(null);
            User blocked = userRepository.findById(blockedUserId).orElse(null);

            if (blocker == null || blocked == null) {
                return new ResponseEntity("User not found", 404, null);
            }

            if (!blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
                return new ResponseEntity("Block not found", 404, null);
            }

            blockRepository.unblockUser(blocker, blocked);
            return new ResponseEntity("User unblocked successfully", 200, null);

        } catch (Exception e) {
            return new ResponseEntity("Error unblocking user: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getBlockedUsers() {
        try {
            Long currentUserId = getCurrentUserId();
            User blocker = userRepository.findById(currentUserId).orElse(null);

            if (blocker == null) {
                return new ResponseEntity("User not found", 404, null);
            }

            List<BlockResponse> blockedList = blockRepository.findByBlocker(blocker).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new ResponseEntity("Blocked users fetched", 200, blockedList);

        } catch (Exception e) {
            return new ResponseEntity("Error fetching blocked users: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getBlockedByUsers() {
        try {
            Long currentUserId = getCurrentUserId();
            User blocked = userRepository.findById(currentUserId).orElse(null);

            if (blocked == null) {
                return new ResponseEntity("User not found", 404, null);
            }

            List<BlockResponse> blockedByList = blockRepository.findByBlocked(blocked).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new ResponseEntity("Users who blocked you fetched", 200, blockedByList);

        } catch (Exception e) {
            return new ResponseEntity("Error fetching users who blocked you: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteAllBlocksForUser() {
        try {
            Long currentUserId = getCurrentUserId();
            User user = userRepository.findById(currentUserId).orElse(null);

            if (user == null) {
                return new ResponseEntity("User not found", 404, null);
            }

            blockRepository.deleteAllUserBlocks(user);
            return new ResponseEntity("All blocks for user deleted", 200, null);

        } catch (Exception e) {
            return new ResponseEntity("Error deleting blocks: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity isBlocked(Long otherUserId) {
        try {
            Long currentUserId = getCurrentUserId();
            User user1 = userRepository.findById(currentUserId).orElse(null);
            User user2 = userRepository.findById(otherUserId).orElse(null);

            if (user1 == null || user2 == null) {
                return new ResponseEntity("User not found", 404, null);
            }

            Boolean blocked = blockRepository.isBlocked(user1, user2);
            return new ResponseEntity("Block status fetched", 200, blocked);

        } catch (Exception e) {
            return new ResponseEntity("Error fetching block status: " + e.getMessage(), 500, null);
        }
    }
}
