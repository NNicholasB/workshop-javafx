package org.study.worksjavafx.dao;


import org.study.worksjavafx.dao.impl.DepartmentDaoJDBC;
import org.study.worksjavafx.dao.impl.SellerDaoJDBC;
import org.study.worksjavafx.db.DB;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
