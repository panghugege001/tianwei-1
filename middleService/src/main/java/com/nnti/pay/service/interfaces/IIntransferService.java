package com.nnti.pay.service.interfaces;

import com.nnti.pay.controller.vo.IntransferVo;
import com.nnti.pay.model.vo.Intransfer;

import java.util.List;

/**
 * Created by wander on 2017/3/8.
 */
public interface IIntransferService {

    int create(Intransfer intransfer);

    int createBatch(List<Intransfer> intransfers);

    void batchExcute(IntransferVo vo) throws Exception;
}
