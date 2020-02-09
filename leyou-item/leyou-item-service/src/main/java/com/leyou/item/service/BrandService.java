package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;

    /**
     * 根据品牌条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        PageHelper.startPage(page, rows);
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //先新增brand表
        brandMapper.insertSelective(brand);
        //再新增中间表
        cids.forEach(cId -> brandMapper.insertCategoryBrand(brand.getId(), cId));
    }

    /**
     * 根据分类id查询品牌
     * @param cid
     * @return
     */
    public List<Brand> queryBrandsByCid(Long cid) {
        return brandMapper.queryBrandsByCid(cid);
    }
}
