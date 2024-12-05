package backend.model.VO.log;

import backend.model.VO.log.LogVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageLogVO {

    Integer total;

    List<LogVO> logList;
}
