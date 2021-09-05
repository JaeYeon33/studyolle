package com.jaeyeon.studyolle.modules.account;

import com.jaeyeon.studyolle.modules.tag.Tag;
import com.jaeyeon.studyolle.modules.zone.Zone;
import com.querydsl.core.types.Predicate;

import java.util.Set;

import static com.jaeyeon.studyolle.modules.account.QAccount.*;

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        return account.zones.any().in(zones).and(account.tags.any().in(tags));
    }
}
