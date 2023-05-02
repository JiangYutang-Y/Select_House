package com.selecthome.utils;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;

public class IDUtils {

    static {
        // 创建 IdGeneratorOptions 对象，可在构造函数中输入 WorkerId：
        // TODO: 多实例情况下需要修改 worker id
        IdGeneratorOptions options = new IdGeneratorOptions((short)1);
        YitIdHelper.setIdGenerator(options);
    }

    /**
     * 生成唯一 ID
     *
     */
    public static Long generateId() {
        return YitIdHelper.nextId();
    }
}
