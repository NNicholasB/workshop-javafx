package org.study.worksjavafx.services;

import org.study.worksjavafx.dao.DaoFactory;
import org.study.worksjavafx.dao.DepartmentDao;
import org.study.worksjavafx.entities.Department;


import java.util.List;

public class DepartmentService {

    private DepartmentDao dao= DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if (obj.getId()==null){
            dao.insert(obj);
        }
        dao.update(obj);
    }

    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}
