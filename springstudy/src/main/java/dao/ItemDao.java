package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;
import logic.Item;

@Repository
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private Map<String,Object> param = new HashMap<String,Object>();
	private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public List<Item> list() {
		return template.query("select * from item order by id desc", param,mapper);
	}	
	
	public Item selectOne(Integer id) {
		param.clear();
		param.put("id", id);		
		return template.queryForObject("select * from item where id = :id", param, mapper);
	}
	
	public int maxNo() {
		return template.queryForObject("select nvl(max(id),0) from item", param,Integer.class);
	}
	public void insert(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		String sql = "insert into item "
				+ " (id,name,price,description,pictureUrl)"
				+" values (:id,:name,:price,:description,:pictureUrl)";
				
		template.update(sql, param);//db에 데이터 추가하기 //param 에는  board 객체가 들어간다.
		
	}

	

}
