package com.jaeyeon.studyolle.modules.study;

import com.jaeyeon.studyolle.modules.account.QAccount;
import com.jaeyeon.studyolle.modules.tag.QTag;
import com.jaeyeon.studyolle.modules.zone.QZone;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.jaeyeon.studyolle.modules.account.QAccount.*;
import static com.jaeyeon.studyolle.modules.study.QStudy.*;
import static com.jaeyeon.studyolle.modules.tag.QTag.*;
import static com.jaeyeon.studyolle.modules.zone.QZone.*;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyword(String keyword) {
        JPQLQuery<Study> query =
                from(study)
                    .where(study.published.isTrue()
                    .and(study.title.containsIgnoreCase(keyword))
                    .or(study.tags.any().title.containsIgnoreCase(keyword))
                    .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                    .leftJoin(study.tags, tag).fetchJoin()
                    .leftJoin(study.zones, zone).fetchJoin()
                    .leftJoin(study.members, account).fetchJoin()
                    .distinct();
        return query.fetch();
    }
}
