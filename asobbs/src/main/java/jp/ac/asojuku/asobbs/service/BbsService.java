package jp.ac.asojuku.asobbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.entity.AttachedFileTblEntity;
import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.form.BbsInputForm;
import jp.ac.asojuku.asobbs.repository.AttachedFileRepository;
import jp.ac.asojuku.asobbs.repository.BbsRepository;
import jp.ac.asojuku.asobbs.repository.CategoryRepository;
import jp.ac.asojuku.asobbs.repository.RoomRepository;
import jp.ac.asojuku.asobbs.repository.UserRepository;

@Service
public class BbsService {
	
	@Autowired
	BbsRepository bbsRepository;
	@Autowired 
	UserRepository userRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	AttachedFileRepository attachedFileRepository;
	
	/**
	 * 最近1週間の掲示板情報を取得する
	 * @return
	 */
	public List<BbsTblEntity> getRecentlyBbs() {
		return bbsRepository.getRecentlyBbs();
	}

	@Transactional
	public void insert(BbsInputForm bbsInputForm,LoginInfoDto loginInfo) {
		
		//カテゴリを更新する
		CategoryTblEntity categoryTblEntity = saveCategoryTblEntity(bbsInputForm);
		
		//掲示板登録
		BbsTblEntity bbsEntity = saveBbsTblEntity(
				bbsInputForm,categoryTblEntity,loginInfo.getUserId());
		
		//添付ファイル情報を登録する
		saveAttachedFiles(bbsInputForm,bbsEntity);
	}
	
	/**
	 * 添付ファイルの情報を保存する
	 * @param bbsInputForm
	 * @param bbsEntity
	 */
	private void saveAttachedFiles(BbsInputForm bbsInputForm,BbsTblEntity bbsEntity) {
		if( bbsInputForm.getUploadFilePathList() == null ) {
			return;
		}
		
		//いったん削除
		if( bbsInputForm.getUploadFilePathList().size() > 0 ) {
			List<AttachedFileTblEntity> list = attachedFileRepository.getBy(bbsEntity.getBbsId());
			attachedFileRepository.deleteAll(list);
		}
		
		for( String filePath :  bbsInputForm.getUploadFilePathList() ) {
			AttachedFileTblEntity attachedFileTblEntity = new AttachedFileTblEntity();
			
			attachedFileTblEntity.setBbsId(bbsEntity.getBbsId());
			attachedFileTblEntity.setFilePath(filePath);
			
			attachedFileRepository.save(attachedFileTblEntity);
		}
	}
	
	/**
	 * カテゴリ情報を保存する
	 * 
	 * @param bbsInputForm
	 * @return
	 */
	private CategoryTblEntity saveCategoryTblEntity(BbsInputForm bbsInputForm) {
		//カテゴリが属しているルームを取得
		RoomTblEntity roomEntity = roomRepository.getOne(bbsInputForm.getRoomId());
		
		//カテゴリを取得
		CategoryTblEntity categoryTblEntity = 
				categoryRepository.getBy(roomEntity, bbsInputForm.getCategoryName());
		
		if( categoryTblEntity == null ) {
			//カテゴリが居ない場合は、まずカテゴリを新規登録する
			categoryTblEntity = new CategoryTblEntity();
			categoryTblEntity.setRoomId(roomEntity.getRoomId());
			categoryTblEntity.setName(bbsInputForm.getCategoryName());
			categoryTblEntity.setCount(1);//新規なので掲示数は1
		}else {
			//カテゴリがある場合は掲示数を増やす
			categoryTblEntity.addCount();
		}
		categoryRepository.save(categoryTblEntity);
		
		return categoryTblEntity;
	}
	
	/**
	 * 掲示板情報を保存する
	 * 
	 * @param bbsInputForm
	 * @param categoryTblEntity
	 * @param userId
	 * @return
	 */
	private BbsTblEntity saveBbsTblEntity(
			BbsInputForm bbsInputForm,
			CategoryTblEntity categoryTblEntity ,
			Integer userId) {
		BbsTblEntity entity = new BbsTblEntity();
		
		//掲示板の情報を作成する
		entity.setCategoryTbl(categoryTblEntity);
		entity.setParentBbsId(null);//親のIDは無し
		entity.setTitle(bbsInputForm.getTitle());
		entity.setMessage(bbsInputForm.getContent());
		entity.setEmergencyFlg(  (bbsInputForm.getEmergencyFlg() ? 1:0)  );
		entity.setReplyOkFlg(1);
		//作成情報
		UserTblEntity createUserTbl = userRepository.getOne(userId);
		entity.setCreateUserId(createUserTbl);
		entity.setUpdateUserId(createUserTbl);
		
		bbsRepository.save(entity);
		
		return entity;
	}
}
