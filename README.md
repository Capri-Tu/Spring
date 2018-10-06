# Spring Demo

---

> 该项目是作为我的Spring Demo，Demo源码见 ./spring-lihuijuan ,可以下载后直接运行。Demo的搭建见下面部分,下面是一个Spring Demo体现IOC作用的例子:.......

## Intellij IDEA的安装

Intellij IDEA要使用旗舰版，旗舰版网上有很多激活方法就不赘述了。我使用的版本为Ultimate 2018.2


## 新建Spring工程

新建Spring工程，project命名为spring-lihuijuan。

**勾选 Create empty spring-config.xml**

![image.png-77.3kB][2]

![image.png-22.8kB][3]

![image.png-33kB][4]

生成的project如下:

![image.png-75kB][5]

其中lib目录下已经自动下载好了我们需要的jar包


![image.png-17.5kB][6]

spring-config.xml中的内容如下图所示：

![1.png-78.6kB][7]

## 新建一个Bean类

首先创建一个com.lihuijuan.spring.beans.HelloWorld类，有一个studentName属性，还有一个sayHello的方法，还有一个setter方法用来设置studentName属性。 

在编辑框中右击鼠标，点击generate，即可自动生成setter和getter方法

![image.png-55.8kB][8]

![image.png-11.7kB][9]

```
package com.lihuijuan.spring.beans;

public class HelloWorld {
    private String studentName;

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void sayHello(){
        System.out.println("Hello"+" "+studentName);
    }
}
```

> 在我们不使用框架的时候，也就是平常的编程中，我们要调用sayHello这个方法，可以分为3步。 

* 1. 创建一个HelloWorld的实例对象 

* 2. 设置实例对象的name属性 

* 3. 调用对象的sayHello()方法 

```
public static void main(String[]args){
        //不使用Spring框架之前的步骤
        //1.创建一个HelloWorld的对象
        HelloWorld helloWorld = new HelloWorld();
        //2.为实例对象的属性赋值
        helloWorld.setStudentName("lihuijuan");
        //3.调用对象的方法
        helloWorld.sayHello();

    }
```
执行该main方法，输出为Hello lihuijuan

![image.png-132.5kB][11]

## 利用Spring IOC调用 Bean 

接下来我们就要使用Spring了

首先在Spring的配置文件中加入如下内容。 

```
public static void main(String[]args){
        //使用Spring框架后
        //1.创建Spring IOC 容器对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        //2.通过IOC容器对象来得到Helloworld对应的对象，利用bean id来唯一标识这个对象
        HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");
        //3.调用sayHello()方法
        helloWorld.sayHello();
    }
```

执行一下该main方法，可以得到结果如下:

![image.png-44.3kB][12]


## Spring调用Bean的流程

> Spring IOC在创建的时候会先调用 HelloWorld的构造函数，然后调用setter方法对studentName进行赋值

为了便于分析，我们将HelloWorld中的构造方法和setter方法打印输出，如下:
```
public class HelloWorld {
    private String studentName;
    public HelloWorld(){
       System.out.println("Constructor...");
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
        System.out.println("setStudentName...");
    }

    public String getStudentName() {
        return studentName;
    }

    public void sayHello(){
        System.out.println("Hello"+" "+studentName);
    }
}

```
这时候再执行main方法，结果如下:
![image.png-49kB][13]


> 到这一步为止，我们实现了利用setter方法设值注入的方式获得HelloWorld类对象Bean,这种方法必须要有一个默认无参构造函数，因为在Spring配置文件xxx.xml中利用了Helloworld全类名反射的方式创建Helloworld对象，反射这种方式用的就是无参构造函数。



> 上述是使用setter方法注入Bean,还可以用构造函数的方式进行注入（设置参数值）

  [2]: http://static.zybuluo.com/lihuijuan114/6zjs1bcewcuj4crk0gbyv151/image.png
  [3]: http://static.zybuluo.com/lihuijuan114/dfb1ay84bz5p4jl2cbuwu96x/image.png
  [4]: http://static.zybuluo.com/lihuijuan114/8se5fj0nkcfbrymw86g10yyd/image.png
  [5]: http://static.zybuluo.com/lihuijuan114/gtvyqif2p4xr79gk43cfi9ir/image.png
  [6]: http://static.zybuluo.com/lihuijuan114/4hqgt3j9slxrin4ddb06erve/image.png
  [7]: http://static.zybuluo.com/lihuijuan114/3y93rms3qg2ln7ebxzcjehu1/1.png
  [8]: http://static.zybuluo.com/lihuijuan114/9kmb8sp4p3wwubzd6piwgzj2/image.png
  [9]: http://static.zybuluo.com/lihuijuan114/3pjaxxyaglf3gclhrqcn1suh/image.png
  [10]: http://static.zybuluo.com/lihuijuan114/0z1yubdia6xh49kdnkbfnwrz/image.png
  [11]: http://static.zybuluo.com/lihuijuan114/9iddctncxovreybr2mfw1ozd/image.png
  [12]: http://static.zybuluo.com/lihuijuan114/h8uzptxl3y6anbidknd803wx/image.png
  [13]: http://static.zybuluo.com/lihuijuan114/hmgztjlq6k59naw2laztl7aj/image.png
