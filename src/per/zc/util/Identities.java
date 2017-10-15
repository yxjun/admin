package per.zc.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 *
 * @author xiaojing
 */
public class Identities {

	private static SecureRandom random = new SecureRandom();

	private Identities() {
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成,中间有-分割
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成,中间无-分割
	 */
	public static String uuid2() {
		return IdGentral.get().nextId()+"";
//		return idGentral.nextId()+"";
//		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return random.nextLong();
	}


	/**
	 *根据传入参数获取下一值
	 * @param
	 * @return String
	 */
	public static  String getKey(String index,String type) {
		String lstmp = "", lsret = "", asmax = "";
		int nums, j, i;
		String[] sw = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		asmax = index;
		if (asmax == null)
			asmax = type;
		nums = asmax.length();
		boolean stepOut = false;
		for (j = nums - 1; j >= 0; j--) {
			if (stepOut) break;
			lstmp = asmax.substring(j, j + 1);
			for (i = 0; i < sw.length; i++) {
				if (lstmp.equals(sw[i])) {
					if (i == sw.length - 1) {
						lstmp = "0";
						lsret = lstmp + lsret;
					} else {
						lstmp = sw[i + 1];
						lsret = lstmp + lsret;
						stepOut = true;
					}
					break;
				}
			}
		}
		lsret = asmax.substring(0, asmax.length() - lsret.length()) + lsret;
		return lsret;
	}

	public static void main(String[] args){
		for(int i=0;i<10;i++){
			System.out.println(Identities.uuid2());
			System.out.println(Identities.uuid2());
			System.out.println(Identities.uuid2());
			System.out.println(Identities.uuid2());
			System.out.println(Identities.uuid2());

		}
		//System.out.println(Identities.uuid2().length());
	}
}
