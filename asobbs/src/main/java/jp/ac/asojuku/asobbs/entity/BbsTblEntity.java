package jp.ac.asojuku.asobbs.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

/**
 * 掲示板テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="BBS_TBL")
public class BbsTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 掲示板ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bbsId;

	/** カテゴリテーブル. */
	@OneToOne
    @JoinColumn(name="categoryId")
	private CategoryTblEntity categoryTbl;

	/** タイトル. */
	private String title;

	/** メッセージ. */
	private String message;

	/** 緊急フラグ. */
	private Integer emergencyFlg;

	/** お知らせフラグ. */
	private Integer anyoneFlg;

	/** 親書き込みID. */
	private Integer parentBbsId;

	/** 返信可能フラグ. */
	private Integer replyOkFlg;

	/** 作成日. */
	private Date createDate;

	/** 作成ユーザーID. */
	@OneToOne
    @JoinColumn(name="CREATE_USER_ID",insertable=true ,updatable=true)
	private UserTblEntity createUserId;

	/** 更新日付. */
	private Date updateDate;

	/** 更新ユーザーID. */
	@OneToOne
    @JoinColumn(name="UPDATE_USER_ID",insertable=true ,updatable=true)
	private UserTblEntity updateUserId;

	/** 掲示板確認テーブル 一覧. */
	@OneToMany(mappedBy = "bbsId")
	private Set<BbsCheckTblEntity> bbsCheckTblSet;

	@OneToMany(mappedBy = "bbsId")
	private Set<AttachedFileTblEntity> attachedFileTblSet;

	/**
	 * コンストラクタ.
	 */
	public BbsTblEntity() {
		this.bbsCheckTblSet = new HashSet<BbsCheckTblEntity>();
		this.attachedFileTblSet = new HashSet<AttachedFileTblEntity>();
	}
	

	@PrePersist
    public void onPrePersist() {
		setCreateDate(new Date());
		setUpdateDate(new Date());
    }

    @PreUpdate
    public void onPreUpdate() {
    	setUpdateDate(new Date());
    }

}
