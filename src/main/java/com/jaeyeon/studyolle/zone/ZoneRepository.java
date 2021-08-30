package com.jaeyeon.studyolle.zone;

import com.jaeyeon.studyolle.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Zone findByCityAndProvince(String cityName, String provinceName);
}
