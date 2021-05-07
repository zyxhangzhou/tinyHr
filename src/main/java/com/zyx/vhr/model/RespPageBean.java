package com.zyx.vhr.model;

import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2021/5/3 11:23
 */
public class RespPageBean {
    private Long total;
    private List<?> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
