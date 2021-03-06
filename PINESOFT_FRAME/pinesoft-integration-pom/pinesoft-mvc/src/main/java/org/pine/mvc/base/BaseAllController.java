package org.pine.mvc.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.pine.ibaits.mapper.contract.IMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


public class BaseAllController<T> extends BaseMapperController<T> {
	
	public BaseAllController(IMapper<T> mapper) {
		super(mapper);
		// TODO Auto-generated constructor stub
	}

	@GetMapping(value = "all")
	public List<T> selectAll() throws Exception {
		return mapper.selectAll();
	}

	@PostMapping(value = "record")
	public List<T> select(@RequestBody T record) throws Exception {
		return mapper.select(record);
	}

	@GetMapping(value = { "{key}", "key/{key}" })
	public T selectByPrimaryKey(@PathVariable String key) throws Exception {

		return mapper.selectByPrimaryKey(key);
	}

	@PostMapping(value = "one")
	public T selectOne(@RequestBody T record) throws Exception {

		return mapper.selectOne(record);
	}

	@PostMapping(value = "condition")
	public List<T> selectByExample(@RequestBody Object condition) throws Exception {

		return mapper.selectByExample(condition);
	}

	@PostMapping(value = "count")
	public int selectCount(@RequestBody T record) throws Exception {

		return mapper.selectCount(record);
	}

	@PostMapping(value = "count/condition")
	public int selectCountByExample(@RequestBody Object condition) throws Exception {

		return mapper.selectCountByExample(condition);
	}

	@DeleteMapping(value = "condition")
	public int deleteByExample(@RequestBody Object condition) throws Exception {

		return mapper.deleteByExample(condition);
	}

	@DeleteMapping(value = "")
	public int delete(@RequestBody T record) throws Exception {

		return mapper.delete(record);
	}

	@DeleteMapping(value = { "{key}", "key/{key}" })
	public int deleteByPrimaryKey(@PathVariable String key) throws Exception {
		return mapper.deleteByPrimaryKey(key);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@PutMapping(value = "conditon/selective")
	public int updateByExampleSelective(@RequestBody Map<String, Object> map) throws Exception {
		final String recordKey = "record";
		final String conditonKey = "condition";
		Class<T> entityClass;
		try {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		} catch (Exception e) {
			throw new Exception("无法获取泛型T class！");
		}

		Object recordObj = map.get(recordKey);
		Object conditionObj = map.get(conditonKey);
		if (recordObj == null) {
			throw new Exception("不存在record键值！");
		}
		if (conditionObj == null) {
			throw new Exception("不存在condition键值！");
		}
		return 0;
	}

	@PutMapping(value = "key")
	public int updateByPrimaryKey(@RequestBody T record) throws Exception {

		return mapper.updateByPrimaryKey(record);
	}

	@PutMapping(value = "key/selective")
	public int updateByPrimaryKeySelective(@RequestBody T record) throws Exception {

		return mapper.updateByPrimaryKeySelective(record);
	}

	@PutMapping(value = "condition")
	public int updateByExample(@RequestBody Map<String, Object> map) throws Exception {

		return 0;
	}

	@PostMapping
	public int insert(@Validated @RequestBody T record) throws Exception {

		return mapper.insert(record);
	}

	@PostMapping(value = "selective")
	public int insertSelective(@RequestBody T record) throws Exception {

		return mapper.insertSelective(record);
	}
	
	@Transactional
	@PostMapping(value = "all")
	public int insertAll( @RequestBody List<T> records) throws Exception {
		int count = 0;
		for (T record : records) {
			count = count + mapper.insert(record);
		}
		return count;
	}

	@Transactional
	@PostMapping(value = "all/selective")
	public int insertAllSelective(@RequestBody List<T> records) throws Exception {
		int count = 0;
		for (T record : records) {
			count = count + mapper.insertSelective(record);
		}
		return count;
	}

	@GetMapping(value = "/copy/{key}")
	public int copyByPrimaryKey(@PathVariable("key") String key) throws Exception {
		T record = selectByPrimaryKey(key);
		return mapper.insert(record);
	}
}
