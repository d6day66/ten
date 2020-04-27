package com.tensquate.encryp.rsa;

/**
 * rsa加解密用的公钥和私钥
 * @author Administrator
 *
 */
public class RsaKeys {

	//生成秘钥对的方法可以参考这篇帖子
	//https://www.cnblogs.com/yucy/p/8962823.html

//	//服务器公钥
//	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Dw9nwjBmDD/Ca1QnRGy"
//											 + "GjtLbF4CX2EGGS7iqwPToV2UUtTDDemq69P8E+WJ4n5W7Iln3pgK+32y19B4oT5q"
//											 + "iUwXbbEaAXPPZFmT5svPH6XxiQgsiaeZtwQjY61qDga6UH2mYGp0GbrP3i9TjPNt"
//											 + "IeSwUSaH2YZfwNgFWqj+y/0jjl8DUsN2tIFVSNpNTZNQ/VX4Dln0Z5DBPK1mSskd"
//											 + "N6uPUj9Ga/IKnwUIv+wL1VWjLNlUjcEHsUE+YE2FN03VnWDJ/VHs7UdHha4d/nUH"
//											 + "rZrJsKkauqnwJsYbijQU+a0HubwXB7BYMlKovikwNpdMS3+lBzjS5KIu6mRv1GoE"
//											 + "vQIDAQAB";
//
//	//服务器私钥(经过pkcs8格式处理)
//	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDoPD2fCMGYMP8J"
//				 								 + "rVCdEbIaO0tsXgJfYQYZLuKrA9OhXZRS1MMN6arr0/wT5YniflbsiWfemAr7fbLX"
//				 								 + "0HihPmqJTBdtsRoBc89kWZPmy88fpfGJCCyJp5m3BCNjrWoOBrpQfaZganQZus/e"
//				 								 + "L1OM820h5LBRJofZhl/A2AVaqP7L/SOOXwNSw3a0gVVI2k1Nk1D9VfgOWfRnkME8"
//				 								 + "rWZKyR03q49SP0Zr8gqfBQi/7AvVVaMs2VSNwQexQT5gTYU3TdWdYMn9UeztR0eF"
//				 								 + "rh3+dQetmsmwqRq6qfAmxhuKNBT5rQe5vBcHsFgyUqi+KTA2l0xLf6UHONLkoi7q"
//				 								 + "ZG/UagS9AgMBAAECggEBANP72QvIBF8Vqld8+q7FLlu/cDN1BJlniReHsqQEFDOh"
//				 								 + "pfiN+ZZDix9FGz5WMiyqwlGbg1KuWqgBrzRMOTCGNt0oteIM3P4iZlblZZoww9nR"
//				 								 + "sc4xxeXJNQjYIC2mZ75x6bP7Xdl4ko3B9miLrqpksWNUypTopOysOc9f4FNHG326"
//				 								 + "0EMazVaXRCAIapTlcUpcwuRB1HT4N6iKL5Mzk3bzafLxfxbGCgTYiRQNeRyhXOnD"
//				 								 + "eJox64b5QkFjKn2G66B5RFZIQ+V+rOGsQElAMbW95jl0VoxUs6p5aNEe6jTgRzAT"
//				 								 + "kqM2v8As0GWi6yogQlsnR0WBn1ztggXTghQs2iDZ0YkCgYEA/LzC5Q8T15K2bM/N"
//				 								 + "K3ghIDBclB++Lw/xK1eONTXN+pBBqVQETtF3wxy6PiLV6PpJT/JIP27Q9VbtM9UF"
//				 								 + "3lepW6Z03VLqEVZo0fdVVyp8oHqv3I8Vo4JFPBDVxFiezygca/drtGMoce0wLWqu"
//				 								 + "bXlUmQlj+PTbXJMz4VTXuPl1cesCgYEA6zu5k1DsfPolcr3y7K9XpzkwBrT/L7LE"
//				 								 + "EiUGYIvgAkiIta2NDO/BIPdsq6OfkMdycAwkWFiGrJ7/VgU+hffIZwjZesr4HQuC"
//				 								 + "0APsqtUrk2yx+f33ZbrS39gcm/STDkVepeo1dsk2DMp7iCaxttYtMuqz3BNEwfRS"
//				 								 + "kIyKujP5kfcCgYEA1N2vUPm3/pNFLrR+26PcUp4o+2EY785/k7+0uMBOckFZ7GIl"
//				 								 + "FrV6J01k17zDaeyUHs+zZinRuTGzqzo6LSCsNdMnDtos5tleg6nLqRTRzuBGin/A"
//				 								 + "++xWn9aWFT+G0ne4KH9FqbLyd7IMJ9R4gR/1zseH+kFRGNGqmpi48MS61G0CgYBc"
//				 								 + "PBniwotH4cmHOSWkWohTAGBtcNDSghTRTIU4m//kxU4ddoRk+ylN5NZOYqTxXtLn"
//				 								 + "Tkt9/JAp5VoW/41peCOzCsxDkoxAzz+mkrNctKMWdjs+268Cy4Nd09475GU45khb"
//				 								 + "Y/88qV6xGz/evdVW7JniahbGByQhrMwm84R9yF1mNwKBgCIJZOFp9xV2997IY83S"
//				 								 + "habB/YSFbfZyojV+VFBRl4uc6OCpXdtSYzmsaRcMjN6Ikn7Mb9zgRHR8mPmtbVfj"
//				 								 + "B8W6V1H2KOPfn/LAM7Z0qw0MW4jimBhfhn4HY30AQ6GeImb2OqOuh3RBbeuuD+7m"
//				 								 + "LpFZC9zGggf9RK3PfqKeq30q";

