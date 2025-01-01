package com.rankmonkeysvc.dto.assessment;

import com.rankmonkeysvc.constants.DocumentType;
import com.rankmonkeysvc.constants.ProgrammingLanguage;
import com.rankmonkeysvc.constants.QuestionDifficultyLevel;
import com.rankmonkeysvc.constants.QuestionType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuestionCreationRequest {
    private String question;
    private QuestionType questionType;
    private QuestionDifficultyLevel questionDifficultyLevel;
    private ProgrammingLanguage programmingLanguage;
    private Option option;
    private Long score;
    private Long durationInSec;
    private Document document;
    private Long minimumCharacterLength;
    private Long maximumCharacterLength;

    @Data
    @Accessors(chain = true)
    private static class Option {
        private Boolean isCorrect;
        private String value;
    }

    @Data
    @Accessors(chain = true)
    private static class Document {
        private DocumentType documentType;
        private String s3Url;
    }
}