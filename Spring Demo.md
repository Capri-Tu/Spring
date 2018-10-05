Intellij IDEA中如何创建一个简单的Spring项目
--

**步骤总结:**

(1)创建一个Spring工程，创建完成之后会自动生成一个xxx.xml的配置文件

(2)新建一个类Hello，按照Bean规范写

(3)在xxx.xml中添加该Bean类，设置id名，并设置属性值。

(4)在main函数中调用该xxx.xml配置文件生成的IOC容器对象ApplicationContext类型

(5)调用该容器对象的getBean(Hello对应的id名)，获取Hello对象

(6)调用Hello对象的方法，输出结果


参考链接
--
https://blog.csdn.net/shanmoweiyunjun/article/details/77727257
