package com.zyx.vhr.utils;

import com.zyx.vhr.model.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author Zhang Yuxiao
 * @Date 2021/4/30 22:40
 */
public class HrUtils {
    public static Hr getCurrentHr() {
        return ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
