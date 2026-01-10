package com.jsdc.core.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public class BaseService<T> extends ServiceImpl<BaseMapper<T>,T> {
}
