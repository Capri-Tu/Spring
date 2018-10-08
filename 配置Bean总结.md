
配置Bean
--

配置形式：基于XML文件的方式；基于注解的方式（注解功能优于配置文件装配bean,在工作中大多数遇到的是使用注解的方法注入bean。）

Bean的配置方式：通过全类名（反射）、通过工厂方法（静态工厂方法&实例工厂方法）、FactoryBean(Spring自带的接口，我们需要实现该接口)

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

使用p名称空间配置属性
--
给XML配置文件"减肥"的另一个选择就是使用p名称空间，从 2.0开始，Spring支持使用名称空间的可扩展配置格式。

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="car" class="com.lihuijuan.spring.beans.Car" p:brand="sss"
    p:corp="adf" p:price="222" p:maxSpeed="32">
    </bean>
   
    <bean id="person" class="com.lihuijuan.spring.beans.Person"
          p:name="Tom" p:car-ref="car"></bean> 
</beans>
```
car-ref表示引用Car类型的bean


自动装配
--
Spring IOC容器可以自动装配Bean，我们需要做的仅仅是在<bean>的autowire属性里指定自动装配的模式
 
* byType（根据类型自动装配）：若IOC容器中有多个与目标Bean类型一致的Bean，在这种情况下，Spring将无法判断哪个Bean最合适该属性，所以不能执行自动装配

* byName(根据名称id自动装配)，必须将目标Bean的名称和属性名设置的完全相同 

* constructor(通过构造器自动装配)，当Bean中存在多个构造器时，此种自动装配方式会很复杂，不推荐使用



autowire-config.xml:
```
  <bean id="car" class="com.lihuijuan.spring.beans.Car" p:brand="sss"
    p:corp="adf" p:price="222" p:maxSpeed="32">
    </bean>
    <!--可以使用autowire属性指定自动装配的方式，
    byName根据bean的名字(id)和当前bean的setter风格属性名相同与否进行自动装配。
    若有匹配的，则进行自动装配，若没有匹配的，则不装配
    byType根据bean的类型和当前bean的属性的类型进行自动匹配，若IOC容器中有1个以上的类型
    匹配的bean，则抛异常
    -->
    <bean id="person" class="com.lihuijuan.spring.beans.Person"
          p:name="Tom" autowire="byName"></bean>
```

执行：
```
public static void main(String[]args){
        ApplicationContext context = new ClassPathXmlApplicationContext("autowire-config.xml");
        Person person = (Person) context.getBean("person");
        System.out.println(person.toString());
    }
```

输出结果为:
```
Person{name='Tom', age=0, car=Car{brand='sss', corp='adf', price=222.0, maxSpeed=32}}
```

XML配置里的Bean自动装配的缺点:

* 在Bean配置文件里设置autowire属性进行自动装配将会配置Bean的所有属性。然而，若只希望装配个别属性时，autowire属性就不够灵活了。

* autowire属性要么根据类型自动装配，要么根据名称自动装配，二者不能兼而有之

* 一般情况下，在实际的项目中很少使用自动装配功能，因为和自动装配的好处比起来，明确清晰的配置文档更有说服力一些

Bean之间的关系：继承；依赖
--
Spring允许继承bean的配置，被继承的bean称为父bean，继承这个父bean的bean称为子bean

子bean也可以覆盖从父bean继承过来的配置

```
 <bean id="address" class="com.lihuijuan.spring.beans.Address"
          p:city="beijing" p:street="wudaokou"></bean>
    <!--bean配置的继承：使用bean的parent属性指定继承哪个bean的配置-->
    <bean id = "address2" p:street = "dazhongsi" parent="address"></bean>
```

父bean可以作为配置模板，也可以作为bean的实例。若只想把父bean作为模板，可以设置<bean>的abstract属性为true，这样Spring就不会实例化这个bean
```
    <!--抽象bean，不能被实例化-->
    <bean id="address" class="com.lihuijuan.spring.beans.Address"
          p:city="beijing" p:street="wudaokou" abstract="true"></bean>
    <!--bean配置的继承：使用bean的parent属性指定继承哪个bean的配置-->
    <bean id = "address2" p:street = "dazhongsi" parent="address"></bean>
