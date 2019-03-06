package jp.ac.asojuku.asobbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;

public interface BbsRepository 
extends JpaRepository<BbsTblEntity,Integer>,
			JpaSpecificationExecutor<BbsTblEntity>{

	@Query("select b from BbsTblEntity b "
			+ "left join b.categoryTbl c "
			+ "left join c.roomTbl r "
			+ "  where (now() - b.updateDate) <= 6048000 "
			+ " and exists(select 1 from RoomUserTblEntity ru where ru.roomId = r.roomId and ru.userId = :userId) "
			+ " and b.parentBbsId is null "
			+ "order by b.updateDate DESC")
	public List<BbsTblEntity> getRecentlyBbs(@Param("userId")Integer userId);
	
	@Query("select count(*) from BbsTblEntity b where categoryTbl = :categoryTblEntity and b.parentBbsId is null")
	public int getCount(@Param("categoryTblEntity")CategoryTblEntity categoryTblEntity);

	@Query("select b from BbsTblEntity b where categoryTbl = :categoryTblEntity and b.parentBbsId is null order by b.updateDate desc")
	public List<BbsTblEntity> getList(@Param("categoryTblEntity")CategoryTblEntity categoryTblEntity);

	@Query("select b from BbsTblEntity b "
			+ "left join b.categoryTbl c "
			+ "left join c.roomTbl r "
			+ "where r.roomId = :roomId and b.parentBbsId is null order by b.updateDate desc")
	public List<BbsTblEntity> getRoomList(@Param("roomId")Integer roomId);
	
	@Query("select b from BbsTblEntity b "
			+ "left join b.categoryTbl c "
			+ "left join c.roomTbl r "
			+ "left join r.roomUserTblSet ru "
			+ "where b.bbsId = :bbsId and (ru.userId = :userId or r.allFlg = 1) ")
	public BbsTblEntity getBy(@Param("bbsId")Integer bbsId,@Param("userId")Integer userId);

	@Query("select b from BbsTblEntity b where parentBbsId = :bbsId order by b.updateDate ASC")
	public List<BbsTblEntity> getReply(@Param("bbsId")Integer bbsId);
}
