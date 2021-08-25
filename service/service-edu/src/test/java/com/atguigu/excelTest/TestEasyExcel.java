package com.atguigu.excelTest;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {

        String fileName = "D:\\11.xlsx";

        /*
        * 写的参数，页面下标 执行写操作
        * 文件地址名称，文件内容格式
        * 执行写操作需要传入数据
        * */
       //EasyExcel.write(fileName,DemoData.class).sheet("写入方法一").doWrite(data());
        EasyExcel.read(fileName,DemoData.class,new ExcelListenner()).sheet().doRead();

    }

    private static List<DemoData> data(){

        List<DemoData> list = new ArrayList<>();
        for(int i = 0;i<10;i++){
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname(i+"lihua");
            list.add(demoData);
        }
        return list;
    }

}
