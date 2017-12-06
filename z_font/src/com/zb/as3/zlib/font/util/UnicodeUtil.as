package com.zb.as3.zlib.font.util
{
	public class UnicodeUtil
	{
		
		
		
		public function UnicodeUtil()
		{
			
		}
		/**
		 * \\u4E09 转 0x4E09
		 * @param char
		 * @return 
		 * 
		 */		
		public function uni2U( char:String ):String
		{
			return String.fromCharCode.apply(null, char.replace(/\\u/g,"0x").match(/0x[A-Za-z0-9]{4}/g) );
		}
	}
}