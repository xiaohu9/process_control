package entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class FileInfo {

    /**
     * 文件唯一编号
     */
    private Integer uid;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件分类
     */
    private Integer directory;

    /**
     * 文件状态：0: 回收站 1: 正常 2: 已删除
     */
    private Integer status;

    /**
     * 文件父级目录
     */
    private Integer pid;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
}
