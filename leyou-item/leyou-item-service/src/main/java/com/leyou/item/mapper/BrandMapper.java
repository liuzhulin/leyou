package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand(category_id, brand_id) values (#{cId}, #{bId})")
    void insertCategoryBrand(@Param("bId") Long bId, @Param("cId") Long cId);

    @Select("select b.* from tb_brand b join tb_category_brand cb on cb.brand_id=b.id where cb.category_id=#{cid}")
    List<Brand> queryBrandsByCid(Long cid);
}
