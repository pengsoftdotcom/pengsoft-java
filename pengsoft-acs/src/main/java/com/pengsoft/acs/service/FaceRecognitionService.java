package com.pengsoft.acs.service;

import java.util.Map;

/**
 * The face recognition service.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface FaceRecognitionService {

    /**
     * 同步人员列表
     */
    Map<String, Object> syncPersons(Map<String, Object> params);

    /**
     * 同步人员详情
     */
    Map<String, Object> syncPerson(Map<String, Object> params);

    /**
     * 同步通行记录
     */
    Map<String, Object> syncAccessRecords(Map<String, Object> params);

    /**
     * 设备心跳
     */
    Map<String, Object> heartbeat(Map<String, Object> params);

}
