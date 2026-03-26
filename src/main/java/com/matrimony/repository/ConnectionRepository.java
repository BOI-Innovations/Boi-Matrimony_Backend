package com.matrimony.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.matrimony.model.entity.Connection;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

	@Query("""
			SELECT c FROM Connection c
			WHERE (c.sender.id = :user1 AND c.receiver.id = :user2)
			   OR (c.sender.id = :user2 AND c.receiver.id = :user1)
			""")
	Optional<Connection> findExistingConnection(@Param("user1") Long user1, @Param("user2") Long user2);

	List<Connection> findByReceiver_Id(Long receiverId);

	List<Connection> findBySender_Id(Long senderId);

	List<Connection> findByStatusAndReceiver_Id(Connection.Status status, Long receiverId);

	List<Connection> findByStatusAndSender_Id(Connection.Status status, Long senderId);

	@Query("""
			SELECT c FROM Connection c
			WHERE (c.sender.id = :userId OR c.receiver.id = :userId)
			  AND c.status = :status
			""")
	List<Connection> findAllByUserAndStatus(@Param("userId") Long userId, @Param("status") Connection.Status status);

	@Query(value = "SELECT * FROM connections c WHERE c.sender_id = :userId OR c.receiver_id = :userId", nativeQuery = true)
	List<Connection> findUserConnections(@Param("userId") Long userId);

	@Query("""
			    SELECT
			        CASE
			            WHEN c.sender.id = :userId
			            THEN c.receiver.id
			            ELSE c.sender.id
			        END
			    FROM Connection c
			    WHERE c.sender.id = :userId
			       OR c.receiver.id = :userId
			""")
	List<Long> findConnectedUserIds(@Param("userId") Long userId);

	@Query("""
			    SELECT c.receiver.id
			    FROM Connection c
			    WHERE c.sender.id = :userId
			      AND c.status = 'PENDING'
			""")
	List<Long> findPendingSentUserIds(@Param("userId") Long userId);

	@Query("""
			    SELECT c.sender.id
			    FROM Connection c
			    WHERE c.receiver.id = :userId
			      AND c.status = 'PENDING'
			""")
	List<Long> findPendingReceivedUserIds(@Param("userId") Long userId);
	
	@Query("""
		    SELECT
		        CASE
		            WHEN c.sender.id = :userId
		            THEN c.receiver.id
		            ELSE c.sender.id
		        END
		    FROM Connection c
		    WHERE (c.sender.id = :userId OR c.receiver.id = :userId)
		      AND c.status = 'ACCEPTED'
		""")
		List<Long> findAcceptedConnectedUserIds(@Param("userId") Long userId);

    @Query("""
            SELECT COUNT(c) > 0 FROM Connection c
            WHERE 
            (
                (c.sender.id = :viewerId AND c.receiver.id = :ownerId)
                OR
                (c.sender.id = :ownerId AND c.receiver.id = :viewerId)
            )
            AND c.status = 'ACCEPTED'
        """)
        boolean existsAcceptedConnection(Long viewerId, Long ownerId);


}
