package com.xmaven.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.xmaven.entity.SysUser;
import com.xmaven.service.SysUserService;
import com.xmaven.utils.DownExcel;
import com.xmaven.utils.ExcelListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @RequestMapping("/list")
    public ModelAndView page(ModelAndView mav,@RequestParam(defaultValue="1")Integer pageNum,@RequestParam(defaultValue="5")Integer pageSize){
        PageInfo<SysUser> pageInfo = sysUserService.selectpage(pageNum, pageSize);
        mav.addObject("pi",pageInfo);//把集合装入模型数据
        mav.setViewName("selectpage");//路径：/WEB-INF/selectpage.jsp
        return mav;
    }

    //导出为Excel
    @RequestMapping("/down")
    public void getExcel(HttpServletResponse response) throws IllegalAccessException, IOException, InstantiationException {
        List<SysUser> list = sysUserService.getAll();
        DownExcel.download(response,SysUser.class,list);
    }

    //导入Excel
    @RequestMapping("/import")
    @ResponseBody
    public String importexcel(@RequestParam(value = "excelFile") MultipartFile file) throws IOException{
        EasyExcel.read(file.getInputStream(), SysUser.class, new ExcelListener(sysUserService)).sheet().doRead();
        return "success";
    }
}
