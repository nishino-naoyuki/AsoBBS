package jp.ac.asojuku.asobbs.dto;

import java.util.List;

import lombok.Data;

@Data
public class CategoryDto {
	private Integer id;
	private String name;
	private List<BbsListDto> bbsList;
}
