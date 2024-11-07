package backend.mapper;

import backend.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper {

    /**
     * 插入截止日期
     *
     * @param departmentMapper 截止日期
     */
    void insertDepartment(DepartmentMapper departmentMapper);

    /**
     * 查询截止日期
     *
     * @param departmentMapper 查询条件
     * @param scope      查询返回的字段
     * @return 截止日期列表
     */
    List<Department> selectDepartment(DepartmentMapper departmentMapper, Map<String, Boolean> scope);

    /**
     * 更新截止日期
     *
     * @param departmentMapperUpdate 更新值
     * @param departmentMapperQuery  更新的条件
     */
    void updateDepartment(DepartmentMapper departmentMapperUpdate, DepartmentMapper departmentMapperQuery);

    /**
     * 获取所有部门
     * @return 所有部门
     */
    List<Department> selectAllDepartments();
}