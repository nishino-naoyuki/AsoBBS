package jp.ac.asojuku.asobbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.asobbs.entity.BbsTblEntity;
import jp.ac.asojuku.asobbs.repository.BbsRepository;

@Service
public class BbsService {
	
	@Autowired
	BbsRepository bbsRepository;
	
	/**
	 * 最近1週間の掲示板情報を取得する
	 * @return
	 */
	public List<BbsTblEntity> getRecentlyBbs() {
		return bbsRepository.getRecentlyBbs();
	}

}
