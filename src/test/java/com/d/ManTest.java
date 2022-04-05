package com.d;

import com.d.mapper.ManMapper;
import com.d.model.IdName;
import com.d.model.Man;
import com.github.dingey.common.util.JsonUtil;
import com.github.dingey.common.util.MapUtil;
import com.github.dingey.mybatis.mapper.lambda.*;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;


public class ManTest extends AbstractTest {

    @Resource
    private ManMapper manMapper;

    @Test
    public void list2Col() {
        List<Man> men = manMapper.selectList(new Select<Man>()
                .select(Man::getId, Man::getName));
        System.out.println(JsonUtil.toJson(men));
        Assert.assertNotEquals(0, men.size());
    }

    @Test
    public void selectCount() {
        long count = manMapper.selectCount(new Select<Man>()
                .select(Man::getId, Man::getName));
        System.out.println(JsonUtil.toJson(count));
        Assert.assertNotEquals(0, count);
    }

    @Test
    public void group() {
        Select<Man> select = new Select<>();
        select
                .select(Man::getSex)
                .select(select.max(Man::getAge))
                .groupBy(Man::getSex).having(select.max(Man::getAge).ge(1));

        List<Map<String, Object>> maps = manMapper.selectMaps(select);
        System.out.println(JsonUtil.toJson(maps));
        Assert.assertNotEquals(0, maps.size());
    }

    @Test
    public void list() {
        Select<Man> select = new Select<>();
        ColumnValue<Man> count = select.count(Man::getAge);
        select
                .select(Man::getIsDel)
                .select(count)
                .ge(Man::getId, 1)
                .groupBy(Man::getIsDel)
                .having(count.ge(1));

        List<Map<String, Object>> maps = manMapper.selectMaps(select);
        System.out.println(JsonUtil.toJson(maps));
        Assert.assertNotEquals(0, maps.size());
    }

    @Test
    public void list2col() {
        List<Map<String, Object>> maps = manMapper.selectMaps(new Select<Man>()
                .select(Man::getId)
                .select(Man::getName)
                .ge(Man::getId, 1));
        System.out.println(JsonUtil.toJson(maps));
        Assert.assertNotEquals(0, maps.size());
        List<IdName> idNames = MapUtil.toObjects(maps, IdName.class);
        System.out.println(JsonUtil.toJson(idNames));
    }

    @Data
    public static class IsDelCount {
        private Integer isDel;
        private Integer count;
    }

    @Test
    public void selectObjs() {
        List<Object> objects = manMapper.selectObjs(new Select<Man>()
                .select(Man::getName, Man::getAge)
                .ge(Man::getId, 1));
        System.out.println(JsonUtil.toJson(objects));
        Assert.assertNotEquals(0, objects.size());
    }

    @Test
    public void selectKVMap() {
        Map<Object, Object> map = manMapper.selectKVMap(new Select<Man>()
                .select(Man::getId, Man::getName)
                .ge(Man::getId, 1));
        System.out.println(JsonUtil.toJson(map));
        Assert.assertNotEquals(0, map.size());
    }

    @Test
    public void updateById() {
        Man man = new Man()
                .setId(1L)
                .setAge(777);
        int update = manMapper.updateById(man);
        Assert.assertNotEquals(0, update);
    }

    @Test
    public void updatePlus() {
        Update<Man> update1 = new Update<>();
        update1.set(Man::getAge, update1.val(Man::getAge).plus(1))
                .eq(Man::getId, 1);
        int update = manMapper.update(update1);
        Assert.assertNotEquals(0, update);
    }

    @Test
    public void updateMultiply() {
        Man m1 = manMapper.getById(1);
        Update<Man> manUpdate = new Update<>();
        int update = manMapper.update(manUpdate
                .set(Man::getAge, manUpdate.val(Man::getAge).multiply(5))
                .eq(Man::getId, 1));
        Assert.assertNotEquals(0, update);

        Man m2 = manMapper.getById(1);
        System.out.println();
        System.out.println("before " + m1);
        System.out.println("after " + m2);

        Assert.assertEquals(java.util.Optional.of(m1.getAge() * 5).get(), m2.getAge());
    }

