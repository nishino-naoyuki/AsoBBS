package jp.ac.asojuku.asobbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.entity.BookmarkTblEntity;
import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;

public interface BookMarkRepository 
extends JpaRepository<BookmarkTblEntity,Integer>,
			JpaSpecificationExecutor<BookmarkTblEntity>{

	@Query("select b from BookmarkTblEntity b where userTbl = :userTbl order by b.bbsTbl.title ASC")
	public List<BookmarkTblEntity> getList(@Param("userTbl")UserTblEntity userTbl);

	@Query("select b from BookmarkTblEntity b where b.userTbl.userId = :userId and b.bbsTbl.bbsId = :bbsId")
	public BookmarkTblEntity getBy(@Param("userId")Integer userId,@Param("bbsId")Integer bbsId);
}
