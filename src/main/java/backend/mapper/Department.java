package backend.mapper;

import backend.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
@Mapper
public interface Department {
    
    /**
     * 插入截止日期
     * @param department 截止日期
     */
    void insertDeadline(Department department);

    /**
     * 查询截止日期
     * @param department 查询条件
     * @param scope 查询返回的字段
     * @return 截止日期列表
     */
    List<Department> selectDeadline(Department department, Map<String, Boolean> scope);

    /**
     * 更新截止日期
     * @param departmentUpdate 更新值
     * @param departmentQuery 更新的条件
     */
    void updateDeadline(Department departmentUpdate, Department departmentQuery);
}
