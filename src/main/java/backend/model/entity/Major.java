package backend.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 学科表
 *
 * @field id 主键 int
 * @field name 学科名称 varchar
 * @field mid 学科号码 varchar
 * @field pid 上级学科 int (first class -> null)
 * @field description 概述 / 备注 text default ""
 * @field type 学科类型 int 学硕0/专硕1
 * @field total 年度总指标 int
 * @field addition 补充指标 int
 * @field year 年度 int
 * @field initial 初试科目 JSON
 * @field interview 复试科目 JSON
 * @field recommend 推免数 int
 * @field disabled 是否废弃 int
 * @field department 所属学院 int
 * @field allowReassign 是否允许调剂，仅针对一级学科 int
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Major {

    private Integer id;

    private String name;

    private String mid;

    // first class -> null
    private Integer pid;

    private String description;

    //学硕--->0 专硕--->1
    private Integer type;

    private Integer total;

    private Integer addition;

    private Integer year;

    private String initial;

    private String interview;

    private Integer recommend;

    private Integer disabled;

    private Integer department;

    private Integer allowReassign;


    public static List<Integer> initialToList(String initial) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(initial, List.class);
    }

    public static List<Integer> interviewToList(String interview) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(interview, List.class);
    }

}
