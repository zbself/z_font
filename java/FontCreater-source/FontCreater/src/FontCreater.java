import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.adobe.fonts.transcoder.Font2SWF;


public class FontCreater {

	private static int MIN_CODE = 0x0000;
	private static int MAX_CODE = 0xffff;
	public static void main( String[] paramArrayOfString )
	{
		try {
			String fontUrl 		= null;
			String fontname 	= null;
			String charFileUrl 	= null;
			String outputUrl 	= null;
			String rangeStr 	= null;
			String isCFF 		= "";
			String isBold 		= "";
			String isItalic 	= "";
			
			int paramLen = paramArrayOfString.length;
			if( paramLen <= 1)
			{
				String configUrl = paramLen > 0 ? ( String ) ( paramArrayOfString[0] ) : "config.properties";
				Properties config = new Properties();
				
				InputStream in = new FileInputStream( configUrl );
				config.load( in );
				in.close();
				
				fontUrl 		= config.getProperty( "fontfile" );
				fontname 		= config.getProperty( "fontname" );
				charFileUrl 	= config.getProperty( "characterfile");
				outputUrl		= config.getProperty( "outputSwf", fontname + ".swf");
				rangeStr 		= config.getProperty( "codeRange" );
				isCFF			= config.getProperty( "cff" );
				isBold			= config.getProperty( "bold" );
				isItalic		= config.getProperty( "italic" );
				
			}
			else
			{
				for( int j = 0 ; j < paramLen ; j++ )
				{
					String str1 = getArg(paramArrayOfString, j);
					String str2;
					if( str1.equals( "-f" ) || str1.equals( "-font" ) )
					{
						j++;
						str2 = getArg(paramArrayOfString, j);
						fontUrl = str2;
					}
					else if( str1.equals( "-n" ) || str1.equals( "-name" ) )
					{
						j++;
						str2 = getArg(paramArrayOfString, j);
						fontname = str2;
					}
					else if( str1.equals( "-c" ) || str1.equals( "-char" ) )
					{
						j++;
						str2 = getArg(paramArrayOfString, j);
						charFileUrl = str2;
					}
					else if( str1.equals( "-r" ) || str1.equals( "-range" ) )
					{
						j++;
						str2 = getArg(paramArrayOfString, j);
						rangeStr = str2;
					}
					else if( str1.equals( "-o" ) )
					{
						j++;
						str2 = getArg(paramArrayOfString, j);
						outputUrl = str2;
					}
					else if( str1.equals( "-cff" ) || str1.equals( "-4" ) )
					{
						j++;
						isCFF = "true";
					}
					else if( str1.equals( "-b" ) || str1.equals( "-bold" ) )
					{
						j++;
						isBold = "true";
					}
					else if( str1.equals( "-i" ) || str1.equals( "-italic" ) )
					{
						j++;
						isItalic = "true";
					}
				}
			}
			
			MIN_CODE = 0 ;
			if( rangeStr != null )
			{
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(rangeStr);
				int max = 0 ;
				while( m.find() )
				{
					String s0 = m.group();
					int num = Integer.parseInt( s0);
					if( num > max)
						max = num;
				}
				MAX_CODE = max;
			}
			
			if( !( fontUrl != null  && fontname != null && charFileUrl != null  && outputUrl != null  ))
			{
				ArrayList<String> arr = new ArrayList<String>();
				if( fontUrl == null )
				{
					arr.add( "-f -font");
				}
				if( fontname == null )
				{
					arr.add( "-n -name");
				}
				if( charFileUrl == null )
				{
					arr.add( "-c -char");
				}
				if( outputUrl == null )
				{
					arr.add( "-o");
				}
				String str = new String("下列参数不能为空" );
				for( String s : arr )
				{
					str += ( '\n' +  s );
				}
				System.out.print( str );
				return;
			}
			
			File charFile = new File( charFileUrl);
			Long fileLen = charFile.length();
			byte[] content = new byte[ fileLen.intValue()];
			InputStream in = new FileInputStream(charFile);
			in.read( content );
			in.close();
			String characters = new String(content);
			
//			Dictionary<int, boolean> info= new Dictionary<int, boolean>();
			
			int[] info = new int[MAX_CODE + 1];
			int len = characters.length();
			for ( int i = 0 ; i < len ; i ++ )
			{
				int code = (int)( characters.charAt(i));
				info[ code ] = 1;
			}
			
			String paramRange = createRangeFromDict(info);
			
			int index= 0;
			
			int $length = 8;
			if( (isBold.equalsIgnoreCase("true") || isBold.equalsIgnoreCase("bold") ))
			{
				$length++;
			}
			if( (isItalic.equalsIgnoreCase("true") || isItalic.equalsIgnoreCase("italic") ))
			{
				$length++;
			}
			String[] params = new String[$length];
			
			String paramCFF = "-3";
			paramCFF =  ((isCFF != null) && isCFF.equalsIgnoreCase("true")) ?  "-4" : "-3";//cff
			
			params[ index ++ ] = paramCFF;
			params[ index ++ ] = "-u";
			params[ index ++ ] = paramRange;
			params[ index ++ ] = "-a";
			params[ index ++ ] = fontname;
			params[ index ++ ] = "-o";
			params[ index ++ ] = outputUrl;
			params[ index ++ ] = fontUrl;
			
			if( (isItalic!=null) && ( isItalic.equalsIgnoreCase("true") || isItalic.equalsIgnoreCase("italic") ) )//italic
			{
				params[ index ++ ] = "-i";
			}
			if( (isBold!=null) && ( isBold.equalsIgnoreCase("true") || isBold.equalsIgnoreCase("bold") ) )//bold
			{
				params[ index ++ ] = "-b";
			}
			
			for (String string : params) {
				System.out.println("param : "+string);
			}
			
			Font2SWF.main( params );
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
	}
	
	
	private static String createRangeFromDict( int[] info ){
		int start= MIN_CODE;
		int end= MAX_CODE; 
		
		int curStart = start;
		int curEnd = start;
		String ret= "";
		
//		ArrayList<int> = new ArrayList<int>();
		
		
//		 codes:Array = new Array();
		for( int i = start ; i<= end ; i++ ){
			if( info[i] <= 0 && curEnd==i-1 ){
				if( curStart >= curEnd )
					ret += "U+" + Integer.toHexString( curStart ) + ",";
				else
					ret += "U+" + Integer.toHexString( curStart ) + '-' + Integer.toHexString( curEnd ) + ',' ;
			}
			else if( info[i] > 0 ){
				if( i > 0 && info[i - 1] <= 0 )
					curStart = i;
				curEnd = i;
			}
//			if( info[i] > 0  )
//				codes.push( i );
		}
//		ret = addEnglishToRangStr( ret );
		if( ret.charAt( ret.length() - 1 ) == ',' )
			ret = ret.substring( 0, ret.length() - 1 );
		
//		
//		var showStr:String = '';
//		for each ( var code:int in codes ){
//			showStr += String.fromCharCode( code );
//		}
//		var url:String = File.applicationDirectory.nativePath + '\\' + TEMP_RANGE_FILE;
//		FileFunc.writeStringFile( url, showStr );
		
		return ret;
//		return '(type ' + createShortUrl(url) +')';
	}
	
	
	private static String getArg(String[] paramArrayOfString, int paramInt)
		  {
		    if ((paramInt >= 0) && (paramInt < paramArrayOfString.length))
		      return paramArrayOfString[paramInt];
		    return null;
		  }
}
