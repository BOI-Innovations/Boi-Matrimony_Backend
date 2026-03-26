package com.matrimony.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matrimony.model.entity.Message;
import com.matrimony.model.entity.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findBySenderAndReceiver(User sender, User receiver);

	List<Message> findByReceiverAndIsRead(User receiver, Boolean isRead);

	long countByReceiverAndIsRead(User receiver, Boolean isRead);

	@Query("SELECT m FROM Message m WHERE " + "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR "
			+ "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " + "ORDER BY m.sentAt DESC")
	List<Message> findConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2, Pageable pageable);

	List<Message> findBySender(User sender);

	List<Message> findByReceiver(User receiver);

	@Query("SELECT m FROM Message m WHERE " + "(m.sender = :user1 AND m.receiver = :user2) OR "
			+ "(m.sender = :user2 AND m.receiver = :user1) " + "ORDER BY m.sentAt DESC")
	Page<Message> findConversationPage(@Param("user1") User user1, @Param("user2") User user2, Pageable pageable);

	@Query("SELECT m FROM Message m WHERE m.receiver = :receiver AND m.isRead = false")
	List<Message> findUnreadMessages(@Param("receiver") User receiver);

	@Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :receiver AND m.isRead = false")
	Long countUnreadMessages(@Param("receiver") User receiver);

	@Query("SELECT m FROM Message m WHERE m.sentAt BETWEEN :startDate AND :endDate")
	List<Message> findMessagesSentBetween(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Modifying
	@Query("UPDATE Message m SET m.isRead = true, m.readAt = :readAt WHERE m.id = :messageId")
	void markAsRead(@Param("messageId") Long messageId, @Param("readAt") LocalDateTime readAt);

	@Modifying
	@Query("UPDATE Message m SET m.isRead = true, m.readAt = :readAt "
			+ "WHERE m.receiver = :receiver AND m.sender = :sender AND m.isRead = false")
	void markConversationAsRead(@Param("sender") User sender, @Param("receiver") User receiver,
			@Param("readAt") LocalDateTime readAt);

	@Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver = :user "
			+ "UNION SELECT DISTINCT m.receiver FROM Message m WHERE m.sender = :user")
	List<User> findUserConversations(@Param("user") User user);

	@Query("SELECT m FROM Message m WHERE m.receiver = :user ORDER BY m.sentAt DESC")
	Page<Message> findLatestMessages(@Param("user") User user, Pageable pageable);

	@Modifying
	@Query("DELETE FROM Message m WHERE m.sender = :user OR m.receiver = :user")
	void deleteAllUserMessages(@Param("user") User user);

	@Query("SELECT COUNT(m) FROM Message m " + "WHERE m.sender = :sender " + "AND m.receiver = :receiver "
			+ "AND m.isRead = false")
	Long countUnreadBetweenUsers(@Param("sender") User sender, @Param("receiver") User receiver);

	int countByReceiverAndIsReadFalseAndDeletedByReceiverFalse(User user);

}