```


并不是<bean>元素里的所有属性都会被继承，比如：autowire,abstract等
    
也可以忽略父bean的class属性，让子bean指定自己的类，而共享相同的属性配置，但此时abstract必须设为true

```
 <!--若某一个bean没有指定class属性，则必须设置为抽象bean：abstract=true-->
    <bean id="address" abstract="true">
        <property name="city" value="beijing"></property>
    </bean>
    <!--bean配置的继承：使用bean的parent属性指定继承哪个bean的配置-->
    <bean id = "address2" class="com.lihuijuan.spring.beans.Address"
          p:street = "dazhongsi" parent="address"></bean>
```

如果增加一个depends-on属性，那么声明的依赖的bean id必须被配置，这样做只是为了不让该属性为null

```
   <bean id="person" class="com.lihuijuan.spring.beans.Person"
          p:name="Tom" p:car-ref="car" ></bean>
   <bean id = "address2" class="com.lihuijuan.spring.beans.Address"
          p:street = "dazhongsi" depends-on="person"></bean>
```

Bean的作用域：Singleton；prototype;WEB环境作用域
--
默认情况下bean是单例的

利用bean的scope属性来配置bean的作用域

singleton:默认值，容器初始时创建bean实例，在整个容器的周期只有这一个bean实例，每次getbean()都是这同一个bean

prototype:原型的，容器初始化时不创建bean的实例，而在每次请求时都创建一个新的bean实例，并返回。

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="car" class="com.lihuijuan.spring.beans.Car"
          scope="prototype" p:brand="sss" p:corp="adf" p:price="222" p:maxSpeed="32">
    </bean>
</beans>
```

执行:
```
   public static void main(String[]args){
        ApplicationContext context = new ClassPathXmlApplicationContext("prototype-config.xml");
    }
```

则不会对Car进行初始化。如果去掉scope或者 scope="singleton",则会初始化

 scope="prototype"时执行如下语句：
```
 public static void main(String[]args){
        ApplicationContext context = new ClassPathXmlApplicationContext("prototype-config.xml");
        Car car1 = (Car) context.getBean("car");
        Car car2 = (Car) context.getBean("car");
        System.out.println(car1==car2);
    }
```

可输出:
```
Setter: Construct Car ....
Setter: Construct Car ....
false
```
说明每次调用getBean，就会生成一个新的bean，也就是会创建多例

使用外部属性文件
--
这部分以后再看。

SpEL
--
语言

IOC容器中Bean的生命周期
--
Spring容器可以管理Bean的生命周期，类似于Servlet容器管理Servlet的生命周期一样。

Car类添加了两个方法initCar()和destroyCar()分别用来作为init-method和destroy-method
```
package com.lihuijuan.spring.beans.cycle;

public class Car {
    private String brand;
    private String corp;
    private double price;
    private int maxSpeed;
    public Car(){
        System.out.println("Construct Car");
    }
    public Car(String brand,String corp,double price){
        System.out.println("Construct Car....");
        this.brand = brand;
        this.corp = corp;
        this.price = price;
    }
    public Car(String brand,String corp,int maxSpeed){
        System.out.println("Construct Car....");
        this.brand = brand;
        this.corp = corp;
        this.maxSpeed = maxSpeed;
    }
    public Car(String brand,String corp,double price,int maxSpeed){
        System.out.println("Construct Car....");
        this.brand = brand;
        this.corp = corp;
        this.price = price;
        this.maxSpeed = maxSpeed;
    }
    public void setBrand(String brand) {
        System.out.println("Setter: Construct Car ....");
        this.brand = brand;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void initCar(){
        System.out.println("init...");

    }
    public void destroyCar(){
        System.out.println("destroy...");
    }
    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", corp='" + corp + '\'' +
                ", price=" + price +
                ", maxSpeed=" + maxSpeed +
                '}';
    }
}

```


```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="car" class="com.lihuijuan.spring.beans.cycle.Car"
          init-method="initCar" destroy-method="destroyCar"
          scope="singleton" p:brand="sss" p:corp="adf" p:price="222" p:maxSpeed="32">
    </bean>
</beans>
```

