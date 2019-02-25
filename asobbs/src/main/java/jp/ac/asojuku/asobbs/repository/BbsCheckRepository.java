package jp.ac.asojuku.asobbs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.BbsCheckTblEntity;
import jp.ac.asojuku.asobbs.entity.BbsCheckTblId;

public interface BbsCheckRepository 
extends JpaRepository<BbsCheckTblEntity,BbsCheckTblId>,
JpaSpecificationExecutor<BbsCheckTblEntity>{

	@Query("select count(*) from BbsCheckTblEntity b where b.bbsId = :bbsId and b.userId = :userId")
	public int getCount(@Param("bbsId")Integer bbsId,@Param("userId")Integer userId);
	
	@Query("select b from BbsCheckTblEntity b where b.bbsId = :bbsId")
	public List<BbsCheckTblEntity> getList(@Param("bbsId")Integer bbsId);
}
