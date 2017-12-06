package com.zb.as3.zlib.font.core
{
	import flash.events.Event;
	
	public class zFontEvent extends Event
	{
		public static const LOAD_COMPLETE:String = "load_complete";
		public static const LOAD_ERROR:String = "load_error";
		
		public var eventData:*;
		public function zFontEvent(type:String,data:*=null)
		{
			eventData = data;
			super(type);
		}
	}
}