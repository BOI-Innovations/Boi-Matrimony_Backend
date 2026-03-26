package com.matrimony.security;

import com.matrimony.security.services.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    public static Optional<UserPrincipal> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return Optional.of((UserPrincipal) authentication.getPrincipal());
        }
        
        return Optional.empty();
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(UserPrincipal::getId);
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentUser().map(UserPrincipal::getUsername);
    }

    public static boolean isCurrentUser(Long userId) {
        return getCurrentUserId().map(id -> id.equals(userId)).orElse(false);
    }

    public static boolean hasRole(String role) {
        return getCurrentUser()
                .map(user -> user.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals(role)))
                .orElse(false);
    }

    public static boolean hasAnyRole(String... roles) {
        return getCurrentUser()
                .map(user -> user.getAuthorities().stream()
                        .anyMatch(authority -> {
                            for (String role : roles) {
                                if (authority.getAuthority().equals(role)) {
                                    return true;
                                }
                            }
                            return false;
                        }))
                .orElse(false);
    }

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public static boolean isModerator() {
        return hasRole("ROLE_MODERATOR");
    }

    public static boolean isAdminOrModerator() {
        return hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR");
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() &&
               !"anonymousUser".equals(authentication.getPrincipal());
    }
}