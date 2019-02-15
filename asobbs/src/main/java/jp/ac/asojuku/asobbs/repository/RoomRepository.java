package jp.ac.asojuku.asobbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.asobbs.entity.RoomTblEntity;

public interface RoomRepository extends JpaRepository<RoomTblEntity,Integer>,JpaSpecificationExecutor<RoomTblEntity> {

}