    @Test
    public void insert() {
        Man m = new Man()
                .setName("Alice")
                .setAge(18)
                .setSex(1)
                .setCreated(LocalDateTime.now())
                .setIsDel(0)
                .setUpdated(LocalDateTime.now());
        int executeInsert = manMapper.insert(m);
        Assert.assertEquals(1, executeInsert);
    }

    @Test
    public void insertBatch() {
        Man m = new Man()
                .setName("Alice")
                .setAge(18)
                .setSex(1)
                .setCreated(LocalDateTime.now())
                .setIsDel(0)
                .setUpdated(LocalDateTime.now());
        int executeInsert = manMapper.insertBatch(Collections.singletonList(m));
        Assert.assertEquals(1, executeInsert);
    }

    @Test
    public void executeInsert() {
        Insert<Man> insert = new Insert<Man>()
                .insert(Man::getSex)
                .values(1);
        int executeInsert = manMapper.executeInsert(insert);
        Assert.assertEquals(1, executeInsert);
    }

    @Test
    public void executeInserts() {
        Man m1 = new Man()
                .setName("Alice")
                .setAge(18)
                .setSex(1)
                .setCreated(LocalDateTime.now())
                .setIsDel(0)
                .setUpdated(LocalDateTime.now());

        MysqlInsert<Man> insert = new MysqlInsert<Man>()
                .into(Man.class)
//                .ignore()
//                .replace()
                .values(Arrays.asList(m1, m1));
//        insert.onDuplicateKeyUpdate()
//                .set(Man::getUpdated, LocalDateTime.now());
        int executeInsert = manMapper.executeInsert(insert);
        Assert.assertEquals(2, executeInsert);
    }

    @Test
    public void page() {
        MysqlSelect<Man> select = new MysqlSelect<Man>()
                .select(Man::getId, Man::getName)
                .ge(Man::getId, 1)
                .eq(Man::getIsDel, 0);
        long count = manMapper.selectCount(select);
        System.out.println(count);

        List<Map<String, Object>> maps = manMapper.selectMaps(select.limit(2).orderByDesc(Man::getId));
        System.out.println(JsonUtil.toJson(maps));
        Assert.assertNotEquals(0, maps.size());
    }

    @Test
    public void pageOracle() {
        OracleSelect<Man> select = new OracleSelect<Man>()
                .select(Man::getId, Man::getName)
                .ge(Man::getId, 1)
                .eq(Man::getIsDel, 0);
        long count = manMapper.selectCount(select);
        System.out.println(count);

        List<Map<String, Object>> maps = manMapper.selectMaps(select.rowNum(0, 10).orderByDesc(Man::getId));
        System.out.println(JsonUtil.toJson(maps));
        Assert.assertNotEquals(0, maps.size());
    }

    @Test
    public void listByIds() {
        List<Long> longs = Arrays.asList(1L, 2L);
        List<Man> men = manMapper.listByIds(longs);
        System.out.println(JsonUtil.toJson(men));
        Assert.assertNotEquals(0, men.size());

        List<Integer> integers = Arrays.asList(1, 2);
        List<Man> men2 = manMapper.listByIds(integers);
        System.out.println(JsonUtil.toJson(men2));

        Set<String> set = Collections.singleton("3");
        List<Man> men3 = manMapper.listByIds(set);
        System.out.println(JsonUtil.toJson(men3));
    }

    @Test
    public void in() {
        MysqlSelect<Man> select = new MysqlSelect<Man>()
                .in(Man::getId, 1, 2, 3)
                .between(Boolean.FALSE, Man::getId, 1, 9)
                .like(Boolean.TRUE, Man::getName, "%A%")
                .isNotNull(Man::getIsDel).limit(1);
        List<Man> men = manMapper.selectList(select);
        System.out.println(JsonUtil.toJson(men));

        Man man = manMapper.selectOne(select);
        System.out.println(JsonUtil.toJson(man));
    }
}
