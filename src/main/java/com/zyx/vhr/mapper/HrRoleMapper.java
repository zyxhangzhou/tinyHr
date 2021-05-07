package com.zyx.vhr.mapper;

import com.zyx.vhr.model.HrRole;
import org.apache.ibatis.annotations.Param;

public interface HrRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrRole record);

    int insertSelective(HrRole record);

    HrRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HrRole record);

    int updateByPrimaryKey(HrRole record);

    void deleteByHrId(Integer hrid);

    Integer addRole(@Param("hrid") Integer hrid,@Param("rids")  Integer[] rids);
}