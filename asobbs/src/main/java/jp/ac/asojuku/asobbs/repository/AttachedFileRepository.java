package jp.ac.asojuku.asobbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.asobbs.entity.AttachedFileTblEntity;

public interface AttachedFileRepository 
extends JpaRepository<AttachedFileTblEntity,Integer>{

	@Query("select a from AttachedFileTblEntity a where a.bbsId = :bbsId ")
	public List<AttachedFileTblEntity> getBy(@Param("bbsId")Integer bbsId);

	@Query("select a from AttachedFileTblEntity a where a.attachedFileId = :attachedFileId and  a.fileSize = :fileSize")
	public AttachedFileTblEntity getBy(@Param("attachedFileId")Integer attachedFileId,@Param("fileSize")Long fileSize);
}
