package backend.model.DTO;

import backend.annotation.DTO.AdminTeacherDTOValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@AdminTeacherDTOValidation
public class AdminTeacherDTO {
    private Integer teacherID;

    private Integer title;

    private Integer identity;
}
