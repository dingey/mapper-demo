package com.d.util;

import com.github.dingey.mybatis.mapper.exception.MapperException;
import com.github.dingey.mybatis.mapper.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unused")
public class MapUtils {

    private static ConversionService conversionService;
    private static boolean spring = false;

    @PostConstruct
    public void init() {
        spring = true;
    }

    public static <T> List<T> toObjects(List<Map<String, Object>> list, Class<T> tClass) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        if (!spring) {
            throw new MapperException("is not spring env,unsupported!");
        }
        List<T> res = new ArrayList<>();
        for (Map<String, Object> map : list) {
            T t = BeanUtils.instantiateClass(tClass);
            for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(tClass)) {
                String s = StringUtils.snakeCase(pd.getName()).toUpperCase();
                Object o = map.get(s);
                if (o == null) {
                    continue;
                }
                if (o.getClass() != pd.getPropertyType()) {
                    o = conversionService.convert(o, pd.getPropertyType());
                }

                try {
                    pd.getWriteMethod().invoke(t, o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MapperException(e.getMessage(), e);
                }
            }
            res.add(t);
        }
        return res;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        MapUtils.conversionService = conversionService;
    }
}
