package com.jaeyeon.studyolle.modules.study.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class StudyDescriptionForm {

    @NotBlank
    @Length(max = 10)
    private String shortDescription;

    @NotBlank
    private String fullDescription;
}
