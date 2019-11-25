package com.xxk.bookstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xxk.bookstore.dao.Dao;
import com.xxk.bookstore.utils.ReflectionUtils;

public class BaseDAO<T> implements Dao<T> {

	// private QueryRunner queryRunner = new QueryRunner();

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Class<T> clazz;

	public BaseDAO() {

		clazz = ReflectionUtils.getSuperGenericType(getClass());
	}

	@Override
	public long insert(String sql, Object... args) {
		long id = 0;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			if(args != null) {
				for(int i = 0; i < args.length;i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			preparedStatement.executeUpdate();
			// 获取生成的主键值
			resultSet = preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				id = resultSet.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return id;
	}

	@Override
	public void update(String sql, Object... args) {
		jdbcTemplate.update(sql, args);

	}

	@Override
	public T query(String sql, Object... args) {

		try {
			return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<T>(clazz), args);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<T> queryForList(String sql, Object... args) {
		List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(clazz), args);
		return list;
	}

	@Override
	public <V> V getSingleVal(String sql, Class<V> type, Object... args) {

		return jdbcTemplate.queryForObject(sql, type, args);

	}

	@Override
	public void batch(String sql, Object[]... params) {

		List<Object[]> list = new ArrayList<Object[]>();
		
		for (int i = 0; i < params.length; i++) {
			list.add(params[i]);
		}
		
		jdbcTemplate.batchUpdate(sql, list);

	}

}
