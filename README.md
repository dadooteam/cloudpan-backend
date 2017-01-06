# cloudpan-backend

## 文件夹查询
  GET /traverse
  
  参数：path 需要查询的文件夹路径(不带最后的/)
  
## 文件下载
  GET /download
  
  参数：path 需要下载的文件路径
  
## 文件上传
  POST /upload
  
  参数：path需要上传到的文件夹路径(不带最后的/)，file需要上传的文件
