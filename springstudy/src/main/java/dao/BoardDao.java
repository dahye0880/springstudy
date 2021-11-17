package dao;
/*
 * JDBC 이용하여 db 연결
 * 1. 드라이버 연결 : Class.forName("드라이버 클래스 이름") -> 클래스화 시켜서 메모리에 로드하는거
 * 2. Connection 연결 객체 : DriverManager.getConnection("url","userid","password")
 * 3. Statement 객체를 만듬 : conn.createStatement()
 * 4. sql 구문실행 : stmt.executeUpdate() 
 */

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

@Repository
public class BoardDao {
	private NamedParameterJdbcTemplate template; //데이터베이스커넥션은 완성됐다라고 알아도 된다. template가 되면
	private Map<String,Object> param = new HashMap<String,Object>();
	private RowMapper<Board> mapper = new BeanPropertyRowMapper<Board>(Board.class); //래퍼
	@Autowired
	//변수, Setter메서드, 생성자, 일반 메서드에 적용이 가능하며 <property>, <constructor-arg>태그와 동일한 역할을 한다.
	//dataSource : 연결객체를 저장.
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	//board : 파라미터값 + 파일이름
	public void write(Board board) {//모든 클래스는 대문자로 시작한다
		int num = maxNum()+1;//첫번째 게시물ㅢ num값 1로 설정
		board.setNum(num); //원글이어서 num과 grp를 같이 집어넣어준것임
		board.setGrp(num);
		
		//board 객체의 프로퍼티값으로 sql의 파라미터 값으로 사용할거냐.
		//:num,:name,:pass,:subject,:content,:fileurl => sql 파라미터
		//sysdate 오라클에서 현재날짜
		//https://github.com/benelog/spring-jdbc-tips/blob/master/spring-jdbc-core.md -> Spring JDBC의 핵심 사용법
		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
		String sql = "insert into board "
				+ "(num,name,pass,subject,content,file1,regdate,readcnt,grp,grplevel,grpstep)"
				+" values (:num,:name,:pass,:subject,:content,:fileurl,sysdate,"
				+ "0,:grp,:grplevel,:grpstep)";
		template.update(sql, param);//db에 데이터 추가하기 //param 에는  board 객체가 들어간다.
	}
	private int maxNum() {
		/*
		 * select nvl(max(num),0) from board 구문실행
		 * param : 해당없음
		 * Integer.class : 결과값.
		 */
		return template.queryForObject("select nvl(max(num),0) from board", param,Integer.class);
	}
	public int count() {
		return template.queryForObject("select count(*) from board", param,Integer.class);
	}
	public List<Board> list(Integer pageNum, int limit) {
		param.clear();//Map객체에 저장된 데이터를 제거. //현재 param에 등록돼어 있는 요소를 제거하라. //param을 제일 윗단에 멤버변수로 만들어놓음
		/*
		 * 현재페이지    보여주는 게시물   startrow    endrow
		 *   1         1~10 번         1          10
		 *   2         11~20 번       11          20
		 */
		int startrow = (pageNum - 1) * limit + 1;
		int endrow = startrow + limit - 1;
		param.put("startrow", startrow);
		param.put("endrow", endrow);
		/*
		 * String sql = "select * from (select rownum rnum , num, name,subject, content,file1 fileurl,regdate,"
				    + " grp,grplevel,grpstep, pass, readcnt from "
				    + "(select * from board order by grp desc, grpstep asc))"
				    + " where rnum >= :startrow and rnum <= :endrow";
				    rownum 의 특징때문에 이런식으로 오라클에서 쓴다 페이징을 한다.
		 */
		String sql = "select * from (select rownum rnum , num, name,subject, content,file1 fileurl,regdate,"
				    + " grp,grplevel,grpstep, pass, readcnt from "
				    + "(select * from board order by grp desc, grpstep asc))"
				    + " where rnum >= :startrow and rnum <= :endrow";		
		return template.query(sql, param,mapper);
	}
	/*
	 * 조회되는 컬럼이           Board
	 *      num         => board.setNum(num컬럼의값)	 *     
	 *      name        => board.setName(name컬럼의값)
	 *      subject     => board.setSubject(subject컬럼의값)
	 *      content     => board.setContent(content컬럼의값)
	 *      .....
	 */
	//queryForObject : 조회되는 레코드가 한건인 경우 사용.
	//query          : 조회되는 레코드가 여러건인 경우 사용.
	
	//num에 해당하는 게시물을 조회하여 Board 객체로 리턴한다.
	public Board selectOne(Integer num) {
		param.clear();
		param.put("num", num);
		String sql = "select num, name,subject, content,file1 fileurl,regdate,"
					+ " grp,grplevel,grpstep, pass, readcnt from board where num=:num";
		//queryForObject : 조회되는 레코드가 한건인 경우.
		return template.queryForObject(sql, param, mapper); //sql을 실행하고 param을 넣고 mapper는 쿼리문에서 나온
	}
	public void readcntadd(Integer num) {
		param.clear();
		param.put("num",num);
		String sql = "update board set readcnt = readcnt+1 where num = :num";
		template.update(sql, param);
	}
	public void update(Board board) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
		String sql = "update board set name=:name, subject=:subject,content=:content,file1=:fileurl where num=:num";
		template.update(sql, param);//db에 데이터 수정하기
	}
	public void delete(int num) {
		param.clear();
		param.put("num", num);
		template.update("delete from board where num=:num", param);//db에 데이터 삭제하기
	}
	//답글의 grpstep 증가
	public void updateGrpStep(Board board) {//grp에 해당하는 것들만 업데이트 하자.
		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
		String sql = "update board set grpstep = grpstep+1 where grp = :grp and grpstep > :grpstep";
		template.update(sql, param);
	}
	//답글의 등록
	//1. board.setNum(maxnum + 1)
	//2. grp 변경은 안됨 (그룹이기 때문에 원글하고 똑같이 가야된다.)
	//3. grplevel, grpstep은 원글+1
	public void reply(Board board) {
		board.setNum(maxNum()+1);
		board.setGrplevel(board.getGrplevel()+1);
		board.setGrpstep(board.getGrpstep()+1);
		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
		String sql = "insert into board "
				+ "(num,name,pass,subject,content,file1,regdate,readcnt,grp,grplevel,grpstep)"
				+" values (:num,:name,:pass,:subject,:content,:fileurl,sysdate,"
				+ "0,:grp,:grplevel,:grpstep)";	
		template.update(sql, param);
	}
}
