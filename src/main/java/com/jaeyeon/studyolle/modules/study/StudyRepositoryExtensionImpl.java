package com.jaeyeon.studyolle.modules.study;

import com.jaeyeon.studyolle.modules.tag.Tag;
import com.jaeyeon.studyolle.modules.zone.QZone;
import com.jaeyeon.studyolle.modules.zone.Zone;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

import static com.jaeyeon.studyolle.modules.account.QAccount.*;
import static com.jaeyeon.studyolle.modules.study.QStudy.*;
import static com.jaeyeon.studyolle.modules.tag.QTag.*;
import static com.jaeyeon.studyolle.modules.zone.QZone.*;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public Page<Study> findByKeyword(String keyword, Pageable pageable) {
        JPQLQuery<Study> query =
                from(study)
                    .where(study.published.isTrue()
                    .and(study.title.containsIgnoreCase(keyword))
                    .or(study.tags.any().title.containsIgnoreCase(keyword))
                    .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                    .leftJoin(study.tags, tag).fetchJoin()
                    .leftJoin(study.zones, zone).fetchJoin()
                    .distinct();
        JPQLQuery<Study> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones) {
        JPQLQuery<Study> query = from(study)
                .where(study.published.isTrue()            // 스터디가 공개되어 있고
                        .and(study.closed.isFalse())       // 스터디가 아직 종료되지 않았고
                        .and(study.tags.any().in(tags))    // 스터디 태그 매칭
                        .and(study.zones.any().in(zones))) // 스터디 지역 매칭
                .leftJoin(study.tags, tag).fetchJoin()     // STUDY.TAG, TAG 참고하고 있기 때문에 fetchJoin
                .leftJoin(study.zones, zone).fetchJoin()   // STUDY.ZONE, ZONE 참고하고 있기 떄문에 fetchJoin
                .orderBy(study.publishedDateTime.desc())   // 공개된 스터디 순서대로 정렬
                .distinct()
                .limit(9);
        return query.fetch();
    }
}
