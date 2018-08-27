package com.redcity.cureprostate.vo;

/**
 * Created by COREIGHT-101 on 2018/8/27.
 */

public class ParamVo {

    public short electricityParamer;
    public short voltageParamer;
    public int coulombParamer;
    public short electricityRateParamer ;

    @Override
    public String toString() {
        return "ParamVo{" +
                "electricityParamer=" + electricityParamer +
                ", voltageParamer=" + voltageParamer +
                ", coulombParamer=" + coulombParamer +
                ", electricityRateParamer=" + electricityRateParamer +
                '}';
    }
}
