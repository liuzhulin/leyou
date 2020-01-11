package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 根据父节点查询子节点
     * @param pId
     * @return
     */
    public List<Category> queryCategoryByPid(Long pId) {
        Category category = new Category();
        category.setParentId(pId);
        return categoryMapper.select(category);
    }
}
