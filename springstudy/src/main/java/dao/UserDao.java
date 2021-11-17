package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;
import logic.User;

@Repository
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private Map<String,Object> param = new HashMap<String,Object>();
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount (userid, password, username, phoneno, postcode, "
				+ " address, email, birthday)"
				+ " values (:userid, :password, :username, :phoneno, :postcode, :address, :email, :birthday)";
		template.update(sql, param);
	}///////4교시 아래 코딩
	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);		
		return template.queryForObject("select * from useraccount where userid = :userid", param, mapper);
	}
	public String search(User user, String url) {
		String sql = null;
		if(url.equals("id")) {//idsearch 인 경우 // 오라클 db함수로 ** 처리 해줄수 있다. 와 이거 공부하자......
			sql ="select substr(userid,1,length(userid)-2) || '**' from useraccount "
					+ " where email=:email and phoneno=:phoneno";
		}
		else if(url.equals("pw")) {//pwsearch 인경우
			sql = "select '**'||substr(password,3,length(password)-2) from useraccount "
					+ " where userid=:userid and email=:email and phoneno=:phoneno";
		}
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		return template.queryForObject(sql, param, String.class);
	}
	public void update(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "update useraccount set username=:username, birthday=:birthday, phoneno=:phoneno, postcode=:postcode, address=:address, email=:email where userid=:userid";
		template.update(sql, param);
	}
	public void passwordUpdate(String userid, String chgpass) {
		String sql = "update useraccount set password=:password where userid=:userid";
		param.clear();
		param.put("userid", userid);
		param.put("password", chgpass);
		template.update(sql, param);
	}
	
	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		template.update("delete from useraccount where userid=:userid", param);
	}
	public List<User> list() {
		return template.query("select * from useraccount", param,mapper);
	}
	/*
	 * test1, test3
	 * 
	 * select * from useraccount where userid in ('test1','test3')
	 */
	public List<User> list(String[] idchks) {
		String ids = ""; //'test1','test'
		for(int i=0;i<idchks.length;i++) {
			ids += "'" + idchks[i] + ((i==idchks.length-1)?"'":"',");
		}
		String sql = "select * from useraccount where userid in ("+ids+")";
		return template.query(sql, param, mapper);//select해서 mapper 정보에 매퍼하기.
	}
	
	
	
}
