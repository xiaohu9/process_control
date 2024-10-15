package com.brian.process.Mapper;

import com.brian.process.po.FileInfo;

public interface FileMapper {

    // 保存文件记录
    void insert(FileInfo file);
}
