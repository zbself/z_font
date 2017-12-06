package com.zb.as3.zlib.font.core
{
	import flash.events.ProgressEvent;
	
	public class zFontProgressEvent extends ProgressEvent
	{
		
		/**		进度 */
		public static const PROGRESS:String = "progress";
		public function zFontProgressEvent(type:String,bytesLoaded:Number=0, bytesTotal:Number=0)
		{
			super(type, false, false, bytesLoaded, bytesTotal);
		}
	}
}