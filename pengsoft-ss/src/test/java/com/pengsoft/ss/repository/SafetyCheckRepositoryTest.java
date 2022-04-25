package com.pengsoft.ss.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.pengsoft.support.util.DateUtils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata", "ss" })
class SafetyCheckRepositoryTest {

    @Inject
    SafetyCheckRepository repository;

    @Test
    void getDays() {
        List<Map<String, Object>> result = repository.getCheckedDays(List.of("24efc508-b6c1-437a-a3dd-dd30212f5e14"),
                DateUtils.atStartOfCurrentYear(),
                DateUtils.atEndOfCurrentYear());
        System.out.println(result);
    }

    @Test
    void statistic() {
        List<Map<String, Object>> result = repository.statistic(List.of("24efc508-b6c1-437a-a3dd-dd30212f5e14"),
                DateUtils.atStartOfCurrentYear(),
                DateUtils.atEndOfCurrentYear());
        System.out.println(result);
    }

    @Test
    void findAllUncheckedOrUnhandledDates() {
        List<Map<String, Object>> result = repository.findAllUncheckedOrUnhandledDates(
                List.of("24efc508-b6c1-437a-a3dd-dd30212f5e14"),
                DateUtils.atStartOfCurrentMonth(),
                DateUtils.atStartOfToday());
        System.out.println(result);
    }

}
