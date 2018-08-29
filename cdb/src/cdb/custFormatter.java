package cdb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class custFormatter {
	private static final int COEFFICIENT_LIMIT = 14;
	
	public static double[] formatFloat(float value) {
		//returns coefficient, exponent
		double[] result = {0,0,0};
//		int coefficient = 0;
//		int idx = 0;
		
		/*String text = Double.toString(value);
    	int integerPlaces = text.indexOf('.');
    	int exponent = -(text.length() - integerPlaces - 1); //number of digits
    	if exponent>COEFFICIENT_LIMIT
    	double coefficient = value*Math.pow(10.0, -exponent);*/
		
		return result;
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    byte[] b = new byte[s.length() / 2];
	    for (int i = 0; i < b.length; i++) {
	      int index = i * 2;
	      int v = Integer.parseInt(s.substring(index, index + 2), 16);
	      b[i] = (byte) v;
	    }
	    return b;
	}
	
	public static byte[] byteCat(byte[]... inbyte) throws IOException {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream( );
		for (byte[] i : inbyte) {
			outstream.write(i);
		}
		return outstream.toByteArray();
	}
	
	public static byte[] c2b(int c) {
		return BigInteger.valueOf(c).toByteArray();
	}
	
	public static byte[] c2bl(int arraySize, Long c) {
		return ByteBuffer.allocate(arraySize).putLong(c).array();
	}
	
	public static byte[] c2b(int arraySize, int c) {
		return ByteBuffer.allocate(arraySize).putInt(c).array();
	}
	
	public static byte[] c2b(int arraySize, float c) {
		return ByteBuffer.allocate(arraySize).putFloat(c).array();
	}
	
	public static byte[] inttoBytes(int i) throws IOException
	{
	  byte[] result = ByteBuffer.allocate(4).putInt(i).array();
	  
	  byte[] emptyBytes = new byte[3];
	  if ((int) Math.signum(i)==-1) {
		  for(int k=0;k<emptyBytes.length;k++) {
			  emptyBytes[k] = (byte) (emptyBytes[k] | 0xff);
		  }
	  }
	  return byteCat(emptyBytes,result);
	}
	
	private static Integer[] getNumberArr(double number) {
	    //will get the total number of digits in the number
	    double temp = number;
	    int counter = 0;

	    while (temp > 0) {
	        temp /= 10;
	        counter++;
	    }
	    //reset the temp
	    temp = number;

	    // make an array
	    int modulo;     //modulo is equivalent to single digit of the number.
	    Integer[] numberArr = new Integer[counter];
	    for (int i = counter - 1; i >= 0; i--) {
	        modulo = (int) (temp % 10);
	        System.out.println(modulo);
	        numberArr[i] = modulo;  
	        temp /= 10;
	    }

	    return numberArr;
	}
	
	private static int[] asTuple(double value) {
		//return coefficient and exponent from number
		int[] result = null;
		
		String text = Double.toString(value);
		String nNumbT = text.replace(".", "");
		System.out.println(nNumbT);
		double newVal = Double.valueOf(nNumbT);
		
		Integer[] digits = getNumberArr(newVal);
		
    	@SuppressWarnings("unused")
		int exponent = Math.getExponent(value);
    	
    	int coefficient = 0;
    	int idx = 0;
    	
    	for (int i = 0; i <= digits.length; i++) {
    		idx++;
    		if (idx>=COEFFICIENT_LIMIT) {
    			exponent++;
    			continue;
    		}
    		if (coefficient>0) {
    			coefficient = coefficient*10;
    		}
    		coefficient = coefficient + digits[i];
    	}
		
//    	result[0] = coefficient;
//    	result[1] = exponent;
		return result;
	}
	
	public static int retCoefficient(double value) {
		return asTuple(value)[0];
	}
	
	public static int retExponent(double value) {
		return asTuple(value)[1];
	}
}