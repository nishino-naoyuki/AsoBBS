package jp.ac.asojuku.asobbs.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.StringUtils;

import jp.ac.asojuku.asobbs.entity.UserTblEntity;


public class UserSpecifications {
	/**
     * 指定文字をメールアドレスに含むユーザーを検索する。
     */
    public static Specification<UserTblEntity> mailContains(String mail) {
        return StringUtils.isEmpty(mail) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.like(root.get("mailadress"),  "%" + mail + "%" );
			}
        };
    }
    

    public static Specification<UserTblEntity> passwordContains(String password) {
        return StringUtils.isEmpty(password) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("password"),  password );
			}
        };
    }

    /**
     * 学年の検索
     * @param grade
     * @return
     */
    public static Specification<UserTblEntity> gradeEquals(Integer grade) {
        return grade == null ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("grade"),  grade );
			}
        };
    }

    /**
     * 学科コードの検索
     * 
     * @param courseId
     * @return
     */
    public static Specification<UserTblEntity> courseEquals(Integer courseId) {
        return courseId == null ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("courseMaster").get("courseId"),  courseId );
			}
        };
    }

    /**
     * ニックネームの検索
     * @param nickName
     * @return
     */
    public static Specification<UserTblEntity> nicknameContains(String nickName) {
        return StringUtils.isEmpty(nickName) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.like(root.get("nickName"),  "%"+nickName+"%" );
			}
        };
    }
}
