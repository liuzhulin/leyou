package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock;// 秒杀可用库存
    private Integer seckillTotal;// 已秒杀数量
    private Integer stock;// 正常库存
}