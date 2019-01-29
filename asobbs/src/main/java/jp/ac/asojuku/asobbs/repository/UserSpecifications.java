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
     * 指定文字をユーザー名に含むユーザーを検索する。
     */
    public static Specification<UserTblEntity> nameContains(String mail) {
        return StringUtils.isEmpty(mail) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("mail"),  mail );
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
}
