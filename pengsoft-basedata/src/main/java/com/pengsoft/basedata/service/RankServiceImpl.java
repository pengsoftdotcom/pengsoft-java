package com.pengsoft.basedata.service;

import com.pengsoft.basedata.domain.Rank;
import com.pengsoft.basedata.repository.RankRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link RankService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class RankServiceImpl extends EntityServiceImpl<RankRepository, Rank, String> implements RankService {

    @Override
    public Rank save(final Rank rank) {
        getRepository().findOneByOrganizationIdAndName(rank.getOrganization().getId(), rank.getName())
                .ifPresent(source -> {
                    if (EntityUtils.notEquals(source, rank)) {
                        throw getExceptions().constraintViolated("name", "exists", rank.getName());
                    }
                });
        return super.save(rank);
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Direction.DESC, "sequence");
    }

}