```
public static void main(String[]args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean-cycle.xml");
        context.getBean("car");
        //IOC容器close，才会调用bean的destroy-method
        context.close();
    }
```
输出为:
```
Construct Car  //无参构造函数
Setter: Construct Car ....  //setter方法
init...
十月 08, 2018 9:12:04 下午 org.springframework.context.support.ClassPathXmlApplicationContext doClose
destroy...
```
静态工厂方法创建bean实例
--

Car类：
```
package com.lihuijuan.spring.beans.factory;

public class Car {
    private String name;
    private int price;
    public Car(String name,int price){
        this.name = name;
        this.price = price;

    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

```
静态工厂类:（简单工厂模式实现）
```
package com.lihuijuan.spring.beans.factory;


import java.util.HashMap;
import java.util.Map;

//静态工厂方法：直接调用某一个类的静态方法就可以调用一个bean的实例
public class StaticCarFactory {
    private static Map<String,Car> cars = new HashMap();
    static {
        cars.put("audi",new Car("audi",300000));
        cars.put("ssss",new Car("ssss",200000));
    }
    //静态工厂方法
    public static Car getCar(String name){
        return cars.get(name);
    }
}

```

xml文件配置
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--通过静态工厂方法配置bean，注意不是配置静态工厂方法实例，而是配置bean实例
        class属性：指向静态工厂方法的全类名
        factory-method:指向静态工厂方法的名字
        constructor-arg:如果工厂方法需要传入参数，则使用contructor-arg来配置参数
    -->
    <bean id = "car" class="com.lihuijuan.spring.beans.factory.StaticCarFactory"
          factory-method="getCar">
          <constructor-arg value="audi"></constructor-arg>
    </bean>

</beans>
```
测试：
```
public static void main(String[]args){
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-factory.xml");
        Car car = (Car) context.getBean("car");
        System.out.println(car.toString());
    }
```
输出为:
```
Car{name='audi', price=300000}
```

实例工厂方法方式创建bean实例
--

Car类不变，同上

实例工厂类如下所示：
```
package com.lihuijuan.spring.beans.factory;

import java.util.HashMap;
import java.util.Map;
//实例工厂方法：实例工厂的方法，即需要创建工厂本身，再调用工厂的实例方法来返回bean
public class InstanceCarFactory {
    private static Map<String,Car> cars = null;
    public InstanceCarFactory(){
        cars = new <String,Car>HashMap();
        cars.put("audi",new Car("audi",300000));
        cars.put("ssss",new Car("ssss",200000));
    }
    //实例工厂方法
    public Car getCar(String name){
        return cars.get(name);
    }
}

```

xml配置文件中关于bean的配置为:

```
 <!--配置工厂的实例-->
    <bean id = "car" class="com.lihuijuan.spring.beans.factory.StaticCarFactory"
          factory-method="getCar">
          <constructor-arg value="audi"></constructor-arg>
    </bean>
    <!--通过实例工厂方法配置bean，注意不是配置静态工厂方法实例，而是配置bean实例
        class属性：指向静态工厂方法的全类名
        factory-bean:指向实力工程方法的bean
        factory-method:指向实例工厂方法的名字
        constructor-arg:如果工厂方法需要传入参数，则使用contructor-arg来配置参数
    -->
    <bean id = "carFactory" class="com.lihuijuan.spring.beans.factory.InstanceCarFactory"
    ></bean>
    <bean id = "car2" factory-bean="carFactory" factory-method="getCar">
        <constructor-arg value="ssss"></constructor-arg>
    </bean>
```
测试：
```
 public static void main(String[]args){
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-factory.xml");
        Car car = (Car) context.getBean("car");
        System.out.println(car.toString());
        Car car2 = (Car) context.getBean("car2");
        System.out.println(car2.toString());
    }
```

输出结果为：
```
Car{name='ssss', price=200000}
```


基于注解的方式配置bean
--
肯定需要xml文件的，xml文件配置IOC容器

而在那些bean类中加上注解标识来使得IOC容器进行自动的管理


Spring4.x新特性：泛型依赖注入
