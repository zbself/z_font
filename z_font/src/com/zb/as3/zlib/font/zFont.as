 package com.zb.as3.zlib.font
{
	import com.zb.as3.zlib.font.core.FontLoader;
	import com.zb.as3.zlib.font.core.zFontEvent;
	import com.zb.as3.zlib.font.core.zFontProgressEvent;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLRequest;
	import flash.text.Font;
	import flash.text.TextField;
	import flash.text.engine.FontLookup;
	
	import mx.events.StyleEvent;
	import mx.styles.IStyleManager2;
	import mx.styles.StyleManager;
	
	
	/**
	 * Flex
	 * @font-face {
        src:url("ZBFont.otf");
        fontFamily:zbFont;
        embedAsCFF: true;
        unicodeRange:
           U+0041-005A, // Upper-Case [A..Z] 
           U+0061-007A, // Lower-Case a-z
           U+0030-0039, // Numbers [0..9]
           U+002E-002E; // Period [.]
     }
	 */
	
	/**		字体加载成功 */
	[Event(name="load_complete", type="com.zb.as3.zlib.font.core.zFontEvent")]
	/**		字体加载错误 */
	[Event(name="load_error", type="com.zb.as3.zlib.font.core.zFontEvent")]
	/**		字体加载错误 */
	[Event(name="progress", type="com.zb.as3.zlib.font.core.zFontProgressEvent")]
	/**
	 * 单例类 使用ZFont.instance 获取实例<br>
	 * 加载字体swf( flex-css编译的swf / flash嵌字体的swf ...)<br>
	 * <br>
	 * zFont.instance.addEventListener(zFontEvent.LOAD_COMPLETE,loadCompleteHandler);<br>
	 * zFont.instance.loadSWF("zbfont.swf");<br>
	 * function loadCompleteHandler(event:zFontEvent):void<br>
	 * {<br>
	 *		 trace("字体加载完毕");<br>
	 * 		zFont.instance.fonts;//Font数组<br>
	 * }<br>
	**/
	public class zFont extends EventDispatcher
	{
		/*[Embed(source="ZBFont.otf",fontName="zb_font",embedAsCFF="true",unicodeRange="U+0030-U+0039,U+002E")] //使用fontName字段
		public var zbFont:Class;*/
		private static var _instance:zFont;
		private var fonts:Array;
		private var unRegisterLoaders:Vector.<FontLoader>;
		public function zFont(_single:Single)
		{
			fonts = [];
			unRegisterLoaders = new Vector.<FontLoader>();
		}
		
		private function creatFontLoader( url:String,autoRegister:Boolean=true):void
		{
			var tempLoader:FontLoader = new FontLoader(null,autoRegister);
			tempLoader.id = url;
			tempLoader.addEventListener("complete",fontLoadOnComplete);
			tempLoader.addEventListener("open",fontLoadOpen);
			tempLoader.addEventListener("ioError",fontLoadIoError);
			tempLoader.addEventListener("verifyError",fontLoadVerifyError);
			tempLoader.addEventListener("httpStatus",fontLoadHttpStatus);
			tempLoader.addEventListener("progress",fontLoadOnProgress);
			tempLoader.addEventListener("securityError",fontLoadSecurityError);
			tempLoader.load( new URLRequest(url),autoRegister );
		}
		
		protected function fontLoadVerifyError(event:Event):void
		{
			dispatchEvent( new zFontEvent(zFontEvent.LOAD_ERROR,event));
		}
		
		protected function fontLoadIoError(event:Event):void
		{
			dispatchEvent( new zFontEvent(zFontEvent.LOAD_ERROR,event));
		}
		protected function fontLoadOpen(event:Event):void
		{
		}
		protected function fontLoadHttpStatus(event:Event):void
		{
		}
		protected function fontLoadSecurityError(event:SecurityErrorEvent):void
		{
			dispatchEvent( new zFontEvent(zFontEvent.LOAD_ERROR,event));
		}
		protected function fontLoadOnProgress(event:ProgressEvent):void
		{
			dispatchEvent( new zFontProgressEvent(zFontProgressEvent.PROGRESS,event.bytesLoaded,event.bytesTotal));
		}
		protected function fontLoadOnComplete(event:Event):void
		{
			var targetLoader:FontLoader = event.target as FontLoader;
			if(targetLoader.autoRegister)
			{
				if( targetLoader.fonts.length )
				{
					fonts = fonts.concat(targetLoader.fonts);
				}
			}else{
				unRegisterLoaders.push( targetLoader );
			}
			lookFonts();
			this.dispatchEvent( new zFontEvent( zFontEvent.LOAD_COMPLETE,fonts));
		}
		/**
		 * 未注册字体的Fontload进行注册
		 * @param id 指定某个id(url)默认:空.全部进行注册
		 */
		public function registerFromURL(id:String=""):void
		{
			var tIndex:int = 0;
			for each (var i:FontLoader in unRegisterLoaders) 
			{
				if( id )
				{
					if(id == i.id)
					{
						i.registerFonts();
						fonts = fonts.concat(i.fonts);
						unRegisterLoaders.splice(tIndex,1);
					}else{};
				}
				else{
					i.registerFonts();
					fonts = fonts.concat(i.fonts);
					unRegisterLoaders.splice(tIndex,1);
				}
				tIndex++;
			}
		}
		/**
		 * trace信息,查看swf中的字体
		 */
		public function lookFonts():void
		{
			if(fonts.length)
			{
				for each (var i:Font in fonts)
				{
					trace("fontName: "+i.fontName+" - fontStyle: "+i.fontStyle+" - fontType: "+i.fontType);
				}
			}
		}
		private var fontLoader:FontLoader;
		public static function get instance():zFont
		{
			if(_instance == null)
			{
				_instance = new zFont(new Single());
			}
			return _instance;
		}

		/**
		 * 加载字体SWF
		 * @param url .swf文件 <br>
		 * 派发zFontEvent.LOAD_COMPLETE(完成)/zFontEvent.LOAD_ERROR(错误)<br>
		 * 加载成功之后,调用 fonts .
		 */
		public function loadSWF(url:String,autoRegister:Boolean=true):void
		{
			creatFontLoader(url,autoRegister);
		}
	}
}
/**	内部类 */
class Single{}