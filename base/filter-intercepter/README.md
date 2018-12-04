# 介绍
从概念上来讲，filter是servlet规范定义的，而interceptor是spring定义的

过滤器和拦截器在对请求进行拦截时：
* 发生的时机不一样，filter是在servlet容器外，interceptor在servlet容器内，且可以对请求的3个关键步骤进行拦截处理
* 另外filter在过滤是只能对request和response进行操作，而interceptor可以对request、response、handler、modelAndView、exception进行操作。

相关博客：
* https://blog.csdn.net/dshf_1/article/details/81112595