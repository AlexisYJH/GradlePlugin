# Gradle插件
[Gradle Plugin技术及玩转transform](https://blog.csdn.net/Lebron_xia/article/details/123172825)

在Gradle官方文档里目前定义插件的方式有三种：
- **脚本插件**：直接在构建脚本中直接写插件的代码，编译器会自动将插件编译并添加到构建脚本的classpath中。
- **buildSrc project**：执行Gradle时 会把根目录下的buildSrc目录作为插件源码目录进行编译，编译后会把结果加入到构建脚本的classpath中，对于整个项目是可用的。
- **Standalone project**：可以在独立项目中开发插件，然后将项目达成jar包，发布到本地或者maven服务器上。

AGP(Android Gradle Plugin) 7.0.0版本之后Transform 已经过期即将废弃。替换的方式是Transform Action

## 1.直接在build.gradle文件中实现
这种方式在构建脚本之外是不可以见的，所以只有在定义该插件的gradle脚本里才可以引用该插件。
```
//应用插件
apply plugin: CustomPluginA

//自定义插件示例
class CustomPluginA implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println 'Hello gradle!'
    }
}
```

## 2.在默认目录buildSrc中实现
buildSrc目录是gradle默认的目录之一，该目录会在构建时自动的进行编译打包，所以在这里面不需要任何额外的配置，就可以直接被其他模块中的gradle脚本所引用。
- 只能是buildSrc这个名称
- 可以直接被其他模块中的gradle脚本所引用
- 该目录默认有gradle库的依赖（gradle本身会依赖ASM（依赖传递），所以gradle中可以直接使用ASM，其他地方使用需要添加ASM依赖）

1. 新建buildSrc目录
2. 新建src/main/java(kotlin/groovy)目录(java目录不需要引入依赖，gradle插件是可以使用java，groovy，kotlin编写，所以你可以根据自己的需要引入相关的依赖。)
3. (可选)buildSrc目录下，新建build.gradle，引入仓库和android插件的依赖【230113-1】
4. gradle sync
5. 新建package(com.example.plugin)，新建MyPlugin类，实现Plugin<Project>接口
6. (可选)新建配置文件
- main目录下，新建resources/META-INF/gradle-plugins目录(注意是三级目录)
- 新建com.example.plugin.properties（xxx.properties）文件，文件内部配置实现插件结构的类全限定名：implementation-class=com.example.plugin.MyPlugin
7. 应用插件
- 直接引用：apply plugin: com.example.plugin.MyPlugin
- 配置引用：apply plugin: 'com.example.plugin'

## 3.在独立项目开发中实现
[一文学会字节码替换，再也不用担心隐私合规审核](https://juejin.cn/post/7121985493445083149)

这种方式基本跟第二种相似，不过要引入这个插件的话要先把它发布到本地或者maven服务器上。

1. 首先在settings.gradle添加如下代码:
```
pluginManagement {
    repositories {
        maven {
            url uri('repo')
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
}
```
2. 新建模块
3. 执行publish生成插件（Tasks-publishing-publish）
4. 在根目录build.gradle添加插件依赖
```
buildscript {
    dependencies {
        classpath 'com.example.plugin:plugin:1.0.0'
    }
}
```
5. 在app目录 build.gradle apply插件
```
plugins {
    id 'com.example.plugin'
}
```