package jp.ac.asojuku.asobbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.asobbs.entity.CourseMasterEntity;

public interface CourseRepository 
								extends JpaRepository<CourseMasterEntity,Integer>,
								JpaSpecificationExecutor<CourseMasterEntity>{

}
