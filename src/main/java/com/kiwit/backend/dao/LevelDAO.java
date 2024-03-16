package com.kiwit.backend.dao;

import com.kiwit.backend.domain.Level;
import com.kiwit.backend.dto.ContentDTO;

import java.util.List;

public interface LevelDAO {

    List<Level> selectLevelList();
}
