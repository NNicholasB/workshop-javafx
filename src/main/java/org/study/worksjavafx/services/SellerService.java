package org.study.worksjavafx.services;

import org.study.worksjavafx.dao.DaoFactory;
import org.study.worksjavafx.dao.SellerDao;
import org.study.worksjavafx.entities.Seller;

import java.util.List;

public class SellerService {
    private SellerDao dao= DaoFactory.createSellerDao();

    public List<Seller> findAll(){ return dao.findAll();}

    public void saveOrUpdate(Seller obj){
        if (obj.getId() == null){
            dao.insert(obj);
        }
        dao.update(obj);
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}

