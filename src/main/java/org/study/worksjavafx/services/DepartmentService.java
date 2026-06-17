package org.study.worksjavafx.services;

import org.study.worksjavafx.dao.DaoFactory;
import org.study.worksjavafx.dao.DepartmentDao;
import org.study.worksjavafx.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao dao= DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return dao.findAll();
    }
}
