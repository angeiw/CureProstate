package com.redcity.cureprostate.service;

import android.os.Handler;

import com.redcity.cureprostate.vo.ParamVo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

/**
 * Created by COREIGHT-101 on 2018/8/20.
 */

public interface IService {
    boolean verifyParameter( short electricityParamer,short voltageParamer,int coulombParamer,short electricityRateParamer);
    void sendData(Handler sendHandler, ParamVo vo);
}
