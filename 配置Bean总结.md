
配置Bean
--

配置形式：基于XML文件的方式；基于注解的方式（注解功能优于配置文件装配bean,在工作中大多数遇到的是使用注解的方法注入bean。）

Bean的配置方式：通过全类名（反射）、通过工厂方法（静态工厂方法&实例工厂方法）、FactoryBean

IOC容器BeanFactory&ApplicationContext概述
--

在Spring容器读取Bean配置创建Bean实例之前，必须对容器进行实例化。只有在容器实例化后，才可以从IOC容器里获取Bean实例并使用。

Spring提供了两种类型的IOC容器实现

* BeanFactory：是Spring框架的基础设施，面向Spring本身

* ApplicationContext提供了更多的高级特性，是BeanFactory的子接口。几乎所有的应用场合都直接使用ApplicationContext而非底层的BeanFactory。Application在初始化

上下文时就实例化所有的Bean

ApplicationContext的主要实现类:

* ClassPathXmlApplicationContext：从类路径下读取xml配置文件

* FileSystemXmlApplicationContext

依赖注入的方式：属性注入；构造器注入，工厂方法注入(工厂方法注入很少用，不推荐)

(1)属性注入

```
<bean id="helloWorld" class = "com.lihuijuan.spring.beans.HelloWorld"> //全类名反射,id是唯一标识
    //如果Helloworld有一个setStudentName方法，那么这里的name就是studentname
    <property name="studentName" value="lihuijuan"></property>    
</bean>
```
(2)构造器注入

```
  <!--通过构造方法来配置Bean的属性，在constructor-arg里面配置属性-->
<bean id="car1" class="com.lihuijuan.spring.beans.Car">
    <constructor-arg value="Audi"></constructor-arg>
    <constructor-arg value="Shanghai"></constructor-arg>
    <constructor-arg value="300000" type="int"></constructor-arg>
    <!--使用构造器可以设定参数的位置和参数的类型，以区分重载-->
</bean>
    <bean id="car2" class="com.lihuijuan.spring.beans.Car">
        <constructor-arg value="Audi"></constructor-arg>
        <constructor-arg value="Shanghai"></constructor-arg>
        <constructor-arg value="300000" type="double"></constructor-arg>
    </bean>
```
Bean之间的关系：继承；依赖

引用其他Bean
--

一个类Person:
```
package com.lihuijuan.spring.beans;

public class Person {
    private String name;
    private int age;
    private Car car;

    public  Person(){

    }
    public Person(String name,int age,Car car){
        this.name = name;
        this.age = age;
        this.car = car;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", car=" + car +
                '}';
    }
}


```
如果想 注入这个bean，由于属性中包含了bean，可以用两种方式来配置：

* 外部bean: 用ref建立引用关系
```
 <!--可以使用property中的ref属性建立bean之间的引用关系-->
    <bean id="person" class="com.lihuijuan.spring.beans.Person">
        <property name="name" value="Tom"></property>
        <property name="age" value="24"></property>
        <property name="car" ref="car2"></property>
    </bean>
```

或者
```
  <!--可以使用constructor-arg的ref属性建立bean之间的引用关系-->
    <bean id="person" class="com.lihuijuan.spring.beans.Person">
        <constructor-arg value="Ford"></constructor-arg>
        <constructor-arg value="1"></constructor-arg>
        <constructor-arg ref="car2"></constructor-arg>
    </bean>
```
* 内部bean
```
<bean id="person2" class="com.lihuijuan.spring.beans.Person">
        <property name="name" value="Tom"></property>
        <property name="age" value="24"></property>
        <!--内部bean，不能被外部引用，没有id，只能内部使用-->
        <property name="car" >
            <bean class="com.lihuijuan.spring.beans.Car">
                <constructor-arg value="Ford"></constructor-arg>
                <constructor-arg value="changan"></constructor-arg>
                <constructor-arg value="20000"></constructor-arg>
            </bean>
         </property>
    </bean>
```

如果将某个属性赋值为Null
```
<bean id="person" class="com.lihuijuan.spring.beans.Person">
        <constructor-arg value="Ford"></constructor-arg>
        <constructor-arg value="1"></constructor-arg>
        <!--测试赋值null-->
        <constructor-arg><null></null></constructor-arg>
    </bean>
```

自动装配
--
Spring IOC容器可以自动装配Bean，我们需要做的仅仅是在<bean>的autowire属性里指定自动装配的模式
 
* byType（根据类型自动装配）：若IOC容器中有多个与目标Bean类型一致的Bean，在这种情况下，Spring将无法判断哪个Bean最合适该属性，所以不能执行自动装配

* byName(根据名称id自动装配)，必须将目标Bean的名称和属性名设置的完全相同 

* constructor(通过构造器自动装配)，当Bean中存在多个构造器时，此种自动装配方式会很复杂，不推荐使用

Bean的作用域：Singleton；prototype;WEB环境作用域

IOC容器中Bean的生命周期

Spring4.x新特性：泛型依赖注入
