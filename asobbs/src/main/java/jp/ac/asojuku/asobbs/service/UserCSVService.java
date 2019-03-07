package jp.ac.asojuku.asobbs.service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import jp.ac.asojuku.asobbs.csv.UserCSV;
import jp.ac.asojuku.asobbs.entity.CourseMasterEntity;
import jp.ac.asojuku.asobbs.entity.RoleMasterEntity;
import jp.ac.asojuku.asobbs.entity.UserTblEntity;
import jp.ac.asojuku.asobbs.err.ActionErrors;
import jp.ac.asojuku.asobbs.err.ErrorCode;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.RoleId;
import jp.ac.asojuku.asobbs.repository.UserRepository;
import jp.ac.asojuku.asobbs.util.Digest;
import jp.ac.asojuku.asobbs.validator.UserValidator;

@Service
public class UserCSVService {
	private static final Logger logger = LoggerFactory.getLogger(UserCSVService.class);
	private static final String[] HEADER = new String[] { "roleId", "name", "mailAddress", "nickName", "courseId", "password","admissionYear" };

	@Autowired 
	UserRepository userRepository;
	
	/**
	 * CSVファイルの中身をチェックする
	 * 
	 * @param csvPath
	 * @param errors
	 * @param type
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public List<UserCSV> checkForCSV(String csvPath, BindingResult err,String type) throws AsoBbsSystemErrException {
		List<UserCSV> list = null;
		FileReader fileReader = null;
		
		try {
			///////////////////////////////
			//CSVを読み込みマッピング
			fileReader = new FileReader(csvPath); 
			list = new CsvToBeanBuilder<UserCSV>(
                    fileReader).withType(UserCSV.class).build().parse(); 

            // エラーチェック
            for(UserCSV userCsv : list){
        		UserValidator.useName(userCsv.getName(), err);
        		UserValidator.useNickName(userCsv.getNickName(), err);
        		UserValidator.roleId(String.valueOf(userCsv.getRoleId()), err);
        		if( RoleId.STUDENT.equals(userCsv.getRoleId())){
        			UserValidator.admissionYear(userCsv.getAdmissionYear(), err);
        		}
        		UserValidator.mailAddress(userCsv.getMailAddress(), err);
        		UserValidator.password(userCsv.getPassword(), err);
            }
            
//            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath), "SJIS"), ',', '"', 1);
//            ColumnPositionMappingStrategy<UserCSV> strat = new ColumnPositionMappingStrategy<UserCSV>();
//            strat.setType(UserCSV.class);
//            strat.setColumnMapping(HEADER);
//            CsvToBean<UserCSV> csv = new CsvToBean<UserCSV>();
//            list = csv.parse(strat, reader);
		}catch (Exception e) {
        	logger.warn("CSVパースエラー：",e);
        	UserValidator.setErrorcode("",err,ErrorCode.ERR_CSV_FORMAT_ERROR);
        }finally {
        	if( fileReader != null ) {
        		try {
					fileReader.close();
				} catch (IOException e) {
					;//ignore
				}
        	}
        }
		
		return list;
	}
	
	/**
	 * CSV用の追加・更新処理
	 * 
	 * @param userList
	 * @throws AsoBbsSystemErrException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertByCSV(List<UserCSV> userList) throws AsoBbsSystemErrException {
		
		for(UserCSV csv : userList) {
			insertOrUpdate(csv);
		}
		
	}
	
	/**
	 * CSV登録・更新処理
	 * 
	 * @param userCSV
	 * @throws AsoBbsSystemErrException 
	 */
	private void insertOrUpdate(UserCSV userCSV) throws AsoBbsSystemErrException {
		//すでに登録済みかどうかをチェック
		UserTblEntity entity = userRepository.getUserByMail(userCSV.getMailAddress());
		
		UserTblEntity iuEntity = createEntityFromUserCSV(userCSV);
		if( entity == null ) {
			//追加
			userRepository.save(iuEntity);
		}else {
			//更新
			iuEntity.setUserId(entity.getUserId());
			iuEntity.setEntryDate(entity.getEntryDate());
			userRepository.save(iuEntity);
		}
	}
	
	/**
	 * UserCSVからUserEntityを作成する
	 * 
	 * @param userCSV
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private UserTblEntity createEntityFromUserCSV(UserCSV userCSV) throws AsoBbsSystemErrException {
		UserTblEntity entity = new UserTblEntity();

		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(userCSV.getMailAddress(), userCSV.getPassword());
		
		entity.setAccountExpryDate(null);
		if( RoleId.STUDENT.equals(userCSV.getRoleId())){
			entity.setAdmissionYear(Integer.parseInt(userCSV.getAdmissionYear()));
		}
		entity.setCertifyErrCnt(0);
		entity.setGiveUpYear(null);
		entity.setGrade(1);//後でバッチで更新
		entity.setGraduateYear(null);
		entity.setIsFirstFlg(1);
		entity.setIsLockFlg(0);
		entity.setMailadress(userCSV.getMailAddress());
		entity.setName(userCSV.getName());
		entity.setNickName(userCSV.getNickName());
		entity.setPassword(hashedPwd);
		entity.setPasswordExpirydate(null);
		entity.setRemark(null);
		entity.setRepeatYearCount(0);
		entity.setStudentNo(userCSV.getName());
		entity.setDelFlg(0);
		
		CourseMasterEntity cm = new CourseMasterEntity();
		cm.setCourseId(userCSV.getCourseId());
		
		RoleMasterEntity rm = new RoleMasterEntity();
		rm.setRoleId(userCSV.getRoleId());
		
		entity.setCourseMaster(cm);
		entity.setRoleMaster(rm);
		
		return entity;
	}
}
