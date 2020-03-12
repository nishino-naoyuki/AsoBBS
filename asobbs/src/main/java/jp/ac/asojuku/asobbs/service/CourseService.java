package jp.ac.asojuku.asobbs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.dto.CourseDto;
import jp.ac.asojuku.asobbs.entity.CourseMasterEntity;
import jp.ac.asojuku.asobbs.repository.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	CourseRepository courseRepository;
	
	/**
	 * 学科一覧を取得する
	 * @return
	 */
	public List<CourseDto> getAllList(){
		List<CourseDto> list = new ArrayList<CourseDto>();
		
		//一覧取得
		List<CourseMasterEntity> entityList  = courseRepository.findAll();
		
		//entity -> DTO
		for( CourseMasterEntity entity : entityList ) {
			CourseDto dto = new CourseDto();
			
			dto.setCourseId(entity.getCourseId());
			dto.setCourseName(entity.getCourseName());
			
			list.add(dto);
		}
		
		return list;
	}
	
	/**
	 * 学科名の更新処理
	 * 
	 * @param courseId
	 * @param courseName
	 * @return
	 */
	public void update(int courseId,String courseName) {
		
		CourseMasterEntity entity = courseRepository.getOne(courseId);
		
		entity.setCourseName(courseName);
		
		courseRepository.save(entity) ;
		
	}
	
	/**
	 * 学科の挿入
	 * 
	 * @param courseName
	 */
	public void insert(String courseName) {
		CourseMasterEntity entity = new CourseMasterEntity();
		
		entity.setCourseName(courseName);
		
		courseRepository.save(entity);
	}
	

}
