package com.pengsoft.acs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.pengsoft.acs.domain.PersonFaceData;
import com.pengsoft.acs.repository.PersonFaceDataRepository;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.coobird.thumbnailator.Thumbnails;

/**
 * The implementer of {@link PersonFaceData} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PersonFaceDataServiceImpl extends EntityServiceImpl<PersonFaceDataRepository, PersonFaceData, String>
        implements PersonFaceDataService {

    @Override
    public PersonFaceData save(PersonFaceData personFaceData) {
        try (final var is = new ByteArrayInputStream(Base64.getDecoder().decode(personFaceData.getFace()));
                final var os = new ByteArrayOutputStream();) {
            Thumbnails.of(is).outputFormat("jpg").size(500, 500).toOutputStream(os);
            personFaceData.setFace(Base64.getEncoder().encodeToString(os.toByteArray()));
        } catch (Exception e) {
            throw new BusinessException("person_face_data.save.zoom_image_error", e.getMessage());
        }
        return super.save(personFaceData);
    }

    @Override
    public Page<PersonFaceData> findPageByPersonIdentityCardNumberNotNull(Pageable pageable) {
        return getRepository().findPageByPersonIdentityCardNumberNotNull(pageable);
    }

    @Override
    public List<PersonFaceData> findAllByPersonIn(List<Person> persons) {
        return getRepository().findAllByPersonIdIn(persons.stream().map(Person::getId).toList());
    }

    @Override
    public Optional<PersonFaceData> findOneByPersonIdentityCardNumber(String identityCardNumber) {
        return getRepository().findOneByPersonIdentityCardNumber(identityCardNumber);
    }

}
