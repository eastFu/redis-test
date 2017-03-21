package cn.east.redis.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import cn.east.redis.dao.IUserDao;
import cn.east.redis.dao.impl.UserDaoImpl;
import cn.east.redis.model.User;
import junit.framework.Assert;


//@ContextConfiguration(locations = { "classpath*:spring.xml" })
public class RedisSpringTest {

	@Before
	public void init(){
		ApplicationContext app=new ClassPathXmlApplicationContext("classpath:spring.xml");
		userDaoImpl= app.getBean(UserDaoImpl.class);
	}
	private UserDaoImpl userDaoImpl;


	/**
	 * 新增 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testAddUser() {
		User user = new User();
		user.setId("user1");
		user.setName("java2000_wl");
		boolean result = userDaoImpl.add(user);
		Assert.assertTrue(result);
	}

	/**
	 * 批量新增 普通方式 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testAddUsers1() {
		List<User> list = new ArrayList<User>();
		for (int i = 10; i < 50000; i++) {
			User user = new User();
			user.setId("user" + i);
			user.setName("java2000_wl" + i);
			list.add(user);
		}
		long begin = System.currentTimeMillis();
		for (User user : list) {
			userDaoImpl.add(user);
		}
		System.out.println(System.currentTimeMillis() - begin);
	}

	/**
	 * 批量新增 pipeline方式 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testAddUsers2() {
		List<User> list = new ArrayList<User>();
		for (int i = 10; i < 1500000; i++) {
			User user = new User();
			user.setId("user" + i);
			user.setName("java2000_wl" + i);
			list.add(user);
		}
		long begin = System.currentTimeMillis();
		boolean result = userDaoImpl.add(list);
		System.out.println(System.currentTimeMillis() - begin);
		Assert.assertTrue(result);
	}

	/**
	 * 修改 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testUpdate() {
		User user = new User();
		user.setId("user1");
		user.setName("new_password");
		boolean result = userDaoImpl.update(user);
		Assert.assertTrue(result);
	}

	/**
	 * 通过key删除单个 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testDelete() {
		String key = "user1";
		userDaoImpl.delete(key);
	}

	/**
	 * 批量删除 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testDeletes() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			list.add("user" + i);
		}
		userDaoImpl.delete(list);
	}

	/**
	 * 获取 <br>
	 * ------------------------------<br>
	 */
	@Test
	public void testGetUser() {
		String id = "user1";
		User user = userDaoImpl.get(id);
		System.out.println(user.getName());
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getName(), "password");
	}

	
}
