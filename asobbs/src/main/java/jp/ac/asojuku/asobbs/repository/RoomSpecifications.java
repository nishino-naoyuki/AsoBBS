package jp.ac.asojuku.asobbs.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.sql.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.StringUtils;

import jp.ac.asojuku.asobbs.entity.RoomTblEntity;

public class RoomSpecifications {

    /**
     * ルーム名での検索
     * @param roomName
     * @return
     */
    public static Specification<RoomTblEntity> roomNameContains(String roomName) {
        return StringUtils.isEmpty(roomName) ? null : new Specification<RoomTblEntity>() {
			@Override
			public Predicate toPredicate(Root<RoomTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.like(root.get("name"),  "%" + roomName + "%" );
			}
        };
    }

    /**
     * 所属ユーザーでの検索
     * @param userId
     * @return
     */
    public static Specification<RoomTblEntity> roomUserContains(Integer userId) {
        return userId == null ? null : new Specification<RoomTblEntity>() {
			@Override
			public Predicate toPredicate(Root<RoomTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.join("roomUserTblSet") .get("userId"),  userId );
			}
        };
    }
}
