方式1. 
设置配置文件 config.properties:
打开 config.properties 文件. 每个设置选项都有说明.



方式2.
cmd:
java -jar FontCreater.jar -o zFont.swf -c char.txt -f "zFont.otf" -n zFont -4

-o			输出字体文件目录
-c(char)		所有需要的字体文本( 比如你只需要 "你我他" 三个汉字, 就在 char.txt 中添加这三个字. 然后生成的 swf 字体文件就会有这个三个字的字形 
-f(font)		字体文件url
-n(name)		生成的字体文件名( 在 AS3 中设置 fontFamily 就用这个名称 )
-r(range)		需要选取的 字符编码范围( 0-65535 )
-3/-4			cff:false-TextFiled/true-Flex	(不加-3/-4 默认:-3)
-b(bold)		加粗(默认不用)[加粗/斜体 不共存]
-i(italic)		斜体(默认不用)[加粗/斜体 不共存]