	//服务器公钥
	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu3tFt0cz3VLhF//EZmex" +
			"9tyFek56ZYhrC11uBa9q0M9hJQc1BU8FF1VnOa5OpmMgFSdUIV1WhIAC74CFPtj7" +
			"Nm+nEa+n+SupMcn4fC0p1pzLm0KkfhY644iaqQLltWQucjccvwp6XSGrOit3qR53" +
			"wMB3CcG7SNw5QACzHpoWWtql4OrMT5CK4hgkfUVWgQLt5adXRQtA0RzxHPwlS722" +
			"SiYgPF5IvN02YXrMyPyX5RWsAZJPYPGL5p1qtbFFfn+OUvRIUlJZu698QXEOLmhd" +
			"1+ZfPAyNP9/QaFcDjoGyblpMY9ykRBOo49scu9edtpOYFdC928jshJpA9/qhttNV" +
			"nwIDAQAB";

	//服务器私钥(经过pkcs8格式处理)
	private static final String serverPrvKeyPkcs8 = "MIIEpAIBAAKCAQEAu3tFt0cz3VLhF//EZmex9tyFek56ZYhrC11uBa9q0M9hJQc1" +
			"BU8FF1VnOa5OpmMgFSdUIV1WhIAC74CFPtj7Nm+nEa+n+SupMcn4fC0p1pzLm0Kk" +
			"fhY644iaqQLltWQucjccvwp6XSGrOit3qR53wMB3CcG7SNw5QACzHpoWWtql4OrM" +
			"T5CK4hgkfUVWgQLt5adXRQtA0RzxHPwlS722SiYgPF5IvN02YXrMyPyX5RWsAZJP" +
			"YPGL5p1qtbFFfn+OUvRIUlJZu698QXEOLmhd1+ZfPAyNP9/QaFcDjoGyblpMY9yk" +
			"RBOo49scu9edtpOYFdC928jshJpA9/qhttNVnwIDAQABAoIBAFyzblxaW53C/kPU" +
			"lrMf8NIkZed86G5NzsgW0XEENKe88sbRUnkmgljwRJMzyfXhq5pqEOJSeZYYidsM" +
			"zFWwCj73ZD+NgFvnJ8jTmgckebyBWPeTMyyZJ1McV4TcRuKy/3hAIfmgX7KvFDA2" +
			"D/vScdogEHwFi+pR4gjwj6DBBe6ztrRrxeXZjiOAJ3MLcZWnAdHayKoJFMy3meMB" +
			"TrAiSuORtkrfRh+2pJsNfKwAtLnQwXcVFx3yMDq2LCgHxZciHJcWeIxozmKJwpzp" +
			"rMZnc8AfRmNDJpu8KxjHvG+TrMN6I1ThBc8wrxQsOiLObYegnJ//WwMeo6juJCvk" +
			"HPZteKECgYEA702zv3UDMzAgCE089LVgAswdfB63MMAWeVXt9aS++T4W/hgx3g86" +
			"0CzC2tFy3sk+/UP8bSW5y+H82iS+SU4dp6eS9xtwlq7zD8PR4jQGwQIKM69hwFeJ" +
			"4xo4vXSuv+gw0hn4Om+PY9Al/VKQj0rJ14+qMiLW/3nUSh3JXbD0hHECgYEAyI/0" +
			"2nx3nhuK7KTNwENZOy7tTiTOQOv6mu2G5pr0olidCNdBJXlmvF2r5lui1uE5fmbS" +
			"uFxnQGDZS7dJ209UQomDJeI9Ajs5+kBMsLCV4L9OJ8jqlg8EEX7a0mEEoLvoFzz1" +
			"BY22fPR15Xzxs7Bd9E/Mci+TDGqcJhADVAo2Qw8CgYEA3FO6T2i4zYRduccDfHsS" +
			"LwEnlIPjPqtdBdIsCaLpgim8ma5FG6MCD3Fm5nk5Pg4kRKM34nZ9nk4vs9loqa2W" +
			"eCo9ke5dtx7VA4rTz2T/Yu0x32x2n9T5S3N1ElpAGsj5SjhG5+aXKtsWXyqJlqTR" +
			"Ofs4RHj3YrkU0DikkfK+7sECgYB9ykw3/HjJoVywr2yceaEtjXEo4cy6y0aShQdm" +
			"+ykT6aAUO22Qo2PcIKQABtlLDFeS/8qJ9uqLZjLjWkHoWwjt8nFfAVZiX+MtIcb3" +
			"ysk1/MZ4DXuUQja7AmUcLd1JMpGOgZzrrsYV+P04wfSWTree063WsB/6WaM1/DUa" +
			"MI134QKBgQCniCe9Do0+JbiS+U3CmQeMkhJE+OG4Jr2hQx+icBl2YVCJdu+vt0KQ" +
			"yKqq062XeOnLXb5h10fmZbmfcyaa94VqscsHOr3FUje9xdIF+IW2y6atfWfHbATt" +
			"R02uznc80cau95blhokdHrzS8xON3TtJ0iTzR0x89C97cDkRWiiJRQ==";

	public static String getServerPubKey() {
		return serverPubKey;
	}

	public static String getServerPrvKeyPkcs8() {
		return serverPrvKeyPkcs8;
	}
	
}
