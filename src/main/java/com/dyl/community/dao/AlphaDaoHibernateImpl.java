package com.dyl.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author admin
 */
@Repository("AlphaDaoHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {

    @Override
    public String select() {
        return "Hibernate";
    }
}
