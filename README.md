七牛云文件快捷上传(其实不快捷)

运行主程序前需要先编辑application.properties文件

添加所有相应的配置信息

特点：
- 该程序自动可自动上传指定文件夹的所有文件，如果指定了特定的头文件信息也可生成自定义文件名：
    
    比如：
    ~~~properties
  #需要上传的文件/文件夹位置
  qiniu.baseUploadFilePath=D:\\Private\\github\\qiniu\\image
  #指定的文件名前缀
  qiniu.specifiedFileHeader=image/blog/2020/08
    ~~~
    配置了文件夹位置以及文件名前缀
    如果`D:\\Private\\github\\qiniu\\image`下有`a.png`
    以及`hi\\b.png`，那么程序将会将二者均上传，文件名分别为
    `image/blog/2020/08/a.png`和`image/blog/2020/08/hi/b.png`
- 没了
