package Mapper;

import entity.po.FileInfo;

public interface FileMapper {

    // 保存文件记录
    void insert(FileInfo file);
}
