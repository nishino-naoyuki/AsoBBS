package jp.ac.asojuku.asobbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.RoomUserTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomUserTblId;

public interface RoomUserRepository extends JpaRepository<RoomUserTblEntity,RoomUserTblId>,JpaSpecificationExecutor<RoomUserTblEntity>{

	@Query("select ru from RoomUserTblEntity ru where roomId = :roomId")
	public List<RoomUserTblEntity> getListBy(@Param("roomId")Integer roomId);
}
