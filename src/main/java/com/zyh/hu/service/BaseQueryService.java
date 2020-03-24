package com.zyh.hu.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zyh.hu.utils.DateUtil;


/**
 * 数据访问服务层
 * @author HU
 *
 */
@Repository("BaseQueryService")
public class BaseQueryService<E extends Serializable> {

	private static final Logger logger = LoggerFactory.getLogger(BaseQueryService.class);

	@PersistenceContext
	private EntityManager em;
	private static SessionFactory sf;
	protected Class<E> entityClass = null;
	
	@SuppressWarnings("unchecked")
	public BaseQueryService(){
		if (getClass().getGenericSuperclass() instanceof ParameterizedType){
			if (! (((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0] instanceof TypeVariable)){
				entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			}
		}
	}
	
	public void flush(){
		em.flush();
	}
	
	public void clear(){
		em.flush();
		em.clear();
	}
	
	public void detach(E entity){
		em.detach(entity);
	}
	
	public void detachAll(Collection<E> entities){
		for (E entity:entities){
			this.detach(entity);
		}
	}
	
	public boolean contains(E entity){
		return em.contains(entity);
	}
	
	public void save(E entity){
		if (! em.contains(entity)){
			em.merge(entity);
		}
	}
	
	public void save(Collection<E> entities){
		for (E entity:entities){
			save(entity);
		}
	}
	
	public void insert(E entity){
		em.persist(entity);
	}
	
	public void insert(Collection<E> entities){
		for (E entity:entities){
			insert(entity);
		}
	}
	
	public void delete(E entity){
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}
	
	public void delete(Object id){
		delete(load(id));
	}
	
	public E load(Object id){
		return em.find(entityClass, id);
	}
	
	/**
	 * 使用hql查询语句返回单个entity对象，使用位置参数
	 * @param hql
	 * @param values
	 * @return
	 */
	protected E findOneEntityObject(final String hql, final Object[] values){
		return findOneEntityObject(hql, null, values);
	}
	
	/**
	 * 使用hql查询语句返回单个entity对象，使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @return
	 */
	protected E findOneEntityObject(final String hql, final Map<String, Object> conditionMap){
		return findOneEntityObject(hql, conditionMap, null);
	}
	
	@SuppressWarnings("unchecked")
	protected E findOneEntityObject(final String hql, final Map<String, Object> map, final Object[] values){
		if (hql != null){
			Query query = em.createQuery(hql, entityClass);
			setParameters(query, map, values);
			return (E) getSingleResult(query);
		}else {
			return null;
		}
	}
	
	/**
	 * 使用hql查询语句返回entity对象列表，使用位置参数
	 * @param hql
	 * @param values
	 * @return
	 */
	protected List<E> findEntityObjects(final String hql, final Object[] values){
		return findAllEntityObjects(hql, null, values);
	}
	
	/**
	 * 使用hql查询语句返回entity对象列表，使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @return
	 */
	protected List<E> findEntityObjects(final String hql, final Map<String, Object> conditionMap){
		return findAllEntityObjects(hql, conditionMap, null);
		
	}
	
	@SuppressWarnings("unchecked")
	protected List<E> findAllEntityObjects(final String hql, final Map<String, Object> conditionMap, final Object[] values){
		if (hql != null){
			Query query = em.createQuery(hql, entityClass);
			setParameters(query, conditionMap, values);
			return query.getResultList();
		}else {
			return null;
		}
	}
	
	/**
	 * 使用hql查询语句若对象非entity对象，返回Object[],使用位置参数
	 * @param hql
	 * @param values
	 * @return
	 */
	protected Object findOneResultObject(final String hql, final Object[] values){
		return findOneResultObject(hql, null, values);
	}
	
	/**
	 * 使用hql查询语句若对象非entity对象，返回Object[],使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @return
	 */
	protected Object findOneResultObject(final String hql, final Map<String, Object> conditionMap){
		return findOneResultObject(hql, conditionMap, null);
	}
	
	@SuppressWarnings("unchecked")
	protected Object findOneResultObject(final String hql, final Map<String, Object> conditionMap, final Object[] values){
		if (hql != null){
			Query query = em.createQuery(hql);
			setParameters(query, conditionMap, values);
			return (E) getSingleResult(query);
		}else {
			return null;
		}
	}
	
	/**
	 * 使用hql查询语句若对象非entity对象，返回Object[]的列表,使用位置参数
	 * @param hql
	 * @param values
	 * @return
	 */
	protected List<?> findResultObjects(final String hql, final Object[] values){
		return findAllResultObjects(hql, null, values);
	}
	
	/**
	 * 使用hql查询语句若对象非entity对象，返回Object[]的列表,使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @return
	 */
	protected List<?> findResultObjects(final String hql, final Map<String, Object> conditionMap){
		return findAllResultObjects(hql, conditionMap, null);
	}
	
	protected List<?> findAllResultObjects(final String hql, final Map<String, Object> conditionMap, final Object[] values){
		if (hql != null){
			Query query = em.createQuery(hql);
			setParameters(query, conditionMap, values);
			return query.getResultList();
		}else {
			return null;
		}
	}
	
	/**
	 * 使用hql查询语句返回分页的entity对象列表，使用位置参数
	 * @param hql
	 * @param values
	 * @param pageRequest
	 * @return
	 */
	protected Page<E> findEntityObjects(final String hql, final Object[] values, final Pageable pageRequest){
		return findEntityObjects(hql, null, values, pageRequest);
	}
	
	/**
	 * 使用hql查询语句返回分页的entity对象列表，使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @param pageRequest
	 * @return
	 */
	protected Page<E> findEntityObjects(final String hql, final Map<String, Object> conditionMap, final Pageable pageRequest){
		return findEntityObjects(hql, conditionMap, null, pageRequest);
	}
	
	@SuppressWarnings("unchecked")
	protected Page<E> findEntityObjects(final String hql, final Map<String, Object> conditionMap, final Object[] values, final Pageable pageRequest){
		if (hql != null && pageRequest.getOffset()>=0 && pageRequest.getPageSize()>0){
			long total = findCount(hql, conditionMap, values);
			Query query = em.createQuery(hql, entityClass);
			setParameters(query, conditionMap, values);
			query.setFirstResult(pageRequest.getOffset());
			query.setMaxResults(pageRequest.getPageSize());
			List<E> content = query.getResultList();
			return new PageImpl<E>(content, pageRequest, total);
		}else{
			return null;
		}
	}
	
	/**
	 * 使用hql查询语句返回分页的查询结果对象列表，使用位置参数
	 * @param hql
	 * @param values
	 * @param pageRequest
	 * @return
	 */
	protected Page<Object[]> findResultObjects(final String hql, final Object[] values, final Pageable pageRequest){
		return findResultObjects(hql, null, values, pageRequest);
	}
	
	/**
	 * 使用hql查询语句返回分页的查询结果对象列表，使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @param pageRequest
	 * @return
	 */
	protected Page<Object[]> findResultObjects(final String hql, final Map<String, Object> conditionMap, final Pageable pageRequest){
		return findResultObjects(hql, conditionMap, null, pageRequest);
	}
	
	@SuppressWarnings("unchecked")
	protected Page<Object[]> findResultObjects(final String hql, final Map<String, Object> conditionMap, final Object[] values, final Pageable pageRequest){
		if (hql != null && pageRequest.getOffset()>=0 && pageRequest.getPageSize()>0){
			long total = findCount(hql, conditionMap, values);
			Query query = em.createQuery(hql);
			setParameters(query, conditionMap, values);
			query.setFirstResult(pageRequest.getOffset());
			query.setMaxResults(pageRequest.getPageSize());
			List<Object[]> content = query.getResultList();
			return new PageImpl<Object[]>(content, pageRequest, total);
		}else{
			return null;
		}
	}
	
	/**
	 * 使用sql查询返回单个Entity对象，使用位置参数
	 * @param sql
	 * @param values
	 * @return
	 */
	protected E findOneEntityObjectBySql(final String sql, final Object[] values){
		return findOneEntityObjectBySql(sql, null, values);
	}
	
	/**
	 * 使用sql查询返回单个Entity对象，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @return
	 */
	protected E findOneEntityObjectBySql(final String sql, final Map<String, Object> conditionMap){
		return findOneEntityObjectBySql(sql, conditionMap, null);
	}
	
	@SuppressWarnings("unchecked")
	protected E findOneEntityObjectBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values){
		if (sql != null){
			Query query = em.createNativeQuery(sql, entityClass);
			setParameters(query, conditionMap, values);
			return (E) getSingleResult(query);
		}else{
			return null;
		}
	}
	
