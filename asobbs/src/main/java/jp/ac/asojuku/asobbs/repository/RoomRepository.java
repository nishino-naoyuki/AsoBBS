package jp.ac.asojuku.asobbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;

public interface RoomRepository extends JpaRepository<RoomTblEntity,Integer>,JpaSpecificationExecutor<RoomTblEntity> {

	@Query("select r from RoomTblEntity r where r.name = :name")
	public RoomTblEntity getBy(@Param("name")String name);
}
