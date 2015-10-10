package com.shivajivarma.brs.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.shivajivarma.brs.model.entity.Reserve;

/**
 * @author: Shivaji Varma (contact@shivajivarma.com)
 */
public class ReserveDAOImpl extends BaseDAO implements ReserveDAO {
		
	
	public ReserveDAOImpl(){
		this.table = Reserve.indentity;
	}
	
	@Override
	public int save(Reserve reserve){
		
		String query = "INSERT INTO "+table+" VALUES(TID_AUTO.NEXTVAL,?,?,?,?,?)";
		
		getJdbcTemplate().update(query, 
				new Object[] { 
				reserve.getPid(),
				reserve.getBid(),
				reserve.getDt(),
				reserve.getTstamp(),
				reserve.getSeat()});
		
		query = "select max(id) from "+table;
		
		return Integer.parseInt(getJdbcTemplate().queryForObject(query, 
				new Object[] {}, String.class));
	}
	
	public int findNewId(){
		String query = "select TID_AUTO.currval from DUAL";
		
		return (Integer) getJdbcTemplate().queryForObject(query, 
        		new BeanPropertyRowMapper<Integer>(Integer.class));
	}

	
	@Override
    public Reserve findById(long id) throws EmptyResultDataAccessException{
        String query = "select * from "+table+" where id = ?";
        
        //query single row with BeanPropertyRowMapper (Passenger.class)
        Reserve reserve = (Reserve) getJdbcTemplate().queryForObject(query, 
        		new Object[] { id }, 
				new BeanPropertyRowMapper<Reserve>(Reserve.class));
       
        return reserve;
    }

	@Override
	public List<Integer> getSeatNumbersByBusAndDate(long bid, String date) throws EmptyResultDataAccessException{
		String query = "select SEAT from RESERVE where bid=? and dt=?";
			
	 	List<Integer> seatNumbers = new ArrayList<Integer>();
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(query,new Object[] {bid,date});
		for (Map<String, Object> row : rows) {
			seatNumbers.add(((BigDecimal) row.get("SEAT")).intValue());
		}
			
		return seatNumbers;
	 }
}
