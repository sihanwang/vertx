package vertx.study.buffer;

import io.vertx.core.buffer.Buffer;

public class TryBuffer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Buffer buff = Buffer.buffer("some string");
		
		byte[] bytes = new byte[] {1, 3, 5,7,9};
		Buffer buff1 = Buffer.buffer(bytes);
		
		for (int i = 0; i < buff1.length(); i +=1) {
			  System.out.println("int value at " + i + " is " + buff1.getInt(i));
		}
		
		Buffer buff2 = Buffer.buffer(10000);
		
		buff2.appendInt(3).appendString("Hello\n");
		
		Buffer buff3 = Buffer.buffer();
		buff3.setInt(1000, 123);
		buff3.setString(0, "hello");
		
		System.out.println(buff3.getInt(1000));
		System.out.println(buff3.getString(0,5));
		
	}

}
