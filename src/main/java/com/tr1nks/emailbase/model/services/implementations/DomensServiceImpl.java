package com.tr1nks.emailbase.model.services.implementations;

import com.tr1nks.emailbase.model.repositories.DomenRepository;
import com.tr1nks.emailbase.model.services.DomensService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * реализация для {@link DomensService DomensService}
 */
@Service
public class DomensServiceImpl implements DomensService {
    @Resource
    private DomenRepository domensRepository;
    private static String emailDomen;
    private static String imagineDomen;
    private static String officeDomen;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getEmailDomen() {
        if (null == emailDomen) {
            emailDomen = domensRepository.getFirstById(1).getEmailDomen();
        }
        return emailDomen;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getImagineDomen() {
        if (null == imagineDomen) {
            imagineDomen = domensRepository.getFirstById(1).getImagineDomen();
        }
        return imagineDomen;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getOfficeDomen() {
        if (null == officeDomen) {
            officeDomen = domensRepository.getFirstById(1).getOfficeDomen();
        }
        return officeDomen;
    }
}
