package jp.ac.asojuku.asobbs.dto;

import lombok.Data;

/**
 * ダッシュボードに表示する掲示板情報
 * @author nishino
 *
 */
@Data
public class DashBoadBbsDto {
	//掲示板情報
	private Integer bbsId;
	private String bbsName;
	private boolean update;
}
