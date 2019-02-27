package jp.ac.asojuku.asobbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.ChatTableEntity;

public interface ChatRepository 
extends JpaRepository<ChatTableEntity,Integer>,
			JpaSpecificationExecutor<ChatTableEntity>{

	@Query("select c from ChatTableEntity c where (c.fromUserTbl.userId = :userId1 and c.toUserTbl.userId = :userId2) or (c.fromUserTbl.userId = :userId2 and c.toUserTbl.userId = :userId1) order by c.regsterDatetime")
	List<ChatTableEntity> getList(@Param("userId1")Integer userId1,@Param("userId2")Integer userId2);
}
