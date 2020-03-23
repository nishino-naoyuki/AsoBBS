package jp.ac.asojuku.asobbs.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.BookMarkDto;

import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.entity.BookmarkTblEntity;

import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.exception.AsoBbsDuplicateException;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.repository.BbsRepository;
import jp.ac.asojuku.asobbs.repository.BookMarkRepository;

import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.DateUtil;

@Service
public class BookMarkService {
	
	@Autowired
	BookMarkRepository bookMarkRepository;
	@Autowired 
	UserRepository userRepository;
	@Autowired 
	BbsRepository bbsRepository;

	Logger logger = LoggerFactory.getLogger(BookMarkService.class);
	
	/**
	 * ブックマーク情報をセットする
	 * 
	 * @param bbsId
	 * @param userId
	 */
	public void insert(Integer bbsId,Integer userId) throws AsoBbsDuplicateException{
		if( bbsId == null || userId == null ) {
			//何もしない
			return;
		}
		//二重登録チェック
		if( bookMarkRepository.getBy(userId, bbsId) != null ) {
			//二重登録
			String errMsg  = "お気に入りの二重登録（BBSID="+bbsId+":USERID="+userId;
			logger.warn(errMsg);
			throw new AsoBbsDuplicateException(errMsg);
		}
		
		BookmarkTblEntity entity = new BookmarkTblEntity();
		
		UserTblEntity userEntity = userRepository.getOne(userId);
		BbsTblEntity bbsTblEntity = bbsRepository.getOne(bbsId);
		
		entity.setBbsTbl(bbsTblEntity);
		entity.setUserTbl(userEntity);
		
		bookMarkRepository.save(entity);
	}
	
	/**
	 * お気に入りリストを取得する
	 * 
	 * @param userId
	 * @return
	 */
	public List<BookMarkDto> getList(Integer userId){
		List<BookmarkTblEntity> entityList = 
									bookMarkRepository.getList(userRepository.getOne(userId));
		//Entity -> DTO
		List<BookMarkDto> bookMarkList = new ArrayList<BookMarkDto>();
		for( BookmarkTblEntity entity : entityList ) {			
			bookMarkList.add( getBookMarkDtoFromEntity(entity));
		}
		
		return bookMarkList;
	}
	
	/**
	 * 削除する
	 * 
	 * @param bookmarkId
	 */
	public void delete(Integer bookmarkId,Integer userId) throws AsoBbsSystemErrException{

		BookmarkTblEntity bookmarkTblEntity = bookMarkRepository.getOne(bookmarkId);
		
		if( bookmarkTblEntity == null  || bookmarkTblEntity.getUserTbl().getUserId() != userId) {
			//ここに来る場合は、不正操作があった場合
			throw new AsoBbsSystemErrException("BookMarkService.delete：不正な操作が行われました。");
		}
		
		bookMarkRepository.delete( bookmarkTblEntity );
	}
	
	/**
	 * ブックマーク情報を1件取得する
	 * 
	 * @param userId
	 * @param bookmarkId
	 * @return
	 */
	public BookMarkDto getBy(Integer userId,Integer bookmarkId) throws AsoBbsSystemErrException{
		
		BookmarkTblEntity bookmarkTblEntity = bookMarkRepository.getOne(bookmarkId);
		
		if( bookmarkTblEntity == null  || bookmarkTblEntity.getUserTbl().getUserId() != userId) {
			//ここに来る場合は、不正操作があった場合
			throw new AsoBbsSystemErrException("BookMarkService.getBy：不正な操作が行われました。");
		}
		
		return getBookMarkDtoFromEntity(bookmarkTblEntity);
	}
	
	/**
	 * EntityをDTOに変換する
	 * 
	 * @param entity
	 * @return
	 */
	private BookMarkDto getBookMarkDtoFromEntity(BookmarkTblEntity entity) {

		BookMarkDto dto = new BookMarkDto();
		
		dto.setBookmarkId(entity.getBookmarkId());
		dto.setBbsId(entity.getBbsTbl().getBbsId());
		dto.setRoomName(entity.getBbsTbl().getCategoryTbl().getRoomTbl().getName());
		dto.setCategoryName(entity.getBbsTbl().getCategoryTbl().getName());
		dto.setTitle(entity.getBbsTbl().getTitle());
		dto.setDate(
				DateUtil.formattedDate(entity.getCreateDateTime(), "yyyy/MM/dd")
				);
		return dto;
	}
}
