package com.matrimony.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Profile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CustomSearchRepositoryImpl implements CustomSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Profile> searchProfilesWithFilters(
            String religion,
            String gender,
            Integer minAge,
            Integer maxAge,
            String maritalStatus,
            String caste,
            String education,
            String occupation,
            Double minIncome,
            String city,
            String country,
            Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Profile> query = cb.createQuery(Profile.class);
        Root<Profile> profile = query.from(Profile.class);

        List<Predicate> predicates = new ArrayList<>();

        // Add filters
        if (religion != null) {
            predicates.add(cb.equal(profile.get("religion").as(String.class), religion));
        }
        if (gender != null) {
            predicates.add(cb.equal(profile.get("gender").as(String.class), gender));
        }
        if (maritalStatus != null) {
            predicates.add(cb.equal(profile.get("maritalStatus").as(String.class), maritalStatus));
        }
        if (caste != null) {
            predicates.add(cb.like(profile.get("caste"), "%" + caste + "%"));
        }
        if (education != null) {
            predicates.add(cb.like(profile.get("education"), "%" + education + "%"));
        }
        if (occupation != null) {
            predicates.add(cb.like(profile.get("occupation"), "%" + occupation + "%"));
        }
        if (minIncome != null) {
            predicates.add(cb.greaterThanOrEqualTo(profile.get("annualIncome"), minIncome));
        }
        if (city != null) {
            predicates.add(cb.like(profile.get("city"), "%" + city + "%"));
        }
        if (country != null) {
            predicates.add(cb.like(profile.get("country"), "%" + country + "%"));
        }

        // Age filter
        if (minAge != null || maxAge != null) {
            LocalDate currentDate = LocalDate.now();
            if (minAge != null) {
                LocalDate maxBirthDate = currentDate.minusYears(minAge);
                predicates.add(cb.lessThanOrEqualTo(profile.get("dateOfBirth"), maxBirthDate));
            }
            if (maxAge != null) {
                LocalDate minBirthDate = currentDate.minusYears(maxAge + 1L);
                predicates.add(cb.greaterThan(profile.get("dateOfBirth"), minBirthDate));
            }
        }

        // Only active and verified profiles
        predicates.add(cb.equal(profile.get("user").get("isActive"), true));
        predicates.add(cb.equal(profile.get("verificationStatus").as(String.class), "VERIFIED"));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(profile.get("createdAt")));

        TypedQuery<Profile> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Profile.class)));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }

    @Override
    public Page<Profile> searchProfilesByPreferences(Long profileId, Pageable pageable) {
        // Implementation for preference-based search
        // This would join with ProfilePreference and match based on user preferences
        return Page.empty();
    }
}