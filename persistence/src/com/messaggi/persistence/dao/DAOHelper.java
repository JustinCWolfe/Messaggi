package com.messaggi.persistence.dao;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.messaggi.persistence.dao.DAOException.ErrorCode;

public class DAOHelper
{
    private static Logger log = Logger.getLogger(DAOHelper.class);

    @SuppressWarnings("unchecked")
    public static <T extends Object> T clonePrototype(T prototype) throws DAOException
    {
        T clone = null;
        try {
            clone = (T) BeanUtils.cloneBean(prototype);
        } catch (Exception e) {
            log.error(e);
            throw new DAOException(ErrorCode.CLONE_ERROR, e.getMessage());
        }
        return clone;
    }
}

