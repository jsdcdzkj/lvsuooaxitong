package com.jsdc.lsoa.controller;


import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.model.SysFile;
import com.jsdc.lsoa.service.SysFileService;
import com.jsdc.lsoa.vo.FileUploadVO;
import com.jsdc.lsoa.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/app/sys/file")
@Api(tags = "文件管理")
public class SysFileController {

    @Autowired
    private SysFileService fileService;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ResultInfo<SysFile> uploadFile(FileUploadVO vo) {
        SysFile file = fileService.uploadFile(vo.getFile(), vo.getBizType(), vo.getBizId());
        return ResultInfo.success(file);
    }

    @GetMapping("/download")
    @ApiOperation("文件下载")
    public ResultInfo<Void> download(@RequestParam Integer id, HttpServletResponse response) {
        SysFile entity = fileService.getById(id);
        File file = new File(entity.getFilePath());
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStream fis = new BufferedInputStream(fileInputStream);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                response.reset();
                response.setCharacterEncoding("UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(entity.getOriginalFilename(), "UTF-8"));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                outputStream.write(buffer);
                outputStream.flush();
            }catch (Exception e) {
                log.error("文件下载失败：", e);
                throw new CustomException("文件下载失败");
            }
        }else {
            throw new CustomException("文件不存在");
        }
        return ResultInfo.success();
    }

    @PostMapping("/delete")
    @ApiOperation("根据ID删除")
    public ResultInfo<Void> deleteById(@RequestBody SysFile entity) {
        fileService.deleteById(entity.getId());
        return ResultInfo.success();
    }

    @PostMapping("/list")
    @ApiOperation("列表查询")
    public ResultInfo<List<SysFile>> list(@RequestBody SysFile entity) {
        List<SysFile> list = fileService.list(entity);
        return ResultInfo.success(list);
    }

}