	/**
	 * 使用sql查询返回Entity对象列表，使用位置参数
	 * @param sql
	 * @param values
	 * @return
	 */
	protected List<E> findEntityObjectsBySql(final String sql, final Object[] values){
		return findAllEntityObjectsBySql(sql, null, values);
	}
	
	/**
	 * 使用sql查询返回Entity对象列表，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @return
	 */
	protected List<E> findEntityObjectsBySql(final String sql, final Map<String, Object> conditionMap){
		return findAllEntityObjectsBySql(sql, conditionMap, null);
	}
	
	@SuppressWarnings("unchecked")
	protected List<E> findAllEntityObjectsBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values){
		if (sql != null){
			Query query = em.createNativeQuery(sql, entityClass);
			setParameters(query, conditionMap, values);
			return query.getResultList();
		}else{
			return null;
		}
	}
	
	/**
	 * 使用sql查询返回结果对象列表，使用位置参数
	 * @param sql
	 * @param values
	 * @return
	 */
	protected List<?> findResultObjectsBySql(final String sql, final Object[] values){
		return findAllResultObjectsBySql(sql, null, values);
	}
	
	/**
	 * 使用sql查询返回结果对象列表，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @return
	 */
	protected List<?> findResultObjectsBySql(final String sql, final Map<String, Object> conditionMap){
		return findAllResultObjectsBySql(sql, conditionMap, null);
	}
	
	protected List<?> findAllResultObjectsBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values){
		if (sql != null){
			Query query = em.createNativeQuery(sql);
			setParameters(query, conditionMap, values);
			return query.getResultList();
		}else{
			return null;
		}
	}
	
	/**
	 * 使用sql查询语句返回单个查询结果，使用位置参数
	 * @param sql
	 * @param values
	 * @return
	 */
	protected Object findOneResultObjectBySql(final String sql, final Object[] values){
		return findOneResultObjectBySql(sql, null, values);
	}
	
	/**
	 * 使用sql查询语句返回单个查询结果，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @return
	 */
	protected Object findOneResultObjectBySql(final String sql, final Map<String, Object> conditionMap){
		return findOneResultObjectBySql(sql, conditionMap, null);
	}
	
	protected Object findOneResultObjectBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values){
		if (sql != null){
			Query query = em.createNativeQuery(sql);
			setParameters(query, conditionMap, values);
			return getSingleResult(query);
		}else{
			return null;
		}
	}
	
	/**
	 * 使用sql查询语句返回分页的entity对象列表，使用位置参数
	 * @param sql
	 * @param values
	 * @param pageRequest
	 * @return
	 */
	protected Page<E> findEntityObjectsBySql(final String sql, final Object[] values, final Pageable pageRequest){
		return findEntityObjectsBySql(sql, null, values, pageRequest);
	}
	
	/**
	 * 使用sql查询语句返回分页的entity对象列表，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @param pageRequest
	 * @return
	 */
	protected Page<E> findEntityObjectsBySql(final String sql, final Map<String, Object> conditionMap, final Pageable pageRequest){
		return findEntityObjectsBySql(sql, conditionMap, null, pageRequest);
	}
	
	@SuppressWarnings("unchecked")
	protected Page<E> findEntityObjectsBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values, final Pageable pageRequest){
		if (sql != null && pageRequest.getOffset()>=0 && pageRequest.getPageSize()>0){
			long total = findCountBySql(sql, conditionMap, values);
			Query query = em.createNativeQuery(sql, entityClass);
			setParameters(query, conditionMap, values);
			query.setFirstResult(pageRequest.getOffset());
			query.setMaxResults(pageRequest.getPageSize());
			List<E> content = query.getResultList();
			return new PageImpl<E>(content, pageRequest, total);
		}else{
			return null;
		}
	}
	
	/**
	 * 使用sql查询语句返回分页的entity对象列表，使用位置参数
	 * @param sql
	 * @param values
	 * @param pageRequest
	 * @return
	 */
	protected Page<Object[]> findResultObjectsBySql(final String sql, final Object[] values, final Pageable pageRequest){
		return findResultObjectsBySql(sql, null, values, pageRequest);
	}
	
	/**
	 * 使用sql查询语句返回分页的entity对象列表，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @param pageRequest
	 * @return
	 */
	protected Page<Object[]> findResultObjectsBySql(final String sql, final Map<String, Object> conditionMap, final Pageable pageRequest){
		return findResultObjectsBySql(sql, conditionMap, null, pageRequest);
	}
	
	@SuppressWarnings("unchecked")
	protected Page<Object[]> findResultObjectsBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values, final Pageable pageRequest){
		if (sql != null && pageRequest.getOffset()>=0 && pageRequest.getPageSize()>0){
			long total = findCountBySql(sql, conditionMap, values);
			Query query = em.createNativeQuery(sql);
			setParameters(query, conditionMap, values);
			query.setFirstResult(pageRequest.getOffset());
			query.setMaxResults(pageRequest.getPageSize());
			List<Object[]> content = query.getResultList();
			return new PageImpl<Object[]>(content, pageRequest, total);
		}else{
			return null;
		}
	}
	
	/**
	 * 使用hql语句更新或删除，返回影响行数，使用位置参数
	 * @param hql
	 * @param values
	 * @return
	 */
	protected int bulkUpdate(final String hql, final Object[] values){
		return bulkUpdate(hql, null, values);
	}
	
	/**
	 * 使用hql语句更新或删除，返回影响行数，使用命名参数
	 * @param hql
	 * @param conditionMap
	 * @return
	 */
	protected int bulkUpdate(final String hql, final Map<String, Object> conditionMap){
		return bulkUpdate(hql, conditionMap, null);
	}
	
	protected int bulkUpdate(final String hql, final Map<String, Object> conditionMap, final Object[] values){
		if (hql != null){
			Query query = em.createQuery(hql);
			setParameters(query, conditionMap, values);
			return query.executeUpdate();
		}else{
			return 0;
		}
	}
	
	/**
	 * 使用sql语句更新或删除，返回影响行数，使用位置参数
	 * @param sql
	 * @param values
	 * @return
	 */
	protected int bulkUpdateBySql(final String sql, final Object[] values){
		return bulkUpdateBySql(sql, null, values);
	}
	
	/**
	 * 使用sql语句更新或删除，返回影响行数，使用命名参数
	 * @param sql
	 * @param conditionMap
	 * @return
	 */
	protected int bulkUpdateBySql(final String sql, final Map<String, Object> conditionMap){
		return bulkUpdateBySql(sql, conditionMap, null);
	}
	
	protected int bulkUpdateBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values){
		if (sql != null){
			Query query = em.createNativeQuery(sql);
			setParameters(query, conditionMap, values);
			return query.executeUpdate();
		}else{
			return 0;
		}
	}
	
	private long findCount(final String hql, Map<String, Object> conditionMap, final Object[] values){
		String sql = hqlToSql(hql);
		if (conditionMap != null && !conditionMap.isEmpty()){
			Object[] paras = mapToArray(hql, conditionMap);
			sql = improveSql(sql);
			return findCountBySql(sql, null, paras);
		}else{
			return findCountBySql(sql, null, values);
		}
	}
	
	private String hqlToSql(final String hql){
		if (StringUtils.isEmpty(hql)){
			return hql;
		}
		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(hql, hql, Collections.EMPTY_MAP, (SessionFactoryImplementor)getSessionFactory() );
		queryTranslator.compile(Collections.EMPTY_MAP, true);
		String sql = queryTranslator.getSQLString();
		return sql;
	}
	
	private SessionFactory getSessionFactory(){
		Session session = (Session) em.getDelegate();
		sf = session.getSessionFactory();
		if (sf == null)
			sf = session.getSessionFactory();
		return sf;
	}
	
	private Object[] mapToArray(final String hql, final Map<String, Object> conditionMap){
		String tmp = " " + hql + " ";
		Object[] param = new Object[conditionMap.size()];
		int i = 0;
		while(true){
			int start = tmp.indexOf(':');
			if (start<0)
				break;
			tmp = tmp.substring(start + 1);
			int end = tmp.indexOf(' ');
			String key;
			if (end<0){
				key = tmp.substring(0, tmp.length());
			}else{
				key = tmp.substring(0, end);
			}
			int m = key.indexOf(')');
			if (m>0){
				key = key.substring(0, m);
			}
			param[i++] = conditionMap.get(key);
		}
		return param;
	}
	
	/**
	 * 对SQL中"?"的后面加上序号，比如?1,?2,?3
	 * @param sql
	 * @return
	 */
	private String improveSql(String sql){
		sql = " " + sql + " ";
		String[] split = sql.split("[?]");
		for(int i=0;i<split.length;i++){
			if (i != split.length-1)
		split[i] += " ?" + (i+1) + " " ;
		}
		return toStr(split);
	}
	
	private String toStr(Object[] param){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<param.length;i++){
			sb.append(param[i].toString());
		}
		return sb.toString();
	}
	
	private long findCountBySql(final String sql, final Map<String, Object> conditionMap, final Object[] values){
		if (sql != null){
			Object obj = findOneResultObjectBySql("select count(*) from ( " + sql +" )", conditionMap, values);
			return ((BigDecimal)obj).longValue();
		}else{
			return 0;
		}
	}
	/**
	 * 使用SQL进行增删改操作，返回影响条数
	 * @param sql
	 * @return
	 */
	@Transactional
	protected int getUpdateForSql(final String sql) {
		if (sql != null) {
			Query query = em.createNativeQuery(sql);
			return query.executeUpdate();
		} else {
			return 0;
		}
	}
	
	/**
	 * 限制执行时间为90秒,超时直接退出
	 * @param sql
	 * @return
	 * @throws NumberFormatException
	 * @throws TimeoutException
	 */
	@SuppressWarnings({ "unchecked" })
	protected List<Map<String, Object>> getSelectForSql(final String sql) throws NumberFormatException, TimeoutException{
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<List<Map<String, Object>>> future = new FutureTask<List<Map<String, Object>>>(new Callable<List<Map<String, Object>>>() {
			public List<Map<String, Object>> call() throws Exception {
				logger.info("---执行开始时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				Query query = em.createNativeQuery(sql);
				query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.getResultList();
			}
		});

		int exeSqlTimeout = 90;

		try {
			executor.execute(future);
			List<Map<String, Object>> result = future.get(1000*exeSqlTimeout, TimeUnit.MILLISECONDS);
			logger.info("---执行结束时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			return result;

		} catch (TimeoutException e) { 
			throw new TimeoutException("---执行超时！系统规定执行时间不能超过" + exeSqlTimeout + "秒");

		} catch (Exception e) {
			throw new RuntimeException("---执行异常:" + e.getMessage());

		} finally {

			future.cancel(true);
			executor.shutdown();

		}
		
	}
	
	private void setParameters(Query query, Map<String, Object> map, Object[] values){
		if (map != null && map.size()>0){
			for (Map.Entry<String, Object> entry : map.entrySet()){
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}else if (null != values && values.length>0){
			for (int i = 0; i<values.length; ++i){
				query.setParameter(i+1, values[i]);
			}
		}
	}
	
	private Object getSingleResult(Query query){
		return query.getSingleResult();
	}
	
}
