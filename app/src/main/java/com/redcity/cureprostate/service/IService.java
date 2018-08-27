package com.redcity.cureprostate.service;

import java.util.Objects;

/**
 * Created by COREIGHT-101 on 2018/8/20.
 */

public interface IService {
    boolean verifyParameter( short electricityParamer,short voltageParamer,int coulombParamer,short electricityRateParamer);
    void sendData(byte order,Object o);
}
