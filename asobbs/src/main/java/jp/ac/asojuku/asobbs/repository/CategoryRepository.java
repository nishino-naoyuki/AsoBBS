package jp.ac.asojuku.asobbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;

public interface CategoryRepository 
extends JpaRepository<CategoryTblEntity,Integer>,
					JpaSpecificationExecutor<CategoryTblEntity>{

	@Query("select c from CategoryTblEntity c where c.roomTbl = :roomTblEntity and name = :name")
	public CategoryTblEntity getBy(@Param("roomTblEntity")RoomTblEntity roomTblEntity,@Param("name")String name);
}
