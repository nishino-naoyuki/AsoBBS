package jp.ac.asojuku.asobbs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.asobbs.config.ValidationConfig;
import jp.ac.asojuku.asobbs.dto.AttachedFileDto;
import jp.ac.asojuku.asobbs.dto.BbsCheckTblDto;
import jp.ac.asojuku.asobbs.dto.BbsDetailDto;
import jp.ac.asojuku.asobbs.dto.BbsListDto;
import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.dto.ReplyDto;
import jp.ac.asojuku.asobbs.entity.AttachedFileTblEntity;
import jp.ac.asojuku.asobbs.entity.BbsCheckTblEntity;
import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.entity.CategoryTblEntity;
import jp.ac.asojuku.asobbs.entity.RoomTblEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsIllegalException;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.form.BbsInputForm;
import jp.ac.asojuku.asobbs.form.BbsReplyInputForm;
import jp.ac.asojuku.asobbs.param.IntConst;
import jp.ac.asojuku.asobbs.param.StringConst;
import jp.ac.asojuku.asobbs.repository.AttachedFileRepository;
import jp.ac.asojuku.asobbs.repository.BbsCheckRepository;
import jp.ac.asojuku.asobbs.repository.BbsRepository;
import jp.ac.asojuku.asobbs.repository.CategoryRepository;
import jp.ac.asojuku.asobbs.repository.RoomRepository;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.DateUtil;
import jp.ac.asojuku.asobbs.util.FileUtils;

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
	@Autowired
	BbsCheckRepository bbsCheckRepository;
	
	/**
	 * 最近1週間の掲示板情報を取得する
	 * @return
	 */
	public List<BbsTblEntity> getRecentlyBbs() {
		return bbsRepository.getRecentlyBbs();
	}

	/**
	 * 緊急時の確認しましたを登録する
	 * @param bbsId
	 * @param loginInfo
	 */
	public void insertBbsCheck(Integer bbsId,LoginInfoDto loginInfo) {
		BbsCheckTblEntity bbsCheckTblEntity = new BbsCheckTblEntity();
		
		bbsCheckTblEntity.setBbsId(bbsId);
		bbsCheckTblEntity.setUserId(loginInfo.getUserId());
		
		bbsCheckRepository.save(bbsCheckTblEntity);
	}
	
	/**
	 * 掲示情報登録処理
	 * 
	 * @param bbsInputForm
	 * @param loginInfo
	 */
	@Transactional
	public void insert(BbsInputForm bbsInputForm,LoginInfoDto loginInfo) {
		
		//カテゴリを更新する
		CategoryTblEntity categoryTblEntity = saveCategoryTblEntity(bbsInputForm);
		
		//掲示板登録
		BbsTblEntity bbsEntity = saveBbsTblEntity(
				bbsInputForm,categoryTblEntity,loginInfo.getUserId(),false);
		
		//添付ファイル情報を登録する
		saveAttachedFiles(bbsInputForm,bbsEntity);
	}

	/**
	 * 更新処理
	 * 
	 * @param bbsInputForm
	 * @param loginInfo
	 */
	@Transactional
	public void update(BbsInputForm bbsInputForm,LoginInfoDto loginInfo) {
		
		//カテゴリを更新する
		CategoryTblEntity categoryTblEntity = saveCategoryTblEntity(bbsInputForm);
		
		//掲示板登録
		BbsTblEntity bbsEntity = saveBbsTblEntity(
				bbsInputForm,categoryTblEntity,loginInfo.getUserId(),true);
		
		//添付ファイル情報を登録する
		saveAttachedFiles(bbsInputForm,bbsEntity);
	}

	/**
	 * 返信処理の登録を行う
	 * 
	 * @param bbsReplyInputForm
	 * @param loginInfo
	 */
	@Transactional
	public void insertChild(BbsReplyInputForm bbsReplyInputForm,LoginInfoDto loginInfo) {
		
		//掲示板登録
		saveBbsTblEntity(bbsReplyInputForm,loginInfo.getUserId());
		
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
		List<AttachedFileTblEntity> list = null;
		
		if( bbsInputForm.getUploadFilePathList().size() > 0 ) {
			list = attachedFileRepository.getBy(bbsEntity.getBbsId());
		}else {
			list = new ArrayList<AttachedFileTblEntity>();
		}
		
		//同じIDがあれば、更新
		for( AttachedFileTblEntity attachedFileTblEntity :  list ) {
			AttachedFileTblEntity attachedFileTblEntityNew = null;
			//同じIDを探す
			for( AttachedFileDto fileDto :  bbsInputForm.getUploadFilePathList() ) {
				if( fileDto.getId() == attachedFileTblEntity.getAttachedFileId() ) {
					attachedFileTblEntityNew = new AttachedFileTblEntity();
					
					attachedFileTblEntityNew.setBbsId(bbsEntity.getBbsId());
					attachedFileTblEntityNew.setFilePath(fileDto.getFilePath());
					attachedFileTblEntityNew.setFileSize(fileDto.getSize());
					break;
				}
			}
			//あれば更新、なければ削除
			if(attachedFileTblEntityNew != null) {
				attachedFileRepository.save(attachedFileTblEntityNew);
			}else {
				attachedFileRepository.delete(attachedFileTblEntity);
			}
		}

		//IDが振られていないのは、新規作成なので登録する
		for( AttachedFileDto fileDto :  bbsInputForm.getUploadFilePathList() ) {
			if( fileDto.getId() == null) {
				AttachedFileTblEntity attachedFileTblEntity = new AttachedFileTblEntity();
				
				attachedFileTblEntity.setBbsId(bbsEntity.getBbsId());
				attachedFileTblEntity.setFilePath(fileDto.getFilePath());
				attachedFileTblEntity.setFileSize(fileDto.getSize());
				
				attachedFileRepository.save(attachedFileTblEntity);
			}
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
			Integer userId,boolean update) {
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
		if( !update ) {
			entity.setCreateUserId(createUserTbl);
		}
		entity.setUpdateUserId(createUserTbl);
		
		bbsRepository.save(entity);
		
		return entity;
	}

	/**
	 * 掲示板情報（リプライ）の登録
	 * 
	 * @param bbsReplyInputForm
	 * @param userId
	 * @return
	 */
	private BbsTblEntity saveBbsTblEntity(
			BbsReplyInputForm bbsReplyInputForm,
			Integer userId) {
		BbsTblEntity entity = new BbsTblEntity();
		

		CategoryTblEntity categoryTblEntity = 
				categoryRepository.getOne (bbsReplyInputForm.getCategoryId());
		
		//掲示板の情報を作成する
		entity.setCategoryTbl(categoryTblEntity);
		entity.setParentBbsId(bbsReplyInputForm.getBbsId());//親のID
		entity.setTitle("reply:"+bbsReplyInputForm.getBbsId());
		entity.setMessage(bbsReplyInputForm.getComment());
		entity.setEmergencyFlg(0);
		entity.setReplyOkFlg(0);
		//作成情報
		UserTblEntity createUserTbl = userRepository.getOne(userId);
		entity.setCreateUserId(createUserTbl);
		entity.setUpdateUserId(createUserTbl);
		
		bbsRepository.save(entity);
		
		return entity;
	}
	/**
	 * 掲示板リストを取得する
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<BbsListDto> getBbsListDto(Integer categoryId,Integer roomId){
		
		List<BbsTblEntity> entityList =  null;
		//カテゴリの指定が無い場合は全権取得する
		if( categoryId != IntConst.ALL_BBS_DATA ) {
			entityList = bbsRepository.getList(categoryRepository.getOne(categoryId));
		}else {
			//entityList = bbsRepository.findAll(new Sort(Sort.Direction.DESC, "updateDate"));
			entityList = bbsRepository.getRoomList(roomId);
		}
		
		List<BbsListDto> bbsList = new ArrayList<BbsListDto>();
		
		for(BbsTblEntity bbsEntity : entityList) {
			bbsList.add( getBbsListDtoFrom(bbsEntity) );
		}
		
		return bbsList;
	}
	
	/**
	 * 添付ファイルのパスを取得する
	 * IDとサイズを渡すのは、セキュリティ保護のため
	 * 不正にIDを改ざんされて、想定外のファイルを取得するのを予防する
	 * @param fId
	 * @param fSize
	 * @return
	 */
	public String getAttacedFileName(Integer fId,Long fSize) {
		AttachedFileTblEntity attachedFileTblEntity = attachedFileRepository.getBy(fId,fSize);
		
		return ( attachedFileTblEntity != null ? attachedFileTblEntity.getFilePath():"");
	}
	
	/**
	 * Entityより掲示板用の情報を取得する
	 * 
	 * @param bbsEntity
	 * @return
	 */
	private BbsListDto getBbsListDtoFrom(BbsTblEntity bbsEntity) {
		BbsListDto bbsListDto = new BbsListDto();
		
		bbsListDto.setId( bbsEntity.getBbsId() );
		bbsListDto.setTitle( bbsEntity.getTitle() );
		bbsListDto.setUpdateDate( DateUtil.formattedDate(bbsEntity.getUpdateDate(), StringConst.DATE_FMT)  );
		
		return bbsListDto;
	}
	
	/**
	 * 掲示板データを取得する
	 * 
	 * @param bbsId
	 * @param loginInfo
	 * @return
	 * @throws AsoBbsSystemErrException 
	 * @throws AsoBbsIllegalException 
	 */
	public BbsDetailDto getBy(Integer bbsId,LoginInfoDto loginInfo) throws AsoBbsIllegalException, AsoBbsSystemErrException {
		BbsTblEntity bbsTblEntity = bbsRepository.getBy(bbsId, loginInfo.getUserId());
		
		if( bbsTblEntity == null ) {
			//取得できないということは不正にURLを変更してパラメータを取得しようと
			//した恐れがある（＝パラメータを手動で変更したなど）
			throw new AsoBbsIllegalException( ValidationConfig.getInstance().getMsg(ErrorCode.ERR_INVLIDATE) );
		}
		
		BbsDetailDto bbsDetailDto = new BbsDetailDto();
		
		//基本情報を設定
		bbsDetailDto.setBbsId( bbsTblEntity.getBbsId() );
		bbsDetailDto.setTitle( bbsTblEntity.getTitle() );
		bbsDetailDto.setEmergencyFlg( (bbsTblEntity.getEmergencyFlg() == 1 ? true:false) );
		bbsDetailDto.setReplyOkFlg((bbsTblEntity.getReplyOkFlg() == 1 ? true:false));
		bbsDetailDto.setContent( bbsTblEntity.getMessage() );
		bbsDetailDto.setRoomId(bbsTblEntity.getCategoryTbl().getRoomTbl().getRoomId());
		bbsDetailDto.setRoomName(bbsTblEntity.getCategoryTbl().getRoomTbl().getName());
		bbsDetailDto.setCategoryId(bbsTblEntity.getCategoryTbl().getCategoryId());
		bbsDetailDto.setCategoryName( bbsTblEntity.getCategoryTbl().getName() );
		bbsDetailDto.setEmergencyReplyFlg( (bbsCheckRepository.getCount(bbsTblEntity.getBbsId(),loginInfo.getUserId()) > 0 ? true:false));
		
		//添付ファイル情報をセット
		for(AttachedFileTblEntity attachedFileEntity : bbsTblEntity.getAttachedFileTblSet()) {
			AttachedFileDto attachedFileDto = new AttachedFileDto();
			
			attachedFileDto.setId(attachedFileEntity.getAttachedFileId());
			attachedFileDto.setFilePath(attachedFileEntity.getFilePath());
			attachedFileDto.setSize(attachedFileEntity.getFileSize());
			attachedFileDto.setFileName(FileUtils.getFileNameFromPath(attachedFileEntity.getFilePath()));
			
			bbsDetailDto.addAttachedFileDto(attachedFileDto);
		}
		
		//返信情報を設定
		List<BbsTblEntity> bbsReplyEntityList = bbsRepository.getReply(bbsId);
		for( BbsTblEntity replyEntity : bbsReplyEntityList ) {
			ReplyDto replyDto = new ReplyDto();
			
			replyDto.setContent( replyEntity.getMessage() );
			replyDto.setWriterName( replyEntity.getUpdateUserId().getNickName() );
			replyDto.setWriteDate( DateUtil.formattedDate(replyEntity.getUpdateDate(), StringConst.DATE_FMT) );
			
			bbsDetailDto.addReplyDto(replyDto);
		}
		
		return bbsDetailDto;
	}
	
	/**
	 * 緊急掲示板のチェック済みリストを取得する
	 * @param bbsId
	 * @return
	 */
	public List<BbsCheckTblDto> getBbsCheckTblDtoList(Integer bbsId){
		
		List<BbsCheckTblEntity> checkList = bbsCheckRepository.getList(bbsId);
		List<BbsCheckTblDto> dtoList = new ArrayList<BbsCheckTblDto>();
		
		for( BbsCheckTblEntity entity : checkList ) {
			BbsCheckTblDto bbsCheckTblDto = new BbsCheckTblDto();
			
			bbsCheckTblDto.setCourseName(entity.getUserTbl().getCourseMaster().getCourseName());
			bbsCheckTblDto.setGrade(entity.getUserTbl().getGrade());
			bbsCheckTblDto.setMailadress(entity.getUserTbl().getMailadress());
			bbsCheckTblDto.setNickname(entity.getUserTbl().getNickName());
			bbsCheckTblDto.setStudentNo(entity.getUserTbl().getStudentNo());
			bbsCheckTblDto.setCheckDate( DateUtil.formattedDate(entity.getCheckDate(), StringConst.DATE_FMT) );
			
			dtoList.add(bbsCheckTblDto);
		}
		
		return dtoList;
	}
}
