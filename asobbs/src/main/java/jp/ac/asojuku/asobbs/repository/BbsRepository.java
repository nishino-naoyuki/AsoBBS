package jp.ac.asojuku.asobbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import jp.ac.asojuku.asobbs.entity.BbsTblEntity;

public interface BbsRepository 
extends JpaRepository<BbsTblEntity,Integer>,
			JpaSpecificationExecutor<BbsTblEntity>{

	@Query("select b from BbsTblEntity b where b.updateDate <= now() ")
	public List<BbsTblEntity> getRecentlyBbs();
}
