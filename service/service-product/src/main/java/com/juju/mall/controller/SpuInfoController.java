package com.juju.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juju.mall.entity.BaseSaleAttr;
import com.juju.mall.entity.SpuImage;
import com.juju.mall.entity.SpuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.result.Result;
import com.juju.mall.service.SpuImageService;
import com.juju.mall.service.SpuInfoService;
import com.juju.mall.service.BaseSaleAttrService;
import com.juju.mall.service.SpuSaleAttrService;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/product")
@CrossOrigin
public class SpuInfoController {

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private BaseSaleAttrService baseSaleAttrService;

    @Autowired
    private SpuImageService spuImageService;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @GetMapping("{page}/{limit}")
    public Result<IPage<SpuInfo>> spuInfoByPage(@PathVariable("page") Long page,@PathVariable("limit") Long limit,String category3Id){
        IPage<SpuInfo> iPage = new Page<>();
        iPage.setPages(page);
        iPage.setSize(limit);
        IPage<SpuInfo> infoIPage = spuInfoService.spuList(iPage,category3Id);
        return Result.ok(infoIPage);
    }

    @GetMapping("baseSaleAttrList")
    public Result<List<BaseSaleAttr>> baseSaleAttrList(){
        List<BaseSaleAttr> list = baseSaleAttrService.list(null);
        return Result.ok(list);
    }

    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    @GetMapping("spuImageList/{spuId}")
    public Result<List<SpuImage>> spuImageList(@PathVariable("spuId")String spuId){
        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(wrapper);
        return Result.ok(list);
    }

    @GetMapping("spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> spuSaleAttrList(@PathVariable("spuId")String spuId){
        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrList(spuId);
        return Result.ok(list);
    }

    @PostMapping("fileUpload")
    public Result fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String path = SpuInfoController.class.getClassLoader().getResource("tracker.conf").getPath();
        try {
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //得到tracker
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //根据tracker获得storage
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //上传文件
        String[] jpgs = new String [0];
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            //获取后缀名
            String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
            jpgs = storageClient.upload_file(multipartFile.getBytes(),filenameExtension,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer url = new StringBuffer("http://192.168.200.128:8080");
        for (String jpg : jpgs){
            url.append("/"+jpg);
        }
        System.out.println(url);
        return Result.ok(url);
    }

}
