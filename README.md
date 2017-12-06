# z_font
flash/flex Font

zFont.instance.addEventListener(zFontEvent.LOAD_COMPLETE,loadCompleteHandler);<br/>
zFont.instance.addEventListener(zFontProgressEvent.PROGRESS,onProgressHandler);<br/>
zFont.instance.addEventListener(zFontEvent.LOAD_ERROR,loadErrorHandler);<br/>
zFont.instance.loadSWF("assets/font/zFont.swf",true);<br/>
function loadCompleteHandler(event:zFontEvent):void<br/>
{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;zFont.instance.registerFromURL();//注册字体<br/>
&nbsp;&nbsp;&nbsp;&nbsp;flexLabel.setStyle("fontFamily","zFont");//flex - CFF<br/>
  
&nbsp;&nbsp;&nbsp;&nbsp;var tf:TextField = new TextField();//flash - unCFF<br />
&nbsp;&nbsp;&nbsp;&nbsp;tf.type = TextFieldType.DYNAMIC;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;tf.autoSize = TextFieldAutoSize.LEFT;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;tf.embedFonts = true;//嵌入设置<br/>
&nbsp;&nbsp;&nbsp;&nbsp;var tft:TextFormat = new TextFormat("zFont", 40,0xFF0000,true);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;tf.defaultTextFormat = tft;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;tf.text = "ZB";<br/>
&nbsp;&nbsp;&nbsp;&nbsp;ui.addChild(tf);<br/>
}
