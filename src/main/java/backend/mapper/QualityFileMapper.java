package backend.mapper;

import backend.model.entity.QualityFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QualityFileMapper {

    /**
     * 插入一条资格审查文件
     * 
     * @param qualityFile 资格审查文件
    */
    void insertQualityFile(QualityFile qualityFile);

    /**
     * 查询资格审查文件
     * 
     * @param qualityFile 查询条件
     * @param scope 查询返回的字段
     * @return 资格审查文件列表
    */
    List<QualityFile> selectQualityFile(QualityFile qualityFile, Map<String, Boolean> scope);

    /**
     * 更新资格审查文件
     * 
     * @param qualityFileUpdate 更新值
     * @param qualityFileQuery 更新的条件
    */
    void updateQualityFile(QualityFile qualityFileUpdate, QualityFile qualityFileQuery);

    /**
     * 删除资格审查文件
     *
     * @param id fileID
     * */
    void deleteQualityFile(Integer id);

    List<String> selectAllFiles();
}
