# 学生论坛--前后端分离项目（JWT方案）
采用SpringBoot3 + Vue3编写的学生论坛，使用JWT校验方案。
***
### 后端功能与技术点
用户注册、用户登录、重置密码等基础功能以及对应接口
* 采用Mybatis-Plus作为持久层框架，使用更便捷
* 采用Redis存储注册/重置操作验证码，带过期时间控制
* 采用RabbitMQ积压短信发送任务，再由监听器统一处理
* 采用SpringSecurity作为权限校验框架，手动整合Jwt校验方案
* 采用Redis进行IP地址限流处理，防刷接口
* 视图层对象和数据层对象分离，编写工具方法利用反射快速互相转换
* 错误和异常页面统一采用JSON格式返回，前端处理响应更统一
* 手动处理跨域，采用过滤器实现
* 使用Swagger作为接口文档自动生成
* 采用过滤器实现对所有请求自动生成雪花ID方便定位出现的问题
* 针对于多环境进行处理，开发环境和生产环境采用不同的配置
* 日志中包含单次请求完整信息以及对应的雪花ID，支持文件记录

### 前端功能与技术点
用户注册、用户登录、重置密码等界面
* 采用Vue-Router作为路由
* 采用Axios作为异步请求框架
* 采用Element-Plus作为UI组件库
* 使用VueUse适配深色模式切换
* 使用unplugin-auto-import按需引入，减少打包后体积
